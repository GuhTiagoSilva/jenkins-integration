package com.demo.jenkinsintegration.service;

import com.demo.jenkinsintegration.dto.BookInsertDTO;
import com.demo.jenkinsintegration.dto.BookResponseDTO;
import com.demo.jenkinsintegration.exception.BookNotFoundException;
import com.demo.jenkinsintegration.model.Book;
import com.demo.jenkinsintegration.repository.BookRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Observed(name = "bookService")
@RequiredArgsConstructor
@Log4j2
public class BookService {

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookResponseDTO findById(UUID id) {

        Book book = bookRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book with id: " + id + " not found"));

        log.info("Book with ID: [{}] found successfully", id);

        return BookResponseDTO.from(book);
    }

    public BookResponseDTO create(BookInsertDTO bookInsertDTO) {

        Book book = new Book();

        book.setPage(bookInsertDTO.page());
        book.setTitle(bookInsertDTO.title());
        book.setPrice(bookInsertDTO.price());
        book.setDescription(bookInsertDTO.description());

        book = bookRepository.save(book);

        log.info("Book created successfully");

        return BookResponseDTO.from(book);
    }

    @Transactional(readOnly = true)
    public Page<BookResponseDTO> findAll(Pageable pageable) {
        Page<Book> booksPaged = bookRepository.findAll(pageable);
        log.info("Found [{}] books", booksPaged.getSize());
        return booksPaged.map(BookResponseDTO::from);
    }
}
