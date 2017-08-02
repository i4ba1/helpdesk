/**
 * 
 */
package id.co.knt.helpdesk.api.model.dto;

import java.util.List;

import id.co.knt.helpdesk.api.model.Product;
import id.co.knt.helpdesk.api.model.SubProduct;

/**
 * @author marlina_kreative
 *
 */
public class ProductDTO {
	
	private Product product;
	
	private List<SubProduct> subProducts;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public List<SubProduct> getSubProducts() {
		return subProducts;
	}

	public void setSubProducts(List<SubProduct> subProducts) {
		this.subProducts = subProducts;
	}	

}
