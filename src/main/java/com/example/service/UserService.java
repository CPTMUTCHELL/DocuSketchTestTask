package com.example.service;

import com.example.collection.Role;
import com.example.collection.User;
import com.example.dto.UserCredentialsDto;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder encoder;


    public User save(UserCredentialsDto user) throws CustomException {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            var msg = "User with this name already exists";
            throw new CustomException(msg, HttpStatus.CONFLICT, new ErrorResponse(msg));
        }
        var userToSave = new User();
        userToSave.setUsername(user.getUsername());
        userToSave.setRoles(new ArrayList<>(Arrays.asList(roleRepository.findByName("ROLE_USER"))));
        userToSave.setPassword(encoder.encode(user.getPassword()));
      return repository.save(userToSave);
    }
    public User getByUsername(String username) throws CustomException {
        var msg = "User not found";
        return repository.findByUsername(username).orElseThrow(()->new CustomException(msg, HttpStatus.NOT_FOUND,new ErrorResponse(msg)));
    }
    public List<User> getAll(){
        return repository.findAll();
    }
    public User getById(String id) throws CustomException {
        var msg = "User not found";
        return repository.findById(id).orElseThrow(()->new CustomException(msg, HttpStatus.NOT_FOUND,new ErrorResponse(msg)));
    }
    public void deleteById(String id) throws CustomException {
        var userToDelete = getById(id);
         repository.delete(userToDelete);
    }


    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = null;
        try {
            user = getByUsername(username);
        } catch (CustomException e) {
            throw new RuntimeException(e);
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), mapRolesToAuth(user.getRoles()));
    }
    private List<?extends GrantedAuthority> mapRolesToAuth(Collection<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
