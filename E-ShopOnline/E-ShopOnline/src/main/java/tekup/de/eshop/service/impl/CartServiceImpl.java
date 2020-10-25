package tekup.de.eshop.service.impl;


import  tekup.de.eshop.model.Cart;
import tekup.de.eshop.model.OrderMain;
import tekup.de.eshop.model.ProductInOrder;
import tekup.de.eshop.model.ProductInfo;
import tekup.de.eshop.model.User;
import tekup.de.eshop.repository.CartRepository;
import tekup.de.eshop.repository.OrderRepository;
import tekup.de.eshop.repository.ProductInOrderRepository;
import tekup.de.eshop.repository.UserRepository;
import tekup.de.eshop.service.CartService;
import tekup.de.eshop.service.ProductService;
import tekup.de.eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

/**
 * Created By Zhu Lin on 3/11/2018.
 */
@Service
public class CartServiceImpl implements CartService {
    private ProductService productService;
   private  OrderRepository orderRepository;
   private  UserRepository userRepository;
    private ProductInOrderRepository productInOrderRepository;    
    private CartRepository cartRepository;
    private UserService userService;
    @Autowired
    public CartServiceImpl(ProductService   p,OrderRepository o,UserRepository u,ProductInOrderRepository po,CartRepository c,UserService user) {
    	super();
    	this.cartRepository=c;
    	this.orderRepository=o;
    	this.productInOrderRepository=po;
    	this.productService=p;
    	this.userRepository=u;
    	this.userService=user;
    }
    @Override
    public Cart getCart(User user) {
        return user.getCart();
    }

    @Override
    @Transactional
    public void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user) {
        Cart finalCart = user.getCart();
        productInOrders.forEach(productInOrder -> {
            Set<ProductInOrder> set = finalCart.getProducts();
            Optional<ProductInOrder> old = set.stream().filter(e -> e.getProductId().equals(productInOrder.getProductId())).findFirst();
            ProductInOrder prod;
            if (old.isPresent()) {
                prod = old.get();
                prod.setCount(productInOrder.getCount() + prod.getCount());
            } else {
                prod = productInOrder;
                prod.setCart(finalCart);
                finalCart.getProducts().add(prod);
            }
            productInOrderRepository.save(prod);
        });
        cartRepository.save(finalCart);

    }

    @Override
    @Transactional
    public void delete(String itemId, User user) {
        Optional <ProductInOrder>  op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCart(null);
            productInOrderRepository.deleteById(productInOrder.getId());
        });
    }



    @Override
    @Transactional
    public void checkout(User user) {
        // Creat an order
        OrderMain order = new OrderMain(user);
        orderRepository.save(order);

        // clear cart's foreign key & set order's foreign key& decrease stock
        user.getCart().getProducts().forEach(productInOrder -> {
            productInOrder.setCart(null);
            productInOrder.setOrderMain(order);
            productService.decreaseStock(productInOrder.getProductId(), productInOrder.getCount());
            productInOrderRepository.save(productInOrder);
        });

    }
}
