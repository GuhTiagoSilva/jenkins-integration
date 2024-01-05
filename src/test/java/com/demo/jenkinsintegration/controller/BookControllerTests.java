package com.demo.jenkinsintegration.controller;

import com.demo.jenkinsintegration.dto.BookResponseDTO;
import com.demo.jenkinsintegration.faker.BookResponseDTOFaker;
import com.demo.jenkinsintegration.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookController.class)
class BookControllerTests {

    @Autowired

    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("Should find a book by id when existing ID is informed")
    void shouldFindABookByIdWhenExistingIdIsInformed() throws Exception {
        UUID existingBookId = UUID.randomUUID();
        BookResponseDTO bookResponse = BookResponseDTOFaker.fakeOne(existingBookId);
        when(bookService.findById(existingBookId)).thenReturn(bookResponse);

        var httpResponse =
                mockMvc.perform(get("/books/{id}", existingBookId).accept(MediaType.APPLICATION_JSON));


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
    }

}
