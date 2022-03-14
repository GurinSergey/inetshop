package com.webstore.security.services;

import com.webstore.repository.UserRepo;
import com.webstore.security.config.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;

    public SecurityUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        CustomUserDetails user;
        if(userName == null || userName.isEmpty()){
            throw new UsernameNotFoundException(userName);
        }
        user = new CustomUserDetails(userRepo.findUserByUserName(userName));

        if (user.getUser() == null) {
            throw new UsernameNotFoundException(userName);
        }
        return user;
    }


}