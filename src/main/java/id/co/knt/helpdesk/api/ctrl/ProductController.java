/**
 * 
 */
package id.co.knt.helpdesk.api.ctrl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import id.co.knt.helpdesk.api.model.Product;
import id.co.knt.helpdesk.api.repositories.ProductRepo;

/**
 * @author MNI
 *
 */
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping(value = "/productManagement")
public class ProductController {
	private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	public ProductRepo productRepo;
	
	public ResponseEntity<Product> createProduct(){
		return null;
	}
	
	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ResponseEntity<List<Product>> getAllProduct(){
		List<Product> products = productRepo.findAll();
		
		if(products.size() > 0) {
			return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
		}
		
		return new ResponseEntity<List<Product>>(products, HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(value = { "/productDetail/{productId}" }, method = RequestMethod.GET)
	public ResponseEntity<Product> getDetailProduct(@PathVariable Integer productId){
		Product product = productRepo.findOne(productId);
		
		if(!product.equals(null)) {
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		}
		
		return new ResponseEntity<Product>(product, HttpStatus.NOT_FOUND);
	}
}
