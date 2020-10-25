package tekup.de.eshop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import tekup.de.eshop.form.ItemForm;
import tekup.de.eshop.model.*;
import tekup.de.eshop.repository.*;
import tekup.de.eshop.service.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

/**
 * Created By Zhu Lin on 3/11/2018.
 */
@CrossOrigin
@RestController
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;

    private UserService userService;
 
    private ProductService productService;
   
    private ProductInOrderService productInOrderService;
    
    private ProductInOrderRepository productInOrderRepository;
    @Autowired
    public CartController(CartService c,UserService  u,ProductService p ,ProductInOrderService po,ProductInOrderRepository por) {
		super();
		this.cartService=c;
		this.productInOrderRepository=por;
		this.productInOrderService=po;
		this.productService=p;
		this.userService=u;
	}
    
    @PostMapping("/{email}")
    public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders,@PathVariable  String email /*Principal principal*/) {
        User user = userService.findOne(email);//(/*principal.getName()*/email);
        try {
            cartService.mergeLocalCart(productInOrders, user);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @GetMapping("{email}")
    public Cart getCart(@PathVariable String email ){//Principal principal) {
        User user = userService.findOne(email);
        return cartService.getCart(user);
    }


    @PostMapping("/add/{email}")
    public boolean addToCart(@RequestBody ItemForm form,@PathVariable String principal){//String principal) {
    	ProductInfo  productInfo = productService.findOne(form.getProductId());
        try {
            mergeCart(Collections.singleton(new ProductInOrder(productInfo, form.getQuantity())), principal);
        } catch (Exception e) {
            //return false;
        }
        return true;
    }

    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        User user = userService.findOne(principal.getName());
         productInOrderService.update(itemId, quantity, user);
        return productInOrderService.findOne(itemId, user);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Principal principal) {
        User user = userService.findOne(principal.getName());
         cartService.delete(itemId, user);
         // flush memory into DB
    }


    @PostMapping("/checkout")
    public ResponseEntity checkout(Principal principal) {
        User user = userService.findOne(principal.getName());// Email as username
        cartService.checkout(user);
        return ResponseEntity.ok(null);
    }


}
