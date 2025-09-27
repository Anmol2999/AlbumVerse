package com.example.AlbumVerse.payload.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritiesDTO {
    @NotBlank
    @Schema(description = "User authorities", example = "USER", requiredMode = Schema.RequiredMode.REQUIRED)
    private String authorities;
}
