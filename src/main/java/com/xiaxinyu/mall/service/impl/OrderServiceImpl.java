package com.xiaxinyu.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.xiaxinyu.mall.common.Constant;
import com.xiaxinyu.mall.exception.ExceptionEnum;
import com.xiaxinyu.mall.exception.MyException;
import com.xiaxinyu.mall.filter.UserFilter;
import com.xiaxinyu.mall.model.dao.CartMapper;
import com.xiaxinyu.mall.model.dao.OrderItemMapper;
import com.xiaxinyu.mall.model.dao.OrderMapper;
import com.xiaxinyu.mall.model.dao.ProductMapper;
import com.xiaxinyu.mall.model.pojo.Order;
import com.xiaxinyu.mall.model.pojo.OrderItem;
import com.xiaxinyu.mall.model.pojo.Product;
import com.xiaxinyu.mall.model.request.CreateOrderReq;
import com.xiaxinyu.mall.model.vo.CartVO;
import com.xiaxinyu.mall.model.vo.OrderItemVO;
import com.xiaxinyu.mall.model.vo.OrderVO;
import com.xiaxinyu.mall.service.CartService;
import com.xiaxinyu.mall.service.OrderService;
import com.xiaxinyu.mall.service.UserService;
import com.xiaxinyu.mall.util.OrderCodeFactory;
import com.xiaxinyu.mall.util.QRCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月29日 15:39
 * @Copyright:
 * @version: 1.0.0
 */

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private UserService userService;

    @Value("${file.upload.ip}")
    String ip;

    @Override
    //数据库事务
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateOrderReq createOrderReq){
        //拿到用户ID
        Integer userId = UserFilter.user.getId();
        //从购物车查找已经勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        cartVOList = cartVOList.stream()
                .filter(cartVO -> cartVO.getSelected().equals(Constant.Cart.CHECKED))
                .collect(Collectors.toList());

        //如果购物车已勾选的为空，报错
        if(CollectionUtils.isEmpty(cartVOList)){
            throw new MyException(ExceptionEnum.CART_EMPTY);
        }
        //判断商品是否存在、上下架状态、库存
        validSaleStatusAndStock(cartVOList);
        //把鼓舞车状态转为订单item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);
        //扣库存
        for(OrderItem orderItem : orderItemList){
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock = product.getStock() - orderItem.getQuantity();
            if(stock < 0){
                throw new MyException(ExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }
        //把购物车中的已勾选商品删除
        cleanCart(cartVOList);
        //生成订单
        Order order = new Order();
        //生成订单号，有独立的规则
        String orderCode = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderCode);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        //插入到order表
        orderMapper.insertSelective(order);
        //循环保存每个商品到order_item表
        for(OrderItem orderItem : orderItemList){
            orderItem.setOrderNo(orderCode);
            orderItemMapper.insertSelective(orderItem);
        }
        //把结果返回
        return orderCode;
    }

    @Override
    public OrderVO detail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new MyException(ExceptionEnum.NO_ORDER);
        }
        Integer userId = UserFilter.user.getId();
        if(! order.getUserId().equals(userId)){
            throw new MyException(ExceptionEnum.NOT_YOUR_ORDER);
        }
        return getOrderVO(order);
    }

    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize){
        Integer userId = UserFilter.user.getId();
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectForCustomer(userId);
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo<OrderVO> pageInfo = new PageInfo<>(orderVOList);
        return pageInfo;
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAllForAdmin();
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo<OrderVO> pageInfo = new PageInfo<>(orderVOList);
        return pageInfo;
    }

    @Override
    public void pay(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new MyException(ExceptionEnum.NO_ORDER);
        }
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MyException(ExceptionEnum.WRONG_ORDER_STATUS);
        }

    }

    @Override
    public void deliver(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new MyException(ExceptionEnum.NO_ORDER);
        }
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.PAID.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MyException(ExceptionEnum.WRONG_ORDER_STATUS);
        }

    }

    @Override
    public void finish(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new MyException(ExceptionEnum.NO_ORDER);
        }

        //如果是普通用户，就要校验订单的所属
        if (! userService.checkAdminRole(UserFilter.user) && !order.getUserId().equals(UserFilter.user.getId())) {
            throw new MyException(ExceptionEnum.NOT_YOUR_ORDER);
        }


        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.DELIVERED.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.FINISHED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MyException(ExceptionEnum.WRONG_ORDER_STATUS);
        }

    }


    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = orderList.stream()
                .map(this::getOrderVO)
                .collect(Collectors.toList());
        return orderVOList;
    }

    @Override
    public void cancel(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw new MyException(ExceptionEnum.NO_ORDER);
        }
        Integer userId = UserFilter.user.getId();
        if(! order.getUserId().equals(userId)){
            throw new MyException(ExceptionEnum.NOT_YOUR_ORDER);
        }
        if(order.getOrderStatus().equals(Constant.OrderStatusEnum.NOT_PAID.getCode())){
            order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new MyException(ExceptionEnum.WRONG_ORDER_STATUS);
        }
    }

    @Override
    public String qrcode(String orderNo){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        String address = ip + ":" + request.getLocalPort();
        String payUrl = "http://" + address + "/pay?orderNo=" + orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl,350,350,Constant.FILE_UPLOAD_DIR + orderNo + ".png");
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String pngAddress = "http://" + address + "/images/" + orderNo + ".png";
        return pngAddress;
    }



    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        //获取订单对应的orderItemVOList
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = orderItemList.stream().map(orderItem -> {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem,orderItemVO);
            return orderItemVO;
        }).collect(Collectors.toList());
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(order.getOrderStatus()).getValue());
        return orderVO;
    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for(OrderItem orderItem : orderItemList){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    private void cleanCart(List<CartVO> cartVOList) {
        for(CartVO cartVO : cartVOList){
            cartMapper.deleteByPrimaryKey(cartVO.getProductId());
        }
    }

    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for(CartVO cartVO : cartVOList){
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            //记录商品快照信息
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }


    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for(CartVO cartVO : cartVOList){
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            //判断商品是否存在，商品是否上架
            if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
                throw new MyException(ExceptionEnum.NOT_SALE);
            }
            //判断商品库存
            if (cartVO.getQuantity() > product.getStock()) {
                throw new MyException(ExceptionEnum.NOT_ENOUGH);
            }
        }

    }


}