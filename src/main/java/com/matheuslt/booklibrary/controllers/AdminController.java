package com.matheuslt.booklibrary.controllers;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheuslt.booklibrary.controllers.dtos.BookDto;
import com.matheuslt.booklibrary.models.Book;
import com.matheuslt.booklibrary.security.authentication.AuthenticationResponse;
import com.matheuslt.booklibrary.security.authentication.AuthenticationService;
import com.matheuslt.booklibrary.security.authentication.RegisterRequest;
import com.matheuslt.booklibrary.services.BookService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	private BookService bookService;
	private AuthenticationService authService;
	
	public AdminController(BookService bookService, AuthenticationService authService) {
		this.bookService = bookService;
		this.authService = authService;
	}
	
	@PostMapping()
	public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.registerAdmin(request));
	}
	
	@PostMapping("/users")
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.registerUser(request));
	}

	@GetMapping("/books")
	public ResponseEntity<Page<Book>> findAllBooks(
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
		return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll(pageable));
	}	
	
	@GetMapping("/books/{id}")
	public ResponseEntity<Object> findBookById(@PathVariable(value = "id") Integer id) {
		Optional<Book> bookOptional = bookService.findById(id);
		
		if (!bookOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(bookOptional.get());
	}
	
	@PostMapping("/books")
	public ResponseEntity<Book> saveBook(@RequestBody @Valid BookDto bookDto) {
		Book book = new Book();
		BeanUtils.copyProperties(bookDto, book);
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book));
	}
	
	@DeleteMapping("/books/{id}")
	public ResponseEntity<Object> deleteBook(@PathVariable(value = "id") Integer id) {
		Optional<Book> book = bookService.findById(id);
		
		if (!book.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found!");
		}
		
		bookService.delete(book.get());
		return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully");
	}
}
