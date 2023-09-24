package br.com.alurafood.orders.controller;

import br.com.alurafood.orders.dto.OrderDTO;
import br.com.alurafood.orders.dto.StatusDTO;
import br.com.alurafood.orders.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

        private OrderService orderService;

        @GetMapping()
        public List<OrderDTO> findAll() {
            return orderService.findAll();
        }

        @GetMapping("/{id}")
        public ResponseEntity<OrderDTO> findById(@PathVariable @NotNull Long id) {
            return  ResponseEntity.ok(orderService.findById(id));
        }

        @GetMapping("/port")
        public String getPort(@Value("${local.server.port}") String port) {
            return "Order service is running on port: " + port;
        }

        @PostMapping()
        public ResponseEntity<OrderDTO> placeOrder(@RequestBody @Valid OrderDTO dto, UriComponentsBuilder uriBuilder) {
            OrderDTO orderPlaced = orderService.placeOrder(dto);

            return ResponseEntity.created(uriBuilder.path("/orders/{id}")
                    .buildAndExpand(orderPlaced.getId()).toUri())
                    .body(orderPlaced);

        }

        @PutMapping("/{id}/status")
        public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestBody StatusDTO status){
            return ResponseEntity.ok(orderService.updateStatus(id, status));
        }


        @PutMapping("/{id}/paid")
        public ResponseEntity<Void> approveOrder(@PathVariable @NotNull Long id) {
            orderService.approvePaymentOrder(id);
            return ResponseEntity.ok().build();
        }
}
