package com.platform.market.trading.cryptocurrency.wallet.controller.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateTransactionRequest {

    @NotBlank(message = "Symbol is required")
    @Pattern(
        regexp = "^(BTC|ETH|USDT|XRP|SOL|ADA|DOGE|TRX|WBTC|XLM|AVAX|LINK|BCH|DOT|SUI|TON|SHIB|AAVE|FIL|ETC)/USD$",
        message = "Symbol must be a valid trading pair like BTC/USD, ETH/USD, etc."
    )
    private String symbol;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00001", inclusive = true, message = "Amount must be greater than 0")
    private Double amount;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private Double price;

    @NotBlank(message = "Type is required")
    @Pattern(
        regexp = "^(buy|sell)$",
        flags = Pattern.Flag.CASE_INSENSITIVE,
        message = "Type must be either 'buy' or 'sell'"
    )
    private String type;

}
