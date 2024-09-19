package tz.go.zanemr.auth.modules.authority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    @KafkaListener(id = "authServer", topics = {"authority.create"})
    public void consume(Authority authority) {

        if(!authorityRepository.existsByName(authority.getName())) {
            authority.setUuid(UUID.randomUUID());
            authorityRepository.save(authority);
            log.info("***** Created authority: {}", authority.getUuid());
        }
    }
}
