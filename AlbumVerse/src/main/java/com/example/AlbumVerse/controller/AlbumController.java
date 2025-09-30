package com.example.AlbumVerse.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.AlbumVerse.model.Account;
import com.example.AlbumVerse.model.Album;
import com.example.AlbumVerse.model.Photo;
import com.example.AlbumVerse.payload.album.AlbumPayloadDTO;
import com.example.AlbumVerse.payload.album.AlbumViewDTO;
import com.example.AlbumVerse.service.AccountService;
import com.example.AlbumVerse.service.AlbumService;
import com.example.AlbumVerse.service.PhotoService;
import com.example.AlbumVerse.utils.constants.AlbumError;
import com.example.AlbumVerse.utils.constants.AppUtils.AppUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/albums")
@Tag(name = "Album Controller", description = "APIs for managing albums and photos")
@Slf4j
public class AlbumController {

    static final String PHOTOS_FOLDER_NAME = "photos";
    static final String THUMBNAIL_FOLDER_NAME = "thumbnails";
    static final int THUMBNAIL_WIDTH = 300;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private PhotoService photoService;

    @PostMapping(value = "/add", produces = "application/json", consumes = "application/json")
    @Operation(summary = "Add a new album", description = "Creates a new album for the authenticated user")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "201", description = "Album created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid album data")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AlbumViewDTO> addAlbum(@Valid @RequestBody AlbumPayloadDTO albumPayloadDTO,
            Authentication authentication) {
        try {
            Album album = new Album();
            album.setName(albumPayloadDTO.getName());
            album.setDescription(albumPayloadDTO.getDescription());
            String email = authentication.getName();
            Optional<Account> accountOptional = accountService.findByEmail(email);
            Account account = accountOptional.get();
            album.setAccount(account);
            album = albumService.save(album);
            AlbumViewDTO albumViewDTO = new AlbumViewDTO();
            albumViewDTO.setId(album.getId());
            albumViewDTO.setName(album.getName());
            albumViewDTO.setDescription(album.getDescription());
            return ResponseEntity.ok(albumViewDTO);
        } catch (Exception e) {
            log.debug(AlbumError.ADD_ALBUM_ERROR.toString() + ": " + e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

    @GetMapping(value = "/albums", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Albums retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Operation(summary = "Get all albums for the authenticated user", description = "Retrieves all albums associated with the authenticated user")
    @SecurityRequirement(name = "bearerAuth")
    public List<AlbumViewDTO> Albums(Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> accountOptional = accountService.findByEmail(email);
        Account account = accountOptional.get();
        List<AlbumViewDTO> albums = new ArrayList<>();
        for (Album album : albumService.findByAccountId(account.getId())) {
            AlbumViewDTO albumViewDTO = new AlbumViewDTO();
            albumViewDTO.setId(album.getId());
            albumViewDTO.setName(album.getName());
            albumViewDTO.setDescription(album.getDescription());
            albums.add(albumViewDTO);

        }
        return albums;
    }

    @PostMapping(value = "/{album_id}/upload-photos", consumes = { "multipart/form-data" })
    @Operation(summary = "Add photos to an album", description = "Uploads and adds photos to a specified album")
    @ApiResponse(responseCode = "400", description = "Please check the payload or token")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<HashMap<String, List<String>>>> addPhotos(@PathVariable("album_id") Long albumId,
            @RequestPart(required = true) MultipartFile[] files, Authentication authentication) {
        String email = authentication.getName();
        Optional<Account> accountOptional = accountService.findByEmail(email);
        Account account = accountOptional.get();
        Optional<Album> albumOptional = albumService.findById(albumId);
        Album album;
        if (albumOptional.isPresent()) {
            album = albumOptional.get();
            if (account.getId() != album.getAccount().getId()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
        List<String> fileNamesWithSuccess = new ArrayList<>();
        List<String> fileNamesWithError = new ArrayList<>();
        Arrays.asList(files).stream().forEach(file -> {
            String contentType = file.getContentType();
            if (contentType.equals("image/jpeg") || contentType.equals("image/png")
                    || contentType.equals("image/jpg")) {
                fileNamesWithSuccess.add(file.getOriginalFilename());

                int length = 10;
                boolean useLetters = true;
                boolean useNumbers = true;
                try {

                    // What if user uploads same file name again?
                    String fileName = file.getOriginalFilename();
                    String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
                    String final_photo_name = generatedString + fileName;

                    // Save the file locally
                    String absolute_filelocation = AppUtil.get_photo_upload_path(final_photo_name, PHOTOS_FOLDER_NAME,
                            albumId);
                    // OS neutral path
                    Path path = Paths.get(absolute_filelocation);
                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    Photo photo = new Photo();
                    photo.setName(fileName);
                    photo.setFileName(final_photo_name);
                    photo.setOriginalFileName(fileName);
                    photo.setAlbum(album);
                    photoService.save(photo);

                    BufferedImage thumImg = AppUtil.getThumbnail(file, THUMBNAIL_WIDTH);
                    File thumbnail_location = new File(
                            AppUtil.get_photo_upload_path(final_photo_name, THUMBNAIL_FOLDER_NAME, albumId));
                    ImageIO.write(thumImg, file.getContentType().split("/")[1], thumbnail_location);
                } catch (Exception e) {
                    log.debug(AlbumError.PHOTO_UPLOAD_ERROR.toString() + ": " + e.getMessage());
                    fileNamesWithError.add(file.getOriginalFilename());
                }
            } else {
                fileNamesWithError.add(file.getOriginalFilename());
            }

        });
        HashMap<String, List<String>> response = new HashMap<>();
        response.put("success", fileNamesWithSuccess);
        response.put("error", fileNamesWithError);
        List<HashMap<String, List<String>>> responseList = new ArrayList<>();
        responseList.add(response);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{album_id}/photos/{photo_id}/download_photo")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getMethodName(@PathVariable("album_id") Long albumId,
            @PathVariable("photo_id") Long photoId, Authentication authentication) {

        String email = authentication.getName();
        Optional<Account> accountOptional = accountService.findByEmail(email);
        Account account = accountOptional.get();
           Optional<Album> albumOptional = albumService.findById(albumId);
            if (albumOptional.isPresent()) {
                Album album = albumOptional.get();
                if (account.getId() != album.getAccount().getId()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

       Optional<Photo> optionalPhoto=photoService.findById(photoId);
       if (optionalPhoto.isPresent()) {
           Photo photo = optionalPhoto.get();
           Resource resource=null;
           try{
            resource = AppUtil.getFileResource(albumId, PHOTOS_FOLDER_NAME, photo.getFileName());
           }
           catch(IOException e)
           {
            return ResponseEntity.internalServerError().build();
       }
       if (resource==null) {
        return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        
       }
       String contentType = "application/octet-stream";
       String headerValue=   "attachment; filename=\"" + photo.getOriginalFileName() + "\"";

       return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
               .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
               .body(resource);
    }else{
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

}
}