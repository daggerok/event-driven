package daggerok;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@EnableWebFlux
@SpringBootApplication
@RequiredArgsConstructor
public class DurablePubsubProducerApplication {

  //  final KafkaTemplate<String, Object> kafka;
  final KafkaOperations<String, Object> kafka;

  public static void main(String[] args) {
    SpringApplication.run(DurablePubsubProducerApplication.class, args);
  }

  @Bean
  public RouterFunction<ServerResponse> routes() {

    return

        route(
            POST("/api/v1/send"),
            request -> status(ACCEPTED).contentType(MediaType.APPLICATION_JSON_UTF8)
                                       .body(request.bodyToMono(Map.class)
                                                    .map(req -> req.get("message"))
                                                    .map(msg -> {
                                                      kafka.send("durable", msg);
                                                      return "message sent.";
                                                    }),
                                             String.class)
        )

        ;
  }
}
