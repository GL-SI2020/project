package tekup.de.eshop.service;


import java.util.Collection;

import org.springframework.stereotype.Service;

import tekup.de.eshop.model.Cart;
import tekup.de.eshop.model.ProductInOrder;
import tekup.de.eshop.model.User;

/**
 * Created By Zhu Lin on 3/10/2018.
 */
public interface CartService {
    Cart getCart(User user);

    void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user);

    void delete(String itemId, User user);

    void checkout(User user);
}
