package zerobase.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI api() {
	return new OpenAPI().info(info());
    }

    private Info info() {
	return new Info().version("v1.0.1")
	    .title("날씨 일기 프로젝트")
	    .description("날씨 일기를 CRUD 할 수 있는 백엔드 API 입니다");
		
    }
}
