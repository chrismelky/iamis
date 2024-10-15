package tz.go.zanemr.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
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
                TopicBuilder.name("authority.create")
                        .partitions(1)
                        .replicas(1)
                        .config(TopicConfig.RETENTION_MS_CONFIG, "1000")
                        .build()
        );
    }

    @Bean
    public RecordMessageConverter converter() {
        return new JsonMessageConverter();
    }
}
