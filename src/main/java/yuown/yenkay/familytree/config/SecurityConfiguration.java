package yuown.yenkay.familytree.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import yuown.yenkay.familytree.auth.PropertiesAuthenticationProvider;
import yuown.yenkay.familytree.auth.YuownAuthenticationEntryPoint;
import yuown.yenkay.familytree.auth.YuownAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
	
	@Autowired
	private PropertiesAuthenticationProvider propertiesAuthenticationProvider;
	
	@Autowired
	private YuownAuthenticationFilter yuownAuthenticationFilter;
	
	@Autowired
	private YuownAuthenticationEntryPoint yuownAuthenticationEntryPoint;

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//	        .antMatcher("/api/login/")
//	        .authenticationProvider(propertiesAuthenticationProvider)
//	        .httpBasic()
//	        .authenticationEntryPoint(yuownAuthenticationEntryPoint)
//	    .and()
//	    	.antMatcher("/api/**")
//	        .addFilterBefore(yuownAuthenticationFilter, BasicAuthenticationFilter.class)
//	        .authorizeRequests()
//	    .and()
//	        .logout()
//	        .logoutUrl("/api/logout")
//	    .and()
//	        .headers()
//	        .frameOptions()
//	        .disable()
//	    .and()
//	        .authorizeRequests()
//	        .antMatchers("/api/register").permitAll()
//	        .antMatchers("/api/activate").permitAll()
//	        .antMatchers("/api/login").permitAll()
//	        .antMatchers("/api/**").authenticated()
//	    .and()
//	    	.csrf()
//	    	.disable()
//	    	.sessionManagement()
//	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// Permit access to the login and activate endpoints without authentication
				.authorizeHttpRequests((authz) -> authz
						.requestMatchers("/api/register", "/api/activate", "/api/login").permitAll()
						// Require authentication for any other requests under /api/**
						.requestMatchers("/api/**").authenticated()
				)
				// Set the authentication provider
				.authenticationProvider(propertiesAuthenticationProvider)
				// Use HTTP Basic authentication
				.httpBasic(withDefaults())
				// Add the custom authentication filter before BasicAuthenticationFilter
				.addFilterBefore(yuownAuthenticationFilter, BasicAuthenticationFilter.class)
				// Configure logout functionality
				.logout(logout -> logout
						.logoutUrl("/api/logout")
				)
				// Disable CSRF protection since we're stateless (for example, in APIs)
				.csrf(csrf -> csrf.disable())
				// Disable frame options for accessing frames, useful when working with H2 console or similar
				.headers(headers -> headers
						.frameOptions().disable()
				)
				// Set session management policy to stateless (no session is maintained)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);

		return http.build();
	}

}
