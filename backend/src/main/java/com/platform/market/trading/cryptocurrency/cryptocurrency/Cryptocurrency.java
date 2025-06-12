package com.platform.market.trading.cryptocurrency.cryptocurrency;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cryptocurrency {

    private String name;
    private String symbol;
    private Double price;

}
