package com.example.AlbumVerse.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.AlbumVerse.model.Account;
import com.example.AlbumVerse.payload.auth.AccountDTO;
import com.example.AlbumVerse.payload.auth.AccountViewDTO;
import com.example.AlbumVerse.payload.auth.AuthoritiesDTO;
import com.example.AlbumVerse.payload.auth.PasswordDTO;
import com.example.AlbumVerse.payload.auth.ProfileDTO;
import com.example.AlbumVerse.payload.auth.TokenDTO;
import com.example.AlbumVerse.payload.auth.UserLoginDTO;
import com.example.AlbumVerse.service.AccountService;
import com.example.AlbumVerse.service.TokenService;
import com.example.AlbumVerse.utils.constants.AccountError;
import com.example.AlbumVerse.utils.constants.AccountSuccess;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

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
            log.debug(AccountError.TOKEN_GENERATION_ERROR.toString() + ": " + e.getMessage());
            return new ResponseEntity<>(new TokenDTO(null), HttpStatus.BAD_REQUEST);

        }

    }

    // consumes is the type of data that the endpoint will accept
    // produces is the type of data that the endpoint will return
    @PostMapping(value = "/users/add", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    // Operation is used to describe the endpoint in Swagger
    @Operation(summary = "Add a new user")
    // ApiResponse is used to describe the response of the endpoint in Swagger
    @ApiResponse(responseCode = "200", description = "User added successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or user already exists")
    public ResponseEntity<String> addUser(@Valid @RequestBody AccountDTO accountDTO) {
        try {
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            account.setAuthorities("ROLE_USER");
            accountService.save(account);
            // Save the account to the database
            return ResponseEntity.ok(AccountSuccess.ADD_ACCOUNT_SUCCESS.toString());
        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR.toString() + ":" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(value = "/users", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "List users")
     @SecurityRequirement(name = "bearerAuth")
    public List<AccountViewDTO> Users() {
        List<AccountViewDTO> accounts = new ArrayList<>();

        for(Account account:accountService.findAll()){
            AccountViewDTO dto = new AccountViewDTO();
            dto.setId(account.getId());
            dto.setEmail(account.getEmail());
            dto.setAuthorities(account.getAuthorities());
            accounts.add(dto);
        }
        return accounts;
    }

    @GetMapping(value = "/profile", produces = "application/json")
    @ApiResponse(responseCode = "200", description = "Profile retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Get user profile")
     @SecurityRequirement(name = "bearerAuth")
     //Spring Security will automatically inject the authenticated user's details into the Authentication object by converting the JWT token present in the request's Authorization header.
    public ProfileDTO profile(Authentication authentication) {
        //we are using email as username but spring security uses username as default
        String email=authentication.getName();
        Optional<Account> accountOpt=accountService.findByEmail(email);
        if (accountOpt.isPresent()) {
            Account account=accountOpt.get();
            ProfileDTO profileDTO=new ProfileDTO();
            profileDTO.setId(account.getId());
            profileDTO.setEmail(account.getEmail());
            profileDTO.setAuthorities(account.getAuthorities());
            return profileDTO;
            
        }

       return null;
    }

      @PutMapping(value = "/profile/update-password", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Password updated successfully")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update password")
     @SecurityRequirement(name = "bearerAuth")

    public AccountViewDTO updatePassword(@Valid @RequestBody PasswordDTO passwordDTO, Authentication authentication) {

        String email=authentication.getName();
        Optional<Account> accountOpt=accountService.findByEmail(email);
        
            Account account=accountOpt.get();
            account.setPassword(passwordDTO.getPassword());
            accountService.save(account);
            AccountViewDTO dto = new AccountViewDTO();
            dto.setId(account.getId());
            dto.setEmail(account.getEmail());
            dto.setAuthorities(account.getAuthorities());
            return dto;
       

      
    }


     @PutMapping(value = "/profile/{user_id}/update-authorities", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Authorities updated successfully")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Update authorities")
     @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<AccountViewDTO> updateAuth(@Valid @RequestBody AuthoritiesDTO authoritiesDTO,@PathVariable("user_id") long userId) {

        
        Optional<Account> accountOpt = accountService.findById(userId);
        if (accountOpt.isPresent()) {
            Account account=accountOpt.get();
            account.setAuthorities(authoritiesDTO.getAuthorities());
            accountService.save(account);
            AccountViewDTO dto = new AccountViewDTO();
            dto.setId(account.getId());
            dto.setEmail(account.getEmail());
            dto.setAuthorities(account.getAuthorities());
            return ResponseEntity.ok(dto);
        }

       return new ResponseEntity<>(new AccountViewDTO(),HttpStatus.NOT_FOUND);
    }

       @PutMapping(value = "/profile/delete", produces = "application/json", consumes = "application/json")
    @ApiResponse(responseCode = "200", description = "Profile deleted successfully")
    @ApiResponse(responseCode = "401", description = "Token missing")
    @ApiResponse(responseCode = "403", description = "Token error")
    @Operation(summary = "Delete profile")
     @SecurityRequirement(name = "bearerAuth")

    public ResponseEntity<String> deleteProfile(Authentication authentication) {

        String email=authentication.getName();
        Optional<Account> accountOpt=accountService.findByEmail(email);

        if (accountOpt.isPresent()) {
            Account account=accountOpt.get();
            accountService.deleteById(account.getId());
            return ResponseEntity.ok("User deleted successfully");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

}

         
