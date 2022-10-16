package com.example.service;

import com.example.collection.Blog;
import com.example.collection.User;
import com.example.dto.BlogDto;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import com.example.repository.BlogRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final BlogRepository repository;
    private final UserService userService;
    public Blog save(BlogDto blog) throws CustomException {
        var blogToSave = new Blog();
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        blogToSave.setValue(blog.getValue());
        blogToSave.setUserId(userService.getByUsername(username).getId());
        return repository.save(blogToSave);

    }
    public List<Blog> getAllByUserId(String id){
       return repository.findAllByUserId(id);

    }
    public List<Blog> getAll(){
        return repository.findAll();
    }
    public Blog getById(String id) throws CustomException {
        var msg = "Blog with id "+id+" not found";
        return repository.findById(id).orElseThrow(()->new CustomException(msg, HttpStatus.NOT_FOUND,new ErrorResponse(msg)));
    }
    public void deleteById(String id) throws CustomException {
        var blogToDelete = getById(id);
        repository.delete(blogToDelete);
    }
    public Blog updateById(String id, BlogDto blog) throws CustomException {

        var blogToUpdate = getById(id);
        var user = userService.getByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user.getId().equals(blogToUpdate.getUserId()))
            blogToUpdate.setValue(blog.getValue());
        else {
            var msg = "Blog with id "+id+" does not belong to user with name "+user.getUsername();
            throw new CustomException(msg, HttpStatus.FORBIDDEN,new ErrorResponse(msg));
        }
       return repository.save(blogToUpdate);
    }
}
