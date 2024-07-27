package org.cihan.elibrarian.cart.service;

import lombok.RequiredArgsConstructor;
import org.cihan.elibrarian.book.model.Book;
import org.cihan.elibrarian.book.repository.BookRepository;
import org.cihan.elibrarian.cart.models.Cart;
import org.cihan.elibrarian.cart.models.CartItem;
import org.cihan.elibrarian.cart.models.CartRequest;
import org.cihan.elibrarian.cart.models.CartResponse;
import org.cihan.elibrarian.cart.repository.CartItemRepository;
import org.cihan.elibrarian.cart.repository.CartRepository;
import org.cihan.elibrarian.exceptions.GenException;
import org.cihan.elibrarian.user.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;


    public CartResponse createCart(CartRequest cartRequest) {
        Book book = bookRepository.findById(cartRequest.getBookId())
                .orElseThrow(() -> new GenException("Book not found", HttpStatus.NOT_FOUND.value()));

        Cart userCart = cartRepository.findByUser(getCurrentUser());
        if (userCart == null) {
            userCart = new Cart();
            userCart.setUser(getCurrentUser());
            userCart = cartRepository.save(userCart);
        }

        if (book.getStockQuantity() == 0)
            throw new GenException("Book out of stock", HttpStatus.BAD_REQUEST.value());

        if (book.getStockQuantity() < cartRequest.getQuantity())
            throw new GenException("Requested quantity exceeds available stock for " + book.getName()
                    + ". Available stock: " + book.getStockQuantity(), HttpStatus.BAD_REQUEST.value());

        CartItem existingCartItem = cartItemRepository.findCartItemByBookIdAndCartId(userCart.getId(), cartRequest.getBookId());


        // If cart item exists, update its quantity, otherwise create a new cart item
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartRequest.getQuantity());
        } else {
            existingCartItem = CartItem.builder()
                    .book(book)
                    .cart(userCart)
                    .quantity(cartRequest.getQuantity())
                    .bookPrice(book.getPrice())
                    .build();
        }

        cartItemRepository.save(existingCartItem);

        book.setStockQuantity(book.getStockQuantity() - cartRequest.getQuantity());

//        bookRepository.save(book); // Update book stock quantity
        userCart.setTotalPrice(userCart.getTotalPrice().add(book.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity()))));

        List<Book> bookList = userCart.getCartItems().stream().map(CartItem::getBook).toList();

        return CartResponse.builder()
                .cartId(userCart.getId())
                .totalPrice(userCart.getTotalPrice())
                .books(bookList)
                .build();
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public void updateBookInCarts(Long cartId, Long bookId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new GenException("Cart not found", HttpStatus.NOT_FOUND.value()));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new GenException("Book not found", HttpStatus.NOT_FOUND.value()));

        CartItem cartItem = cartItemRepository.findCartItemByBookIdAndCartId(cartId, bookId);
        if (cartItem == null) {
            throw new GenException("Cart item not found", HttpStatus.NOT_FOUND.value());
        }

        BigDecimal cartPrice = cart.getTotalPrice().subtract(cartItem.getBookPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

        cartItem.setBookPrice(book.getPrice());
        cart.setTotalPrice(cartPrice.add(book.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
        cartItemRepository.save(cartItem);

    }
}
