package daggerok;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Slf4j
@RestController
@SpringBootApplication
@RequiredArgsConstructor
@EnableBinding(Source.class)
public class SpringCloudStreamSimpleApplication {

  final Source source;

  @PostMapping
  @ResponseStatus(ACCEPTED)
  public void handle(@RequestBody final HashMap<String, String> req) {

    source.output()
          .send(MessageBuilder.withPayload(req.getOrDefault("message", "Oo"))
                              .build());
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudStreamSimpleApplication.class, args);
  }
}
