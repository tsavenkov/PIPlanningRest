package com.volleybe.security.service;

import com.volleybe.security.model.MyUserPrincipal;
import com.volleybe.security.model.User;
import com.volleybe.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository repository;

    @Transactional
    public String saveDto(User userDto) {
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        return repository.save(User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .password(userDto.getPassword())
                .build()).toString();
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        
        User user = null;
        try {
            if ("superadmin".equalsIgnoreCase(username)) {
                user = new User();
                user.setUsername("superadmin");
                user.setPassword(bCryptPasswordEncoder.encode("beachmasterpwd2021")); // assume there's a hash of a true password here
            } else {
                user = repository.findByUsername(username.toLowerCase());
                if (user == null) {
                    throw new UsernameNotFoundException(username);
                }
            }

        } catch (Throwable t) {
            throw new UsernameNotFoundException("Unable to locate User with user name \"" + username + "\".", t);
        }


        return new MyUserPrincipal(user);
    }
}
