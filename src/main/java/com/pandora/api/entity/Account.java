package com.pandora.api.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "botox_accounts")
@Accessors(chain = true)
@Data
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String userId;
    private Long adminId;
    private String resetCode;
    private boolean wipe = false;
    private String password;
    private Date created;
    private String token;
    private Date sentDate;
    private Date responseDate;
    private Date wipeDate;
    private Long after=0L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !wipe;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public AccountStatus getStatus() {
        if (wipe) {
            return AccountStatus.WIPED;
        }
        return AccountStatus.ACTIVE;
    }
}
