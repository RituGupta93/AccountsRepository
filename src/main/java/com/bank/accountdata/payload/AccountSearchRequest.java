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
public class AccountSearchRequest {

	@ApiModelProperty(position = 1, required = true, value = "Account ID")
	private String accountId;
	private String fromDate;
	private String toDate;
	private String fromAmount;
	private String toAmount;

}
