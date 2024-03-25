package org.cihan.elibrarian.cart.rest;

import org.cihan.elibrarian.cart.models.Cart;
import org.cihan.elibrarian.cart.models.CartRequest;
import org.cihan.elibrarian.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/v1/carts")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class CartController {

    private final CartService cartService;
    private final UserDetailsService userDetailsService;

    public CartController(CartService cartService, UserDetailsService userDetailsService) {
        this.cartService = cartService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody CartRequest cartRequest) throws URISyntaxException {
        Cart cart = cartService.createCart(cartRequest);

        return ResponseEntity
                .created(new URI("/api/v1/carts/" + cart.getId()))
                .body(cart);
    }
}
