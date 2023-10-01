package zerobase.weather.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import zerobase.weather.domain.Diary;
import zerobase.weather.service.DiaryService;

@RequiredArgsConstructor
@RestController
public class DiaryController {
    private final DiaryService diaryService;

    @Operation(summary = "텍스트와 날씨를 이용해서 DB에 일기 저장", description = "일기 저장")
    @PostMapping("/create/diary")
    void createDiary(@Parameter(name = "date", description = "저장할 날짜", example = "2023-10-02") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		     @RequestBody String text) {
	diaryService.createDiary(date, text);
    }

    @Operation(summary = "선택한 날짜의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diary")
    List<Diary>
	readDiary(@Parameter(name = "date", description = "조회할 날짜", example = "2023-10-02") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
	return diaryService.readDiary(date);
    }

    @Operation(summary = "선택한 기간의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diaries")
    List<Diary>
	readDiaries(@Parameter(name = "startDate", description = "조회할 기간의 시작일", example = "2023-10-02") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		    @Parameter(name = "endDate", description = "조회할 기간의 종료일", example = "2023-10-02") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
	return diaryService.readDiaries(startDate, endDate);
    }

    @Operation(summary = "선택한 날짜의 마지막 일기를 수정합니다")
    @PutMapping("/update/diary")
    Diary updateDiary(@Parameter(name = "date", description = "수정할 날짜", example = "2023-10-02") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
		      @RequestBody String text) {
	return diaryService.updateDiary(date, text);
    }

    @Operation(summary = "선택한 날짜의 모든 일기를 삭제합니다")
    @DeleteMapping("/delete/diary")
    void deleteDiary(@Parameter(name = "date", description = "삭제할 날짜", example = "2023-10-02") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
	diaryService.deleteDiary(date);
    }
}
