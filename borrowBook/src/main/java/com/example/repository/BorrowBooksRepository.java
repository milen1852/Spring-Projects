package com.example.repository;

import com.example.entity.BorrowBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowBooksRepository extends JpaRepository<BorrowBooks, String>,
        JpaSpecificationExecutor<BorrowBooks> {

    Optional<BorrowBooks> findByBorrowIdAndBookId(String borrowId, Integer bookId);
}
