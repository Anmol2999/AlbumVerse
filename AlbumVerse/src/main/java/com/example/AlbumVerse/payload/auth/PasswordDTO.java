package com.example.AlbumVerse.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordDTO {
     @Size(min = 6, max = 20)
    @Schema(description = "User password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED,minLength = 6, maxLength = 20)
    private String password;
}
