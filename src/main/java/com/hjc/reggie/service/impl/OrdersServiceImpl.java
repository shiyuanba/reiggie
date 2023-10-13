package com.hjc.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hjc.reggie.common.BaseContext;
import com.hjc.reggie.common.CustomException;
import com.hjc.reggie.entity.*;
import com.hjc.reggie.mapper.OrdersMapper;
import com.hjc.reggie.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService cartService;
    @Autowired
    private UserService userService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        //获取用户id
        Long currentId = BaseContext.getCurrentId();
        //查询购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        List<ShoppingCart> shoppingCarts = cartService.list();
        if (shoppingCarts == null) {
            throw new CustomException("购物车为空，不能下单！");
        }
        //生成订单号
        long id = IdWorker.getId();

        //封装订单明细表，计算总金额
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails= shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(id);
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setAmount(item.getAmount());
            orderDetail.setImage(item.getImage());
            orderDetail.setNumber(item.getNumber());
            orderDetail.setName(item.getName());

            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //获取用户信息
        User user = userService.getById(currentId);
        //获取地址信息
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址信息有误，不能下单！");
        }

        //向订单表插入数据

        orders.setNumber(String.valueOf(id));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(currentId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));
        this.save(orders);
        //向订单明细表插入数据
        orderDetailService.saveBatch(orderDetails);
        //清空购物车
        cartService.remove(queryWrapper);

    }
}
