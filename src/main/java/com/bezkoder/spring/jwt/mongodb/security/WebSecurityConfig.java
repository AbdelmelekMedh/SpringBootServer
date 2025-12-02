package com.bezkoder.spring.jwt.mongodb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bezkoder.spring.jwt.mongodb.security.jwt.AuthEntryPointJwt;
import com.bezkoder.spring.jwt.mongodb.security.jwt.AuthTokenFilter;
import com.bezkoder.spring.jwt.mongodb.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

    
	// Let Spring Security auto-configure the AuthenticationProvider using
	// the `UserDetailsService` bean and the `PasswordEncoder` bean.
	// Avoid constructing DaoAuthenticationProvider directly to prevent
	// using deprecated constructors/mutators.

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

			http.cors(Customizer.withDefaults())
				.csrf(csrf -> csrf.disable())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/**", "/api/test/**").permitAll()
					.anyRequest().authenticated());

			// Spring Boot will auto-configure an appropriate AuthenticationProvider
			// when a `UserDetailsService` bean and a `PasswordEncoder` bean are present.
			// No explicit `authenticationProvider(...)` registration is required here.
		/*
		 * http.csrf().disable()// to disable CRSF protection
		 * .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
		 * and().authorizeRequests() .antMatchers("/signup",
		 * "/api/post").permitAll().anyRequest().authenticated();
		 */

		/*
		 * http.authorizeRequests().antMatchers("api/auth/signup", "/",
		 * "/api/post").permitAll().anyRequest() .authenticated().and().httpBasic();
		 */

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
