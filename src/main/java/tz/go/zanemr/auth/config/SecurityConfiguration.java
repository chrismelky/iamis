package tz.go.zanemr.auth.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tz.go.zanemr.auth.security.CustomUserDetailsService;
import tz.go.zanemr.auth.security.OidcUserInfoService;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Value("${web-client-url:http://localhost:4200}")
    private String webClientUrl;

    private final CustomUserDetailsService userDetailsService;

    private final OidcUserInfoService userInfoService;


    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {

        Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper = (context) -> {
            OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
            JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
            return new OidcUserInfo(userInfoService.loadUser(principal.getName()).getClaims());
        };

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(oidc -> oidc.userInfoEndpoint(
                        i -> i.userInfoMapper(userInfoMapper)

                ).logoutEndpoint(l ->l.errorResponseHandler(((request, response, exception) -> {
                    exception.printStackTrace();
                    log.warn("**************** {} ",request.getRemoteHost());
                    log.warn("**************** {} ",request.getPathInfo());
                    response.setHeader("cookie", null);
                    response.sendRedirect(webClientUrl);
                }))));

        // Enable OpenID Connect 1.0
        http.cors(Customizer.withDefaults())
                .oauth2ResourceServer(
                        (resourceServer) -> resourceServer
                                .jwt(Customizer.withDefaults())
                )
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> {
                            log.info(exceptions.toString());
                            exceptions
                                    .defaultAuthenticationEntryPointFor(
                                            new LoginUrlAuthenticationEntryPoint("/login"),
                                            new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                                    );
                        }
                )
                .logout(l -> l.logoutSuccessHandler(
                        ((request, response, authentication) -> {
                            log.debug("\n\n\n\n\n 1:**************\n\n\n\n\n");
                        })
                ))
                .userDetailsService(userDetailsService);

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .securityMatcher("/login", "/logout", "/oauth2/**", "/userinfo")
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll() // Allow CORS preflight
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("favicon.ico").permitAll()
                        .requestMatchers("cover.png").permitAll()
                        .requestMatchers("doctor.png").permitAll()
                        .requestMatchers("smz.png").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated()

                ).formLogin(
                        loginForm -> loginForm.loginPage("/login")
                                .loginProcessingUrl("/login")
                                .defaultSuccessUrl(webClientUrl)
                );

        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .securityMatcher("/api/**")
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/**").authenticated()

                )
                .oauth2ResourceServer(
                        (resourceServer) -> resourceServer
                                .jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(
                                        (request, response, authException) -> {
                                            response.setHeader("Content-Type", "application/json; charset=utf-8");
                                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                            response.getWriter().write("{\"message\": \"Unauthorized\"}");
                                            response.getWriter().flush();
                                        }
                                )
                );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId("c204cb1e-1bcb-4615-a6f7-2021c33ff360")
                .clientId("webapp")
                .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(webClientUrl + "/auth-callback")
                .redirectUri("http://localhost/auth-callback")
                .redirectUri("http://102.223.7.208/auth-callback")
                .postLogoutRedirectUri("http://localhost")
                .postLogoutRedirectUri("http://102.223.7.208")
                .postLogoutRedirectUri(webClientUrl)
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("offline_access")
                .clientSettings(ClientSettings.builder()
                        .requireProofKey(true)
                        .requireAuthorizationConsent(false)
                        .build())
                .tokenSettings(TokenSettings.builder()
                        .authorizationCodeTimeToLive(Duration.ofHours(8))
                        .accessTokenTimeToLive(Duration.ofSeconds(1000))
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",
                "http://localhost",
                "http://102.223.7.208"));
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler(OAuth2AuthorizationService authorizationService) {
        return (request, response, authentication) -> {
            log.error("##############\n##########\n");

        };
    }

}
