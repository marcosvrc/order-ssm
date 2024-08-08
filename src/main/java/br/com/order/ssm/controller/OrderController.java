package br.com.order.ssm.controller;

import br.com.order.ssm.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "New Order")
    @PostMapping("/new/{isValid}")
    public String newOrder(@PathVariable Boolean isValid){
        return orderService.newOrder(isValid);
    }

    @Operation(summary = "Pay Order")
    @PostMapping("/pay/{isPaid}")
    public String payOrder(@PathVariable Boolean isPaid){
        return orderService.payOrder(isPaid);
    }

    @Operation(summary = "Ship Order")
    @PostMapping("/ship")
    public String shipOrder(){
        return orderService.shipOrder();
    }

    @Operation(summary = "Complete Order")
    @PostMapping("/complete")
    public String completeOrder(){
        return orderService.completeOrder();
    }

    @Operation(summary = "Validate Order")
    @PostMapping("/validate/{isValidate}")
    public String validateOrder(@PathVariable Boolean isValidate){
        return orderService.validateOrder(isValidate);
    }

}
