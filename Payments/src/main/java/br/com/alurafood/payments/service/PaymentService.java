package br.com.alurafood.payments.service;

import br.com.alurafood.payments.dto.PaymentDTO;
import br.com.alurafood.payments.http.OrderClient;
import br.com.alurafood.payments.model.Payment;
import br.com.alurafood.payments.model.StatusEnum;
import br.com.alurafood.payments.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PaymentService {

    private OrderClient order;

    private PaymentRepository paymentRepository;

    private ModelMapper modelMapper;

    public Page<PaymentDTO> findAll(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(payment -> modelMapper.map(payment, PaymentDTO.class));
    }

    public PaymentDTO findById(Long id) {
        return paymentRepository.findById(id)
                .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }

    public PaymentDTO create(PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setStatus(StatusEnum.CREATED);
        return modelMapper.map(paymentRepository.save(payment), PaymentDTO.class);
    }

    public PaymentDTO update(Long id, PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setId(id);
        return modelMapper.map(paymentRepository.save(payment), PaymentDTO.class);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }

    public void confirmPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        payment.setStatus(StatusEnum.CONFIRMED);
        paymentRepository.save(payment);
        order.approveOrder(payment.getOrderId());
    }

    public void changeStatusToPendent(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        payment.setStatus(StatusEnum.CONFIRMED_WITHOUT_INTEGRATION);
        paymentRepository.save(payment);
    }
}
