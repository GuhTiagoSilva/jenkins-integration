package com.demo.jenkinsintegration.repository;

import com.demo.jenkinsintegration.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
