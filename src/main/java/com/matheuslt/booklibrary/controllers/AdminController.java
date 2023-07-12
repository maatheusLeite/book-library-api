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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheuslt.booklibrary.controllers.dtos.BookDto;
import com.matheuslt.booklibrary.controllers.dtos.UserDto;
import com.matheuslt.booklibrary.models.Book;
import com.matheuslt.booklibrary.models.User;
import com.matheuslt.booklibrary.models.enums.LoanStatus;
import com.matheuslt.booklibrary.security.authentication.AuthenticationResponse;
import com.matheuslt.booklibrary.security.authentication.AuthenticationService;
import com.matheuslt.booklibrary.security.authentication.RegisterRequest;
import com.matheuslt.booklibrary.services.BookService;
import com.matheuslt.booklibrary.services.UserService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	private BookService bookService;
	private UserService userService;
	private AuthenticationService authService;
	
	public AdminController(BookService bookService, AuthenticationService authService, UserService userService) {
		this.bookService = bookService;
		this.userService = userService;
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
	
	@GetMapping("/users/{email}")
	public ResponseEntity<Object> findUserByEmail(@PathVariable(value = "email") String email) {
		Optional<User> userOptional = userService.findByEmail(email);
		
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
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
	
	@PutMapping("/books/{id}")
	public ResponseEntity<Object> update(@PathVariable(value = "id") Integer id, 
			@RequestBody @Valid Book bookDto) {
		
		Optional<Book> bookOptional = bookService.findById(id);
		if(!bookOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not Found.");
		}
		
		Book book = bookOptional.get();
		
		if (bookDto.getAuthor() != null) {
			book.setAuthor(bookDto.getAuthor());
		}
		if (bookDto.getDescription() != null) {
			book.setDescription(bookDto.getDescription());
		}
		if (bookDto.getName() != null) {
			book.setName(bookDto.getName());
		}
		if (bookDto.getPages() != null) {
			book.setPages(bookDto.getPages());
		}
		if (bookDto.getPublicationDate() != null) {
			book.setPublicationDate(bookDto.getPublicationDate());
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(bookService.save(book));
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
	
	@PutMapping("/books/lend/{id}")
	public ResponseEntity<Object> lendBook(@PathVariable(value = "id") Integer id, 
			@RequestBody @Valid UserDto userDto) {
		
		Optional<Book> bookOptional = bookService.findById(id);
		if (!bookOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not Found.");
		}
		
		if (bookOptional.get().getStatus() == LoanStatus.BORROWED) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("This book is already on loan.");
		}
		
		Optional<User> userOptional = userService.findByEmail(userDto.getEmail());
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found.");
		}
		
		User user = userOptional.get();
		Book book = bookOptional.get();
		book.setLoanedTo(user);
		book.setStatus(LoanStatus.BORROWED);
		
		return ResponseEntity.status(HttpStatus.OK).body(bookService.save(book));
	}
	
	@PutMapping("/books/recieve/{id}")
	public ResponseEntity<Object> recieveBook(@PathVariable(value = "id") Integer id, 
			@RequestBody @Valid UserDto userDto) {
		
		Optional<Book> bookOptional = bookService.findById(id);
		if (!bookOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not Found.");
		}
		
		Optional<User> userOptional = userService.findByEmail(userDto.getEmail());
		if (!userOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found.");
		}
		
		Book book = bookOptional.get();
		User user = userOptional.get();
		
		for (Book b: user.getBooksTaken()) {
			if(b.equals(book)) {
				book.setLoanedTo(null);
				book.setStatus(LoanStatus.AVAILABLE);
				
				return ResponseEntity.status(HttpStatus.OK).body(bookService.save(book));
			}
		}
		
		return ResponseEntity.status(HttpStatus.CONFLICT).body("This user has not borrowed this book.");
	}
}
