package com.deeptech.iamis.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.deeptech.iamis.modules.role.Role;
import com.deeptech.iamis.modules.user.User;
import com.deeptech.iamis.modules.user.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //TODO implement password expiration and account locking
        return userRepository
                .findUserByEmail(username)
                .map(u -> new org.springframework.security.core.userdetails.User(
                                u.getEmail(),
                                u.getPassword(),
                                u.getIsActive(),
                                true,
                                true,
                                true,
                                getAuthority(u)
                        )
                )
                .orElseThrow(() -> new UsernameNotFoundException("No user with "
                        + "the name " + username + "was found in the database"));
    }

    private Set<GrantedAuthority> getAuthority(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Role role : user.getRoles()) {
            role.getAuthorities()
                    .forEach(authority ->
                            authorities.add(
                                    new SimpleGrantedAuthority(authority.getName())));
        }
        return authorities;
    }
}
