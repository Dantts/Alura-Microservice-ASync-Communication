package br.com.alurafood.orders.service;

import br.com.alurafood.orders.dto.OrderDTO;
import br.com.alurafood.orders.dto.StatusDTO;
import br.com.alurafood.orders.model.Order;
import br.com.alurafood.orders.model.StatusEnum;
import br.com.alurafood.orders.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;

    private final ModelMapper modelMapper;


    public List<OrderDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(p -> modelMapper.map(p, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO placeOrder(OrderDTO dto) {
        Order order = modelMapper.map(dto, Order.class);

        order.setDateTime(LocalDateTime.now());
        order.setStatus(StatusEnum.ACCOMPLISHED);
        order.getItens().forEach(item -> item.setOrder(order));
        Order orderSaved = orderRepository.save(order);

        return modelMapper.map(orderSaved, OrderDTO.class);
    }

    public OrderDTO updateStatus(Long id, StatusDTO dto) {

        Order order = orderRepository.getByIdWithItens(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(dto.getStatus());
        orderRepository.updateStatus(dto.getStatus(), order);
        return modelMapper.map(order, OrderDTO.class);
    }

    public void approvePaymentOrder(Long id) {

        Order order = orderRepository.getByIdWithItens(id);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(StatusEnum.PAID);
        orderRepository.updateStatus(StatusEnum.PAID, order);
    }
}
