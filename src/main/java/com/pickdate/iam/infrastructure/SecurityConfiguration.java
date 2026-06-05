package com.pickdate.iam.infrastructure;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.pickdate.iam.application.ApplicationSetupUseCase;
import com.pickdate.iam.domain.KeyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcherEntry;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;
import static org.springframework.security.config.Customizer.withDefaults;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityConfiguration {

    private static final String[] SETUP_ENDPOINTS = {"/api/v1/iam/setup/**"};

    private final UserDetailsService userDetailsService;
    private final ApplicationSetupUseCase applicationSetupUseCase;
    private final KeyProperties keyProperties;

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) {
        http
                .oauth2AuthorizationServer((authorizationServer) -> {
                    http.securityMatcher(authorizationServer.getEndpointsMatcher());
                    authorizationServer.oidc(withDefaults());
                })
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/oauth2/token").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> {
                    // returns 401
                    var unauthorized = new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
                    RequestMatcher mediaTypeMatcher = new MediaTypeRequestMatcher(MediaType.APPLICATION_JSON);
                    exceptions.defaultAuthenticationEntryPointFor(unauthorized, mediaTypeMatcher)
                            .authenticationEntryPoint(unauthorized);
                })
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()))
        ;

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(SETUP_ENDPOINTS))
                .addFilterBefore(
                        new SetupRedirectFilter(applicationSetupUseCase),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new AuthenticatedUserRedirectFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests((authorize) -> authorize
                        .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
                        .requestMatchers(
                                // assets
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/icons/**",
                                "/favicon.ico",

                                // browser/agent probes (e.g. Chrome devtools)
                                "/.well-known/**",

                                // public pages
                                "/",
                                "/login",
                                "/register",
                                "/setup",
                                "/reset-password",

                                // public participant voting page
                                "/vote/**",

                                // public api
                                "/api/v1/userinfo"

                        ).permitAll()
                        // anyone with the link can open a poll to vote — no account needed
                        .requestMatchers(HttpMethod.GET, "/api/v1/polls/*").permitAll()
                        .requestMatchers(SETUP_ENDPOINTS).access((authentication, _) -> {
                            if (!applicationSetupUseCase.setupCompleted()) {
                                return new AuthorizationDecision(true);
                            }

                            var auth = authentication.get();
                            boolean isAdmin = auth != null
                                    && auth.isAuthenticated()
                                    && auth.getAuthorities().stream().anyMatch(a -> "ADMIN".equals(a.getAuthority()));

                            return new AuthorizationDecision(isAdmin);
                        })
                        .requestMatchers(
                                "/api/v1/iam/**",
                                "/api/v1/observability/**",
                                "/observability/**",
                                "/admin/**",
                                "/admin"
                        ).hasAuthority("ADMIN")
                        .requestMatchers("/api/v1/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(viewAwareAuthenticationEntryPoint()))
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            var redirectStrategy = new DefaultRedirectStrategy();
                            redirectStrategy.sendRedirect(request, response, "/home");
                        })
                        .permitAll()
                        .authenticationDetailsSource(detailsSource())
                        .failureHandler((request, response, exception) -> {
                            var defaultFailureHandler = new SimpleUrlAuthenticationFailureHandler("/login?error");
                            var redirectStrategy = new DefaultRedirectStrategy();
                            if (exception instanceof CompromisedPasswordException) {
                                redirectStrategy.sendRedirect(request, response, "/reset-password");
                                return;
                            }
                            defaultFailureHandler.onAuthenticationFailure(request, response, exception);
                        })
                )
                .rememberMe(configurer -> configurer
                        .rememberMeServices(tokenBasedRememberMeServices(detailsSource()))
                );

        return http.build();
    }

    private static AuthenticationEntryPoint viewAwareAuthenticationEntryPoint() {
        RequestMatcher apiMatcher = PathPatternRequestMatcher.withDefaults().matcher("/api/**");
        var apiEntryPoint = new RequestMatcherEntry<AuthenticationEntryPoint>(
                apiMatcher, new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

        return new DelegatingAuthenticationEntryPoint(
                new LoginUrlAuthenticationEntryPoint("/login"), List.of(apiEntryPoint));
    }

    @Bean
    RequestDetailsSource detailsSource() {
        return new RequestDetailsSource();
    }

    @Bean
    @Profile("!local")
    CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        var authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        var providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);

        return providerManager;
    }

    @Bean
    AuthenticationEventPublisher authenticationEventPublisher(
            ApplicationEventPublisher applicationEventPublisher) {
        return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
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

    @Bean
    RegisteredClientRepository registeredClientRepository() {
        RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("oidc-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantTypes(types -> {
                    types.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    types.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .redirectUris(redirectUri -> {
                    redirectUri.add("http://127.0.0.1:8080/login/oauth2/code/oidc-client");
                })
                .postLogoutRedirectUri("http://127.0.0.1:8080/")
                .scopes(score -> {
                    score.add(OidcScopes.OPENID);
                    score.add(OidcScopes.PROFILE);
                    score.add(OidcScopes.EMAIL);
                })
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .requireProofKey(true)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    @Bean
    TokenBasedRememberMeServices tokenBasedRememberMeServices(RequestDetailsSource detailsSource) {
        var rememberMeServices = new TokenBasedRememberMeServices(
                keyProperties.getRememberMeKey(),
                userDetailsService
        );
        rememberMeServices.setAuthenticationDetailsSource(detailsSource);
        return rememberMeServices;
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
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
}
