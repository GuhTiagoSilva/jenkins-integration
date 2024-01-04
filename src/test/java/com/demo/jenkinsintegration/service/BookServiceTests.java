package com.demo.jenkinsintegration.service;

import com.demo.jenkinsintegration.dto.BookInsertDTO;
import com.demo.jenkinsintegration.dto.BookResponseDTO;
import com.demo.jenkinsintegration.exception.BookNotFoundException;
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
        UUID existingBookId = UUID.randomUUID();
        Book mockedBook = new Book(
                existingBookId,
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
        BookResponseDTO expectedBookResponseToBeCreated = new BookResponseDTO(
                existingBookId,
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );

        when(bookRepository.findById(existingBookId)).thenReturn(Optional.of(mockedBook));
        BookService service = new BookService(bookRepository);

        BookResponseDTO bookResponseDTO = service.findById(existingBookId);

        assertEquals(expectedBookResponseToBeCreated.id(), bookResponseDTO.id());
        assertEquals(expectedBookResponseToBeCreated.page(), bookResponseDTO.page());
        assertEquals(expectedBookResponseToBeCreated.title(), bookResponseDTO.title());
        assertEquals(expectedBookResponseToBeCreated.description(), bookResponseDTO.description());
        verify(bookRepository, times(1)).findById(existingBookId);
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when non existing id is informed")
    void shouldThrowBookNotFoundExceptionWhenNonExistingIdIsInformed() {
        UUID nonExistingId = UUID.randomUUID();
        when(bookRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        BookService service = new BookService(bookRepository);

        var bookNotFoundException = assertThrows(BookNotFoundException.class, () -> service.findById(nonExistingId));

        assertEquals("Book with id: " + nonExistingId + " not found", bookNotFoundException.getMessage());
        verify(bookRepository, times(1)).findById(nonExistingId);
    }

    @Test
    @DisplayName("Should create a book successfully when all data is valid")
    void shouldCreateABookSuccessfullyWhenAllDataIsValid() {
        UUID expectedBookIdToBeGeneratedAfterSaving = UUID.randomUUID();
        Book bookBeforeSaving = new Book(
                null,
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
        Book createdBook = new Book(
                expectedBookIdToBeGeneratedAfterSaving,
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
        BookInsertDTO bookInsertDTO = new BookInsertDTO(
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
        BookResponseDTO expectedBookResponseToBeCreated = new BookResponseDTO(
                expectedBookIdToBeGeneratedAfterSaving,
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
        when(bookRepository.save(bookBeforeSaving)).thenReturn(createdBook);
        BookService service = new BookService(bookRepository);

        BookResponseDTO bookResponseDTO = service.create(bookInsertDTO);

        assertEquals(expectedBookResponseToBeCreated.id(), expectedBookIdToBeGeneratedAfterSaving);
        assertEquals(expectedBookResponseToBeCreated.id(), bookResponseDTO.id());
        assertEquals(expectedBookResponseToBeCreated.page(), bookResponseDTO.page());
        assertEquals(expectedBookResponseToBeCreated.title(), bookResponseDTO.title());
        assertEquals(expectedBookResponseToBeCreated.description(), bookResponseDTO.description());

        assertEquals(bookInsertDTO.title(), bookResponseDTO.title());
        assertEquals(bookInsertDTO.description(), bookResponseDTO.description());
        assertEquals(bookInsertDTO.page(), bookResponseDTO.page());
        assertEquals(bookInsertDTO.price(), bookResponseDTO.price());
    }


}
