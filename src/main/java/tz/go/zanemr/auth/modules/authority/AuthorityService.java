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
    public void consume(String newAuthorityName) {

        if(!authorityRepository.existsByName(newAuthorityName)) {
            Authority authority = new Authority();
            authority.setUuid(UUID.randomUUID());
            authority.setName(newAuthorityName);
            authority.setDescription(newAuthorityName);
            authorityRepository.save(authority);
            log.info("***** Created authority: {}", authority.getUuid());
        }
    }
}
