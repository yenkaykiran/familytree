package yuown.yenkay.familytree.auth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import yuown.yenkay.familytree.model.DummyUser;
import yuown.yenkay.familytree.model.LocalUser;

@Component
public class PropertiesAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
    @Autowired
    private JwtUtil jwtUtil;
    
	private List<DummyUser> users = new ArrayList<DummyUser>();
	
	@PostConstruct
	private void init() throws Exception {
	    Resource resource = resourceLoader.getResource("classpath:local-users.json");
	    if (resource != null) {
	    	File localUserFile = resource.getFile();
	    	ObjectMapper objMapper = new ObjectMapper();
	    	List<LocalUser> localUsers = objMapper.readValue(localUserFile, new TypeReference<List<LocalUser>>(){});
	    	localUsers.forEach(localUser -> users.add(new DummyUser(localUser.getUser(), localUser.getPassword(), localUser.getRoles())));
	    }
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		DummyUser dummyUser = users.stream()
								   .filter(f -> f.getUsername().equals(username) && f.getPassword().equals(authentication.getCredentials()))
								   .collect(Collectors.toList())
								   .get(0);
		if(dummyUser == null) {
			throw new BadCredentialsException("Username/ Password mismatch!");
		}
		return dummyUser;
	}
	
	@Override
	protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
		Authentication superAuthentication = super.createSuccessAuthentication(principal, authentication, user);
		jwtUtil.populateResponseWithJwt(user.getUsername(), superAuthentication.getAuthorities());
		return superAuthentication;
	}
}
