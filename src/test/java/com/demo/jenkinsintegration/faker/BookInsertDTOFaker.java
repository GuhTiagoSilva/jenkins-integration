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

    public static BookInsertDTO fakeOneWithEmptyTitle() {
        return new BookInsertDTO(
                "",
                "The Spider Man",
                305,
                200.0
        );
    }

    public static BookInsertDTO fakeOneWithNullTitle() {
        return new BookInsertDTO(
                null,
                "The Spider Man",
                305,
                200.0
        );
    }

    public static BookInsertDTO fakeOneWithEmptyDescription() {
        return new BookInsertDTO(
                "Spider Man",
                "",
                305,
                200.0
        );
    }

    public static BookInsertDTO fakeOneWithNullDescription() {
        return new BookInsertDTO(
                "Spider Man",
                null,
                305,
                200.0
        );
    }

    public static BookInsertDTO fakeOneWithNegativePrice() {
        return new BookInsertDTO(
                "Spider Man",
                "The Spider Man",
                305,
                -200.0
        );
    }

    public static BookInsertDTO fakeOneWithNegativePage() {
        return new BookInsertDTO(
                "Spider Man",
                "The Spider Man",
                -305,
                200.0
        );
    }

}
