package yuown.yenkay.familytree.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import yuown.yenkay.familytree.auth.PropertiesAuthenticationProvider;
import yuown.yenkay.familytree.auth.YuownAuthenticationEntryPoint;
import yuown.yenkay.familytree.auth.YuownAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PropertiesAuthenticationProvider propertiesAuthenticationProvider;
	
	@Autowired
	private YuownAuthenticationFilter yuownAuthenticationFilter;
	
	@Autowired
	private YuownAuthenticationEntryPoint yuownAuthenticationEntryPoint;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
	        .antMatcher("/api/login/")
	        .authenticationProvider(propertiesAuthenticationProvider)
	        .httpBasic()
	        .authenticationEntryPoint(yuownAuthenticationEntryPoint)
	    .and()
	    	.antMatcher("/api/**")
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
	        .antMatchers("/api/register").permitAll()
	        .antMatchers("/api/activate").permitAll()
	        .antMatchers("/api/login").permitAll()
	        .antMatchers("/api/**").authenticated()
	    .and()
	    	.csrf()
	    	.disable()
	    	.sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
