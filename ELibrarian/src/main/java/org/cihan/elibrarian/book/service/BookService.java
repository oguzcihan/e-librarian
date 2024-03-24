package org.cihan.elibrarian.book.service;

import lombok.RequiredArgsConstructor;
import org.cihan.elibrarian.auth.service.AuthService;
import org.cihan.elibrarian.book.model.Book;
import org.cihan.elibrarian.book.model.BookRequest;
import org.cihan.elibrarian.book.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BookService {
    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final BookRepository bookRepository;

    public Page<Book> getAllBook(Pageable pageable) {
        log.info("getAllBook called");
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book createBook(BookRequest bookRequest) {
        log.info("createBook called with book: " + bookRequest);
        Book book = Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .language(bookRequest.getLanguage())
                .price(bookRequest.getPrice())
                .build();

        return bookRepository.save(book);
    }

    public Book updateBook(Book book) {
        log.info("updateBook called with book: " + book.getId());
        return bookRepository.save(book);
    }


    public void deleteBookById(Long id) {
        log.info("deleteBookById called with id: " + id);
        bookRepository.deleteById(id);
    }


}

