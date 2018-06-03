package com.parking.Parking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.parking.Parking.model.security.Authority;
import com.parking.Parking.model.security.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
public class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;
    private String phone;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal cost;
    private Boolean isVip = false;
    private Long hours;
    private Boolean isPay = false;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<UserRole> userRoles = new HashSet<>();


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Transaction> transaction = new ArrayList<>();

    public User(String username, String password, String firstName, String lastName, String email, String phone) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public void resetTimes(){
        this.startTime = null;
        this.endTime = null;
        this.hours = 0L;
        BigDecimal cost = new BigDecimal(0);
        this.cost = cost;
    }

    public void setStartTime() {
        this.startTime = LocalDateTime.now();
    }

    public void setEndTime() {
        this.endTime = LocalDateTime.now().plusHours(4l);
    }

    public long setHours(){
        if(this.startTime != null && this.endTime != null){
            hours = ChronoUnit.HOURS.between(startTime, endTime);
        }
        return hours;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach(ur -> authorities.add(new Authority(ur.getRole().getName())));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
