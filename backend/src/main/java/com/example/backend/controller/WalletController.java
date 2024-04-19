package com.example.backend.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;
import lombok.RequiredArgsConstructor;
import com.example.backend.dto.request.WalletRequest;
import com.example.backend.entities.Wallet;
import com.example.backend.exception.ApiRequestException;
import com.example.backend.services.WalletService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    @Value("${frontend.endpoint}")
    private String frontendEndpoint;

    /**
     * Endpoint to create the payment session
     * @param amount Money that the user wants to topup
     * @param principal user token
     * @return Stripe session to make the payment
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map> createPaymentIntent(@RequestParam("amount") String amount, Principal principal) {
        try {
            String sesisonId = walletService.createPaymentIntent(amount, principal);
            Map<String, Object> response = new HashMap<>();
            response.put("sessionId", sesisonId);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    /**
     * Endpoint for the user to topup their wallets
     * @param request information required for wallet topup
     * @return redirect url to the frontend
     */
    @GetMapping("/topup")
    public RedirectView topUp(@ModelAttribute WalletRequest request) {
        try {
            walletService.addMoney(request.getEmail(), request.getAmount());
            return new RedirectView(frontendEndpoint + "/wallet");
        } catch (Exception e) {
            return new RedirectView(frontendEndpoint + "/payment/error");
        }
    }

    /**
     * Endpoint to get the wallet history of a user
     * @param principal user token
     * @return all Transactions performed by a user
     */
    @GetMapping("/history")
    public ResponseEntity<List<Wallet>> walletHistory(Principal principal) {
        return ResponseEntity.ok(walletService.gethistory(principal));
    }

}
