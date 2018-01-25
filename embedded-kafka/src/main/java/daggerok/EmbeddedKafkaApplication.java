package daggerok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.test.rule.KafkaEmbedded;

@SpringBootApplication
public class EmbeddedKafkaApplication {

  @Bean KafkaEmbedded embeddedKafka() {
    final KafkaEmbedded kafkaEmbedded = new KafkaEmbedded(1);
    kafkaEmbedded.setKafkaPorts(9092);
    return kafkaEmbedded;
  }

  public static void main(String[] args) {
    SpringApplication.run(EmbeddedKafkaApplication.class, args);
  }
}
