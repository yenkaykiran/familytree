package yuown.yenkay.familytree.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DummyUser implements UserDetails {
	
	private String username;
	private String password;
	
	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	
	public DummyUser(String username, String password, List<String> auths) {
		this.username = username;
		this.password = password;
		auths.forEach(auth -> authorities.add(new SimpleGrantedAuthority(auth)));
	}

	@Override
	public List<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
