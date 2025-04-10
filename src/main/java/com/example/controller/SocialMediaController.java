package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.entity.*;
import com.example.service.*;
import com.example.exception.*;

import java.util.List;
import java.util.Map;

 @RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /*
     * ACCOUNT HANDLER METHODS
     */

    /**
     * Handler for registering a new user
     * @param account
     * @return ResponseEntity with status 200 OK and persisted account
     */
    @PostMapping("/register")
    public ResponseEntity<Account> postAccount(@RequestBody Account account) {
        Account newAccount = accountService.registerUser(account);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(newAccount);
    }

    /**
     * Handler for logging in to an existing account
     * @param account
     * @return ResponseEntity with status 200 OK and account
     */
    @PostMapping("/login")
    public ResponseEntity<Account> postLogin(@RequestBody Account account) {
        Account loggedInAccount = accountService.userLogin(account);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(loggedInAccount);
    }


    /*
     * MESSAGE HANDLER METHODS
     */

    
    /**
     * Handler for posting a new message
     * @param message
     * @return ResponseEntity with status 200 OK and message
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(createdMessage);
    }

    /**
     * Handler for getting all messages
     * @return ResponseEntity with status 200 OK and a List of Message objects
     */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messages);
    }

    /**
     * Handler for getting a message based on ID
     * @param messageId
     * @return ResponseEntity with status 200 OK and a Message object (or null)
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.getMessageById(messageId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(message);
    }

    /**
     * Handler for Deleting a message based on its ID
     * @param messageId
     * @return ResponseEntity with status 200 OK and body of 1 if message was deleted, body null if it didn't exist
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        int rowsAffected = messageService.deleteMessageById(messageId);
        if (rowsAffected == 0) {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
        } else {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(rowsAffected);
        }
    }

    /**
     * Handler for updating an existing message
     * @param messageId
     * @return ResponseEntity with status 200 OK and a body of the number of rows affected
     */
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessageById(@PathVariable int messageId, @RequestBody Map<String, String> messageText) {
        int rowsAffected = messageService.patchMessageById(messageId, messageText.get("messageText"));
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(rowsAffected);
    }

    /**
     * Handler for getting all messages posted by a particular user
     * @param accountId
     * @return List of Message objects (may be empty)
     */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable int accountId) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(messageService.getMessagesByAccountId(accountId));
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handle duplicate username
     * @param ex
     * @return A ResponseEntity with a status of 409 CONFLICT
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameExists(UsernameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Handle incorrect username/password
     * @param ex
     * @return ResponseEntity with a status of 401 UNAUTHORIZED
     */
    @ExceptionHandler(UsernameOrPasswordNotFoundException.class)
    public ResponseEntity<String> handleUsernameOrPasswordNotFound(UsernameOrPasswordNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Handle invalid message parameters
     * @param ex
     * @return ResponseEntity with a status of 400 BAD REQUEST
     */
    @ExceptionHandler(InvalidMessageException.class)
    public ResponseEntity<String> handleInvalidMessage(InvalidMessageException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


}
