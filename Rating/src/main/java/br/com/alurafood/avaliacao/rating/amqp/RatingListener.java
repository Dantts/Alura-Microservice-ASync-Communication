package br.com.alurafood.avaliacao.rating.amqp;

import br.com.alurafood.avaliacao.rating.dto.PaymentsDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RatingListener {
    @RabbitListener(queues = "payments.details-rating")
    public void receiveMessage(@Payload PaymentsDTO payments) {
        if(payments.getNumber().equals("0001")) {
            System.out.println(payments.getId());
            System.out.println(payments.getNumber());
            throw new RuntimeException("Process Error");
        }

        String message = """
                Create order rating: %s
                Payments id: %s
                Client name: %s
                Value R$: %s
                Status: %s
                """.formatted(payments.getOrderId(),
                payments.getId(),
                payments.getName(),
                payments.getValue(),
                payments.getStatus());

        System.out.println(message);
    }
}
