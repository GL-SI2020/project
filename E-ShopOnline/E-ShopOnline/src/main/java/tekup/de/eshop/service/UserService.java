package tekup.de.eshop.service;



import java.util.Collection;

import org.springframework.stereotype.Service;

import tekup.de.eshop.model.User;

/**
 * Created By Zhu Lin on 3/13/2018.
 */
public interface UserService {
    User findOne(String email);
    Collection<User> findByRole(String role);

    User save(User user);

    User update(User user);
}
