package tekup.de.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tekup.de.eshop.model.Cart;

/**
 * Created By Zhu Lin on 1/2/2019.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}
