package com.example.AlbumVerse.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.AlbumVerse.model.Account;
import com.example.AlbumVerse.payload.auth.AccountDTO;
import com.example.AlbumVerse.payload.auth.TokenDTO;
import com.example.AlbumVerse.payload.auth.UserLoginDTO;
import com.example.AlbumVerse.service.AccountService;
import com.example.AlbumVerse.service.TokenService;
import com.example.AlbumVerse.utils.constants.AccountError;
import com.example.AlbumVerse.utils.constants.AccountSuccess;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
// Request mapping is the path that the controller will listen to
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication and token generation")
@Slf4j
public class AuthController {

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  TokenService tokenService;

    @Autowired
    private AccountService accountService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDTO> token(@Valid @RequestBody UserLoginDTO userLogin) throws AuthenticationException {
       try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
        return ResponseEntity.ok(new TokenDTO(tokenService.generateToken(authentication)));
       } catch (Exception e) {
        log.debug(AccountError.TOKEN_GENERATION_ERROR.toString()+": " + e.getMessage());
        return new ResponseEntity<>(new TokenDTO(null), HttpStatus.BAD_REQUEST);

       }
        
    }

    //consumes is the type of data that the endpoint will accept
    //produces is the type of data that the endpoint will return
    @PostMapping(value="/users/add",consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    //Operation is used to describe the endpoint in Swagger
    @Operation(summary="Add a new user")
    //ApiResponse is used to describe the response of the endpoint in Swagger
    @ApiResponse(responseCode = "200", description = "User added successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    public ResponseEntity<String> addUser(@Valid @RequestBody AccountDTO accountDTO) {
        try {
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            account.setRole("ROLE_USER");
            accountService.save(account);
            // Save the account to the database
            return ResponseEntity.ok(AccountSuccess.ADD_ACCOUNT_SUCCESS.toString());
        } catch (Exception e) {
          log.debug(AccountError.ADD_ACCOUNT_ERROR.toString()+":"+e.getMessage());
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
