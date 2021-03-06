package id.co.knt.helpdesk.api.repositories;

import id.co.knt.helpdesk.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author MNI
 *
 */
@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
	
	@Query("select p from Product p where p.productName= :productName")
	Product findByProductName(@Param("productName") String productName);
	
	@Query("select p from Product p where p.productCode= :productCode")
	Product findByProductCode(@Param("productCode") Integer productCode);
}
