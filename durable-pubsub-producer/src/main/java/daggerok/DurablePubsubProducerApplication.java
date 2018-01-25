package daggerok;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.FluxMessageChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.http.inbound.RequestMapping;
import org.springframework.integration.webflux.dsl.WebFlux;
import org.springframework.integration.webflux.inbound.WebFluxInboundEndpoint;
import org.springframework.integration.webflux.outbound.WebFluxRequestExecutingMessageHandler;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.messaging.MessageChannel;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@EnableWebFlux
@EnableIntegration
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

  /**
   * http :8002/test
   */
  @Configuration
  public static class WebFluxJavaTestConfig {

    @Bean
    public WebFluxInboundEndpoint simpleInboundEndpoint() {
      final WebFluxInboundEndpoint endpoint = new WebFluxInboundEndpoint();
      final RequestMapping requestMapping = new RequestMapping();
      requestMapping.setPathPatterns("/test");
      endpoint.setRequestMapping(requestMapping);
      endpoint.setRequestChannelName("serviceChannel");
      return endpoint;
    }

    @ServiceActivator(inputChannel = "serviceChannel")
    String service() {
      return "It works!";
    }
  }

  /**
   * http :8002/persons
   */
  @Configuration
  static class WebFluxInboundGatewayJavaConfig {

    @Bean
    public WebFluxInboundEndpoint jsonInboundEndpoint() {
      WebFluxInboundEndpoint endpoint = new WebFluxInboundEndpoint();
      RequestMapping requestMapping = new RequestMapping();
      requestMapping.setPathPatterns("/persons");
      endpoint.setRequestMapping(requestMapping);
      endpoint.setRequestChannel(fluxResultChannel());
      return endpoint;
    }

    @Bean
    public MessageChannel fluxResultChannel() {
      return new FluxMessageChannel();
    }

    @ServiceActivator(inputChannel = "fluxResultChannel")
    Flux<List> getPersons() {
      return Flux.just(
          singletonList("Jane"),
          singletonList("Jason"),
          singletonList("John")
      );
    }
  }

  @Configuration
  static class InboundGatewayJavaDSLConfig {

    @Bean
    public IntegrationFlow inboundChannelAdapterFlow() {
      return IntegrationFlows
          .from(WebFlux.inboundChannelAdapter("/reactivePost")
                       .requestMapping(m -> m.methods(HttpMethod.POST))
                       .requestPayloadType(ResolvableType.forClassWithGenerics(Flux.class, String.class))
                       .statusCodeFunction(m -> HttpStatus.ACCEPTED))
          .channel(c -> c.queue("storeChannel"))
          .get();
    }
  }

  @Configuration // TODO
  static class OutboundGatewayJavaConfig {

    @Bean WebClient webClient() {

      return WebClient.builder()
                      .build();
    }

    @ServiceActivator(inputChannel = "reactiveHttpOutRequest")
    @Bean
    public WebFluxRequestExecutingMessageHandler reactiveOutbound(WebClient client) {
      WebFluxRequestExecutingMessageHandler handler =
          new WebFluxRequestExecutingMessageHandler("http://localhost:8002/test", client);
      handler.setHttpMethod(HttpMethod.GET);
//      handler.setHttpMethod(HttpMethod.POST);
      handler.setExpectedResponseType(String.class);
      return handler;
    }
  }

  @Configuration // TODO
  static class OutboundGatewayJavaDSLConfig {

    @Bean
    public IntegrationFlow outboundReactive() {
      return f -> f.handle(
          WebFlux.<MultiValueMap<String, String>>outboundGateway(
              m -> UriComponentsBuilder.fromUriString("http://localhost:8002/test")
                                       .queryParams(m.getPayload())
                                       .build()
                                       .toUri())
              .httpMethod(HttpMethod.GET)
              .expectedResponseType(String.class));
    }
  }
}
