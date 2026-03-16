package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "borrow_books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowBooks {

    @Id
    @Column(name = "borrow_id")
    private String borrowId;

    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "borrowed_by")
    private String borrowedBy;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
