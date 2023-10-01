package zerobase.weather.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import zerobase.weather.service.DiaryService;

@RequiredArgsConstructor
@Controller
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping("/create/diary")
    String createDiary(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		       @RequestBody String text) {
	diaryService.createDiary(date, text);
	return "/diary/create";
    }

}
