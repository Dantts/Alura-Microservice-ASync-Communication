package br.com.alurafood.orders.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrderDTO {

    private Long id;
    private Integer quantity;
    private String description;
}
