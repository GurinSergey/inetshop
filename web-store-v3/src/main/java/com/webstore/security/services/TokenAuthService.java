package com.webstore.security.services;

import com.webstore.base.exception.CheckException;
import com.webstore.domain.User;
import com.webstore.responses.Response;
import com.webstore.security.config.CustomUserDetails;
import com.webstore.security.config.TokenConfig;
import com.webstore.security.config.UserAuthentication;
import com.webstore.util.IPHandler;
import com.webstore.util.Const;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Component
@EnableConfigurationProperties
public class TokenAuthService {

    @Autowired
    private IPHandler ipHandler;

    private final AuthenticationManager authenticationManager;

    private final TokenHandler tokenHandler;

    private final SecurityUserDetailsService userService;

    private TokenConfig tokenConfig = new TokenConfig();

    public TokenAuthService(AuthenticationManager authenticationManager, TokenHandler tokenHandler, SecurityUserDetailsService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenHandler = tokenHandler;
        this.userService = userService;
    }

    private String createToken(String userName, String uAgent, String IP) {
        return tokenHandler.generateAccessToken(userName, LocalDateTime.now().plusMinutes(tokenConfig.getExpirationTimeMins()), uAgent, IP);
    }

    public void logout(HttpServletRequest request){
        String token = request.getHeader(Const.AUTH_HEADER_NAME);
        tokenHandler.invalidateToken(token);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(Const.AUTH_HEADER_NAME);
        if(token != null && !token.isEmpty()){
            try {
                String uAgentFromToken = tokenHandler.extractUAgent(token);
                String uAgentFromRequest = tokenHandler.enCode(request.getHeader(USER_AGENT));
                String iPFromToken = tokenHandler.extractIP(token);
                String iPFromRequest = ipHandler.extractIp(request);
                if(uAgentFromToken.equals(uAgentFromRequest) && iPFromToken.equals(iPFromRequest)){
                    return new UserAuthentication(userService.loadUserByUsername(tokenHandler.extractUserName(token)));
                }
            } catch (ExpiredJwtException | NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public Response addAuthentication(User user, HttpServletRequest request) {
        String uAgent = request.getHeader(USER_AGENT);
        String IP = ipHandler.extractIp(request);
        CustomUserDetails foundUser;
        Authentication authenticatedUser;

        try {
            foundUser = (CustomUserDetails) userService.loadUserByUsername(user.getUserName());
        }catch (UsernameNotFoundException e){
            return Response.error("Such user does not exists", HttpStatus.UNAUTHORIZED);
        }

        Set<GrantedAuthority> grantedAuthorities = (HashSet<GrantedAuthority>) foundUser.getAuthorities();

        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(foundUser, user.getPassword(), grantedAuthorities);

        try {
            authenticatedUser = authenticationManager.authenticate(authentication);
        }
        catch (Exception e){
            return Response.error(/*e.toString()*/"Such user or password does not exists", HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        String token = createToken(foundUser.getUsername(), uAgent, IP);

        /*VDN Обернул для обработки ошибок*/
        try{
            return Response.ok(getPrincipal().getUser(), token);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public CustomUserDetails getPrincipal() throws CheckException {
        //LAO getPrincipal при анонимном пользователе возвращает строку, а мы просто вернем null
        try {
            return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            /*VDN Добавил обработку ошибки получения Principal*/
        } catch (ClassCastException e) {
            throw new CheckException("Требуется авторизация");
        }
    }

    public User getCurrentUser(){
        User user = null;
        try {
            user = getPrincipal().getUser();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return user;
        }

    }
}
