package com.example.AlbumVerse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.AlbumVerse.model.Album;


@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByAccountId(Long id);
  
}
