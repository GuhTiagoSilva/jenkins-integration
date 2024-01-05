package com.demo.jenkinsintegration.faker;

import com.demo.jenkinsintegration.dto.BookInsertDTO;

public class BookInsertDTOFaker {

    public static BookInsertDTO fakeOne() {
        return new BookInsertDTO(
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
    }

}
