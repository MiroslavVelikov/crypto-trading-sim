package com.platform.market.trading.cryptocurrency.trader.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginTraderRequest {

    @NotBlank(message = "Username is required")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "Username must contain at least one letter")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

}
