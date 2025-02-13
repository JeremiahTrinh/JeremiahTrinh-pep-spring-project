package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    public Message insertMessage(Message msg) throws InvalidMessageException{
        if(msg.getMessageText().isBlank() || msg.getMessageText().length() >= 255){
            throw new InvalidMessageException();
        }

        return messageRepository.save(msg);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessage(int id){
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if(optionalMessage.isPresent()){
            return optionalMessage.get();
        }else{
            return null;
        }
    }

    public boolean deleteMessage(int id){
        boolean result = false;
        if(getMessage(id) != null){
            messageRepository.deleteById(id);
            result = true;
        }
        return result;
    }

    public boolean updateMessage(int id, String text) throws InvalidMessageException{
        boolean result = false;
        if(text.isBlank() || text.length() > 255){
            throw new InvalidMessageException();
        }else if(getMessage(id) != null){
            System.out.println("Updated message: " + text);
            Message updated = getMessage(id);
            updated.setMessageText(text);
            messageRepository.save(updated);
            result = true;
        }
        return result;
    }

    public List<Message> getMessagesFromUser(int accountId){
        return messageRepository.findMessagesByPostedBy(accountId);
    }
}
