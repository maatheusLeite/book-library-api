package com.matheuslt.booklibrary.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.matheuslt.booklibrary.models.Book;
import com.matheuslt.booklibrary.repositories.BookRepository;

import jakarta.transaction.Transactional;

@Service
public class BookService {
	
	private final BookRepository repository;
	
	public BookService(BookRepository repository) {
		this.repository = repository;
	}

	public Page<Book> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}
	
	public Optional<Book> findById(Integer id) {
		return repository.findById(id);
	}
	
	public List<Book> findByName(String name) {
		return repository.findByNameContaining(name);
	}
	
	public List<Book> findByAuthor(String author) {
		return repository.findByAuthorContaining(author);
	}
	
	@Transactional
	public Book save(Book book) {
		return repository.save(book);
	}
	
	@Transactional
	public void delete(Book book) {
		repository.delete(book);
	}
}
