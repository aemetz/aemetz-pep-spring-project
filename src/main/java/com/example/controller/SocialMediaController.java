package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.entity.*;
import com.example.service.*;
import com.example.exception.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {

    private final AccountService accountService;

    public SocialMediaController(AccountService accountService) {
        this.accountService = accountService;
    }

    /*
     * HANDLER METHODS
     */

    @PostMapping("/register")
    public ResponseEntity<Account> postAccount(@RequestBody Account account) {
        System.out.println("INSIDE CONTROLLER!!!!!!!!!!!!!!!!!");
        Account newAccount = accountService.registerUser(account.getUsername(), account.getPassword());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(newAccount);
    }





    /*
     * EXCEPTION HANDLING
     */

    /**
     * Handle blank username / short password
     * @param ex
     * @return A ResponseEntity with a status of 400 BAD REQUEST
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleBadUsernamePassword(InvalidCredentialsException ex) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle duplicate username
     * @param ex
     * @return A ResponseEntity with a status of 409 CONFLICT
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameExists(UsernameAlreadyExistsException ex) {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }


}
