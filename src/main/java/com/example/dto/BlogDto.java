package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel(description = "Blog dto", value = "blog text")

public class BlogDto {
    @NotNull(message = "Body mustn't be null")
    @ApiModelProperty(value = "Blog value", example = "Hi, it's my first blog")
    private String value;
}
