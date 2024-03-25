package org.cihan.elibrarian.book.rest;


import org.cihan.elibrarian.book.model.Book;
import org.cihan.elibrarian.book.model.BookRequest;
import org.cihan.elibrarian.book.service.BookService;
import org.cihan.elibrarian.utils.PaginationUtil;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("api/v1/books")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getBooks(@ParameterObject Pageable pageable) {
        Page<Book> bookPage = bookService.getAllBook(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), bookPage);
        return ResponseEntity.ok()
                .headers(headers).body(bookPage.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Book> createBook(@RequestBody BookRequest bookRequest) throws URISyntaxException {
        Book result = bookService.createBook(bookRequest);
        return ResponseEntity.created(new URI("/api/v1/books/" + result.getId())).body(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Book> updateBook(@RequestBody Book book) {
        Book result = bookService.updateBook(book);
        return ResponseEntity.ok(result);
    }
}
