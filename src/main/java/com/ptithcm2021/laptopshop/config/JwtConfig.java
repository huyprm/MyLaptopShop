package com.ptithcm2021.laptopshop.config;

import com.nimbusds.jose.JOSEException;
import com.ptithcm2021.laptopshop.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
public class JwtConfig implements JwtDecoder {
    @Value("${jwt.signer_key}")
    protected String sign_key;

    private final AuthenticationService authenticationService;

    public JwtConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        SecretKey key = new SecretKeySpec(sign_key.getBytes(), "HS512");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS512).build();

        try{
            authenticationService.verifyAccessToken(token);
            return decoder.decode(token);
        } catch (ParseException e) {
            throw new JwtException("Invalid JWT token", e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
