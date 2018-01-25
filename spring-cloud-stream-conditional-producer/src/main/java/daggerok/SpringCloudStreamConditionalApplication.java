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
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
@SpringBootApplication
@RequiredArgsConstructor
@EnableBinding(Source.class)
public class SpringCloudStreamConditionalApplication {

  final Source source;

  @PostMapping("/v1")
  public void handleV1(@RequestBody final HashMap<String, String> req) {

    source.output()
          .send(MessageBuilder.withPayload(req.getOrDefault("message", "O.o"))
                              .setHeader("version", "v1")
                              .build());
  }

  @PostMapping("/v2")
  public void handleV2(@RequestBody final HashMap<String, String> req) {

    source.output()
          .send(MessageBuilder.withPayload(req.getOrDefault("message", "^.^"))
                              .setHeader("version", "v2")
                              .build());
  }

  @PostMapping("/**")
  public void handleAny(@RequestBody final HashMap<String, String> req) {

    source.output()
          .send(MessageBuilder.withPayload(req.getOrDefault("message", "^_^"))
                              .setHeader("ololo", "trololo")
                              .build());
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringCloudStreamConditionalApplication.class, args);
  }
}
