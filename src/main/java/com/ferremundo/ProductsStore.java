package com.ferremundo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

public class ProductsStore {
	
	private static List<Product> listStore;
	
	private static ProductsStore store=null;

	private ProductsStore() {
		EntityManager em=EMF.get(EMF.UNIT_PRODUCT).createEntityManager();
		listStore=em.createNativeQuery("select * from Product",Product.class).getResultList();

	}
	
	public static ProductsStore instance(){
		if(null==store){
			store=new ProductsStore();
			System.out.println("inflating store "+listStore.size()+" Products");
		}
		else{System.out.println("store was inflated "+listStore.size()+" Products");}
		return store;
	}
	
	public List<Product> search(String arg,int size){
		String[] argUP=arg.toUpperCase().split(" ");
		List<Product> ret=new LinkedList<Product>();
		for(int i=0;i<listStore.size()&&ret.size()<size;i++){
			String code =listStore.get(i).getCode();
			String description=listStore.get(i).getDescription();
			String mark=listStore.get(i).getMark();
			boolean codes=false;
			int counter=0;
			for(int j=0;j<argUP.length;j++){
				if(code.equals(argUP[j]))codes=true;
				if(code.contains(argUP[j])||description.contains(argUP[j])||mark.contains(argUP[j])){
					counter++;
				}
				else break;
			}
			if(counter==argUP.length&&codes){
				ret.add(listStore.get(i).cloneL1());
			}
			
		}
		for(int i=0;i<listStore.size()&&ret.size()<size;i++){
			String code =listStore.get(i).getCode();
			String description=listStore.get(i).getDescription();
			String mark=listStore.get(i).getMark();
			boolean startsWith=false;
			boolean toAdd=false;
			int counter=0;
			for(int j=0;j<argUP.length;j++){
				if(code.startsWith(argUP[j])){
					startsWith=true;
					toAdd=true;
					for(int k=0;k<ret.size();k++){
						if(code.equals(ret.get(k).getCode())){
							toAdd=false;
							break;
						}
					}
				}
				if(code.contains(argUP[j])||description.contains(argUP[j])||mark.contains(argUP[j])){
					counter++;
				}
				else break;
				
			}
			if(counter==argUP.length&&startsWith&&toAdd){
				ret.add(listStore.get(i).cloneL1());
			}
			
		}
		for(int i=0;i<listStore.size()&&ret.size()<size;i++){
			String code =listStore.get(i).getCode();
			String description=listStore.get(i).getDescription();
			String mark=listStore.get(i).getMark();
			boolean toAdd=false;
			int counter=0;
			for(int j=0;j<argUP.length;j++){
				if(code.contains(argUP[j])||description.contains(argUP[j])||mark.contains(argUP[j])){
					counter++;
				}
				else break;
			}
			if(counter==argUP.length){
				toAdd=true;
				for(int k=0;k<ret.size();k++){
					if(listStore.get(i).getId().equals(ret.get(k).getId())){
						toAdd=false;
						break;
					}
				}
				if(toAdd){
					ret.add(listStore.get(i).cloneL1());
				}
			}
		}
		return ret;
	}
	
	public String searchToJson(String arg, int consumertype, int size){
		List<Product> products1=search(arg,size);
		List<DistancedProduct> distancedProducts=new LinkedList<DistancedProduct>();
		for(int i=0;i<products1.size();i++){
			distancedProducts.add(new DistancedProduct(products1.get(i),arg));
		}
		Collections.sort(distancedProducts,new DistancedProduct());
		List<Product> products=new LinkedList<Product>();
		for(int i=0;i<distancedProducts.size();i++){
			products.add(distancedProducts.get(i).getProduct());
		}
		String ret="{ \"products\" : [ ";
		for(int i=0;i<products.size();i++){
			Product product=products.get(i);
			int productPriceKind=product.getProductPriceKind();
			float unitPrice=0;
			if(consumertype==Client.TYPE_1){
				unitPrice=product.getUnitPrice();
			}
			else if(consumertype==Client.TYPE_2){
				if(productPriceKind==Product.KIND_1)unitPrice=product.getUnitPrice()*Product.FACTOR_1;
				else if(productPriceKind==Product.KIND_2)unitPrice=product.getUnitPrice()*Product.FACTOR_2;
			}
			else if(consumertype==Client.TYPE_3){
				if(productPriceKind==Product.KIND_1)unitPrice=product.getUnitPrice()*Product.FACTOR_3;
				else if(productPriceKind==Product.KIND_2)unitPrice=product.getUnitPrice()*Product.FACTOR_4;
			}
			ret+="{ "+
				"\"key\" : \""+product.getKey()+"\", "+
				"\"code\" : \""+product.getCode()+"\", "+
				"\"unit\" : \""+product.getUnit()+"\", "+
				"\"mark\" : \""+product.getMark()+"\", "+
				"\"unitPrice\" : \""+unitPrice+"\", "+
				"\"productPriceKind\" : \""+product.getProductPriceKind()+"\", "+
				"\"description\" : \""+product.getDescription()+"\" }"+(i==(products.size()-1)?" ":", ");

		}
		ret+="]}";
		System.out.println(ret);
		return ret;
	}
	
	public static void refresh(){
		store=null;
		ProductsStore.instance();
	}
	
	public static Product getByCode(String code){
		for(int i=0;i<listStore.size();i++){
			if(code.equals(listStore.get(i).getCode()))return listStore.get(i).cloneL1();
		}
		return null;
	}
	
	public static Product getByKey(String key){
		for(int i=0;i<listStore.size();i++){
			if(new Long(key).equals(listStore.get(i).getKey()))return listStore.get(i).cloneL1();
		}
		return null;
	}

}
