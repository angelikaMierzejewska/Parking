package com.parking.Parking.service;

import com.parking.Parking.model.User;
import com.parking.Parking.model.security.UserRole;
import com.parking.Parking.repository.RoleRepository;
import com.parking.Parking.repository.UserRepository;
import com.parking.Parking.utils.Cost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userRepository.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            for (UserRole ur : userRoles) {
                roleRepository.save(ur.getRole());
            }
            user.getUserRoles().addAll(userRoles);
             localUser = userRepository.save(user);
    }
        return localUser;
}

    public List<User> findAllDrivers(){
        List<User> users = new ArrayList<>();
        Iterable<User> usersIterable = userRepository.findAll();
        long  idAdmin = findByUsername("admin").getUserId();
        long  idOwner = findByUsername("owner").getUserId();
        for(User d: usersIterable){
            if(d.getUserId() == idAdmin || d.getUserId() == idOwner){
                continue;
            }
            users.add(d);
        }
        return users;
    }

    public User findById(Long id){

        Optional<User> user =  userRepository.findById(id);
        if(user == null){
            throw new RuntimeException("User Not Found");
        }
        return user.get();
    }

    public void startTime(Long id){
        Optional<User> user =  userRepository.findById(id);
        if(user == null){
            throw new RuntimeException("User Not Found");
        }
        user.get().setStartTime();
        userRepository.save(user.get());
    }

    public void endTime(Long id){
        Optional<User> user =  userRepository.findById(id);
        if(user == null){
            throw new RuntimeException("User Not Found");
        }
        user.get().setEndTime();
        userRepository.save(user.get());
    }

    public void setHours(Long id){
        Optional<User> user =  userRepository.findById(id);
        if(user == null){
            throw new RuntimeException("User Not Found");
        }
        if(user.get().getStartTime() != null && user.get().getEndTime() != null){
            user.get().setHours();
        }
        userRepository.save(user.get());
    }

    public void setCost(Long id) {
        Optional<User> userOptional =  userRepository.findById(id);
        if(userOptional == null){
            throw new RuntimeException("User Not Found");
        }
        User user = userOptional.get();
        BigDecimal cost =  Cost.countCost(user);
        user.setCost(cost);
        userRepository.save(user);
    }

    public User findByUsername(String user ){
        return userRepository.findByUsername(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }
        return false;
    }

    public boolean checkUserExists(String username, String email){
        if (checkUsernameExists(username) || checkEmailExists(username)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }
        return false;
    }

    public void resetTimes(User user){
        user.resetTimes();
    }

}
