package com.javadevzone.cotas.controllers;

import com.javadevzone.cotas.entity.Wallet;
import com.javadevzone.cotas.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wallet")
public class WalletController {

    private final WalletRepository walletRepository;

    @PostMapping
    @ResponseStatus(CREATED)
    public Wallet create(@RequestBody final Wallet wallet) {
        wallet.setQuota(BigDecimal.ONE);
        wallet.setCreatedAt(LocalDateTime.now());

        return walletRepository.save(wallet);
    }

    @PutMapping
    @ResponseStatus(OK)
    public Wallet update(@RequestBody final Wallet wallet) {
        wallet.setUpdatedAt(LocalDateTime.now());
        return walletRepository.save(wallet);
    }

    @GetMapping("/{wallet.id}")
    public ResponseEntity<Wallet> get(@PathVariable("wallet.id") Long walletId) {
        return walletRepository
                .findById(walletId)
                .map(wallet -> ResponseEntity.status(HttpStatus.OK).body(wallet))
                .orElseThrow(() ->
                        new ResponseStatusException(NOT_FOUND, format("Wallet com ID [%s] não foi encontrada.", walletId)));
    }

    @DeleteMapping("/{wallet.id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("wallet.id") Long walletId) {
        try {
            walletRepository.deleteById(walletId);
        }catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(NOT_FOUND, format("Wallet com ID [%s] não foi encontrada.", walletId));
        }
    }

}
