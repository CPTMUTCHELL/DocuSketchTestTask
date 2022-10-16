package com.example.controller;

import com.example.collection.User;
import com.example.dto.Token;
import com.example.dto.UserCredentialsDto;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "Authentication controller")
public class LoginController {
    private final UserService service;
    private final JwtUtil jwtUtil;
    @ApiOperation(value = "Login in to get the pair of tokens")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = Token.class),
            @ApiResponse(code = 500, message = "Internal error")
    })
    //Dummy controller
    @PostMapping("/login")
    public ResponseEntity<Token> login(
            @ApiParam(name = "Sign in object", value = "Fields required for the user login", required = true)
            @Valid @RequestBody UserCredentialsDto user,
            @ApiIgnore
            Authentication authentication) {
        return new ResponseEntity<>(jwtUtil.generateJwt(authentication),HttpStatus.OK);

    }
    @ApiOperation(value = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted",response = User.class),
            @ApiResponse(code = 409, message = "User with this username already exists",response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @PostMapping("/registration")
    public ResponseEntity<User> receiveRegistration(
            @ApiParam(name = "Registration object", value = "Fields required for the user registration", required = true)
            @Valid @RequestBody UserCredentialsDto user) throws CustomException {

            var saved = service.save(user);
            return new ResponseEntity<>(saved,HttpStatus.ACCEPTED);
    }


    @ApiOperation(value = "Generate new pair of tokens by refresh token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok",response = Token.class),
            @ApiResponse(code = 500, message = "Internal error")
    }
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Refresh Token", required = true, paramType = "header", example = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb2xBIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImV4cCI6MTY2Njc3Njg1OX0.Tmri0LLG8GT1kurAy6LcFXu4mdWF8A5Rhy2t7d1nDXo"),
    })
    @PreAuthorize("hasRole('ROLE_REFRESH')")
    @GetMapping("/token")
    public ResponseEntity<Token> generateNew(@ApiIgnore Authentication authentication) {
       var tokens = jwtUtil.generateJwt(authentication);

        return new ResponseEntity<>(tokens,HttpStatus.OK);
    }
}
