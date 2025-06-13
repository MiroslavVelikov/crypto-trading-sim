package com.platform.market.trading.cryptocurrency.wallet.controller;

import com.platform.market.trading.cryptocurrency.transaction.models.TransactionOutput;
import com.platform.market.trading.cryptocurrency.transaction.models.TransactionType;
import com.platform.market.trading.cryptocurrency.wallet.controller.request.CreateTransactionRequest;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletHoldingsOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletOutput;
import com.platform.market.trading.cryptocurrency.wallet.models.WalletTransactionsOutput;
import com.platform.market.trading.cryptocurrency.wallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class WalletControllerImpl implements WalletController {
    // IF YOU HAVE TIME FIX THIS CONTROLLER OUTPUTS

    private static final String TRADER_ID_TOKEN = "traderId";
    private static final String WALLET_ID_TOKEN = "walletId";

    @Autowired
    private WalletService walletService;

    @Override
    @GetMapping
    public ResponseEntity<WalletOutput> getWallet(
        @CookieValue(TRADER_ID_TOKEN) UUID traderId
    ) {
        return new ResponseEntity<>(walletService.getWalletByTraderId(traderId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/transactions")
    public ResponseEntity<WalletTransactionsOutput> getTransactions(
        @CookieValue(TRADER_ID_TOKEN) UUID traderId,
        @RequestHeader(WALLET_ID_TOKEN) UUID walletId
    ) {
        return new ResponseEntity<>(walletService.getWalletTransactions(walletId, traderId), HttpStatus.OK);
    }

    @Override
    @GetMapping("/holdings")
    public ResponseEntity<WalletHoldingsOutput> getHoldings(
        @CookieValue(TRADER_ID_TOKEN) UUID traderId,
        @RequestHeader(WALLET_ID_TOKEN) UUID walletId
    ) {
        return new ResponseEntity<>(walletService.getWalletHoldings(walletId, traderId), HttpStatus.OK);

    }

    @Override
    @PostMapping("/holdings/buy")
    public ResponseEntity<TransactionOutput> buyHolding(
        @CookieValue(TRADER_ID_TOKEN) UUID traderId,
        @RequestHeader(WALLET_ID_TOKEN) UUID walletId,
        @Valid @RequestBody CreateTransactionRequest transactionRequest
    ) {
        TransactionOutput transactionOutput = walletService.makeTransaction(
            walletId,
            traderId,
            transactionRequest.getSymbol(),
            transactionRequest.getAmount(),
            transactionRequest.getPrice(),
            TransactionType.BUY
        );
        return transactionOutput != null ?
            new ResponseEntity<>(transactionOutput, HttpStatus.OK) : new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @Override
    @PostMapping("/holdings/sell")
    public ResponseEntity<TransactionOutput> sellHolding(
        @CookieValue(TRADER_ID_TOKEN) UUID traderId,
        @RequestHeader(WALLET_ID_TOKEN) UUID walletId,
        @Valid @RequestBody CreateTransactionRequest transactionRequest
    ) {
        return new ResponseEntity<>(walletService.makeTransaction(
            walletId,
            traderId,
            transactionRequest.getSymbol(),
            transactionRequest.getAmount(),
            transactionRequest.getPrice(),
            TransactionType.SELL
        ), HttpStatus.OK);
    }

    @Override
    @GetMapping("/reset")
    public ResponseEntity<WalletOutput> resetWallet(
        @CookieValue(TRADER_ID_TOKEN) UUID traderId,
        @RequestHeader(WALLET_ID_TOKEN) UUID walletId
    ) {
        return new ResponseEntity<>(walletService.resetWallet(walletId, traderId), HttpStatus.RESET_CONTENT);
    }

}
