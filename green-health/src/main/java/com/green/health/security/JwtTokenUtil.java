package com.green.health.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.green.health.security.entities.UserSecurityJPA;
import com.green.health.security.repositories.UserSecurityRepository;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_AUDIENCE = "audience";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_AUTHORITIES = "a";
    private static final String CLAIM_KEY_PASSWORD = "pwd";
    private static final String CLAIM_KEY_IP = "ipad";
    
    private static final String AUDIENCE_MOBILE = "mobile";
    private static final String AUDIENCE_TABLET = "tablet";    

    private static final String ROLE_USER = "1urw";
    private static final String ROLE_HERBALIST = "2hrw";
    private static final String ROLE_ADMIN = "3arw";
    private static final String ROLE_SUPER_ADMIN = "4sarwa";

    private String secret = "MySuperSecretSneakyKeyForEncriptingAndDecriptingAuthenticationToken";
    // token is valid time in seconds :
    private Long expiration = 1800L; // token should last 30 minutes before it expires
    
    @Autowired
	private UserSecurityRepository userSecurityRepository;

    public String getUsernameFromToken(String token) {
        String username;
        try {
            //final Claims claims = getClaimsFromToken(token);
            username = getClaimsFromToken(token).getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            //final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) (getClaimsFromToken(token).get(CLAIM_KEY_CREATED)));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            //final Claims claims = getClaimsFromToken(token);
            expiration = getClaimsFromToken(token).getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
    
    public String getIpFromToken(String token){
    	try {
            //final Claims claims = getClaimsFromToken(token);
            return (String) (getClaimsFromToken(token).get(CLAIM_KEY_IP));
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getPasswordFromToken(String token){
    	try {
    		return (String) (getClaimsFromToken(token).get(CLAIM_KEY_PASSWORD));
    	} catch (Exception e) {
            return null;
        }
    }

    public String getAudienceFromToken(String token) {
        String audience;
        try {
            //final Claims claims = getClaimsFromToken(token);
            audience = (String) (getClaimsFromToken(token).get(CLAIM_KEY_AUDIENCE));
        } catch (Exception e) {
            audience = null;
        }
        return audience;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
    
    //TODO
    public String addIpToToken(String token, HttpServletRequest request){
    	Claims claims = getClaimsFromToken(token);
		
    	String ipAddress = (request.getHeader("X-FORWARDED-FOR")!=null ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr());
    	if(ipAddress!=null){	
			claims.put(CLAIM_KEY_IP, ipAddress);
			token = generateTokenFromClaims(claims);
		}
    	
    	return token;
    }
    
    public String generateToken(UserDetails userDetails/*, Device device, HttpServletRequest request*/) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_PASSWORD, userDetails.getPassword());
        //claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_CREATED, new Date());
        
        /*if(request!=null){
			String ipAddress = (request.getHeader("X-FORWARDED-FOR")!=null ? request.getHeader("X-FORWARDED-FOR") : request.getRemoteAddr());
			if(ipAddress!=null){
				claims.put(CLAIM_KEY_IP, ipAddress);
			}
        }*/
        
        return generateTokenFromClaims(generateClaims(claims, userDetails));
    }
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenNotExpired(String token) {
        return getExpirationDateFromToken(token).after(new Date());
    }

    private Boolean isTokenCreatedAfterLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.after(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        String audience = getAudienceFromToken(token);
        return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
    }
    private boolean isTokenCreatedAtLastLogin(final Date created, final Date lastLogin){
    	return Math.abs(created.getTime() - lastLogin.getTime()) < 1000;
    }
    private Date convertLocalToDate(final LocalDateTime ldt){
    	return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    
    
    private Map<String, Object> generateClaims(Map<String, Object> claims, UserDetails userDetails){
    	String authorities = "";
    	
    	for(GrantedAuthority ga : userDetails.getAuthorities()){
	    	//TODO: add role to token
			if(ga.getAuthority().equals("ROLE_SUPERADMIN")){
				authorities += (ROLE_SUPER_ADMIN+"#");
			}
			else if(ga.getAuthority().equals("ROLE_ADMIN")){
				authorities += (ROLE_ADMIN+"#");
			}
			else if(ga.getAuthority().equals("ROLE_HERBALIST")){
				authorities += (ROLE_HERBALIST+"#");
			}
			else if(ga.getAuthority().equals("ROLE_USER")){
				authorities += (ROLE_USER+"#");
			}
    	}
    	
    	claims.put(CLAIM_KEY_AUTHORITIES, authorities);
        
        return claims;
    }
    
    public UserDetails getUserDetailsFromToken(final String token){
    	Map<String, Object> claims = getClaimsFromToken(token);
    	Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	//TODO: extracting authorities
    	for(String role : ((String)claims.get(CLAIM_KEY_AUTHORITIES)).split("#")){
			if(!role.isEmpty()){
				if(role.equals(ROLE_SUPER_ADMIN)){
					authorities.add(new SimpleGrantedAuthority("ROLE_SUPERADMIN"));
					authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN_ADMINIZE"));
				}
				else if(role.equals(ROLE_ADMIN)){
					authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
					authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN_READ"));
					authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN_WRITE"));
				}
				else if(role.equals(ROLE_HERBALIST)){
					authorities.add(new SimpleGrantedAuthority("ROLE_HERBALIST"));
					authorities.add(new SimpleGrantedAuthority("ROLE_HERBALIST_READ"));
					authorities.add(new SimpleGrantedAuthority("ROLE_HERBALIST_WRITE"));
				}
				else if(role.equals(ROLE_USER)){
					authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
					authorities.add(new SimpleGrantedAuthority("ROLE_USER_READ"));
					authorities.add(new SimpleGrantedAuthority("ROLE_USER_WRITE"));
				}
			}
		}
    	
    	String username = (String)claims.get(CLAIM_KEY_USERNAME);
    	UserSecurityJPA userSecurity = userSecurityRepository.findByUsername(username);
    	
    	return new User(username, 
    					(String)claims.get(CLAIM_KEY_PASSWORD), 
    					userSecurity.isActive(), 
    					true, 
    					isTokenNotExpired(token), // login was too old ; token has expired
    					userSecurity.isNotLocked(), 
    					authorities);
    }
    
    public boolean checkRequestIp(final String token, final HttpServletRequest request){
    	String expectedIp = getIpFromToken(token);
    	String actualIp = request.getHeader("X-FORWARDED-FOR") == null ? request.getRemoteAddr() : request.getHeader("X-FORWARDED-FOR");

		if(actualIp!=null && expectedIp!=null){
			return expectedIp.equals(actualIp); // if nether is null, they should be equal
		} else if(actualIp!=null || expectedIp!=null){
			return false; // one is null the other is not
		}
		return true; // both are null
    }

    public String generateTokenFromClaims(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return isTokenCreatedAfterLastPasswordReset(created, lastPasswordReset)
                && (isTokenNotExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateTokenFromClaims(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        final Date created = getCreatedDateFromToken(token);
        
        UserSecurityJPA userSecurity = userSecurityRepository.findByUsername(username);
        try {
        	return ( // username is checked when getting user security
        			isTokenNotExpired(token)
                	&& isTokenCreatedAtLastLogin(created, convertLocalToDate(userSecurity.getLastLogin()))
                	&& isTokenCreatedAfterLastPasswordReset(created, convertLocalToDate(userSecurity.getLastPasswordChange()))
                	// you can also check device, and last login ip
                	);
        
        } catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    }
}