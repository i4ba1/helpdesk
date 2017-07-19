package id.co.knt.helpdesk.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.co.knt.helpdesk.api.model.Product;

/**
 * 
 * @author MNI
 *
 */
@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
	
	@Query("select p from Product p where p.productName= :productName")
	Product findByProductName(@Param("productName") String productName);
}
