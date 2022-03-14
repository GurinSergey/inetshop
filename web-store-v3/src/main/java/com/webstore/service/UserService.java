package com.webstore.service;


import com.webstore.repository.UserRepo;
import com.webstore.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return userRepo.findUserByUserName(email);
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User add(User user) {
        String bcCryptPassword = getPassword(user);
        user.setPassword(bcCryptPassword);
        user.setSex("M");
        return userRepo.save(user);
    }

    public String getPassword(User user) {
        return passwordEncoder.encode(user.getPassword());
    }

    public User find(User item) {
        return userRepo.findOne(item.getId());
    }

    public User find(long id) {
        return userRepo.findOne(id);
    }

    public void delete(User item) {
        userRepo.delete(item);
    }

    public User update(User user) {
        return userRepo.save(user);
    }

    public Boolean checkEqualsPassword(String password,String checkPassword){

        //    System.out.println(password);
        //  System.out.println(passwordEncoder.encode(checkPassword));
        //   System.out.println(passwordEncoder.encode("18521258"));
        return passwordEncoder.matches(checkPassword,password);

        // return password.equals(passwordEncoder.encode(checkPassword));

    }
}
