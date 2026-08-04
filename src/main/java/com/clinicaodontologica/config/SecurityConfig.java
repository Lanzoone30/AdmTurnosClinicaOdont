package com.clinicaodontologica.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuracion de seguridad: acceso publico a login y assets estaticos,
 * el resto de rutas requiere autenticacion. Las contrasenias se hashean
 * con BCrypt.
 *
 * <p>Roles: ADMIN puede todo; ODONTOLOGO ve turnos/pacientes; SECRETARIO
 * ve pacientes/odontologos/turnos.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Define la cadena de filtros de seguridad.
     *
     * @param http builder de HttpSecurity
     * @return la cadena de filtros configurada
     * @throws Exception si la configuracion falla
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**", "/js/**", "/img/**", "/vendor/**", "/scss/**")
                    .permitAll()
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/pacientes/**").hasAnyRole("ADMIN", "ODONTOLOGO", "SECRETARIO")
                .requestMatchers("/odontologos/**").hasAnyRole("ADMIN", "SECRETARIO")
                .requestMatchers("/turnos/**").hasAnyRole("ADMIN", "ODONTOLOGO", "SECRETARIO")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll());
        return http.build();

    }

    /**
     * Codificador de contrasenias para almacenar y verificar hashes BCrypt.
     *
     * @return instancia de {@link BCryptPasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
