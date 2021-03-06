package daggerok;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit4.SpringRunner;

import static java.time.LocalTime.now;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

//hamcrest not
//hamcrest nullValue

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationSimpleLoadBalancerApplicationTests {

  @Autowired @Qualifier("in") MessageChannel in;
  @Autowired IntegrationSimpleLoadBalancerApplication app;

  @Test
  @SneakyThrows
  public void contextLoads() {

    log.info("balance start at: {}", now());

    assertThat(in, not(nullValue()));
    assertThat(app, not(nullValue()));

    in.send(MessageBuilder.withPayload("balanced hey!").build());
    in.send(new GenericMessage<>("balanced hi!"));

    log.info("balance stop at: {}", now());

    in.send(new GenericMessage<>("balanced blocking ho!"), 2);
  }
}
