package com.webstore.security.services;

import com.google.common.io.BaseEncoding;
import com.webstore.core.util.Const;
import com.webstore.security.config.TokenConfig;
import com.webstore.security.crypto.MD5;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TokenHandler {

    private MD5 md5PasswordEncoder = new MD5();

    private TokenConfig tokenConfig = new TokenConfig();

    public TokenHandler() {
        tokenConfig.setKeys(new String[]{"Xa8fHLEP9leri2N", "Xa8fHLEP9leri2N", "DFg1wc3CbfPMo7V", "wrFJS9Gu4dizAna",
                                        "FdNvZGhs5b3Jxmv", "Xw8Fld2NWaTDxVi", "S6Elo9DGdCKTP0Y", "01CA9WlzEh8myri",
                                         "5KkQtUHSRPy1wg0", "cO5a21P7dp4zYNu"});
    }

    public String extractUserName(String token) throws ExpiredJwtException {
        String realToken = token.substring(token.indexOf(Const.DELIMITER)+1);
        String name = "";
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(extractSecretValue(token)).parseClaimsJws(realToken);
        Claims body = claimsJws.getBody();
        name = body.getSubject();

        return name;
    }

    public String extractUAgent(String token) {
        String uAgent = "";
        uAgent = getClaimsBody(token).getAudience();
        return uAgent;
    }

    public String extractIP(String token) throws ExpiredJwtException {
        String IP = "";
        IP = getClaimsBody(token).getIssuer();
        return IP;
    }

    public String generateAccessToken(String userName, LocalDateTime expires, String uAgent, String IP) {
        long keyId = getKeyId();
        String keyHex = Long.toHexString(keyId);
        SecretKey secretValue = getSecretValue((int)keyId);

        return  keyHex + Const.DELIMITER +Jwts.builder()
                .setAudience(enCode(uAgent))
                .setSubject(userName)
                .setIssuer(IP)
                .setExpiration(Date.from(expires.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, secretValue)
                .compact();
    }

    public String enCode(String input){
       return md5PasswordEncoder.encrypt(input);
    }

    private long getKeyId(){
        return System.currentTimeMillis()% tokenConfig.getKeys().length;
    }

    private SecretKey getSecretValue(int key) {
        byte[] decodedKey = BaseEncoding.base64().decode(tokenConfig.getKeys()[key]);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    private SecretKey extractSecretValue(String token){
        String hexKey = token.substring(0, token.indexOf(Const.DELIMITER));
        int key = (int) (Long.parseLong(hexKey, 16));
        return getSecretValue(key);
    }

    private Claims getClaimsBody(String token) throws NullPointerException{
        Jws<Claims> claimsJws;
        String realToken = token.substring(token.indexOf(Const.DELIMITER)+1);
        claimsJws = Jwts.parser().setSigningKey(extractSecretValue(token)).parseClaimsJws(realToken);
        return claimsJws.getBody();
    }

}
