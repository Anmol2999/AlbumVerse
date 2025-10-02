package com.example.AlbumVerse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AlbumVerse.model.Album;
import com.example.AlbumVerse.repository.AlbumRepository;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;


    public Album save(Album album) {
        return albumRepository.save(album);
    }

    public List<Album> findByAccountId(Long id) {
        return albumRepository.findByAccountId(id);
    }
    public Optional<Album> findById(Long id) {
        return albumRepository.findById(id);
    }

    public void delete(Album album) {
        albumRepository.delete(album);
    }
}
