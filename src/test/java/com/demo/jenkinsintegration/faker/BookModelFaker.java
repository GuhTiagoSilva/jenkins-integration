package com.demo.jenkinsintegration.faker;

import com.demo.jenkinsintegration.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookModelFaker {

    public static Book fakeOneWithBookId() {
        UUID existingBookId = UUID.randomUUID();
        return new Book(
                existingBookId,
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
    }

    public static List<Book> fakeManyWithBookId(int desiredAmount) {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < desiredAmount; i++) {
            books.add(fakeOneWithBookId());
        }

        return books;
    }

    public static Book fakeOneWithoutBookId() {
        return new Book(
                null,
                "Spider Man",
                "The Spider Man",
                305,
                200.0
        );
    }

    public static List<Book> fakeManyWithoutBookId(int desiredAmount) {
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < desiredAmount; i++) {
            books.add(fakeOneWithoutBookId());
        }

        return books;
    }


}
