package zerobase.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
    @GetMapping("/")
    public String index() throws Exception {
	log.info("홈");
	return "index";
    }
}
