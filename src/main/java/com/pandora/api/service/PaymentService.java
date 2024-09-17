package com.pandora.api.service;

import com.pandora.api.client.CoinPaymentsClient;
import com.pandora.api.dto.GetCommandPriceRequest;
import com.pandora.api.dto.GetCommandPriceResponse;
import com.pandora.api.dto.PriceDTO;
import com.pandora.api.dto.TransactionDTO;
import com.pandora.api.entity.DiscountCode;
import com.pandora.api.entity.SubscriptionCode;
import com.pandora.api.entity.Transaction;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import com.pandora.api.repository.DiscountCodeRepository;
import com.pandora.api.repository.SubscriptionCodeRepository;
import com.pandora.api.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final DiscountCodeRepository discountCodeRepository;
    private final CoinPaymentsClient coinPaymentsClient;
    private final TransactionRepository transactionRepository;
    private final SubscriptionCodeService subscriptionCodeService;
    private final SubscriptionCodeRepository subscriptionCodeRepository;
    private final SignatureChecker signatureChecker;
    @Value("${subscription.price.1M}")
    private Long m1;
    @Value("${subscription.price.3M}")
    private Long m3;
    @Value("${subscription.price.6M}")
    private Long m6;
    @Value("${subscription.price.12M}")
    private Long m12;
    private final Map<String, Long> prices = new HashMap<>();

    @PostConstruct
    public void setUp() {
        prices.put("1M", m1);
        prices.put("3M", m3);
        prices.put("6M", m6);
        prices.put("12M", m12);
    }

    public PriceDTO getPrice(String duration) {
        if (!prices.containsKey(duration)) {
            throw new NotFoundRestException("price not found");
        }
        return new PriceDTO().setPrice(prices.get(duration));
    }

    public GetCommandPriceResponse getPrice(GetCommandPriceRequest request) {
        if (!prices.containsKey(request.getDuration())) {
            throw new NotFoundRestException("price not found");
        }
        Long unitPrice = prices.get(request.getDuration());
        Long totalPrice = unitPrice * request.getAccounts();
        Long finalPrice = totalPrice;
        if (request.getCoupon() != null && !request.getCoupon().isEmpty()) {
            DiscountCode discountCode = discountCodeRepository.findFirstByCode(request.getCoupon());
            if (discountCode != null && request.getAccounts() >= discountCode.getMinAccounts()) {
                finalPrice = totalPrice - (totalPrice * discountCode.getDiscount() / 100);
            }
        }
        return new GetCommandPriceResponse()
                .setAccounts(request.getAccounts())
                .setDuration(request.getDuration())
                .setCoupon(request.getCoupon())
                .setUnitPrice(unitPrice)
                .setTotalPrice(totalPrice)
                .setFinalPrice(finalPrice);
    }

    public TransactionDTO createPayment(GetCommandPriceRequest getCommandPriceRequest) {
        GetCommandPriceResponse price = getPrice(getCommandPriceRequest);
        TransactionDTO transactionDTO = coinPaymentsClient.createPayment(price.getFinalPrice().doubleValue());
        transactionDTO.setAccounts(price.getAccounts());
        transactionDTO.setDuration(price.getDuration());
        transactionDTO.setCoupon(price.getCoupon());
        transactionDTO.setPrice(price.getFinalPrice());
        transactionDTO.setStatus("pending");
        Transaction transaction = new Transaction(transactionDTO);
        transaction = transactionRepository.save(transaction);
        transactionDTO.setOrderId(transaction.getId());
        return transactionDTO;
    }

    public TransactionDTO getTransaction(Long transactionId) {
        Transaction transaction = transactionRepository.findFirstByIdAndShowDetails(transactionId, true)
                .orElseThrow(() -> new NotFoundRestException("transaction not found"));
        TransactionDTO dto = new TransactionDTO(transaction);
        if (dto.getCountDown() < 0) {
            throw new NotFoundRestException("transaction not found");
        }
        if (dto.getStatus().equalsIgnoreCase("complete")) {
            dto.setCodes(subscriptionCodeRepository.findByTransactionId(transaction.getId())
                    .stream()
                    .map(SubscriptionCode::getCode)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    public TransactionDTO getTransaction(String transactionId) {
        Transaction transaction = transactionRepository.findFirstByTransactionId(transactionId);
        if (transaction == null) {
            throw new NotFoundRestException("transaction not found");
        }
        return getTransaction(transaction.getId());
    }

    public void done(String transactionId) {
        Transaction transaction = transactionRepository.findFirstByTransactionId(transactionId);
        if (transaction == null) {
            throw new NotFoundRestException("transaction not found");
        }
        transaction.setShowDetails(false);
        transactionRepository.save(transaction);
    }

    public void ipn(String address, int status, String body) {
        if (body != null) signatureChecker.check(body);
        Transaction transaction = transactionRepository.findFirstByTransactionIdAndShowDetailsAndStatus(address, true, "pending");
        if (transaction == null) {
            log.info("address not found {}", address);
            return;
        }
        log.info("transaction {} status update to {}", transaction.getTransactionId(), status);
        if (status >= 100) {
            transaction.setStatus("complete");
            transactionRepository.save(transaction);
            subscriptionCodeService.create(transaction);
        }
    }

    public void ipnTest(Long id) {
        Transaction transaction = transactionRepository.findFirstByIdAndShowDetails(id, true)
                .orElseThrow(() -> new NotFoundRestException("transaction not found"));
        if (transaction == null) {
            log.info("address not found {}", id);
            return;
        }
        ipn(transaction.getTransactionId(), 101, null);
    }
}
