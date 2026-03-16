package com.example.service;

import com.example.dto.BookRequestDTO;
import com.example.dto.BookResponseDTO;
import com.example.dto.SpecificationRequestDTO;
import com.example.entity.Book;
import com.example.exceptions.BookAvailableException;
import com.example.exceptions.BookNotFoundException;
import com.example.mapper.BookMapper;
import com.example.repository.BookRepository;
import com.example.repository.BookSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final BookSpecification bookSpecification;

    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO){
        if(bookRepository.existsById(bookRequestDTO.getBookId())){
            throw new BookAvailableException("Book with ID : " + bookRequestDTO.getBookId() + " already exists");
        }

        Book book = bookMapper.convertToEntity(bookRequestDTO);

        Book savedBook = bookRepository.save(book);

        return bookMapper.convertToResponse(savedBook);
    }

    public BookResponseDTO getBookById(Integer bookId) {

        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new BookNotFoundException("Book not found with ID : " + bookId));

        return bookMapper.convertToResponse(book);
    }

    public Page<BookResponseDTO> getBooks(SpecificationRequestDTO spec, Pageable pageable) {

        Specification<Book> specification = bookSpecification.buildFilter(spec);

        Page<Book> books = bookRepository.findAll(specification, pageable);

        return books.map(bookMapper::convertToResponse);
    }

    public List<BookResponseDTO> getAllBooks() {

        List<Book> books = bookRepository.findAll();

        return books.stream().map(bookMapper::convertToResponse).toList();
    }

    public void decreaseCopy(Integer bookId){

        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new BookNotFoundException("Book not found with ID : " + bookId));

        if(book.getAvailableCopies() <= 0)
            throw new BookNotFoundException("No copies available");

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        bookRepository.save(book);
    }

    public void increaseCopy(Integer bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new BookNotFoundException("Book not found with ID : " + bookId));

        book.setAvailableCopies(book.getAvailableCopies() + 1);

        bookRepository.save(book);
    }

    public BookResponseDTO updateCopies(Integer bookId, Integer newTotal) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        int diff = newTotal - book.getTotalCopies();  // +ve or -ve

        int newAvailable = book.getAvailableCopies() + diff;

        log.info("{}", newAvailable);

        if (newAvailable < 0)
            throw new BookAvailableException("Available copies cannot be negative");

        book.setTotalCopies(newTotal);
        book.setAvailableCopies(newAvailable);

        bookRepository.save(book);

        return bookMapper.convertToResponse(book);
    }
}
