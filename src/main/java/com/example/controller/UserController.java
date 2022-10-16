package com.example.controller;
import com.example.collection.User;
import com.example.exception.CustomException;
import com.example.exception.ErrorResponse;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
@Api(tags = "User controller")

public class UserController {
    private final UserService service;

    @ApiOperation(value = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = User.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })

    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }
    @ApiOperation(value = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = User.class),
            @ApiResponse(code = 404, message = "User not found",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(
            @ApiParam(name = "id", example = "63493d1182cad339430831b3", type = "String", value = "user id", required = true)
            @PathVariable String id) throws CustomException {
        return new ResponseEntity<>(service.getById(id), HttpStatus.OK);
    }
    @ApiOperation(value = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "User not found",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    public ResponseEntity<User> deleteById(
            @ApiParam(name = "id", example = "63493d1182cad339430831b3", type = "String", value = "user id", required = true)
            @PathVariable String id) throws CustomException {
        service.deleteById(id);
        return new ResponseEntity<>( HttpStatus.NO_CONTENT);
    }

}
