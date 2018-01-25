package daggerok;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringCloudStreamSimpleApplicationTests {

  @Autowired Source source;
  @Autowired @Output("output") MessageChannel channel;
//  @Autowired @Output(Source.OUTPUT) MessageChannel channel;
//  @Autowired @Qualifier("output") MessageChannel channel;

  @Test
  public void contextLoads() {
    assertThat(source, IsNot.not(IsNull.nullValue()));
    assertThat(channel, IsNot.not(IsNull.nullValue()));
    assertThat(source.output(), IsNot.not(IsNull.nullValue()));
  }
}
