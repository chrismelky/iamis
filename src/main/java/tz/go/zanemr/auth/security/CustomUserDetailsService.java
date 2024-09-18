package tz.go.zanemr.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tz.go.zanemr.auth.modules.role.Role;
import tz.go.zanemr.auth.modules.user.User;
import tz.go.zanemr.auth.modules.user.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
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
