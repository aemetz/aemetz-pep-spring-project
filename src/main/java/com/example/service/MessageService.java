package com.example.service;

import org.springframework.stereotype.*;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceSupport;

import com.example.entity.*;
import com.example.exception.*;

import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Persist a new Message
     * @param message
     * @return the Message object that has been persisted
     * @throws InvalidMessageException thrown if message parameters are invalid
     */
    public Message createMessage(Message message) throws InvalidMessageException {
        // Check that method is not blank and is at most 255 characters
        if (message.getMessageText().isBlank()) {
            throw new InvalidMessageException("Message text cannot be blank.");
        } else if (message.getMessageText().length() > 255) {
            throw new InvalidMessageException("Message text cannot be over 255 characters.");
        }

        // Check that postedBy user exists
        Optional<Account> user = accountRepository.findById(message.getPostedBy());
        if (!user.isPresent()) {
            throw new InvalidMessageException("Message must be posted by an existing user.");
        } else {
            return messageRepository.save(message);
        }
    }

    
}
