package tz.go.zanemr.auth.modules.authority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tz.go.zanemr.auth.core.CustomApiResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityMapper authorityMapper;

    private final AuthorityRepository authorityRepository;

    @KafkaListener(id = "authServer", topics = {"authority.create"})
    public void consume(Map<String, String> auth) {

        log.info("******start consuming");

        if (!authorityRepository.existsByName(auth.get("name"))) {
            Authority authority = new Authority();
            authority.setName(auth.get("name"));
            authority.setService(auth.get("service"));
            authority.setAction(auth.get("action"));
            authority.setResource(auth.get("resource"));
            authority.setMethod(auth.get("method"));
            authority.setDescription(auth.get("description"));
            authority.setUuid(UUID.randomUUID());
            authorityRepository.save(authority);
            log.info("***** Created authority: {}", authority.getUuid());
        }
    }

    public List<AuthorityDto> findAll() {
        return authorityRepository.findAll().stream().map(authorityMapper::toDto).collect(Collectors.toList());
    }
}
