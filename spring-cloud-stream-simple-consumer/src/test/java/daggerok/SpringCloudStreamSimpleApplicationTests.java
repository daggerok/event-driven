package daggerok;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringCloudStreamSimpleApplicationTests {

  @Autowired Sink sink;
  @Autowired @Qualifier("input") SubscribableChannel channel;
//  @Autowired @Qualifier(Sink.INPUT) SubscribableChannel channel;
//  @Autowired @Input(Sink.INPUT) SubscribableChannel channel;
//  @Autowired @Input("input") SubscribableChannel channel;

  @Test
  public void contextLoads() {
    assertThat(sink, IsNot.not(IsNull.nullValue()));
    assertThat(channel, IsNot.not(IsNull.nullValue()));
    assertThat(sink.input(), IsNot.not(IsNull.nullValue()));
  }
}
