package cph.service;

import cph.domain.Orders;

import java.util.List;

public interface IOrderService {

    //查询所有订单
    public List<Orders> findAll(int page,int size) throws Exception;

    //根据id查询订单详情
    Orders findById(String ordersId) throws Exception;
}
