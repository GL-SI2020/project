package tekup.de.eshop.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tekup.de.eshop.model.User;

import java.util.Collection;

/**
 * Created By Zhu Lin on 3/13/2018.
 */
@Repository

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    Collection<User> findAllByRole(String role);

}
