package com.sky.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.properties.BaiduMapProperties;
import com.sky.properties.ShopProperties;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private ShopProperties shopProperties;
    @Autowired
    private BaiduMapProperties baiduMapProperties;
    @Autowired
    private WebSocketServer webSocketServer;
    private static final String ADDRESS = "address";
    private static final String OUTPUT = "output";
    private static final String OUTPUT_FORMAT = "json";
    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    // 骑行种类，0：普通自行车 1：电动自行车
    private static final String RIDING_TYPE = "riding_type";
    private static final String BY_BIKE = "0";
    private static final String BY_ELECTRIC_BICYCLE = "1";
    // 是否下发具体路线规划
    private static final String STEPS_INFO = "steps_info";
    private static final String AK = "ak";


    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return com.sky.vo.OrderSubmitVO
     * @author paxi
     * @data 2023/9/6
     **/
    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // 处理各种异常请求，包括但不限于：用户未设置地址，购物车为空等异常请求信息...
        // 判断用户是否添加了收货地址
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 判断用户选择的收获地址是否超出配送范围
        verifyUserAddress(addressBook.getProvinceName() + addressBook.getCityName() +
                addressBook.getDistrictName() + addressBook.getDetail());

        // 获取当前登录用户ID
        Long userId = BaseContext.getCurrentId();
        // 判断用户购物车是否有商品
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.getList(shoppingCart);
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 向订单表新增一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        // 设置下单用户
        orders.setUserId(userId);
        // 设置下单时间
        orders.setOrderTime(LocalDateTime.now());
        // 设置订单状态，此时为未支付订单
        orders.setStatus(Orders.PENDING_PAYMENT);
        // 设置支付状态
        orders.setPayStatus(Orders.UN_PAID);
        // 生成并设置订单号，按时间戳
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        // 设置收货人
        orders.setConsignee(addressBook.getConsignee());
        // 设置收货地址
        orders.setAddress(addressBook.getProvinceName()
                + addressBook.getCityName()
                + addressBook.getDistrictName()
                + addressBook.getDetail());
        ordersMapper.save(orders);

        // 向订单明细表(存储订单中的商品详细信息)插入多条数据
        ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
        // 遍历购物车取到具体数据
        shoppingCarts.forEach(cart -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orders.getId());
            orderDetail.setName(cart.getName());
            orderDetail.setDishId(cart.getDishId());
            orderDetail.setDishFlavor(cart.getDishFlavor());
            orderDetail.setSetmealId(cart.getSetmealId());
            orderDetail.setImage(cart.getImage());
            orderDetail.setNumber(cart.getNumber());
            orderDetail.setAmount(cart.getAmount());
            orderDetailList.add(orderDetail);
        });
        orderDetailMapper.saveList(orderDetailList);

        // 封装前台展示信息(VO)
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();

        // 清空用户购物车
        shoppingCartMapper.deleteAllByUserId(userId);

        // 返回数据
        return orderSubmitVO;
    }

    /**
     * 用户订单支付
     * @param ordersPaymentDTO
     * @return com.sky.vo.OrderPaymentVO
     * @author paxi
     * @data 2023/9/7
     **/
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        //Long userId = BaseContext.getCurrentId();
        //User user = userMapper.getById(userId);

        /*
        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );*/
        JSONObject jsonObject = new JSONObject();

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException(MessageConstant.ORDER_PAID);
        }

        OrderPaymentVO orderPaymentVO = jsonObject.toJavaObject(OrderPaymentVO.class);
        orderPaymentVO.setPackageStr(jsonObject.getString("package"));

        return orderPaymentVO;
    }

    /**
     * 用户支付成功，修改订单状态
     * @param outTradeNo
     * @return void
     * @author paxi
     * @data 2023/9/7
     **/
    @Override
    public void paySuccess(String outTradeNo) {
        // 根据订单号查询订单
        Orders ordersDB = ordersMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间,未使用微信支付
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        orders.setCheckoutTime(LocalDateTime.now());

        ordersMapper.update(orders);

        // 以下为利用websocket服务完成用户下单提醒的功能
        Map<Object, Object> hashMap = new HashMap<>();
        // 1.代表来单提醒 2.代表用户催单
        hashMap.put("type",1);
        hashMap.put("orderId",ordersDB.getId());
        hashMap.put("content","订单号：" + outTradeNo);

        webSocketServer.sendToAllClient(JSON.toJSONString(hashMap));

    }

    /**
     * 用户查询订单详情
     * @param id
     * @return com.sky.vo.OrderVO
     * @author paxi
     * @data 2023/9/7
     **/
    @Override
    public OrderVO getDetailById(Long id) {
        // 查询订单基本信息
        Orders orders = ordersMapper.getById(id);

        // 根据订单号查询订单详情
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders,orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * 查询历史订单
     * @param page
     * @param pageSize
     * @param status
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/9/7
     **/
    @Override
    public PageResult historyOrders(Integer page, Integer pageSize, Integer status) {
        // 分析接口文档后，返回值包括订单，以及订单详细信息，故需要查询两张表
        // 开始分页查询
        PageHelper.startPage(page,pageSize);
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        // 设置用户ID，表明查询的用户
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);
        Page<Orders> ordersPage = ordersMapper.pageQuery(ordersPageQueryDTO);

        // 查询订单详情
        List<OrderVO> orderVOList = new ArrayList<>();

        // 判断查询到订单不为空，则继续查询订单详情
        if (ordersPage != null && !ordersPage.isEmpty()) {
            ordersPage.forEach(orders -> {
                // 拿到订单号
                Long ordersId = orders.getId();
                // 查询该订单的详情
                List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(ordersId);
                // 封装前台响应数据对象
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders,orderVO);
                orderVO.setOrderDetailList(orderDetailList);
                // 结果列表添加一条记录
                orderVOList.add(orderVO);
            });
        }

        return new PageResult(ordersPage.getTotal(),orderVOList);
    }

    /**
     * 再来一单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/7
     **/
    @Override
    public void repetition(Long orderId) {
        // 再来一单就是将原有订单的信息重新加入购物车
        Long userId = BaseContext.getCurrentId();
        ArrayList<ShoppingCart> shoppingCartList = new ArrayList<>();
        // 1. 根据订单ID查询原订单包含信息
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);

        // 2. 将原有订单信息重新加入购物车
        orderDetailList.forEach(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail,shoppingCart,"id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartList.add(shoppingCart);
        });

        // 3. 将构建的购物车重新加入购物车表
        shoppingCartMapper.insertList(shoppingCartList);
    }

    /**
     * 用户取消订单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/7
     **/
    @Override
    public void userCancelByOrderId(Long orderId) {
        Orders orderOnDatabase = ordersMapper.getById(orderId);

        // 校验订单是否存在
        if (orderOnDatabase == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        //订单状态 已接单的订单不能自助取消
        if (orderOnDatabase.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(orderOnDatabase.getId());
        // 订单处于待接单状态下取消，需要进行退款
        if (orderOnDatabase.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            // 此处调用微信支付的退款接口，本项目未实现
            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(MessageConstant.CANCEL_REASON);
        orders.setCancelTime(LocalDateTime.now());
        ordersMapper.update(orders);
    }

    /**
     * 商家接单
     * @param ordersConfirmDTO
     * @return void
     * @author paxi
     * @data 2023/9/8
     **/
    @Override
    public void confirmOrders(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        ordersMapper.update(orders);
    }

    /**
     * 商家拒单
     * @param ordersRejectionDTO
     * @return void
     * @author paxi
     * @data 2023/9/8
     **/
    @Override
    public void rejectOrders(OrdersRejectionDTO ordersRejectionDTO) {
        Orders ordersDatabase = ordersMapper.getById(ordersRejectionDTO.getId());

        // 只有待接单才可以拒单
        if (ordersDatabase == null || !Objects.equals(ordersDatabase.getStatus(), Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 判断用户支付状态
        Integer payStatus = ordersDatabase.getPayStatus();
        if (Objects.equals(payStatus, Orders.PAID)) {
            // 此时用户已经支付成功，需要调用微信支付接口退款，此项目省略
            log.info("商家拒单退款");
        }
        // 更新订单状态，注明订单被拒绝的原因和时间
        Orders orders = Orders.builder()
                .id(ordersDatabase.getId())
                .status(Orders.CANCELLED)
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .cancelTime(LocalDateTime.now())
                .build();
        ordersMapper.update(orders);
    }

    /**
     * 商家派送订单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/9
     **/
    @Override
    public void deliveryOrders(Integer orderId) {
        Orders ordersDatabase = ordersMapper.getById(Long.valueOf(orderId));

        // 订单只有接单了才可以派送，并且订单需要保证存在
        if (ordersDatabase == null || !Objects.equals(ordersDatabase.getStatus(), Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 更新订单状态为派送中
        Orders orders = new Orders();
        orders.setId(ordersDatabase.getId());
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        ordersMapper.update(orders);
    }

    /**
     * 商家完成订单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/9
     **/
    @Override
    public void completeOrders(Long orderId) {
        Orders ordersDatabase = ordersMapper.getById(orderId);

        // 订单只有派送了才可以完成，并且订单需要保证存在
        if (ordersDatabase == null || !Objects.equals(ordersDatabase.getStatus(), Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 更新订单状态为派送中
        Orders orders = new Orders();
        orders.setId(ordersDatabase.getId());
        orders.setStatus(Orders.COMPLETED);
        ordersMapper.update(orders);
    }

    /**
     * 商家取消订单
     * @param ordersCancelDTO
     * @return void
     * @author paxi
     * @data 2023/9/9
     **/
    @Override
    public void cancelOrders(OrdersCancelDTO ordersCancelDTO) {
        Orders ordersDatabase = ordersMapper.getById(ordersCancelDTO.getId());
        // 判断用户支付状态
        Integer payStatus = ordersDatabase.getPayStatus();
        if (Objects.equals(payStatus, Orders.PAID)) {
            // 此时用户已经支付成功，需要调用微信支付接口退款，此项目省略
            log.info("商家取消订单退款");
        }
        // 更新订单状态，注明订单被取消的原因和时间
        Orders orders = Orders.builder()
                .id(ordersDatabase.getId())
                .status(Orders.CANCELLED)
                .rejectionReason(ordersCancelDTO.getCancelReason())
                .cancelTime(LocalDateTime.now())
                .build();
        ordersMapper.update(orders);
    }

    /**
     * 订单状态查询与统计
     * @return com.sky.vo.OrderStatisticsVO
     * @author paxi
     * @data 2023/9/9
     **/
    @Override
    public OrderStatisticsVO getOrderStatus() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setConfirmed(ordersMapper.countByStatus(Orders.CONFIRMED));
        orderStatisticsVO.setToBeConfirmed(ordersMapper.countByStatus(Orders.TO_BE_CONFIRMED));
        orderStatisticsVO.setDeliveryInProgress(ordersMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS));
        return orderStatisticsVO;
    }

    /**
     * 订单多条件分页查询
     * @param ordersPageQueryDTO
     * @return com.sky.result.PageResult
     * @author paxi
     * @data 2023/9/9
     **/
    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        // 返回数据包含有订单全部信息，以及订单包含的菜品信息，以字符串形式展示
        // 开始分页查询
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Orders> ordersPage = ordersMapper.pageQuery(ordersPageQueryDTO);

        // 对于包含的菜品信息，需要再次查询展示，依托ordersVO对象，即返回数据中的是ordersVO的列表
        List<OrderVO> orderVOList = getOrdersVOList(ordersPage);

        return new PageResult(ordersPage.getTotal(),orderVOList);
    }

    /**
     * 通过websocket实现用户催单
     * @param orderId
     * @return void
     * @author paxi
     * @data 2023/9/11
     **/
    @Override
    public void reminderOrders(Long orderId) {
        Orders ordersDatabase = ordersMapper.getById(orderId);

        // 校验订单是否存在
        if (ordersDatabase == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        // 以下为利用websocket服务完成用户催单的功能
        Map<Object, Object> hashMap = new HashMap<>();
        // 1.代表来单提醒 2.代表用户催单
        hashMap.put("type",2);
        hashMap.put("orderId",orderId);
        hashMap.put("content","订单号：" + ordersDatabase.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(hashMap));
    }

    /**
     * 获取订单详细信息的展示对象列表
     * @param ordersPage
     * @return java.util.List<com.sky.vo.OrderVO>
     * @author paxi
     * @data 2023/9/9
     **/
    private List<OrderVO> getOrdersVOList(Page<Orders> ordersPage) {
        ArrayList<OrderVO> orderVOArrayList = new ArrayList<>();
        // 拿到分页查询的结果，订单列表
        List<Orders> ordersList = ordersPage.getResult();
        if (!ordersList.isEmpty()) {
            ordersList.forEach(orders -> {
                // 给orderVO对象赋值
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders,orderVO);
                // 设置菜品信息,为字符串
                orderVO.setOrderDishes(getDishesStr(orders));
                orderVOArrayList.add(orderVO);
            });
        }
        return orderVOArrayList;
    }

    /**
     * 获取订单中的菜品信息并转换为字符串形式
     * @param orders
     * @return java.lang.String
     * @author paxi
     * @data 2023/9/9
     **/
    private String getDishesStr(Orders orders) {
        // 通过订单ID查询其所包含的菜品信息
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        ArrayList<String> dishesArrayList = new ArrayList<>();
        orderDetailList.forEach(orderDetail -> {
            // 设置菜皮信息字符串，格式：菜品名称*数量
            String dishesTempStr = orderDetail.getName() + "*" + orderDetail.getNumber() + ";";
            dishesArrayList.add(dishesTempStr);
        });
        return String.join("",dishesArrayList);
    }

    /**
     * 校验用户收获地址，超出范围不配送
     * @param address
     * @return void
     * @author paxi
     * @data 2023/9/10
     **/
    private void verifyUserAddress(String address) {
        // 首先获得店铺地址的经纬度坐标
        String shopCoordinate = getCoordinate(shopProperties.getAddress());
        // 再获得用户订单提交的地址的经纬度坐标
        String userCoordinate = getCoordinate(address);

        // 计算两地间距离
        Integer distance = getDistance(shopCoordinate, userCoordinate);
        if(distance > 5000){
            // 配送距离超过5000米
            throw new OrderBusinessException(MessageConstant.OUT_OF_DELIVERY_RANGE);
        }
    }

    /**
     * 调用百度地图接口将结构化地址（省/市/区/街道/门牌号）解析为对应的位置坐标
     * @param address
     * @return java.lang.String
     * @author paxi
     * @data 2023/9/10
     **/
    private String getCoordinate(String address) {
        Map<String,String> params = new HashMap<>();
        params.put(ADDRESS,address);
        params.put(OUTPUT,OUTPUT_FORMAT);
        params.put(AK, baiduMapProperties.getAk());
        String geocoder = HttpClientUtil.doGet(baiduMapProperties.getWebservice_geocoding_url(), params);

        // 解析数据
        JSONObject jsonObject = JSON.parseObject(geocoder);
        if (!jsonObject.getString("status").equals("0")) {
            throw new OrderBusinessException(MessageConstant.GEOCODING_FAILED);
        }
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        // 获取经度信息
        String lng = location.getString("lng");
        // 获取纬度信息
        String lat = location.getString("lat");
        return lat + "," + lng;
    }

    /**
     * 调用百度地图接口根据起终点坐标规划骑行出行路线及距离
     * @param origin
     * @param destination
     * @return java.lang.Integer
     * @author paxi
     * @data 2023/9/10
     **/
    private Integer getDistance(String origin,String destination) {
        Map<String,String> params = new HashMap<>();
        params.put(ORIGIN,origin);
        params.put(DESTINATION,destination);
        params.put(RIDING_TYPE,BY_ELECTRIC_BICYCLE);
        params.put(STEPS_INFO,"0");
        params.put(AK,baiduMapProperties.getAk());

        String routeStr = HttpClientUtil.doGet(baiduMapProperties.getWebservice_DirectionLite_url(), params);
        JSONObject jsonObject = JSON.parseObject(routeStr);
        if (!jsonObject.getString("status").equals("0")) {
            throw new OrderBusinessException(MessageConstant.ROUTE_PARSING_FAILED);
        }
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray routes = result.getJSONArray("routes");
        return (Integer) ((JSONObject) routes.get(0)).get("distance");
    }
}
