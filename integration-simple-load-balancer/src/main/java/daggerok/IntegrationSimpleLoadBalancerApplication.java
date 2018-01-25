package daggerok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.annotation.ServiceActivator;

@Slf4j
@SpringBootApplication
public class IntegrationSimpleLoadBalancerApplication {

  /* these two (handle1 and handle2) will load balance message channel "in": */

  @ServiceActivator(inputChannel = "in")
  public void handle1(final String message) {
    log.info("handler 1: {}", message);
  }

  @ServiceActivator(inputChannel = "in")
  public void handle2(final String message) {
    log.info("handler 2: {}", message);
  }

  public static void main(String[] args) {
    SpringApplication.run(IntegrationSimpleLoadBalancerApplication.class, args);
  }
}
