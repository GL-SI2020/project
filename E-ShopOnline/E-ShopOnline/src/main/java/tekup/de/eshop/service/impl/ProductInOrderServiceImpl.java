package tekup.de.eshop.service.impl;

import tekup.de.eshop.model.ProductInOrder;
import tekup.de.eshop.model.User;
import tekup.de.eshop.repository.ProductInOrderRepository;
import tekup.de.eshop.service.ProductInOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created By Zhu Lin on 1/3/2019.
 */
@Service
public class ProductInOrderServiceImpl implements ProductInOrderService {

    
    private ProductInOrderRepository productInOrderRepository;
    @Autowired
    public ProductInOrderServiceImpl(ProductInOrderRepository p) {
    	super();
    	this.productInOrderRepository=p;
	}
    @Override
    @Transactional
    public void update(String itemId, Integer quantity, User user) {
        Optional <ProductInOrder> op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        op.ifPresent(productInOrder -> {
            productInOrder.setCount(quantity);
            productInOrderRepository.save(productInOrder);
        });

    }

    @Override
    public ProductInOrder findOne(String itemId, User user) {
        Optional <ProductInOrder> op = user.getCart().getProducts().stream().filter(e -> itemId.equals(e.getProductId())).findFirst();
        AtomicReference<ProductInOrder> res = new AtomicReference<>();
        op.ifPresent(res::set);
        return res.get();
    }
}
