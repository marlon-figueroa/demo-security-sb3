package com.mballem.curso.security.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.mballem.curso.security.domain.PerfilTipo;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
    private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
    private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                        antMatcher("/webjars/**"),
                        antMatcher("/css/**"),
                        antMatcher("/image/**"),
                        antMatcher("/js/**"),
                        antMatcher("/"),
                        antMatcher("/home"),
                        antMatcher("/expired"),
                        antMatcher("/u/novo/cadastro"),
                        antMatcher("/u/cadastro/realizado"),
                        antMatcher("/u/cadastro/paciente/salvar"),
                        antMatcher("/u/confirmacao/cadastro"),
                        antMatcher("/u/p/**")
                ).permitAll()
                .requestMatchers(
                        antMatcher("/u/editar/senha"),
                        antMatcher("/u/confirmar/senha")
                ).hasAnyAuthority(PACIENTE, MEDICO)
                .requestMatchers(
                        antMatcher("/u/**")
                ).hasAuthority(ADMIN)
                .requestMatchers(
                        antMatcher("/medicos/especialidade/titulo/*")
                ).hasAnyAuthority(PACIENTE, MEDICO)
                .requestMatchers(
                        antMatcher("/medicos/dados"),
                        antMatcher("/medicos/salvar"),
                        antMatcher("/medicos/editar")
                ).hasAnyAuthority(MEDICO, ADMIN)
                .requestMatchers(
                        antMatcher("/medicos/**")
                ).hasAuthority(MEDICO)
                .requestMatchers(
                        antMatcher("/pacientes/**")
                ).hasAuthority(PACIENTE)
                .requestMatchers(
                        antMatcher("/especialidades/datatables/server/medico/*")
                ).hasAnyAuthority(MEDICO, ADMIN)
                .requestMatchers(
                        antMatcher("/especialidades/titulo")
                ).hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
                .requestMatchers(
                        antMatcher("/especialidades/**")
                ).hasAuthority(ADMIN)
                .anyRequest().authenticated()
        ).formLogin((formLogin) -> formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login-error")
                .permitAll()
        ).logout((logout) -> logout
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
        ).exceptionHandling((ex) -> ex
                .accessDeniedPage("/acesso-negado")
        ).rememberMe(
                withDefaults()
        ).sessionManagement((session) -> session
                .maximumSessions(1)
                .expiredUrl("/expired")
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
        ).sessionManagement((session) -> session
                .sessionFixation()
                .newSession()
                .sessionAuthenticationStrategy(sessionAuthStrategy())
        );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SessionAuthenticationStrategy sessionAuthStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    ServletListenerRegistrationBean<?> servletListenerRegistrationBean() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }
}
