package br.cwust.billscontrol.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.cwust.billscontrol.date.DateUtils;
import br.cwust.billscontrol.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenParser {

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_ROLE = "role";
	static final String CLAIM_KEY_AUDIENCE = "audience";
	static final String CLAIM_KEY_CREATED = "created";
	
	@Autowired
	private DateUtils dateUtils;

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	private Date generateExpirationDate() {
		return dateUtils.addSeconds(dateUtils.dateNow(), expiration);
	}

	private Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	private String createToken(Map<String, Object> claims) {
		return Jwts.builder().
				setClaims(claims)
				.setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	public String createToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, user.getEmail());
		claims.put(CLAIM_KEY_ROLE, user.getRole());
		claims.put(CLAIM_KEY_CREATED, dateUtils.dateNow());

		return createToken(claims);
	}

	public String getUserEmailFromToken(String token) {
		return getClaimsFromToken(token).getSubject();
	}
	
	public Date getExpirationDateFromToken(String token) {
		return getClaimsFromToken(token).getExpiration();
	}
	
	public boolean isTokenStillValid(String token) {
		Date expirationDate = this.getExpirationDateFromToken(token);
		return expirationDate != null && expirationDate.after(dateUtils.dateNow());
	}
}
