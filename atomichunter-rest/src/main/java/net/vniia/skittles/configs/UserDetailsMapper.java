package net.vniia.skittles.configs;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsMapper {

    public UserDetails toUserDetails(net.vniia.skittles.entities.User userCredentials) {

        return User.withUsername(userCredentials.getLogin())
                .password(userCredentials.getPassword())
                .roles(userCredentials.getRole())
                .build();
    }
}