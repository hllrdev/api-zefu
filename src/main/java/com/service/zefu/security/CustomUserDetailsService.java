package com.service.zefu.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.service.zefu.models.UserModel;
import com.service.zefu.services.UserService;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> optionalUser = userService.getByEmail(username);
        UserModel user = optionalUser.get();
        CustomUserDetails customUserDetails = new CustomUserDetails(
            user.getEmail(),
            user.getPassword(),
            user.getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList())
        );

        return customUserDetails;
    }
}
