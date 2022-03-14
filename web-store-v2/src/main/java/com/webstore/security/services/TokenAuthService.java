package com.webstore.security.services;

import com.webstore.core.entities.User;
import com.webstore.core.util.Const;
import com.webstore.core.util.IPHandler;
import com.webstore.security.config.TokenConfig;
import com.webstore.security.config.UserAuthentication;
import com.webstore.web.responses.Response;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Component
public class TokenAuthService {

    private TokenConfig tokenConfig = new TokenConfig();

    @Autowired
    private IPHandler ipHandler;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private SecurityUserDetailsService userService;

    public String createToken(String userName, String uAgent, String IP) {
        return tokenHandler.generateAccessToken(userName, LocalDateTime.now().plusMinutes(tokenConfig.getExpirationTimeMins()), uAgent, IP);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(Const.AUTH_HEADER_NAME);
        if(token != null && !token.isEmpty()){
            String uAgentFromToken = tokenHandler.extractUAgent(token);
            String uAgentFromRequest = tokenHandler.enCode(request.getHeader(USER_AGENT));
            String IPFromToken = tokenHandler.extractIP(token);
            String IPFromRequest = ipHandler.extractIp(request);
            try {
                if((uAgentFromToken.equals(uAgentFromRequest))){
                    return new UserAuthentication(userService.loadUserByUsername(tokenHandler.extractUserName(token)));
                }
            } catch (ExpiredJwtException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public Response addAuthentication(UserDetails userDetails, HttpServletRequest request) {
        String uAgent = request.getHeader(USER_AGENT);
        String IP = ipHandler.extractIp(request);
        UserDetails foundUser;
        Authentication authenticatedUser;

        try {
            foundUser = userService.loadUserByUsername(userDetails.getUsername());
        }catch (UsernameNotFoundException e){
            return Response.error("The username is incorrect", HttpStatus.UNAUTHORIZED);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(foundUser, userDetails.getPassword(), grantedAuthorities);

        try {
            authenticatedUser = authenticationManager.authenticate(authentication);

        }
        catch (Exception e){
            return Response.error("The username is incorrect", HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        String token = createToken(foundUser.getUsername(), uAgent, IP);

        return Response.ok(getPrincipal(), token);
    }

    public User getPrincipal(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return  auth == null || auth  instanceof AnonymousAuthenticationToken ? null : (User) auth.getPrincipal();
    }
}
