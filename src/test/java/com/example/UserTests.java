package com.example;

import com.example.collection.Role;
import com.example.collection.User;
import com.example.config.Config;
import com.example.dto.UserCredentialsDto;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)

class UserTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private UserService userService;


    private final List<User> users = new ArrayList<>();
    private User u1 = new User();
    private User u2 = new User();
    private Role userRole = new Role();
    private Role adminRole = new Role();

    @BeforeEach
    void init(){
        userRole.setName("ROLE_USER");
        adminRole.setName("ROLE_ADMIN");

        u1.setId("1");
        u1.setUsername("u1");
        u1.setPassword("p1");

        u1.setRoles(new ArrayList<>(Arrays.asList(userRole)));
        users.add(u1);
        u2.setId("2");
        u2.setUsername("admin");
        u2.setPassword("admin");
        u2.setRoles(new ArrayList<>(Arrays.asList(adminRole)));
        users.add(u2);

    }


    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(users);
       var userList = userService.getAll();
        assertEquals(2, userList.size());
        verify(userRepository, times(1)).findAll();

    }
    @Test
    void getUserById() throws CustomException {
        when(userRepository.findById("1")).thenReturn(Optional.ofNullable(u1));
        var userById = userService.getById("1");
        assertEquals("u1", userById.getUsername());
        assertEquals("p1", userById.getPassword());
        assertEquals("1", userById.getId());
        assertTrue(userById.getRoles().stream().map(Role::getName).anyMatch(el->el.equals("ROLE_USER")));
        verify(userRepository, times(1)).findById("1");

    }

    @Test
    void getUserByIdNotFound() {
        var msg = "User not found";
        var ex = assertThrows(CustomException.class, () -> userService.getById("3"));
        assertEquals(msg, ex.getMessage());
        assertEquals(404,ex.getResponseStatus().value());
    }

    @Test
    void saveUser() throws CustomException {

        var newUser=new User();
        newUser.setUsername("newUser");
        newUser.setPassword("newUser");
        newUser.setRoles(new ArrayList<>(List.of(userRole)));
        when(userRepository.save(isA(User.class))).thenReturn(newUser);
        var userCreds = new UserCredentialsDto();
        userCreds.setPassword(newUser.getPassword());
        userCreds.setUsername(newUser.getUsername());
        var savedUser =userService.save(userCreds);
        assertEquals(newUser.getUsername(),savedUser.getUsername());
        assertEquals(newUser.getRoles(),savedUser.getRoles());
        verify(userRepository, times(1)).save(any(User.class));

    }


}
