package com.example.service;

import org.springframework.stereotype.*;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceSupport;

import com.example.entity.*;
import com.example.exception.*;

import java.util.List;
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

    /**
     * Get all messages
     * @return a List of message objects
     */
    public List<Message> getAllMessages() {
        List<Message> retrievedMessages = messageRepository.findAll();
        return retrievedMessages;
    }

    /**
     * Get a message based on its ID
     * @param id
     * @return a Message object (may be empty)
     */
    public Message getMessageById(int id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            return message.get();
        }
        return null;
    }

    /**
     * Delete a message based on its ID
     * @param id
     * @return 0 or 1, based on whether the message existed to begin with
     */
    public int deleteMessageById(int id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }


    /**
     * Patch/update a message based on its ID and new text
     * @param id
     * @param text
     * @return 1 if successful
     * @throws InvalidMessageException
     */
    public int patchMessageById(int id, String text) throws InvalidMessageException {
        // If message ID does not exist, throw exception
        Optional<Message> message = messageRepository.findById(id);
        if (!message.isPresent()) {
            throw new InvalidMessageException("No message with id " + id + " exists.");
        }
        
        // If new text is invalid, throw exception
        if (text.isBlank()) {
            throw new InvalidMessageException("Message text cannot be blank.");
        } else if (text.length() > 255) {
            throw new InvalidMessageException("Message text cannot be over 255 characters.");
        }

        // Else, update the message and return 1
        Message updatedMessage = message.get();
        updatedMessage.setMessageText(text);
        messageRepository.save(updatedMessage);
        return 1;
    }


    /**
     * Get all messages posted by a user with id = accountId
     * @param accountId
     * @return List of Message objects (can be empty)
     */
    public List<Message> getMessagesByAccountId(int accountId) {
        return messageRepository.findAllByPostedBy(accountId);
    }
    
}
