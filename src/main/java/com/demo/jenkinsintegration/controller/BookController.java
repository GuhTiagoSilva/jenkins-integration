package com.demo.jenkinsintegration.controller;

import com.demo.jenkinsintegration.dto.BookInsertDTO;
import com.demo.jenkinsintegration.dto.BookResponseDTO;
import com.demo.jenkinsintegration.service.BookService;
import io.micrometer.observation.annotation.Observed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Observed(name = "bookController")
@RequestMapping("/books")
@RequiredArgsConstructor
@Log4j2
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO save(@Valid @RequestBody BookInsertDTO bookInsertDTO) {
        log.info("Received request to create a book with following data: [{}]", bookInsertDTO);
        return bookService.create(bookInsertDTO);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponseDTO findById(@PathVariable UUID id) {
        log.info("Received request to find a book with following ID: [{}]", id);
        return bookService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<BookResponseDTO> findAll(@PageableDefault(
            sort = "title",
            direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("Received request to find all books with following pagination data: [{}]", pageable);
        return bookService.findAll(pageable);
    }
}
