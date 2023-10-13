package com.hjc.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hjc.reggie.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     * @return
     */
    public void submit(Orders orders);
}
