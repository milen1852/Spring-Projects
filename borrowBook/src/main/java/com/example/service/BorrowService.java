package com.example.service;

import com.example.dto.request.BorrowRequestDTO;
import com.example.dto.request.SpecificationRequestDTO;
import com.example.dto.response.BookResponseDTO;
import com.example.dto.response.BorrowResponseDTO;
import com.example.entity.BorrowBooks;
import com.example.entity.Status;
import com.example.exceptions.BookNotFoundException;
import com.example.mapper.BorrowMapper;
import com.example.repository.BorrowBookSpecification;
import com.example.repository.BorrowBooksRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class BorrowService {

    private final BorrowBooksRepository borrowBooksRepository;

    private final BorrowMapper borrowMapper;

    private final BorrowBookSpecification borrowBookSpecification;

    private final RestTemplate restTemplate;
    private static final String BOOK_SERVICE = "http://localhost:8084/api/";

    @Transactional
    public BorrowResponseDTO borrowBook(BorrowRequestDTO requestDTO, String authHeader) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<BookResponseDTO> response = restTemplate.exchange(
                BOOK_SERVICE + "book/" + requestDTO.getBookId(), HttpMethod.GET, entity,
                BookResponseDTO.class);

        BookResponseDTO book = response.getBody();

        if(book == null)
            throw new BookNotFoundException("Book with ID : " + requestDTO.getBookId() + " not found.");

        if(book.getAvailableCopies() <= 0)
            throw new BookNotFoundException("Book Copies not available to borrow.");

        restTemplate.exchange(BOOK_SERVICE + "book/" + requestDTO.getBookId() + "/decrease",
                HttpMethod.PUT, entity, Void.class);

        BorrowBooks borrowBook = borrowMapper.convertToEntity(requestDTO);

        BorrowBooks savedBook = borrowBooksRepository.save(borrowBook);

        return borrowMapper.convertToResponse(savedBook);
    }

    public Page<BorrowResponseDTO> getAllBorrows(SpecificationRequestDTO spec, Pageable pageable) {

        Specification<BorrowBooks> specification = borrowBookSpecification.buildFilter(spec);

        Page<BorrowBooks> borrowBooks = borrowBooksRepository.findAll(specification, pageable);

        return borrowBooks.map(borrowMapper::convertToResponse);
    }

    @Transactional
    public BorrowResponseDTO returnBook(String borrowId, Integer bookId, String authHeader) {

        BorrowBooks borrowBook = borrowBooksRepository.findByBorrowIdAndBookId(borrowId, bookId).orElseThrow(() ->
                new BookNotFoundException("Book Not Found with ID: " + bookId));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        restTemplate.exchange(BOOK_SERVICE + "book/" + bookId + "/increase", HttpMethod.PUT, entity, Void.class);


        borrowBook.setReturnDate(LocalDate.now());
        borrowBook.setStatus(Status.RETURNED);

        borrowBooksRepository.save(borrowBook);

        return borrowMapper.convertToResponse(borrowBook);
    }

    public BorrowResponseDTO getBorrowBook(String borrowId, Integer bookId) {

        BorrowBooks borrowBook = borrowBooksRepository.findByBorrowIdAndBookId(borrowId, bookId).orElseThrow(() ->
                new BookNotFoundException("Book Not Found with ID: " + bookId));

        return borrowMapper.convertToResponse(borrowBook);
    }
}
