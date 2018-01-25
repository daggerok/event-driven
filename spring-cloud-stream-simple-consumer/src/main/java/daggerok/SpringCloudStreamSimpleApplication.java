package daggerok;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.SubscribableChannel;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@EnableBinding(Sink.class)
public class SpringCloudStreamSimpleApplication {

  final Sink sink;
  final SubscribableChannel input;

  @StreamListener(Sink.INPUT)
  //@ServiceActivator(inputChannel = "in")
  public void handle(final String message) {
    log.info("in: {}", sink.input());
    log.info("ch: {}", input);
    log.info("handle: {}", message);
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudStreamSimpleApplication.class, args);
  }
}
