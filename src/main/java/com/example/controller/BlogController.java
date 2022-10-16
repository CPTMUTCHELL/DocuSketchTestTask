package com.example.controller;

import com.example.collection.Blog;
import com.example.collection.User;
import com.example.dto.BlogDto;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import com.example.service.BlogService;
import com.example.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/blogs")
@RequiredArgsConstructor
@Validated
@Api(tags = "Blog controller")
public class BlogController {
    private final BlogService service;
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @ApiOperation(value = "Create blog")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created",response = Blog.class),
            @ApiResponse(code = 400, message = "Body mustn't be null"),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @PostMapping
    public ResponseEntity<Blog> create(
            @ApiParam(name = "Blog dto", value = "Blog text", required = true)
            @RequestBody @Valid BlogDto blog) throws CustomException {
        var savedPost =  service.save(blog);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @ApiOperation(value = "Delete blog by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Blog not found",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Blog> deleteById(
            @ApiParam(name = "id", example = "63493d1182cad339430831b3", type = "String", value = "blog id", required = true)
            @PathVariable String id) throws CustomException {
        service.deleteById(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @ApiOperation(value = "Update blog by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = Blog.class),
            @ApiResponse(code = 403, message = "Blog doesn't belong to this user",response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Body mustn't be null"),
            @ApiResponse(code = 404, message = "Blog not found",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Blog> updateById(
            @ApiParam(name = "id", example = "63493d1182cad339430831b3", type = "String", value = "blog id", required = true)
            @PathVariable String id,
            @ApiParam(name = "Blog dto", value = "Blog text", required = true)
            @RequestBody @Valid BlogDto blogDto) throws CustomException {
       var updatedBlog = service.updateById(id,blogDto);
        return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @ApiOperation(value = "Get all blogs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = Blog.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @GetMapping
    public ResponseEntity<List<Blog>> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @ApiOperation(value = "Get all blogs")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = Blog.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Blog>> getAllByUserId(
            @ApiParam(name = "id", example = "63493d1182cad339430831b3", type = "String", value = "user id", required = true)
            @PathVariable String userId){
        return new ResponseEntity<>(service.getAllByUserId(userId), HttpStatus.OK);
    }
    @ApiOperation(value = "Get blog by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = Blog.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Blog> getById(
            @ApiParam(name = "id", example = "63493d1182cad339430831b3", type = "String", value = "blog id", required = true)
            @PathVariable String id) throws CustomException {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }
}
