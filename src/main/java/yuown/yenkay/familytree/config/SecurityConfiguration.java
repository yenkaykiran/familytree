package yuown.yenkay.familytree.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import yuown.yenkay.familytree.auth.PropertiesAuthenticationProvider;
import yuown.yenkay.familytree.auth.YuownAuthenticationEntryPoint;
import yuown.yenkay.familytree.auth.YuownAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
	
	@Autowired
	private PropertiesAuthenticationProvider propertiesAuthenticationProvider;
	
	@Autowired
	private YuownAuthenticationFilter yuownAuthenticationFilter;
	
	@Autowired
	private YuownAuthenticationEntryPoint yuownAuthenticationEntryPoint;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
	        .securityMatcher("/api/login/")
	        .authenticationProvider(propertiesAuthenticationProvider)
	        .httpBasic()
	        .authenticationEntryPoint(yuownAuthenticationEntryPoint)
	    .and()
	    	.securityMatcher("/api/**")
	        .addFilterBefore(yuownAuthenticationFilter, BasicAuthenticationFilter.class)
	        .authorizeRequests()
	    .and()
	        .logout()
	        .logoutUrl("/api/logout")
	    .and()
	        .headers()
	        .frameOptions()
	        .disable()
	    .and()
	        .authorizeRequests()
	        .requestMatchers("/api/register").permitAll()
	        .requestMatchers("/api/activate").permitAll()
	        .requestMatchers("/api/login").permitAll()
	        .requestMatchers("/api/**").authenticated()
	    .and()
	    	.csrf()
	    	.disable()
	    	.sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();
	}
}
