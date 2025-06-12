package com.platform.market.trading.cryptocurrency.api.kraken;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.platform.market.trading.cryptocurrency.cryptocurrency.CryptoType;
import com.platform.market.trading.cryptocurrency.cryptocurrency.Cryptocurrency;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@SpringBootApplication
public class KrakenWsApp implements CommandLineRunner {

    private static final String KRAKEN_WS_URL = "wss://ws.kraken.com/";

    private static final Map<String, Cryptocurrency> cryptocurrencyMap = new ConcurrentHashMap<>();
    private static List<String> pairs;

    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        initializeCryptoMap();
        initializePairs();

        WebSocketClient client = new WebSocketClient(new URI(KRAKEN_WS_URL)) {
            @Override
            public void onOpen(ServerHandshake handshake) {
                System.out.println("Connected to Kraken WebSocket");

                // Build subscription JSON message
                try {
                    String subscribeMsg = buildSubscribeMessage(pairs);
                    System.out.println("Sending subscription message: " + subscribeMsg);
                    send(subscribeMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String message) {
                try {
                    JsonNode node = mapper.readTree(message);

                    // Kraken sends heartbeat messages as: {"event":"heartbeat"}
                    if (node.has("event")) {
                        String event = node.get("event").asText();
                        if ("subscriptionStatus".equals(event)) {
                            System.out.println("Subscription status: " + message);
                        }
                        return;
                    }

                    // Ticker data arrives as an array:
                    // [channelID, {ticker info}, channelName, pair]
                    if (node.isArray() && node.size() >= 4) {
                        JsonNode tickerData = node.get(1);
                        String pair = node.get(3).asText();

                        // Print basic ticker info - last trade price and volume
                        if (tickerData.has(TickerInfo.LAST_TRADE_CLOSED.getTicker()) &&
                            tickerData.get(TickerInfo.LAST_TRADE_CLOSED.getTicker()).isArray()) {
                            pair = CryptoType.validateLegacy(pair);
                            Cryptocurrency cryptocurrency = cryptocurrencyMap.get(pair);

                            if (cryptocurrency == null) {
                                System.out.printf("Failed to get %s%n", pair);
                            } else {
                                Double lastTradePrice = Double.parseDouble(
                                    tickerData.get(TickerInfo.LAST_TRADE_CLOSED.getTicker()).get(0).asText());
                                String volume = tickerData.get(TickerInfo.VOLUME.getTicker()).get(1).asText();
                                String bidPrice = tickerData.get(TickerInfo.BID.getTicker()).get(0).asText();

                                cryptocurrency.setPrice(lastTradePrice);
                                messagingTemplate.convertAndSend("/market/public", cryptocurrency);
                                System.out.printf("Ticker update %s: Last price: %s, Volume(24h): %s, Bid price: %s%n",
                                    pair, lastTradePrice, volume, bidPrice);
                            }
                        }
                    }

                } catch (Exception e) {
                    System.err.println("Error processing message: " + message);
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("Connection closed: " + reason + " Code: " + code);
            }

            @Override
            public void onError(Exception ex) {
                System.err.println("WebSocket error");
                ex.printStackTrace();
            }
        };

        client.connect();

        // Keep the app running so WebSocket stays open
        Thread.currentThread().join();
    }

    @Bean
    public static Map<String, Cryptocurrency> cryptocurrencyMap() {
        return cryptocurrencyMap;
    }

    private String buildSubscribeMessage(List<String> pairs) throws Exception {
        var root = mapper.createObjectNode();
        root.put("event", "subscribe");

        ArrayNode pairArray = root.putArray("pair");
        for (String pair : pairs) {
            pairArray.add(pair);
        }

        var subscription = mapper.createObjectNode();
        subscription.put("name", "ticker");
        root.set("subscription", subscription);

        return mapper.writeValueAsString(root);
    }

    @PostConstruct
    private void initializeCryptoMap() {
        for (CryptoType type : CryptoType.values()) {
            cryptocurrencyMap.put(type.getSymbol(), new Cryptocurrency(type.getName(), type.getSymbol(), 0.0));
        }
    }

    @PostConstruct
    private void initializePairs() {
        pairs = CryptoType.getAllSymbols();
    }
}
