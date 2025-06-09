package com.platform.market.trading.cryptocurrency.cryptocurrency;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum CryptoType {

    BTC("Bitcoin", "BTC/USD"),
    ETH("Ethereum", "ETH/USD"),
    USDT("Tether", "USDT/USD"),
    XRP("Ripple", "XRP/USD"),
    SOL("Solana", "SOL/USD"),
    ADA("Cardano", "ADA/USD"),
    DOGE("Dogecoin", "DOGE/USD"),
    TRX("TRON", "TRX/USD"),
    WBTC("Wrapped Bitcoin", "WBTC/USD"),
    XLM("Stellar", "XLM/USD"),
    AVAX("Avalanche", "AVAX/USD"),
    LINK("Chainlink", "LINK/USD"),
    BCH("Bitcoin Cash", "BCH/USD"),
    DOT("Polkadot", "DOT/USD"),
    SUI("Sui", "SUI/USD"),
    TON("Toncoin", "TON/USD"),
    SHIB("Shiba Inu", "SHIB/USD"),
    AAVE("Aave", "AAVE/USD"),
    FIL("Filecoin", "FIL/USD"),
    ETC("Ethereum Classic", "ETC/USD");

    @Getter
    public static final String LEGACY_BTC_SYMBOL = "XBT/USD";
    @Getter
    public static final String LEGACY_DOGE_SYMBOL = "XDG/USD";

    @Getter
    private final String name;
    @Getter
    private final String symbol;

    CryptoType(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public static List<String> getAllSymbols() {
        return Arrays.stream(CryptoType.values())
            .map(CryptoType::getSymbol)
            .collect(Collectors.toList());
    }

    public static String validateLegacy(String crypto) {
        if (crypto.equals(LEGACY_BTC_SYMBOL)) {
            return BTC.getSymbol();
        } else if (crypto.equals(LEGACY_DOGE_SYMBOL)) {
            return DOGE.getSymbol();
        }

        return crypto;
    }
}
