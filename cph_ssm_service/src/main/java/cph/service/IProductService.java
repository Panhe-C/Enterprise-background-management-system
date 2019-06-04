package cph.service;

import cph.domain.Product;

import java.util.List;

public interface IProductService {

    //查询所有产品
    public List<Product> findAll() throws Exception;

    //添加产品
    void save(Product product);

    Product findById(String id) throws Exception;
}
