package com.ferremundo;

import java.util.Comparator;


public class DistancedProduct implements Comparator<DistancedProduct>{
	
	private int distance=0;
	private Product product;
	
	DistancedProduct(){}
	
	DistancedProduct(Product product, String path){
		this.product=product;
		String[] paths=path.split(" ");
		for(int i=0;i< paths.length;i++){
			if(paths[i].equals(product.getCode())){
				distance=0;
				//System.out.println(distance+" "+product.getCode()+" "+product.getDescription()+" "+product.getMark());
				return;
			}
		}
		String[] pathProduct=(product.getCode()+" "+product.getDescription()+" "+product.getMark()).split(" ");
		for(int i=0;i<pathProduct.length;i++){
			for(int j=0;j<paths.length;j++){
				distance+=LevenshteinDistance.LD(pathProduct[i],paths[j]);
			}
		}
		//System.out.println(distance+" "+product.getCode()+" "+product.getDescription()+" "+product.getMark());
	}

	public int getDistance() {
		return distance;
	}

	public Product getProduct() {
		return product;
	}

	@Override
	public int compare(DistancedProduct o1, DistancedProduct o2) {
		return (o2.getDistance()>o1.getDistance() ? -1 : (o2.getDistance()==o1.getDistance() ? 0 : 1));
	}
	
}
