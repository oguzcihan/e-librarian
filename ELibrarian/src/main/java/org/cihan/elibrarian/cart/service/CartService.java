package org.cihan.elibrarian.cart.service;

import lombok.RequiredArgsConstructor;
import org.cihan.elibrarian.book.model.Book;
import org.cihan.elibrarian.book.repository.BookRepository;
import org.cihan.elibrarian.cart.models.Cart;
import org.cihan.elibrarian.cart.models.CartRequest;
import org.cihan.elibrarian.cart.repository.CartRepository;
import org.cihan.elibrarian.user.models.User;
import org.cihan.elibrarian.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    public Cart createCart(CartRequest cartRequest) {
        Book book = bookRepository.findById(cartRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Cart userCart = cartRepository.findByUser(getCurrentUser());


        return cartRepository.save(userCart);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}
