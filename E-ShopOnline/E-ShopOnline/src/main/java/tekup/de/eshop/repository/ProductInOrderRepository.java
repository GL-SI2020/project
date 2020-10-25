package tekup.de.eshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tekup.de.eshop.model.ProductInOrder;

/**
 * Created By Zhu Lin on 3/14/2018.
 */
@Repository

public interface ProductInOrderRepository extends JpaRepository<ProductInOrder, Long> {

}
