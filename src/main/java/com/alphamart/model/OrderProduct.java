package com.alphamart.model;

public class OrderProduct {

	 private Product product;
	    private Integer quantity;

	    public Product getProduct() {
	        return product;
	    }

	    public void setProduct(Product product) {
	        this.product = product;
	    }

	    public Integer getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }

		@Override
		public String toString() {
			return "OrderProduct [product=" + product + ", quantity=" + quantity + "]";
		}
	    
	    
	    
}
