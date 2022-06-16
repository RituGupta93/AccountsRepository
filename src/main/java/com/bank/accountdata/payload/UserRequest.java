package com.bank.accountdata.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
@ApiModel
public class UserRequest {
	@ApiModelProperty(position = 1, required = true, value = "Username")
	String username;
	@ApiModelProperty(position = 2, required = true, value = "Password")
	String password;

}
