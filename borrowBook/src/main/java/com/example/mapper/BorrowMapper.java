package com.example.mapper;

import com.example.dto.request.BorrowRequestDTO;
import com.example.dto.response.BorrowResponseDTO;
import com.example.entity.BorrowBooks;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BorrowMapper {

    public BorrowBooks convertToEntity(BorrowRequestDTO requestDTO){

        return BorrowBooks.builder()
                .borrowId(UUID.randomUUID().toString())
                .bookId(requestDTO.getBookId())
                .borrowedBy(requestDTO.getBorrowedBy())
                .borrowDate(requestDTO.getBorrowDate())
                .dueDate(requestDTO.getDueDate())
                .returnDate(requestDTO.getReturnDate())
                .status(requestDTO.getStatus())
                .build();
    }

    public BorrowResponseDTO convertToResponse(BorrowBooks borrowBooks){

        return BorrowResponseDTO.builder()
                .borrowId(borrowBooks.getBorrowId())
                .bookId(borrowBooks.getBookId())
                .borrowedBy(borrowBooks.getBorrowedBy())
                .borrowDate(borrowBooks.getBorrowDate())
                .dueDate(borrowBooks.getDueDate())
                .returnDate(borrowBooks.getReturnDate())
                .status(borrowBooks.getStatus())
                .build();
    }
}
