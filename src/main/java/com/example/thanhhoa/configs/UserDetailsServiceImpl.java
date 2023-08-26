package com.example.thanhhoa.configs;

import com.example.thanhhoa.dtos.UserModels.AuthorizeModel;
import com.example.thanhhoa.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Lazy
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthorizeModel user = null;

        try {
            user = userService.getByUsername(username);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        if(user == null) {
            throw new UsernameNotFoundException(username);
        }

        return UserDetailsImpl.build(user);
    }
}
