package com.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {

    private Integer bookId;

    private String title;

    private String author;

    private String publisher;

    private Integer totalCopies;

    private Integer availableCopies;
}
