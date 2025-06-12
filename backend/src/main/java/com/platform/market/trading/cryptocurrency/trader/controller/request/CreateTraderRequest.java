package com.platform.market.trading.cryptocurrency.trader.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTraderRequest {

    @NotBlank(message = "Username is required")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "Username must contain at least one letter")
    private String username;

    @NotBlank(message = "First name is required")
    @Size(min = 3, message = "First name must be at least 3 characters long")
    @Pattern(regexp = "[a-zA-Z]+", message = "First name must contain only letters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 3, message = "Last name must be at least 3 characters long")
    @Pattern(regexp = "[a-zA-Z]+", message = "Last name must contain only letters")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;

}
