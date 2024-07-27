package org.cihan.elibrarian.cart.repository;

import org.cihan.elibrarian.cart.models.Cart;
import org.cihan.elibrarian.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Boolean existsByUser(User user);

    Cart findByUser(User user);

    @Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.book p WHERE p.id = ?1")
    List<Cart> findCartsByBookId(Long id);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.id = ?2")
    Cart findCartByEmailAndCartId(String email, Long cartId);
}
