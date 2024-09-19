package com.pandora.api.restcontrollers;

import com.pandora.api.dto.GetCommandPriceRequest;
import com.pandora.api.dto.GetCommandPriceResponse;
import com.pandora.api.dto.PriceDTO;
import com.pandora.api.dto.TransactionDTO;
import com.pandora.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Slf4j
public class PaymentRestController {

    private final PaymentService paymentService;

    @GetMapping("/unit-price/{duration}")
    @CrossOrigin
    public PriceDTO getPrice(@PathVariable("duration") String duration) {
        return paymentService.getPrice(duration);
    }

    @PostMapping("/price")
    @CrossOrigin
    public GetCommandPriceResponse getPrice(@RequestBody GetCommandPriceRequest request) {
        return paymentService.getPrice(request);
    }

    @PostMapping("/create")
    @CrossOrigin
    public TransactionDTO create(@RequestBody GetCommandPriceRequest request) {
        return paymentService.createPayment(request);
    }

    @PostMapping("/ipn")
    @CrossOrigin
    public void ipn(@RequestParam("txn_id") String transactionId, @RequestParam("status") int status, @RequestBody String body) {
        log.info("received IPN : {}", body);
        paymentService.ipn(transactionId, status, body);
    }

    @GetMapping("/trx/{id}")
    @CrossOrigin
    public TransactionDTO getTransaction(@PathVariable("id") String id) {
        return paymentService.getTransaction(id);
    }

    @GetMapping("/{id}/done")
    @CrossOrigin
    public void done(@PathVariable("id") String id) {
        paymentService.done(id);
    }

    //todo remove
    @GetMapping("/ipn/{id}")
    @CrossOrigin
    public void ipn(@PathVariable("id") Long id) {
        paymentService.ipnTest(id);
    }

    //todo remove
    @GetMapping("/{id}")
    @CrossOrigin
    public TransactionDTO getTransaction(@PathVariable("id") Long id) {
        return paymentService.getTransaction(id);
    }
}
