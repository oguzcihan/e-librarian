package org.cihan.elibrarian.cart.repository;

import org.cihan.elibrarian.cart.models.Cart;
import org.cihan.elibrarian.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Boolean existsByUser(User user);

    Cart findByUser(User user);
}
