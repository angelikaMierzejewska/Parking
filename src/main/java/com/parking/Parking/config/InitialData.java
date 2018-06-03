package com.parking.Parking.config;

import com.parking.Parking.model.User;
import com.parking.Parking.model.security.Role;
import com.parking.Parking.model.security.UserRole;
import com.parking.Parking.repository.RoleRepository;
import com.parking.Parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class InitialData implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initDate();
    }

    public void initDate(){

        String encryptedPasswordAdmin = passwordEncoder.encode("admin");
        String encryptedPasswordOwner = passwordEncoder.encode("owner");

        User userAdmin = new User("admin", encryptedPasswordAdmin, "Adam", "Kowalski", "a.kowalski80@example.com", "564836834");
        User userOwner = new User("owner", encryptedPasswordOwner, "Kamil", "Kowalski", "k.kowalski80@example.com", "564836833");

        Role roleUser = new Role (0, "ROLE_USER");
        Role roleAdmin = new  Role(1, "ROLE_ADMIN");
        Role roleOwner = new Role(2, "ROLE_OWNER");

        UserRole userRoleAdmin = new UserRole(userAdmin, roleAdmin);
        Set<UserRole> userRolesAdmin = new HashSet<>();
        userRolesAdmin.add(userRoleAdmin);
        userAdmin.setUserRoles(userRolesAdmin);

        UserRole userRoleOwner = new UserRole(userOwner, roleOwner);
        Set<UserRole> userRolesOwner = new HashSet<>();
        userRolesOwner.add(userRoleOwner);
        userOwner.setUserRoles(userRolesOwner);

        if(((ArrayList) roleRepository.findAll()).size() == 0){
            roleRepository.save(roleUser);
            roleRepository.save(roleAdmin);
            roleRepository.save(roleOwner);
        }

        if(((ArrayList) userRepository.findAll()).size() < 2){
            userRepository.save(userAdmin);
            userRepository.save(userOwner);
        }
    }
}


