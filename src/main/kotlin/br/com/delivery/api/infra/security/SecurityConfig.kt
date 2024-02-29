package br.com.delivery.api.infra.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(val tokenFilter: TokenFilter) {

    @Bean
    protected fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.anyRequest().permitAll()
//                it.requestMatchers("/login").permitAll()
//                it.requestMatchers("/v3/api-docs/**").permitAll()
//                it.requestMatchers("/swagger-ui/**").permitAll()
//                it.requestMatchers("/swagger-ui.html").permitAll()
//
//                it.requestMatchers(HttpMethod.POST, "/cliente").permitAll()
//
//                it.anyRequest().authenticated()
            }

            .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    protected fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    protected fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }
}