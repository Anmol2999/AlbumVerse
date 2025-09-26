package com.example.AlbumVerse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.AlbumVerse.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    
}
