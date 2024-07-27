package org.cihan.elibrarian.book.service;

import lombok.RequiredArgsConstructor;
import org.cihan.elibrarian.app.auth.service.AuthService;
import org.cihan.elibrarian.book.model.Book;
import org.cihan.elibrarian.book.model.BookRequest;
import org.cihan.elibrarian.book.model.Category;
import org.cihan.elibrarian.book.repository.BookRepository;
import org.cihan.elibrarian.book.repository.CategoryRepository;
import org.cihan.elibrarian.cart.models.Cart;
import org.cihan.elibrarian.cart.models.CartItem;
import org.cihan.elibrarian.cart.models.CartResponse;
import org.cihan.elibrarian.cart.repository.CartRepository;
import org.cihan.elibrarian.cart.service.CartService;
import org.cihan.elibrarian.exceptions.GenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookService {
    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    public Page<Book> getAllBook(Pageable pageable) {
        log.info("getAllBook called");
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(Long id) {
        log.info("getBookById called with id: " + id);
        if (id == null) {
            throw new GenException("Book id cannot be null", HttpStatus.BAD_REQUEST.value());
        }
        return bookRepository.findById(id).orElse(null);
    }

    public Book createBook(BookRequest bookRequest) {
        log.info("createBook called with book: " + bookRequest);

        Category category = categoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new GenException("Category" + "categoryId" + bookRequest.getCategoryId(), HttpStatus.BAD_REQUEST.value()));

        boolean isBookNotPresent = category.getBooks().stream()
                .noneMatch(p -> p.getName().equals(bookRequest.getName())
                        && p.getAuthor().equals(bookRequest.getAuthor()));

        if (isBookNotPresent) {
            Book book = Book.builder()
                    .name(bookRequest.getName())
                    .author(bookRequest.getAuthor())
                    .publisher(bookRequest.getPublisher())
                    .price(bookRequest.getPrice())
                    .stockQuantity(bookRequest.getStockQuantity())
                    .build();

            return bookRepository.save(book);
        } else {
            throw new GenException("Book already exists", HttpStatus.BAD_REQUEST.value());
        }

    }

    public Book updateBook(Book book) {
        log.info("updateBook called with book: " + book.getId());
        if (book.getId() == null) {
            throw new GenException("Book id cannot be null", HttpStatus.BAD_REQUEST.value());
        }
        Book updatedBook = bookRepository.save(book);
        List<Cart> carts = cartRepository.findCartsByBookId(book.getId());
        List<CartResponse> cartResponses = carts.stream().map(cart -> {
            List<Book> books = cart.getCartItems().stream().map(CartItem::getBook).toList();

            return CartResponse.builder()
                    .books(books)
                    .build();

        }).toList();

        cartResponses.forEach(cart -> cartService.updateBookInCarts(cart.getCartId(),book.getId()));

        return updatedBook;
    }


    public void deleteBookById(Long id) {
        log.info("deleteBookById called with id: " + id);
        if (id == null) {
            throw new GenException("Book id cannot be null", HttpStatus.BAD_REQUEST.value());
        }
        bookRepository.deleteById(id);
    }


}

