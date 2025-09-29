package com.example.AlbumVerse.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.AlbumVerse.model.Account;
import com.example.AlbumVerse.model.Album;
import com.example.AlbumVerse.payload.album.AlbumPayloadDTO;
import com.example.AlbumVerse.payload.album.AlbumViewDTO;
import com.example.AlbumVerse.service.AccountService;
import com.example.AlbumVerse.service.AlbumService;
import com.example.AlbumVerse.utils.constants.AlbumError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;


@RestController
@RequestMapping("/albums")
@Tag(name = "Album Controller", description = "APIs for managing albums and photos")
@Slf4j
public class AlbumController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;

    @PostMapping(value="/add", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Add a new album", description = "Creates a new album for the authenticated user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Album created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid album data")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO, Authentication authentication) {
       try {
         Album album=new Album();
         album.setName(albumPayloadDTO.getName());
         album.setDescription(albumPayloadDTO.getDescription());
         String email=authentication.getName();
         Optional<Account> accountOptional = accountService.findByEmail(email);
        Account account = accountOptional.get();
        album.setAccount(account);
        album=albumService.save(album);
        AlbumViewDTO albumViewDTO = new AlbumViewDTO();
        albumViewDTO.setId(album.getId());
        albumViewDTO.setName(album.getName());
        albumViewDTO.setDescription(album.getDescription());
        return ResponseEntity.ok(albumViewDTO);
       } catch (Exception e) {
        log.debug(AlbumError.ADD_ALBUM_ERROR.toString()+": "+e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
       }
       
    }

    @GetMapping(value="/albums",produces="application/json")
    @ApiResponse(responseCode = "200", description = "Albums retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Get all albums for the authenticated user", description = "Retrieves all albums associated with the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    public List<AlbumViewDTO> Albums(Authentication authentication) {

        String email=authentication.getName();
         Optional<Account> accountOptional = accountService.findByEmail(email);
        Account account = accountOptional.get();
        List<AlbumViewDTO> albums =new ArrayList<>();
        for (Album album : albumService.findByAccountId(account.getId())) {
            AlbumViewDTO albumViewDTO = new AlbumViewDTO();
            albumViewDTO.setId(album.getId());
            albumViewDTO.setName(album.getName());
            albumViewDTO.setDescription(album.getDescription());
            albums.add(albumViewDTO);
            
        }
        return albums;
    }

    @PostMapping(value="/photos",  consumes = {"multipart/form-data"})
    @Operation(summary = "Add photos to an album", description = "Uploads and adds photos to a specified album")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<String>> addPhotos(@RequestPart(required=true) MultipartFile[] files) {
        List<String> fileNames = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file -> {
            fileNames.add(file.getOriginalFilename());     
        });
       return ResponseEntity.ok(fileNames);
    
    }
    
}
