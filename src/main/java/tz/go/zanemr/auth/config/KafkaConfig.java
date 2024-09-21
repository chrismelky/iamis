package tz.go.zanemr.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.converter.RecordMessageConverter;

import java.util.List;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public List<NewTopic> topics() {
        log.info("Creating topics for topic if not exist");
        return List.of(
                new NewTopic("authority.create", 1, (short) 1));
    }

    @Bean
    public RecordMessageConverter converter() {
        return new JsonMessageConverter();
    }
}
