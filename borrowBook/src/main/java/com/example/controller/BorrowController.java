package com.example.controller;

import com.example.dto.request.BorrowRequestDTO;
import com.example.dto.request.SpecificationRequestDTO;
import com.example.dto.response.BorrowResponseDTO;
import com.example.service.BorrowService;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowResponseDTO> borrowBook(@Valid @RequestBody BorrowRequestDTO requestDTO,
                                                        @RequestHeader("Authorization") String authHeader){

        BorrowResponseDTO responseDTO = borrowService.borrowBook(requestDTO, authHeader);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/borrow/{borrowId}/{bookId}")
    public ResponseEntity<BorrowResponseDTO> getBorrowedBook(@PathVariable String borrowId,
                                                             @PathVariable Integer bookId){

        BorrowResponseDTO borrowResponseDTO = borrowService.getBorrowBook(borrowId, bookId);

        return ResponseEntity.ok(borrowResponseDTO);
    }

    @PostMapping("/borrows")
    public ResponseEntity<Map<String, Object>> getBorrowBooks(@RequestBody SpecificationRequestDTO spec){

        log.info("{}", spec);
        Sort sort = spec.getSortDir().equalsIgnoreCase("desc")
                ? Sort.by(spec.getSortField()).descending()
                : Sort.by(spec.getSortField()).ascending();

        Pageable pageable = PageRequest.of(spec.getPage(), spec.getSize(), sort);

        Page<BorrowResponseDTO> borrowPage = borrowService.getAllBorrows(spec, pageable);

        Map<String, Object> response = new HashMap<>();

        response.put("content", borrowPage.getContent());
        response.put("currentPage", borrowPage.getNumber());
        response.put("totalPages", borrowPage.getTotalPages());
        response.put("totalElements", borrowPage.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/return/{borrowId}/{bookId}")
    public ResponseEntity<BorrowResponseDTO> returnBook(@PathVariable String borrowId, @PathVariable Integer bookId,
                                                        @RequestHeader("Authorization") String authHeader){

        BorrowResponseDTO responseDTO = borrowService.returnBook(borrowId, bookId, authHeader);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDTO);
    }
}
