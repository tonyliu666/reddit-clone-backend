package tony.redit_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableRetry(
    proxyTargetClass = true
)
public class ReditCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReditCloneApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
