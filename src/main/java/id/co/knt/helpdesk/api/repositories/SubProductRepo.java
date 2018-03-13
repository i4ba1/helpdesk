/**
 * 
 */
package id.co.knt.helpdesk.api.repositories;

import id.co.knt.helpdesk.api.model.SubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author marlin_kreative
 *
 */
public interface SubProductRepo extends JpaRepository<SubProduct, Integer>{
	
	@Query("select sp from SubProduct sp where sp.product.id= :productId")
	List<SubProduct> findAllSubProductByProductId(@Param("productId") Integer productId);

}
