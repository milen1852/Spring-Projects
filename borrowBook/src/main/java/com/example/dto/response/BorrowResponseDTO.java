package com.example.dto.response;

import com.example.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowResponseDTO {

    private String borrowId;

    private Integer bookId;

    private String borrowedBy;

    private LocalDate borrowDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private Status status;
}
