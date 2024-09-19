package tz.go.zanemr.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Slf4j
@Configuration
@EnableKafka
public class KafkaConfig {


    @Bean
    public NewTopic topic() {
        log.info("***************Creating new topic for topic");
        return new NewTopic("authority.create", 3, (short) 1);
    }

    @Bean
    public RecordMessageConverter converter() {
        return new JsonMessageConverter();
    }
}
