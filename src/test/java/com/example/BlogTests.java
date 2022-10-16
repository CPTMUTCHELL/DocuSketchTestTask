package com.example;

import com.example.collection.Blog;
import com.example.collection.Role;
import com.example.collection.User;
import com.example.dto.BlogDto;
import com.example.dto.UserCredentialsDto;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import com.example.repository.BlogRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.BlogService;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class BlogTests {
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private UserService userService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private BlogService blogService;


    private Blog blog1 = new Blog();
    private Blog blog2 = new Blog();
    private final List<Blog> blogs = new ArrayList<>();
    private final User user = new User();

    @BeforeEach
    void init(){

        blog1.setUserId("1");
        blog1.setId("1");
        blog1.setValue("blog1");
        blog2.setUserId("1");
        blog2.setId("2");
        blog2.setValue("blog2");
        blogs.add(blog1);
        blogs.add(blog2);
    }
    @Test
    void getAllBlogs() {
        when(blogRepository.findAll()).thenReturn(blogs);
        var blogList = blogService.getAll();
        assertEquals(2, blogList.size());
        verify(blogRepository, times(1)).findAll();

    }
    @Test
    void getBlogById() throws CustomException {
        when(blogRepository.findById("1")).thenReturn(Optional.ofNullable(blog1));
        var byId = blogService.getById("1");
        assertEquals("1", byId.getId());
        assertEquals("1", byId.getUserId());
        assertEquals("blog1", byId.getValue());
        verify(blogRepository, times(1)).findById("1");
    }
    @Test
    void getBlogsByUserId() {
        when(blogRepository.findAllByUserId("1")).thenReturn(blogs);
        var blogsByUserId = blogService.getAllByUserId("1");
        assertEquals(2,blogsByUserId.size());
        verify(blogRepository, times(1)).findAllByUserId("1");
    }

    @Test
    void getBlogNotFound()  {
        var msg = "Blog with id 3 not found";
        var ex = assertThrows(CustomException.class, () -> blogService.getById("3"));
        assertEquals(msg, ex.getMessage());
        assertEquals(404,ex.getResponseStatus().value());
    }
    @Test
    void createNewBlog() throws CustomException {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication().getName()).thenReturn(user.getUsername());
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        var blogDto =new BlogDto();
        blogDto.setValue("New blog");
        var blogExpected = new Blog();
        blogExpected.setId("3");
        blogExpected.setUserId(blog1.getUserId());
        blogExpected.setValue(blogDto.getValue());
        when(blogRepository.save(isA(Blog.class))).thenReturn(blogExpected);
        var savedBlog =  blogService.save(blogDto);
        assertEquals(blogExpected.getUserId(),savedBlog.getUserId());
        assertEquals(blogExpected.getId(),savedBlog.getId());
        assertEquals(blogExpected.getValue(),savedBlog.getValue());
        verify(blogRepository, times(1)).save(any(Blog.class));
    }
    @Test
    void updateBlog() throws CustomException {
        user.setId(blog1.getUserId());
        user.setUsername("admin");
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(securityContext.getAuthentication().getName()).thenReturn(user.getUsername());
        when(userService.getByUsername(user.getUsername())).thenReturn(user);
        var blogDto =new BlogDto();
        blogDto.setValue("New blog update");
        blog1.setValue(blogDto.getValue());
        when(blogRepository.save(isA(Blog.class))).thenReturn(blog1);
        when(blogRepository.findById(blog1.getId())).thenReturn(Optional.ofNullable(blog1));
        var savedBlog =  blogService.updateById(blog1.getId(),blogDto);
        assertEquals(blog1.getValue(),savedBlog.getValue());
        verify(blogRepository, times(1)).save(any(Blog.class));
    }

}