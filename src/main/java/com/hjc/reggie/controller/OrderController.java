package com.hjc.reggie.controller;

import com.hjc.reggie.common.R;
import com.hjc.reggie.entity.Orders;
import com.hjc.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> save(@RequestBody Orders orders) {
        log.info("用户下单：{]",orders.toString());
        orderService.submit(orders);
        return R.success("支付成功！");
    }
}
