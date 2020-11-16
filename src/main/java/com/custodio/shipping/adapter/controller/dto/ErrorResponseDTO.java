package com.custodio.shipping.adapter.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ApiModel(description = "API error response")
public class ErrorResponseDTO {

    @ApiModelProperty(position = 1, value = "The reason why an error occurred.", required = true, example = "The query was not performed with success.")
    private final String reason;
}
