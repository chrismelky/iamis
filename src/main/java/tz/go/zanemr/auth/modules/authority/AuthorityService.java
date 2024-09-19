package tz.go.zanemr.auth.modules.authority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorityService {

    @KafkaListener(id = "authServer", topics = {"authority.create"})
    public void consume(Authority authority) {
        log.info("***** Consumed authority: {}", authority.getName());
    }
}
