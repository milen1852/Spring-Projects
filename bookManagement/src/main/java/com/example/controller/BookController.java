package com.example.controller;

import com.example.dto.BookRequestDTO;
import com.example.dto.BookResponseDTO;
import com.example.dto.SpecificationRequestDTO;
import com.example.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @PostMapping("/book")
    public ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody BookRequestDTO bookRequestDTO){
        log.info("{}", bookRequestDTO);
        BookResponseDTO bookResponseDTO = bookService.addBook(bookRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponseDTO);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Integer bookId){

        BookResponseDTO bookResponse = bookService.getBookById(bookId);

        return ResponseEntity.ok(bookResponse);
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDTO>> getAllBooks(){

        List<BookResponseDTO> response = bookService.getAllBooks();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/book/{bookId}/decrease")
    public void decrease(@PathVariable Integer bookId){

        bookService.decreaseCopy(bookId);
    }

    @PutMapping("/book/{bookId}/increase")
    public void increase(@PathVariable Integer bookId){

        bookService.increaseCopy(bookId);
    }

    @PostMapping("/books")
    public ResponseEntity<Map<String, Object>> getBooks(@RequestBody SpecificationRequestDTO spec){

        log.info("{}", spec);
        Sort sort = spec.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(spec.getSortField()).descending()
                : Sort.by(spec.getSortField()).ascending();

        Pageable pageable = PageRequest.of(spec.getPage(), spec.getSize(), sort);

        Page<BookResponseDTO> bookPage = bookService.getBooks(spec, pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("content", bookPage.getContent());
        response.put("currentPage", bookPage.getNumber());
        response.put("totalPages", bookPage.getTotalPages());
        response.put("totalElements", bookPage.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/books/{bookId}/{newTotal}")
    public ResponseEntity<BookResponseDTO> updateCopies(@PathVariable Integer bookId, @PathVariable Integer newTotal){

        BookResponseDTO bookResponse = bookService.updateCopies(bookId, newTotal);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(bookResponse);
    }
}