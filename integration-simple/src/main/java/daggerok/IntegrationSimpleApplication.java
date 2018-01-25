package daggerok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.ServiceActivator;

@Slf4j
@SpringBootApplication
public class IntegrationSimpleApplication {

  @ServiceActivator(inputChannel = "in")
  public void handle(final String message) {
    log.info("message: {}", message);
  }

  public static void main(String[] args) {
    SpringApplication.run(IntegrationSimpleApplication.class, args);
  }
}
