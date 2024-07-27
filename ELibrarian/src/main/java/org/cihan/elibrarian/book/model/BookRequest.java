package org.cihan.elibrarian.book.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    private String name;
    private String author;
    private String publisher;
    private BigDecimal price;
    private int stockQuantity;
    private Long categoryId;
}
