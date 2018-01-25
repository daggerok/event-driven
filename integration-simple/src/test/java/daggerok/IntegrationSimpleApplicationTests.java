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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static java.time.LocalTime.now;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.IsNot.*;
import static org.hamcrest.core.IsNull.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationSimpleApplicationTests {

  @Autowired @Qualifier("in") MessageChannel in;
  @Autowired IntegrationSimpleApplication app;

  @Test
  @SneakyThrows
  public void contextLoads() {

    assertThat(in, not(nullValue()));
    assertThat(app, not(nullValue()));

    log.info("1: {}", now());
    in.send(MessageBuilder.withPayload("hi!").build());
    TimeUnit.SECONDS.sleep(1);
    log.info("2: {}", now());
  }
}
