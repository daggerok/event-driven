package daggerok;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Headers;

import java.util.Map;

@Slf4j
@SpringBootApplication
@EnableBinding(Sink.class)
public class SpringCloudStreamConditionalApplication {

  @StreamListener(value = Sink.INPUT, condition = "headers['version']=='v1'")
  public void handleV1(final String message, @Headers final Map<String, String> headers) {
    log.info("\n\nhandle v1: {}\n", message);
    log.info("\n\nv1 headers: {}\n", headers);
  }

  @StreamListener(value = Sink.INPUT)
  public void handleAny(final String message, @Headers final Map<String, String> headers) {
    log.info("\n\nhandle any: {}\n", message);
    log.info("\n\nany headers: {}\n", headers);
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudStreamConditionalApplication.class, args);
  }
}
