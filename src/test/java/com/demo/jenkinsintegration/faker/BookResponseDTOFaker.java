package com.demo.jenkinsintegration.faker;

import com.demo.jenkinsintegration.dto.BookResponseDTO;

import java.util.UUID;

public class BookResponseDTOFaker {

    public static BookResponseDTO fakeOne(UUID expectedBookId) {
        return new BookResponseDTO(
                expectedBookId,
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
    }

}
