package daggerok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@SpringBootApplication
public class ConsumerGroupsConsumer1Application {

  public static void main(String[] args) {
    SpringApplication.run(ConsumerGroupsConsumer1Application.class, args);
  }

  @KafkaListener(topicPattern = "consumer-groups")
  public void listen(final Object message) {
    log.warn("consumed by 1: {}", message);
  }
}
