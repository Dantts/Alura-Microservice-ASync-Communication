package br.com.alurafood.payments.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "order-ms", path = "/api/orders")
public interface OrderClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}/paid")
    void approveOrder(@PathVariable Long id);
}
