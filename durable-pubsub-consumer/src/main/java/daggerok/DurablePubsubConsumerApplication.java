package daggerok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@SpringBootApplication
public class DurablePubsubConsumerApplication {

  @KafkaListener(topicPattern = "durable")
  public void listen(final Object message) {
    log.info("consume: {}", message);
  }

  public static void main(String[] args) {
    SpringApplication.run(DurablePubsubConsumerApplication.class, args);
  }
}