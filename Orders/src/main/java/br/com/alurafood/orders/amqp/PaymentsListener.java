package br.com.alurafood.orders.amqp;

import br.com.alurafood.orders.dto.PaymentDTO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentsListener {

    @RabbitListener(queues = "payments.details-order")
    public void receiveMessage(PaymentDTO paymentDTO) {
        String message = """
                Payment data: %s
                order number: %s
                Client name: %s
                Value R$: %s
                Status: %s
                """
                .formatted(
                    paymentDTO.getId(),
                    paymentDTO.getOrderId(),
                    paymentDTO.getName(),
                    paymentDTO.getValue(),
                    paymentDTO.getStatus()
                );

        System.out.println(message);
    }
}
