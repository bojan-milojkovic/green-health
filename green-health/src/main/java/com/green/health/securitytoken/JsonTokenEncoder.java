package com.green.health.securitytoken;

import java.util.Date;
import java.util.Random;
import org.springframework.stereotype.Service;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.gen.*;
import com.nimbusds.jwt.*;

@Service
public class JsonTokenEncoder {
	
	public static RSAKey rsaJWK;
	static{
		try {
			rsaJWK = new RSAKeyGenerator(2048)
				    .keyID("123")
				    .algorithm(JWSAlgorithm.RS256)
				    .generate();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
	}
	
	public String encodeJson(final String json){
		try {
			// init header :
			JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
						.contentType("text/plain")
						.keyID(rsaJWK.getKeyID())
						//.x5c(certChain)
						.build();
			
			// Prepare JWT with claims set :
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
			    .subject("APPL_ID")
			    .claim("jti", (new Random()).nextInt(10000) )
			    .expirationTime(new Date(new Date().getTime() + 60 * 1000))
			    .notBeforeTime(new Date())
			    .claim("fes:iv", "use iv from test cases")
			    .claim("fes:data", json)
			    .build();
			
			// create token object :
			JWSObject jwsObject = new JWSObject(header, new Payload(claimsSet.toJSONObject()));
			
			// init signer :
			JWSSigner signer = new RSASSASigner(rsaJWK);
			
			//sign :
			jwsObject.sign(signer);
			
			return jwsObject.serialize();
			
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
