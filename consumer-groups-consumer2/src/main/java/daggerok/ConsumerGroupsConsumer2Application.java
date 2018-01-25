package daggerok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@SpringBootApplication
public class ConsumerGroupsConsumer2Application {

  @KafkaListener(topicPattern = "consumer-groups")
  public void listen(final Object message) {
    log.info("consumed by 2: {}", message);
  }

  public static void main(String[] args) {
    SpringApplication.run(ConsumerGroupsConsumer2Application.class, args);
  }
}
