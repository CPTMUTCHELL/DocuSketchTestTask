package com.example.config;

import com.example.collection.Role;
import com.example.collection.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@ChangeLog
public class MongoMigration {
    @ChangeSet(order = "001", id = "001",author = "1")
    public void createRoles(RoleRepository repository) {
        var admin = new Role();
        admin.setName("ROLE_ADMIN");
        var user = new Role();
        user.setName("ROLE_USER");
        repository.save(admin);
        repository.save(user);
    }
    @ChangeSet(order = "002", id = "002",author = "1")
    public void createAdmin(UserRepository repository, RoleRepository roleRepository, PasswordEncoder encoder) {
        var admin = new User();
        admin.setUsername("admin");
        admin.setPassword(encoder.encode("admin"));
        List<Role> roles_ids = new ArrayList<>();
        roles_ids.add(roleRepository.findByName("ROLE_ADMIN"));
        admin.setRoles(roles_ids);
        repository.save(admin);
    }

}
