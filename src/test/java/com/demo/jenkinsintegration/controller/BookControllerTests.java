package com.demo.jenkinsintegration.controller;

import com.demo.jenkinsintegration.dto.BookInsertDTO;
import com.demo.jenkinsintegration.dto.BookResponseDTO;
import com.demo.jenkinsintegration.exception.BookNotFoundException;
import com.demo.jenkinsintegration.faker.BookInsertDTOFaker;
import com.demo.jenkinsintegration.faker.BookResponseDTOFaker;
import com.demo.jenkinsintegration.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;


    @Test
    @DisplayName("Should return a book when existing book id is informed")
    void shouldReturnABookWhenExistingBookIsIsInformed() throws Exception {
        UUID existingBookId = UUID.randomUUID();
        BookResponseDTO bookResponse = BookResponseDTOFaker.fakeOne(existingBookId);
        when(bookService.findById(existingBookId)).thenReturn(bookResponse);

        var httpResponse =
                this.mockMvc.perform(get("/books/{id}", existingBookId).accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isOk());
        httpResponse.andExpect(jsonPath("$.id").exists());
        httpResponse.andExpect(jsonPath("$.title").exists());
        httpResponse.andExpect(jsonPath("$.description").exists());
        httpResponse.andExpect(jsonPath("$.page").exists());
        httpResponse.andExpect(jsonPath("$.price").exists());
        httpResponse.andExpect(jsonPath("$.id").value(bookResponse.id().toString()));
        httpResponse.andExpect(jsonPath("$.id").value(existingBookId.toString()));
        httpResponse.andExpect(jsonPath("$.title").value(bookResponse.title()));
        httpResponse.andExpect(jsonPath("$.description").value(bookResponse.description()));
        httpResponse.andExpect(jsonPath("$.page").value(bookResponse.page()));
        httpResponse.andExpect(jsonPath("$.price").value(bookResponse.price()));
        verify(bookService, times(1)).findById(existingBookId);
    }

    @Test
    @DisplayName("Should return [NOT FOUND] when non existing book id is informed")
    void shouldReturnNotFoundWhenNonExistingBookIdIsInformed() throws Exception {
        UUID nonExistingBookId = UUID.randomUUID();
        String errorMessage = "Book with id: " + nonExistingBookId + " not found";
        when(bookService.findById(nonExistingBookId)).thenThrow(new BookNotFoundException(errorMessage));

        var httpResponse = this.mockMvc.perform(get("/books/{id}", nonExistingBookId)
                .accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isNotFound());
        httpResponse.andExpect(jsonPath("$.timestamp").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").exists());
        httpResponse.andExpect(jsonPath("$.error").exists());
        httpResponse.andExpect(jsonPath("$.errorMessage").exists());
        httpResponse.andExpect(jsonPath("$.path").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").value(HttpStatus.NOT_FOUND.value()));
        httpResponse.andExpect(jsonPath("$.error").value("Book not found"));
        httpResponse.andExpect(jsonPath("$.errorMessage").value(errorMessage));
        verify(bookService, times(1)).findById(nonExistingBookId);
    }

    @Test
    @DisplayName("Should return [CREATED] when valid book payload is informed")
    void shouldReturnHttpCreatedStatusWhenValidBookPayloadIsInformed() throws Exception {
        UUID expectedBookIdToBeGeneratedAfterSaveBook = UUID.randomUUID();
        BookInsertDTO bookBeforeSaving = BookInsertDTOFaker.fakeOne();
        BookResponseDTO bookResponse = BookResponseDTOFaker.fakeOne(expectedBookIdToBeGeneratedAfterSaveBook);
        String bookBeforeSavingAsJson = objectMapper.writeValueAsString(bookBeforeSaving);
        when(bookService.create(bookBeforeSaving)).thenReturn(bookResponse);

        var httpResponse = this.mockMvc.perform(post("/books")
                .content(bookBeforeSavingAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isCreated());
        httpResponse.andExpect(jsonPath("$.id").exists());
        httpResponse.andExpect(jsonPath("$.title").exists());
        httpResponse.andExpect(jsonPath("$.description").exists());
        httpResponse.andExpect(jsonPath("$.page").exists());
        httpResponse.andExpect(jsonPath("$.price").exists());
        httpResponse.andExpect(jsonPath("$.id").value(bookResponse.id().toString()));
        httpResponse.andExpect(jsonPath("$.title").value(bookResponse.title()));
        httpResponse.andExpect(jsonPath("$.description").value(bookResponse.description()));
        httpResponse.andExpect(jsonPath("$.page").value(bookResponse.page()));
        httpResponse.andExpect(jsonPath("$.price").value(bookResponse.price()));
        verify(bookService, times(1)).create(bookBeforeSaving);
    }

    @Test
    @DisplayName("Should return validation error when book title is blank")
    void shouldReturnValidationErrorWhenBookTitleIsBlank() throws Exception {
        BookInsertDTO bookWithEmptyTitle = BookInsertDTOFaker.fakeOneWithEmptyTitle();
        String bookWithEmptyTitleAsJson = objectMapper.writeValueAsString(bookWithEmptyTitle);

        var httpResponse = this.mockMvc.perform(post("/books")
                .content(bookWithEmptyTitleAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isUnprocessableEntity());
        httpResponse.andExpect(jsonPath("$.timestamp").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").exists());
        httpResponse.andExpect(jsonPath("$.error").exists());
        httpResponse.andExpect(jsonPath("$.errorMessage").exists());
        httpResponse.andExpect(jsonPath("$.path").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));
        httpResponse.andExpect(jsonPath("$.error").value("Validation error"));
        verify(bookService, times(0)).create(bookWithEmptyTitle);
    }

    @Test
    @DisplayName("Should return validation error when book title is null")
    void shouldReturnValidationErrorWhenBookTitleIsNull() throws Exception {
        BookInsertDTO bookWithNullTitle = BookInsertDTOFaker.fakeOneWithNullTitle();
        String bookWithNullTitleAsJson = objectMapper.writeValueAsString(bookWithNullTitle);

        var httpResponse = this.mockMvc.perform(post("/books")
                .content(bookWithNullTitleAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isUnprocessableEntity());
        httpResponse.andExpect(jsonPath("$.timestamp").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").exists());
        httpResponse.andExpect(jsonPath("$.error").exists());
        httpResponse.andExpect(jsonPath("$.errorMessage").exists());
        httpResponse.andExpect(jsonPath("$.path").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));
        httpResponse.andExpect(jsonPath("$.error").value("Validation error"));
        verify(bookService, times(0)).create(bookWithNullTitle);
    }

    @Test
    @DisplayName("Should return validation error when book description is blank")
    void shouldReturnValidationErrorWhenBookDescriptionIsBlank() throws Exception {
        BookInsertDTO bookWithEmptyDescription = BookInsertDTOFaker.fakeOneWithEmptyDescription();
        String bookWithEmptyDescriptionAsJson = objectMapper.writeValueAsString(bookWithEmptyDescription);

        var httpResponse = this.mockMvc.perform(post("/books")
                .content(bookWithEmptyDescriptionAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isUnprocessableEntity());
        httpResponse.andExpect(jsonPath("$.timestamp").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").exists());
        httpResponse.andExpect(jsonPath("$.error").exists());
        httpResponse.andExpect(jsonPath("$.errorMessage").exists());
        httpResponse.andExpect(jsonPath("$.path").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));
        httpResponse.andExpect(jsonPath("$.error").value("Validation error"));
        verify(bookService, times(0)).create(bookWithEmptyDescription);
    }

    @Test
    @DisplayName("Should return validation error when book description is null")
    void shouldReturnValidationErrorWhenBookDescriptionIsNull() throws Exception {
        BookInsertDTO bookWithNullDescription = BookInsertDTOFaker.fakeOneWithNullDescription();
        String bookWithNullDescriptionAsJson = objectMapper.writeValueAsString(bookWithNullDescription);

        var httpResponse = this.mockMvc.perform(post("/books")
                .content(bookWithNullDescriptionAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isUnprocessableEntity());
        httpResponse.andExpect(jsonPath("$.timestamp").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").exists());
        httpResponse.andExpect(jsonPath("$.error").exists());
        httpResponse.andExpect(jsonPath("$.errorMessage").exists());
        httpResponse.andExpect(jsonPath("$.path").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));
        httpResponse.andExpect(jsonPath("$.error").value("Validation error"));
        verify(bookService, times(0)).create(bookWithNullDescription);
    }

    @Test
    @DisplayName("Should return validation error when book price is negative")
    void shouldReturnValidationErrorWhenBookPriceIsNegative() throws Exception {
        BookInsertDTO bookWithNegativePrice = BookInsertDTOFaker.fakeOneWithNegativePrice();
        String bookWithNegativePriceAsJson = objectMapper.writeValueAsString(bookWithNegativePrice);

        var httpResponse = this.mockMvc.perform(post("/books")
                .content(bookWithNegativePriceAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isUnprocessableEntity());
        httpResponse.andExpect(jsonPath("$.timestamp").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").exists());
        httpResponse.andExpect(jsonPath("$.error").exists());
        httpResponse.andExpect(jsonPath("$.errorMessage").exists());
        httpResponse.andExpect(jsonPath("$.path").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));
        httpResponse.andExpect(jsonPath("$.error").value("Validation error"));
        verify(bookService, times(0)).create(bookWithNegativePrice);
    }

    @Test
    @DisplayName("Should return validation error when book page is negative")
    void shouldReturnValidationErrorWhenBookPageIsNegative() throws Exception {
        BookInsertDTO bookWithNegativePage = BookInsertDTOFaker.fakeOneWithNegativePrice();
        String bookWithNegativePageAsJson = objectMapper.writeValueAsString(bookWithNegativePage);

        var httpResponse = this.mockMvc.perform(post("/books")
                .content(bookWithNegativePageAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        httpResponse.andExpect(status().isUnprocessableEntity());
        httpResponse.andExpect(jsonPath("$.timestamp").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").exists());
        httpResponse.andExpect(jsonPath("$.error").exists());
        httpResponse.andExpect(jsonPath("$.errorMessage").exists());
        httpResponse.andExpect(jsonPath("$.path").exists());
        httpResponse.andExpect(jsonPath("$.httpStatus").value(HttpStatus.UNPROCESSABLE_ENTITY.value()));
        httpResponse.andExpect(jsonPath("$.error").value("Validation error"));
        verify(bookService, times(0)).create(bookWithNegativePage);
    }
}
