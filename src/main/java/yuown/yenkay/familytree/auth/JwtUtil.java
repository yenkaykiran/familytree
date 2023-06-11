package yuown.yenkay.familytree.auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	@Value("${token.expiration.time.milliseconds}")
    private long epirationTime;
    
    @Value("${token.secret}")
    private String tokenSecret;
    
    public static final String HEADER_STRING = "Authorization";
    
	public static final String RESPONSE_HEADER_STRING = "jwttoken";
	
	public static final String TOKEN_PREFIX = "Bearer ";
	
	public static final String BASIC_PREFIX = "Basic ";
    
    @Autowired
    private HttpServletResponse response;
    
	public void populateResponseWithJwt(String username, Collection<? extends GrantedAuthority> authorities) {
		List<String> auths = new ArrayList<String>();
		authorities.forEach(auth -> auths.add(auth.getAuthority()));
		
		Claims claims = Jwts.claims().setSubject(username);
        claims.put("userId", username);
        claims.put("authorities", String.join(",", auths));

        String token = Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + epirationTime))
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        response.addHeader(RESPONSE_HEADER_STRING, token);
	}
	
	public UsernamePasswordAuthenticationToken getAuthentication( HttpServletRequest request) throws IOException, ServletException {
		String token = request.getHeader(HEADER_STRING);
		if (token != null && !token.contains("null")) {
			Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
			if (claims != null) {
				if(new Date().getTime() < claims.getExpiration().getTime()) {
					String authStr = claims.get("authorities").toString();
					String[] auths = authStr.split(",");
					List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
					for (int i = 0; i < auths.length; i++) {
						authorities.add(new SimpleGrantedAuthority(auths[i]));
					}
					return new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
				} else {
					throw new ServletException("Token Expired, Authenticate again!");
				}
			}
			return null;
		}
		return null;
	}

}
