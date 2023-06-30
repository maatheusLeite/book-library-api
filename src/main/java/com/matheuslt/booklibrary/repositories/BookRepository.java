package com.matheuslt.booklibrary.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.matheuslt.booklibrary.models.Book;


public interface BookRepository extends JpaRepository<Book, Integer> {
	
	List<Book> findByNameContaining(String name);
	List<Book> findByAuthorContaining(String author);
}
