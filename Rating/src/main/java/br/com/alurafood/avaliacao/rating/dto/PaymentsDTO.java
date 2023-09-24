package br.com.alurafood.avaliacao.rating.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentsDTO {
    private Long id;
    private BigDecimal value;
    private String name;
    private String number;
    private String expiration;
    private String code;
    private StatusPaymentEnum status;
    private Long orderId;
    private Long paymentMethodId;
}
