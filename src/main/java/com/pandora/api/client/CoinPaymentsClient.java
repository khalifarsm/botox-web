package com.pandora.api.client;

import com.pandora.api.dto.TransactionDTO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.HttpClients;
import org.brunocvcunha.coinpayments.CoinPayments;
import org.brunocvcunha.coinpayments.model.CreateTransactionResponse;
import org.brunocvcunha.coinpayments.model.ResponseWrapper;
import org.brunocvcunha.coinpayments.requests.CoinPaymentsCreateTransactionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
@Slf4j
public class CoinPaymentsClient {

    @Value("${coin.payments.key.public}")
    private String publicKey;
    @Value("${coin.payments.key.private}")
    private String privateKey;

    @Value("${coin.payments.ipn.email}")
    private String email;

    @Value("${coin.payments.currency}")
    private String currency;

    private CoinPayments api;

    @PostConstruct
    public void setup() {
        api = CoinPayments.builder()
                .publicKey(publicKey)
                .privateKey(privateKey)
                .client(HttpClients.createDefault()).build();
        //createPayment();
    }

    @SneakyThrows
    public TransactionDTO createPayment(Double amount) {
        ResponseWrapper<CreateTransactionResponse> txResponse = api.sendRequest(CoinPaymentsCreateTransactionRequest.builder().amount(10)
                .currencyPrice("USD")
                .amount(amount)
                .currencyTransfer(currency)
                .buyerEmail(email)
//                .callbackUrl("<callback-url-if-wanted>")
                .custom("Pandora subscription")
                .build());
        log.info(txResponse.getResult().getTransactionId() + " - " + txResponse.getResult().getStatusUrl());
        return new TransactionDTO(txResponse.getResult());
    }
}
