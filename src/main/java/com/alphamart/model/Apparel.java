package com.alphamart.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Apparel")
public class Apparel extends Product{
	
	private String type;
	private String brand;
	private String design;
	
	public Apparel() {
		
	}

	public Apparel(String productName, Float price, String type, String brand, String design) {
		super(productName, price);
		this.type = type;
		this.brand = brand;
		this.design = design;
	}

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getBrand() {
		return brand;
	}


	public void setBrand(String brand) {
		this.brand = brand;
	}


	public String getDesign() {
		return design;
	}


	public void setDesign(String design) {
		this.design = design;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((brand == null) ? 0 : brand.hashCode());
		result = prime * result + ((design == null) ? 0 : design.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Apparel other = (Apparel) obj;
		if (brand == null) {
			if (other.brand != null)
				return false;
		} else if (!brand.equals(other.brand))
			return false;
		if (design == null) {
			if (other.design != null)
				return false;
		} else if (!design.equals(other.design))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Apparel [type=" + type + ", brand=" + brand + ", design=" + design + "]";
	}

	
}
