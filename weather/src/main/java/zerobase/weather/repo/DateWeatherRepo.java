package zerobase.weather.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;

//@Repository
public interface DateWeatherRepo extends JpaRepository<DateWeather, LocalDate> {

    DateWeather findFirstByDate(LocalDate date);
}
