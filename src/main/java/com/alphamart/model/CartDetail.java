package com.alphamart.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class CartDetail {

	@EmbeddedId
	@JsonIgnore
	private CartDetailId cartDetailId;

	private int quantity;

	public CartDetail() {

	}

	public CartDetail(Cart cart, Product product, Integer quantity) {
		this.cartDetailId = new CartDetailId();
		this.cartDetailId.setCart(cart);
		this.cartDetailId.setProduct(product);
		this.quantity = quantity;
	}

	public CartDetailId getCartDetailId() {
		return cartDetailId;
	}

	public void setCartDetailId(CartDetailId cartDetailId) {
		this.cartDetailId = cartDetailId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Transient
    public Product getProduct() {
        return this.cartDetailId.getProduct();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cartDetailId == null) ? 0 : cartDetailId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartDetail other = (CartDetail) obj;
		if (cartDetailId == null) {
			if (other.cartDetailId != null)
				return false;
		} else if (!cartDetailId.equals(other.cartDetailId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderDetail [pkId=" + cartDetailId + ", quantity=" + quantity + "]";
	}

}
