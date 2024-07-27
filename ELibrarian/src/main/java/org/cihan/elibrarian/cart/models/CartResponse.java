package org.cihan.elibrarian.cart.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cihan.elibrarian.book.model.Book;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {
    private Long cartId;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private List<Book> books = new ArrayList<>();
}
