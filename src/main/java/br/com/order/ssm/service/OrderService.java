package br.com.order.ssm.service;

public interface OrderService {
    String newOrder(Boolean isValid);
    String payOrder(Boolean isPaid);
    String shipOrder();
    String completeOrder();
    String validateOrder(Boolean isValidate);
}
