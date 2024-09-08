package com.alex.spring_myfirstpetproject.config;

import com.alex.spring_myfirstpetproject.entities.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.*;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;


@EnableWebSecurity
@Configuration
public class SecurityConfig {


    private final UserSecurityService userSecurityService   ;

    public SecurityConfig(UserSecurityService myUserDetailsService) {
        this.userSecurityService = myUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/petProject/api/admin/**").hasRole(Roles.ADMIN.name())
                                .requestMatchers("/petProject/api/v2/admin/**").hasRole(Roles.ADMIN.name())
                                .requestMatchers("/registration.html").permitAll()
                                .requestMatchers("/startPage.html").permitAll()
                                .requestMatchers("/api/registration").permitAll()

//                        .requestMatchers("/api/add/new/course/**").permitAll()
                                .anyRequest().authenticated()

                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll
                                 // form.loginPage("/startPage.html")
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v2/logout")
                        .logoutSuccessUrl("/startPage.html")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);


        authenticationManagerBuilder
                .userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());


        return authenticationManagerBuilder.build();



    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
