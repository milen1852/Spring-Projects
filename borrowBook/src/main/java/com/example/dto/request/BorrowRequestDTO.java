package com.example.dto.request;

import com.example.entity.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRequestDTO {

    private String borrowId;

    @NotNull(message = "{bookId.NotNull}")
    @Min(value = 1, message = "{bookId.MinVal}")
    private Integer bookId;

    @NotBlank(message = "{borrowedBy.NotBlank}")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "{borrowedBy.Pattern}")
    private String borrowedBy;

    @NotNull(message = "{borrowDate.NotNull}")
    private LocalDate borrowDate;

    @NotNull(message = "{dueDate.NotNull}")
    private LocalDate dueDate;

    private LocalDate returnDate;

    private Status status;
}
