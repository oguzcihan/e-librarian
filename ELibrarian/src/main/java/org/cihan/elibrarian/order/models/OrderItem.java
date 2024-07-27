package org.cihan.elibrarian.order.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cihan.elibrarian.book.model.Book;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer quantity;
    private double discount;
    private BigDecimal orderedBookPrice;

}
