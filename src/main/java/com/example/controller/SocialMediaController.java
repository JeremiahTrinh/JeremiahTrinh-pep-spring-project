package com.example.controller;

import com.example.exception.*;
import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DONE: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Register account -> POST localhost:8080/register
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account){
        try{
            Account result = accountService.insertAccount(account);
            return ResponseEntity.status(200).body(result);
        }catch(DuplicateAccountException e){
            return ResponseEntity.status(409).body(null);
        }catch(InvalidAccountException e){
            return ResponseEntity.status(400).body(null);
        }
    }


    // Get account -> Verify user login -> POST localhost:8080/login
    @PostMapping("/login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account){
        Account result = accountService.getByLogin(account);
        if(result == null){
            return ResponseEntity.status(401).body(null);
        }else{
            return ResponseEntity.status(200).body(result);
        }
    }

    // Create new message -> POST localhost:8080/messages
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        try{
            Message result = messageService.insertMessage(message);
            return ResponseEntity.status(200).body(result);
        }catch(Exception e){
            return ResponseEntity.status(400).body(null);
        }
    }

    // Get all messages -> GET localhost:8080/messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.status(200).body(messages);
    }

    // Get message by its ID -> GET localhost:8080/messages/{messageId}
    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable("messageId") int messageId){
        return ResponseEntity.status(200).body(messageService.getMessage(messageId));
    }

    // Delete message by its ID -> DELETE localhost:8080/messages/{messageId}
    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable("messageId") int messageId){
        if(messageService.deleteMessage(messageId)){
            return ResponseEntity.status(200).body(1);
        }else{
            return ResponseEntity.status(200).body(null);
        }
    }

    // Update message text by its ID -> PATCH localhost:8080/messages/{messageId}
    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable("messageId") int messageId, @RequestBody Message update){
        try{
            if(messageService.updateMessage(messageId, update.getMessageText())){
                return ResponseEntity.status(200).body(1);
            }else{
                return ResponseEntity.status(400).body(null);
            }
        }catch(Exception e){
            return ResponseEntity.status(400).body(null);
        }
    }

    // Get all messages from a particular user -> GET localhost:8080/accounts/{accountId}/messages
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesFromUser(@PathVariable("accountId") int accountId){
        return ResponseEntity.status(200).body(messageService.getMessagesFromUser(accountId));
    }
}
