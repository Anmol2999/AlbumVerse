package com.example.AlbumVerse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.AlbumVerse.model.Account;

import com.example.AlbumVerse.service.AccountService;

@Component
public class SeedData implements CommandLineRunner {

    

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {

        if (accountService.findAll().isEmpty()) {
            Account account1 = new Account();
            account1.setEmail("admin@example.com");
            account1.setPassword("admin123");
            account1.setRole("ROLE_ADMIN");
            accountService.save(account1);

            Account account2 = new Account();
            account2.setEmail("user@example.com");
            account2.setPassword("user123");
            account2.setRole("ROLE_USER");
            accountService.save(account2);

            System.out.println("✅ Seed data inserted");
        } else {
            System.out.println("✅ Seed data already exists");
        }
    }}
        