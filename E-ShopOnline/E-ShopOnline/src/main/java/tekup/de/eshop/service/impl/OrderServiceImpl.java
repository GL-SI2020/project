package tekup.de.eshop.service.impl;


import tekup.de.eshop.model.OrderMain;
import tekup.de.eshop.model.ProductInOrder;
import tekup.de.eshop.model.ProductInfo;
import tekup.de.eshop.enums.OrderStatusEnum;
import tekup.de.eshop.enums.ResultEnum;
import tekup.de.eshop.exception.MyException;
import tekup.de.eshop.repository.OrderRepository;
import tekup.de.eshop.repository.ProductInOrderRepository;
import tekup.de.eshop.repository.ProductInfoRepository;
import tekup.de.eshop.repository.UserRepository;
import tekup.de.eshop.service.OrderService;
import tekup.de.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created By Zhu Lin on 3/14/2018.
 */
@Service
public class OrderServiceImpl implements OrderService {
    
    private OrderRepository orderRepository;
    
   private  UserRepository userRepository;
    
   private ProductInfoRepository productInfoRepository;
    
   private  ProductService productService;
   private  ProductInOrderRepository productInOrderRepository;
    @Autowired
    public OrderServiceImpl(OrderRepository o,UserRepository u,ProductInfoRepository p,ProductService ps,ProductInOrderRepository po ) {
		super();
		this.orderRepository=o;
		this.userRepository=u;
		this.productInOrderRepository=po;
		this.productService=ps;
		this.productInfoRepository=p;
		
	}
    
    @Override
    public Page<OrderMain> findAll(Pageable pageable) {
        return orderRepository.findAllByOrderByOrderStatusAscCreateTimeDesc(pageable);
    }

    @Override
    public Page<OrderMain> findByStatus(Integer status, Pageable pageable) {
        return orderRepository.findAllByOrderStatusOrderByCreateTimeDesc(status, pageable);
    }

    @Override
    public Page<OrderMain> findByBuyerEmail(String email, Pageable pageable) {
        return orderRepository.findAllByBuyerEmailOrderByOrderStatusAscCreateTimeDesc(email, pageable);
    }

    @Override
    public Page<OrderMain> findByBuyerPhone(String phone, Pageable pageable) {
        return orderRepository.findAllByBuyerPhoneOrderByOrderStatusAscCreateTimeDesc(phone, pageable);
    }

    @Override
    public OrderMain findOne(Long orderId) {
        OrderMain orderMain = orderRepository.findByOrderId(orderId);
        if(orderMain == null) {
            throw new MyException(ResultEnum.ORDER_NOT_FOUND);
        }
        return orderMain;
    }

    @Override
    @Transactional
    public OrderMain finish(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderRepository.save(orderMain);
        return orderRepository.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public OrderMain cancel(Long orderId) {
        OrderMain orderMain = findOne(orderId);
        if(!orderMain.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new MyException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderMain.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
        orderRepository.save(orderMain);

        // Restore Stock
        Iterable<ProductInOrder> products = orderMain.getProducts();
        for(ProductInOrder productInOrder : products) {
            ProductInfo productInfo = productInfoRepository.findByProductId(productInOrder.getProductId());
            if(productInfo != null) {
                productService.increaseStock(productInOrder.getProductId(), productInOrder.getCount());
            }
        }
        return orderRepository.findByOrderId(orderId);

    }
}
