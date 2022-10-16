package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(description = "User credentials", value = "Fields required for registration")

public class UserCredentialsDto {
    @NotNull(message = "Name mustn't be null")
    @ApiModelProperty(value = "username", example = "user1")
    private String username;
    @NotNull(message = "pass mustn't be null")
    @ApiModelProperty(value = "password", example = "pass1")
    private String  password;

}

