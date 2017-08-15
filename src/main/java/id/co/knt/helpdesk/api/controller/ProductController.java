/**
 * 
 */
package id.co.knt.helpdesk.api.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import id.co.knt.helpdesk.api.model.Product;
import id.co.knt.helpdesk.api.model.SubProduct;
import id.co.knt.helpdesk.api.model.dto.ProductDTO;
import id.co.knt.helpdesk.api.repositories.ProductRepo;
import id.co.knt.helpdesk.api.repositories.SubProductRepo;

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

	@Autowired
	public SubProductRepo subProductRepo;

	public ResponseEntity<Product> createProduct() {
		return null;
	}

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public ResponseEntity<List<Product>> getAllProduct() {
		LOG.info("========" + "/" + "getAllProduct" + "=========");
		List<Product> products = productRepo.findAll();

		if (products.size() > 0) {
			return new ResponseEntity<>(products, HttpStatus.OK);
		}

		return new ResponseEntity<>(products, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = { "/findProduct/{productCode}" }, method = RequestMethod.GET)
	public ResponseEntity<Product> getProductByCode(@PathVariable Integer productCode){
		Product product = productRepo.findByProductCode(productCode);

		if (product != null) {
			return new ResponseEntity<>(product, HttpStatus.OK);
		}

		return new ResponseEntity<>(product, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = { "/createProduct/" }, method = RequestMethod.POST)
	public ResponseEntity<Void> createProduct(@RequestBody ProductDTO productDTO) {

		try {
			Product newProduct = productRepo.save(productDTO.getProduct());
			List<SubProduct> subProducts = productDTO.getSubProducts();

			for (SubProduct subProduct : subProducts) {
				subProduct.setProduct(newProduct);
				subProductRepo.save(subProduct);
			}

			return new ResponseEntity<>(HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@RequestMapping(value = { "/productDetail/{productId}" }, method = RequestMethod.GET)
	public ResponseEntity<ProductDTO> getDetailProduct(@PathVariable Integer productId) {
		LOG.info("========" + "/productDetail/{productId}" + "getDetailProduct" + "=========");
		ProductDTO productDto = new ProductDTO();
		productDto.setProduct(productRepo.findOne(productId));
		productDto.setSubProducts(subProductRepo.findAllSubProductByProductId(productId));

		if (!productDto.getProduct().equals(null)) {
			return new ResponseEntity<>(productDto, HttpStatus.OK);
		}

		return new ResponseEntity<>(productDto, HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = { "/updateProduct/" }, method = RequestMethod.PUT)
	public ResponseEntity<Void> updateProduct(@RequestBody ProductDTO productDTO) {

		Product currentProduct = productRepo.findOne(productDTO.getProduct().getId());
		if (currentProduct == null) {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		currentProduct.setProductName(productDTO.getProduct().getProductName());
		currentProduct.setProductCode(productDTO.getProduct().getProductCode());
		currentProduct.setDescription(productDTO.getProduct().getDescription());
		currentProduct.setSubModuleLable(productDTO.getProduct().getSubModuleLable());
		

		if (!productRepo.saveAndFlush(currentProduct).equals(null)) {
			
			for (SubProduct subProduct : productDTO.getSubProducts()) {
				subProduct.setProduct(currentProduct);
				subProductRepo.saveAndFlush(subProduct);
			}
			
			return new ResponseEntity<>( HttpStatus.OK);
		}

		return new ResponseEntity<>( HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value = { "/deleteProduct/{productId}" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Integer productId) {
		productRepo.delete(productId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

	@RequestMapping(value = { "/deleteSubProduct/{subProductId}" }, method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteSubProduct(@PathVariable("subProductId") Integer subProductId) {
		subProductRepo.delete(subProductId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = { "/fetchSubProduct/{productId}" }, method = RequestMethod.GET)
	public ResponseEntity<List<SubProduct>> fetchSubProduct(@PathVariable("productId") Integer productId){
		 List<SubProduct> subProducts = subProductRepo.findAllSubProductByProductId(productId);
		 if (subProducts.size() == 0){
		 	return new ResponseEntity<>(subProducts, HttpStatus.NOT_FOUND);
		 }

		return new ResponseEntity<>(subProducts, HttpStatus.OK);
	}
}
