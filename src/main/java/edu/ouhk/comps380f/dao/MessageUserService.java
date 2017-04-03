package edu.ouhk.comps380f.dao;

import edu.ouhk.comps380f.model.MessageUser;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MessageUserService implements UserDetailsService {
    @Autowired
    MessageUserRepository messageRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        MessageUser messageUser = messageRepo.findByUsername(username);
        if (messageUser == null) {
            throw new UsernameNotFoundException("User '" + username + "' not found.");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : messageUser.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new User(messageUser.getUsername(), messageUser.getPassword(), authorities);
    }
    
}
