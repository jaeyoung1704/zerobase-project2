package zerobase.weather.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repo.DateWeatherRepo;
import zerobase.weather.repo.DiaryRepo;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    @Value("${openweathermap.key}")
    private String apiKey;

    private final DiaryRepo diaryRepo;
    private final DateWeatherRepo dateWeatherRepo;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text) {
	// api 또는 DB에서 날씨데이터 가져오기
	DateWeather dateWeather = getDateWeather(date);
	// 데이터 db에 삽입
	Diary nowDiary = new Diary();
	nowDiary.setDateWeather(dateWeather);
	nowDiary.setText(text);
	diaryRepo.save(nowDiary);
	log.info("다이어리 생성 성공 {}", nowDiary);
    }

    private String getWeatherString() {
	String apiUrl =
	    "https://api.openweathermap.org/data/2.5/weather?q=seoul&units=metric&appid="
		+ apiKey;
	try {
	    URL url = new URL(apiUrl);
	    HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    con.setRequestMethod("GET");
	    int responseCode = con.getResponseCode();
	    BufferedReader br;
	    // 정상이면 가져오고 에러면 에러코드 가져오기
	    if (responseCode == 200)
		br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    else
		br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	    String line;
	    StringBuilder sb = new StringBuilder();
	    while ((line = br.readLine()) != null)
		sb.append(line);
	    br.close();
	    return sb.toString();
	}
	catch (Exception e) {
	    // TODO Auto-generated catch block
	    return "url 받아오기 실패";
	}
    }

    // JSON 데이터 파싱
    private Map<String, Object> parseWeather(String jsonString) {
	JSONParser jsonparser = new JSONParser();
	JSONObject jsonObject;
	try {
	    jsonObject = (JSONObject) jsonparser.parse(jsonString);
	}
	catch (ParseException e) {
	    throw new RuntimeException(e);
	}
	Map<String, Object> resultMap = new HashMap<>();
	var weatherArr = (JSONArray) jsonObject.get("weather");
	var weather = (JSONObject) weatherArr.get(0);
	var main = (JSONObject) jsonObject.get("main");
	resultMap.put("temp", main.get("temp"));
	resultMap.put("main", weather.get("main"));
	resultMap.put("icon", weather.get("icon"));
	return resultMap;
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date) {
//	if (date.isAfter(LocalDate.ofYearDay(3050, 1)))
//	    throw new InvalidDate();
	log.debug("다이어리 읽기");
	return diaryRepo.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
	return diaryRepo.findAllByDateBetween(startDate, endDate);
    }

    public Diary updateDiary(LocalDate date, String text) {
	Diary diary = diaryRepo.getFirstByDate(date);
	diary.setText(text);
	return diaryRepo.save(diary);
    }

    public void deleteDiary(LocalDate date) {
	diaryRepo.deleteAllByDate(date);
    }

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void saveWeatherDate() {
	DateWeather dateWeather = getWeatherFromApi();
	dateWeatherRepo.save(dateWeather);
	log.info("{}일 날짜 데이터 가져옴", dateWeather.getDate());
    }

    private DateWeather getWeatherFromApi() {
	// api에서 날씨데이터 가져오기
	String weatherData = getWeatherString();

	// json 파싱
	var parsedWeather = parseWeather(weatherData);
	return DateWeather.builder()
	    .date(LocalDate.now())
	    .weather(parsedWeather.get("main").toString())
	    .icon(parsedWeather.get("icon").toString())
	    .temperature((double) parsedWeather.get("temp"))
	    .build();
    }

    // 해당 날짜에 날씨가 있으면 가져오고 없으면 현재날씨를 저장
    private DateWeather getDateWeather(LocalDate date) {
	DateWeather dateWeatherFromDB = null;
	if ((dateWeatherFromDB = dateWeatherRepo.findFirstByDate(date)) == null)
	    return getWeatherFromApi();
	else
	    return dateWeatherFromDB;
    }
}
