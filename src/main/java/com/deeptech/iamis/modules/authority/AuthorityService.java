package com.deeptech.iamis.modules.authority;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityMapper authorityMapper;

    private final AuthorityRepository authorityRepository;


    public List<AuthorityDto> findAll() {
        return authorityRepository.findAll().stream().map(authorityMapper::toDto).collect(Collectors.toList());
    }
}
