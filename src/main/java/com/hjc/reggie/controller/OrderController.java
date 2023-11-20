package com.hjc.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hjc.reggie.common.R;
import com.hjc.reggie.dto.OrdersDto;
import com.hjc.reggie.entity.Orders;
import com.hjc.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

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
        log.info("用户下单：{]", orders.toString());
        orderService.submit(orders);
        return R.success("支付成功！");
    }


    /**
     * 订单信息分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page<Orders> orderPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, pageSize);
        orderService.page(orderPage);

        return R.success(orderPage);
    }

    /**
     * 修改订单信息
     * @return
     */
    @PutMapping
    public R<String> setStatus(@RequestBody Orders order) {
        orderService.updateById(order);
        return R.success("修改成功");
    }

    /**
     * 返回订单信息
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("userPage")
    public R<Page<Orders>> userPage(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        orderService.page(ordersPage);
        return R.success(ordersPage);
    }
}
