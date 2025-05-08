package com.example.testcenter.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import java.util.Collection;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetails {

    private static final long serialVersionUID = 170526831291229418L;

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;



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
