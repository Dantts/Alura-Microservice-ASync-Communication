package br.com.alurafood.orders.dto;

import br.com.alurafood.orders.model.StatusEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;
    private LocalDateTime dateTime;
    private StatusEnum status;
    private List<ItemOrderDTO> itens = new ArrayList<>();



}
