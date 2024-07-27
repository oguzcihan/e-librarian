package org.cihan.elibrarian.cart.repository;

import org.cihan.elibrarian.book.model.Book;
import org.cihan.elibrarian.cart.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci.book FROM CartItem ci WHERE ci.book.id = ?1")
    Book findBookById(Long productId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.book.id = ?2")
    CartItem findCartItemByBookIdAndCartId(Long cartId, Long productId);

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.id = ?1 AND ci.book.id = ?2")
    void deleteCartItemByBookIdAndCartId(Long productId, Long cartId);
}
