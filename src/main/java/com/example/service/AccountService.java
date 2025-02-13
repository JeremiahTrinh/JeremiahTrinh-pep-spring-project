package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = {InvalidAccountException.class, DuplicateAccountException.class})
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account getById(int id){
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            return optionalAccount.get();
        }else{
            return null;
        }
    }

    public Account getByLogin(Account account){
        Optional<Account> optionalAccount = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        if(optionalAccount.isPresent()){
            return optionalAccount.get();
        }else{
            return null;
        }
    }

    public Account insertAccount(Account account) throws InvalidAccountException, DuplicateAccountException{
        if(account.getUsername().isBlank() || account.getPassword().length() < 4){
            throw new InvalidAccountException();
        }

        if(accountRepository.findByUsername(account.getUsername()).isPresent()){
            throw new DuplicateAccountException();
        }

        return accountRepository.save(account);
    }
}
