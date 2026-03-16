package com.example.mapper;

import com.example.dto.BookRequestDTO;
import com.example.dto.BookResponseDTO;
import com.example.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book convertToEntity(BookRequestDTO requestDTO){

        return Book.builder()
                .bookId(requestDTO.getBookId())
                .title(requestDTO.getTitle())
                .author(requestDTO.getAuthor())
                .publisher(requestDTO.getPublisher())
                .totalCopies(requestDTO.getTotalCopies())
                .availableCopies(requestDTO.getAvailableCopies())
                .build();
    }

    public BookResponseDTO convertToResponse(Book book){

        return BookResponseDTO.builder()
                .bookId(book.getBookId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .build();
    }
}
