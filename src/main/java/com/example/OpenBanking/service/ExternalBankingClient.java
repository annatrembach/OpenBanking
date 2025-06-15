package com.example.OpenBanking.service;

import com.example.OpenBanking.dto.TransactionDTO;
import com.example.OpenBanking.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalBankingClient {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8080/mock-external-api";

    public BigDecimal getBalance(String iban) {
        String url = baseUrl + "/accounts/" + iban + "/balance";
        return restTemplate.getForObject(url, BigDecimal.class);
    }

    public List<TransactionDTO> getTransactions(String iban) {
        String url = baseUrl + "/accounts/" + iban + "/transactions";
        ResponseEntity<TransactionDTO[]> response = restTemplate.getForEntity(url, TransactionDTO[].class);
        return List.of(response.getBody());
    }

    public boolean sendPayment(Payment payment) {
        String url = baseUrl + "/payments/initiate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Payment> requestEntity = new HttpEntity<>(payment, headers);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(url, requestEntity, Void.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
