package br.com.alurafood.avaliacao.rating.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentsAMQPConfig {
    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return  new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return  rabbitTemplate;
    }

    @Bean
    public Queue queueDetailsRating() {
        return QueueBuilder
                .nonDurable("payments.details-rating")
                .deadLetterExchange("payments.dlx")
                .build();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange("payments.ex")
                .build();
    }

    @Bean
    public Binding bindPaymentRating() {
        return BindingBuilder
                .bind(queueDetailsRating())
                .to(fanoutExchange());
    }

    @Bean
    public Queue queueDLQDetailsRating() {
        return QueueBuilder
                .nonDurable("payments.details-rating-dlq")
                .build();
    }

    @Bean
    public FanoutExchange deadLatterExchange() {
        return ExchangeBuilder
                .fanoutExchange("payments.dlx")
                .build();
    }

    @Bean
    public Binding bindDLXPaymentRating() {
        return BindingBuilder
                .bind(queueDLQDetailsRating())
                .to(deadLatterExchange());
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> rabbitInitialize(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

}
