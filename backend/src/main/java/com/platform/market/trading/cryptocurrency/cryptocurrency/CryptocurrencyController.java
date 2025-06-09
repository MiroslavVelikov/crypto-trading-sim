package com.platform.market.trading.cryptocurrency.cryptocurrency;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class CryptocurrencyController {

    @MessageMapping("/market.sendCryptocurrency")
    @SendTo("/market/public")
    public Cryptocurrency sendCryptocurrency(
        @Payload Cryptocurrency cryptocurrency
    ) {
        return cryptocurrency;
    }

}
