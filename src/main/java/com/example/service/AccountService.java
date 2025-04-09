package com.example.service;

import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
     * @param account
     * @return the Account object that has been persisted
     * @throws InvalidCredentialsException
     * @throws UsernameAlreadyExistsException
     */
    public Account registerUser(Account account) throws InvalidCredentialsException, UsernameAlreadyExistsException {
        // If username/password don't satisfy constraints, throw InvalidCredentialsException
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            throw new InvalidCredentialsException("Username must not be empty and password must be at least 4 characters long.");
        }

        // If username already exists, throw UsernameAlreadyExistsException
        Optional<Account> retrievedAccount = accountRepository.findByUsername(account.getUsername());
        if (retrievedAccount.isPresent()) {
            throw new UsernameAlreadyExistsException("Username " + account.getUsername() + " already exists.");
        }

        // Else, persist the account and return the object (now with an id)
        Account addedAccount = accountRepository.save(new Account(account.getUsername(), account.getPassword()));
        return addedAccount;
    }


    /**
     * Login to an existing account
     * @param account
     * @return the Account object if login was successful
     * @throws UsernameOrPasswordNotFoundException thrown if Username doesn't exist or password doesn't match
     */
    public Account userLogin(Account account) throws UsernameOrPasswordNotFoundException {
        Optional<Account> retrievedAccount = accountRepository.findByUsername(account.getUsername());
        if (retrievedAccount.isPresent()) {
            String retrievedPassword = retrievedAccount.get().getPassword();
            if (!retrievedPassword.equals(account.getPassword())) {
                // Password doesn't match; exception
                throw new UsernameOrPasswordNotFoundException("Password is incorrect");
            } else {
                // Username exists and password matches; return account
                return retrievedAccount.get();
            }
        } else {
            // Username doesn't exist; exception
            throw new UsernameOrPasswordNotFoundException("Username is incorrect");
        }
    }




}
