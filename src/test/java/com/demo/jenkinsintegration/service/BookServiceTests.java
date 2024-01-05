package com.demo.jenkinsintegration.service;

import com.demo.jenkinsintegration.dto.BookInsertDTO;
import com.demo.jenkinsintegration.dto.BookResponseDTO;
import com.demo.jenkinsintegration.exception.BookNotFoundException;
import com.demo.jenkinsintegration.faker.BookInsertDTOFaker;
import com.demo.jenkinsintegration.faker.BookModelFaker;
import com.demo.jenkinsintegration.faker.BookResponseDTOFaker;
import com.demo.jenkinsintegration.model.Book;
import com.demo.jenkinsintegration.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTests {

    private final BookRepository bookRepository = mock();

    @Test
    @DisplayName("Should return a valid book response when existing book id is informed")
    void shouldReturnAValidBookResponseWhenExistingBookIdIsInformed() {
        Book existingBook = BookModelFaker.fakeOneWithBookId();
        BookResponseDTO expectedBookResponse = BookResponseDTOFaker.fakeOne(existingBook.getId());
        when(bookRepository.findById(existingBook.getId())).thenReturn(Optional.of(existingBook));
        BookService bookService = new BookService(bookRepository);

        BookResponseDTO bookResponse = bookService.findById(existingBook.getId());

        assertEquals(expectedBookResponse.id(), bookResponse.id());
        assertEquals(expectedBookResponse.page(), bookResponse.page());
        assertEquals(expectedBookResponse.title(), bookResponse.title());
        assertEquals(expectedBookResponse.description(), bookResponse.description());
        verify(bookRepository, times(1)).findById(existingBook.getId());
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when non existing id is informed")
    void shouldThrowBookNotFoundExceptionWhenNonExistingIdIsInformed() {
        UUID nonExistingBookId = UUID.randomUUID();
        when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());
        BookService bookService = new BookService(bookRepository);

        var bookNotFoundException =
                assertThrows(BookNotFoundException.class, () -> bookService.findById(nonExistingBookId));

        assertEquals("Book with id: " + nonExistingBookId + " not found", bookNotFoundException.getMessage());
        verify(bookRepository, times(1)).findById(nonExistingBookId);
    }

    @Test
    @DisplayName("Should create a book successfully when all data is valid")
    void shouldCreateABookSuccessfullyWhenAllDataIsValid() {
        Book bookBeforeSaving = BookModelFaker.fakeOneWithoutBookId();
        Book bookAfterSaving = BookModelFaker.fakeOneWithBookId();
        BookInsertDTO bookInsertDTO = BookInsertDTOFaker.fakeOne();
        BookResponseDTO expectedBookResponse = BookResponseDTOFaker.fakeOne(bookAfterSaving.getId());
        when(bookRepository.save(bookBeforeSaving)).thenReturn(bookAfterSaving);
        BookService service = new BookService(bookRepository);

        BookResponseDTO bookResponse = service.create(bookInsertDTO);

        assertEquals(expectedBookResponse.id(), bookResponse.id());
        assertEquals(expectedBookResponse.page(), bookResponse.page());
        assertEquals(expectedBookResponse.title(), bookResponse.title());
        assertEquals(expectedBookResponse.description(), bookResponse.description());

        assertEquals(bookInsertDTO.title(), bookResponse.title());
        assertEquals(bookInsertDTO.description(), bookResponse.description());
        assertEquals(bookInsertDTO.page(), bookResponse.page());
        assertEquals(bookInsertDTO.price(), bookResponse.price());
        verify(bookRepository, atMost(1)).save(bookBeforeSaving);
    }
}
