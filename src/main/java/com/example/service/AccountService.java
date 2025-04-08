package com.example.service;

import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;

import com.example.entity.Account;

import com.example.exception.*;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    /**
     * Persist a new account with the given username and password
     * 
     * @param username
     * @param password
     * @return the Account object that has been persisted
     */
    public Account registerUser(String username, String password) throws InvalidCredentialsException, UsernameAlreadyExistsException {
        // If username/password don't satisfy constraints, throw InvalidCredentialsException
        if (username.isBlank() || password.length() < 4) {
            System.out.println("Username " + username + " is blank.");
            System.out.println("OR Password " + password + " is <4 chars.");
            throw new InvalidCredentialsException("Username must not be empty and password must be at least 4 characters long.");
        }

        // If username already exists, throw UsernameAlreadyExistsException
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent()) {
            System.out.println("Username " + username + " already exists.");
            throw new UsernameAlreadyExistsException("Username " + username + " already exists.");
        }

        // Else, persist the account and return the object (now with an id)
        Account addedAccount = accountRepository.save(new Account(username, password));
        System.out.println("Returning added account...");
        return addedAccount;
    }

}
