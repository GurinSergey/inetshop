package com.webstore.security.services;

import com.google.common.io.BaseEncoding;
import com.webstore.security.config.TokenConfig;
import com.webstore.util.Const;
import com.webstore.util.MD5;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class TokenHandler {

    public String extractUserName(String token) throws ExpiredJwtException {
        String realToken = token.substring(token.indexOf(Const.DELIMITER) + 1);
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
        MD5 md5 = new MD5();
        return md5.encrypt(input);
    }

    private long getKeyId(){
        return System.currentTimeMillis() % TokenConfig.keys.length;
    }

    private SecretKey getSecretValue(int key) {
        byte[] decodedKey = BaseEncoding.base64().decode(TokenConfig.keys[key]);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    private SecretKey extractSecretValue(String token){
        String hexKey = token.substring(0, token.indexOf(Const.DELIMITER));
        int key = (int) (Long.parseLong(hexKey, 16));
        return getSecretValue(key);
    }

    private Claims getClaimsBody(String token) throws NullPointerException{
        Jws<Claims> claimsJws;
        if(token.indexOf(Const.DELIMITER) == -1){
            return null;
        }
        String realToken = token.substring(token.indexOf(Const.DELIMITER) + 1);
        claimsJws = Jwts.parser().setSigningKey(extractSecretValue(token)).parseClaimsJws(realToken);
        return claimsJws.getBody();
    }

    public void invalidateToken(String token){
        final LocalDateTime expirationDate = LocalDateTime.now().minusSeconds(10);
        Claims claims = this.getClaimsBody(token);
        claims.setExpiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()));
        Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, extractSecretValue(token))
                .compact();

    }

}
