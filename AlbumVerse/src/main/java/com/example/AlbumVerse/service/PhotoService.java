package com.example.AlbumVerse.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AlbumVerse.model.Photo;
import com.example.AlbumVerse.repository.PhotoRepository;

@Service
public class PhotoService {
    
    @Autowired
    private PhotoRepository photoRepository;
    
    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    public Optional<Photo> findById(Long id) {
        return photoRepository.findById(id);
    }

    public List<Photo> findByAlbumId(Long albumId) {
        return photoRepository.findByAlbum_Id(albumId);
    }

}
