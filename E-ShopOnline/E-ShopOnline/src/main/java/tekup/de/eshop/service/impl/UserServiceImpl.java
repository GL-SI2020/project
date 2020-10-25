package tekup.de.eshop.service.impl;


import tekup.de.eshop.model.Cart;
import tekup.de.eshop.model.User;
import tekup.de.eshop.enums.ResultEnum;
import tekup.de.eshop.exception.MyException;
import tekup.de.eshop.repository.CartRepository;
import tekup.de.eshop.repository.UserRepository;
import tekup.de.eshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Created By Zhu Lin on 3/13/2018.
 */
@Service
//@DependsOn("passwordEncoder")
public class UserServiceImpl implements UserService {
    
   // private PasswordEncoder passwordEncoder;
    
    private UserRepository userRepository;
    private CartRepository cartRepository;
    @Autowired
    public UserServiceImpl(UserRepository u,CartRepository c) {
    	super();
    	this.userRepository=u;
    	this.cartRepository=c;
		// TODO Auto-generated constructor stub
	}
    @Override
    public User findOne(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Collection<User> findByRole(String role) {
        return userRepository.findAllByRole(role);
    }
   
    @Override
    @Transactional
    public User save(User user) {
        //register
        user.setPassword(user.getPassword());
        try {
            User savedUser = userRepository.save(user);

            // initial Cart
            Cart savedCart = cartRepository.save(new Cart(savedUser));
            savedUser.setCart(savedCart);
            return userRepository.save(savedUser);

        } catch (Exception e) {
            throw new MyException(ResultEnum.VALID_ERROR);
        }

    }
    

    @Override
    @Transactional
    public User update(User user) {
        User oldUser = userRepository.findByEmail(user.getEmail());
        oldUser.setPassword(user.getPassword());
        oldUser.setName(user.getName());
        oldUser.setPhone(user.getPhone());
        oldUser.setAddress(user.getAddress());
        return userRepository.save(oldUser);
    }

}
