package com.example.AlbumVerse.payload.album;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AlbumPayloadDTO {
    @NotBlank
    @Schema(description = "Name of the album", example = "Vacation Photos", requiredMode= Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Schema(description = "Description of the album", example = "Photos from my vacation", requiredMode= Schema.RequiredMode.REQUIRED)
    private String description;
}
