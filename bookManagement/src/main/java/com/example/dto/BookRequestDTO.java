package com.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotNull(message = "{bookId.NotNull}")
    @Min(value = 1, message = "{bookId.MinVal}")
    private Integer bookId;

    @NotBlank(message = "{title.NotBlank}")
    @Pattern(regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$", message = "{title.Pattern}")
    private String title;

    @NotBlank(message = "{author.NotBlank}")
    @Pattern(regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$", message = "{author.Pattern}")
    private String author;

    @NotBlank(message = "{publisher.NotBlank}")
    @Pattern(regexp = "^[A-Za-z]+(?:[ '-][A-Za-z]+)*$", message = "{publisher.Pattern}")
    private String publisher;

    @NotNull(message = "{tC.NotNull}")
    @Min(value = 0, message = "{tC.MinVal}")
    private Integer totalCopies;

    @NotNull(message = "{aC.NotNull}")
    @Min(value = 0, message = "{aC.MinVal}")
    private Integer availableCopies;
}
