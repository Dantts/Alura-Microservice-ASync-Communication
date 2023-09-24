package br.com.alurafood.payments.controller;

import br.com.alurafood.payments.dto.PaymentDTO;
import br.com.alurafood.payments.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService paymentService;

    private RabbitTemplate rabbitTemplate;

    @GetMapping
    public Page<PaymentDTO> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return paymentService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> create(@RequestBody @Valid PaymentDTO paymentDTO,
                                             UriComponentsBuilder uriComponentsBuilder) {
        PaymentDTO paymentDTOResponse = paymentService.create(paymentDTO);

        rabbitTemplate.convertAndSend(
                "payments.ex",
                "",
                paymentDTOResponse
        );

        return ResponseEntity.created(uriComponentsBuilder.path("/payments/{id}")
                .buildAndExpand(paymentDTOResponse.getId()).toUri()).body(paymentDTOResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable @NotNull Long id,
                                             @RequestBody @Valid PaymentDTO paymentDTO) {
        return ResponseEntity.ok(paymentService.update(id, paymentDTO));
    }

    @PatchMapping("/{id}/confirm")
    @CircuitBreaker(name = "confirmOrder", fallbackMethod = "fallbackMethodPaymentWithPendentIntegration")
    public void confirm(@PathVariable @NotNull Long id) {
        paymentService.confirmPayment(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    public void fallbackMethodPaymentWithPendentIntegration(Long id, Exception e) {
        paymentService.changeStatusToPendent(id);
    }
}
