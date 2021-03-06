package com.ferremundo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jopendocument.dom.spreadsheet.Column;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import com.ferremundo.db.Mongoi;
import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSON;

public class Updater extends HttpServlet{
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//@Override
	protected void doGett(HttpServletRequest request, HttpServletResponse response){
		/*
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		
		try {
			response.getWriter().print(update());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException{
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		int clientReference=new Integer(req.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		if(!(onlineClient.isAuthenticated(req)&&(
				onlineClient.hasAccess(AccessPermission.PRODUCT_UPDATE)||
				onlineClient.hasAccess(AccessPermission.ADMIN)
				))){
			resp.sendError(resp.SC_UNAUTHORIZED,"acceso denegado");return;
		}
		try {
			resp.getWriter().print(update(req,resp));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String update(HttpServletRequest req, HttpServletResponse resp){
		new Log().entry();
		int clientReference=new Integer(req.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		Log log= new Log(onlineClient);
		log.entry();
		String response="{\"response\" : [ ";
		
		
		File file = new File(GSettings.getPathTo("SPREADSHEET_DB_FILE"));
		Sheet sheet=null;
		try {
			sheet = SpreadSheet.createFromFile(file).getSheet(new Integer(0));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean cont=true;
		int i=2;
		/*
		DBCollection collection= new Mongoi().getCollection(Mongoi.PRODUCTS);
		collection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		collection.ensureIndex(new BasicDBObject("hash", 1), new BasicDBObject("unique", true));
		collection.ensureIndex(new BasicDBObject("code", 1), new BasicDBObject("unique", true));
		new Mongoi().getCollection(Mongoi.PRODUCTS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		new Mongoi().doInsert(Mongoi.PRODUCTS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		*/
		int count = 0;
		while(cont){
			String str=sheet.getCellAt("B"+i).getTextValue();
			if(str.equals(""))cont=false;
			else{
				String mark=sheet.getCellAt("A"+i).getTextValue().replaceAll("^\\s+|\\s+$", "").toUpperCase();
				String code=sheet.getCellAt("B"+i).getTextValue().replaceAll("^\\s+|\\s+$", "").toUpperCase();
				String unit=sheet.getCellAt("C"+i).getTextValue().replaceAll("^\\s+|\\s+$", "").toUpperCase();
				String description=sheet.getCellAt("D"+i).getTextValue().replaceAll("^\\s+|\\s+$", "").toUpperCase();
				String prodservCode=sheet.getCellAt("E"+i).getTextValue().replaceAll("^\\s+|\\s+$", "").toUpperCase();
				String unitCode=sheet.getCellAt("F"+i).getTextValue().replaceAll("^\\s+|\\s+$", "").toUpperCase();
				float providerPrice=new Float(sheet.getCellAt("G"+i).getTextValue());
				float incrementPercentage=new Float(sheet.getCellAt("H"+i).getTextValue());
				float providerOffer=new Float(sheet.getCellAt("I"+i).getTextValue());
				float unitPrice=new Float(sheet.getCellAt("J"+i).getTextValue());
				Product product= new Product(code, unitPrice, unit, mark,description, providerPrice,providerOffer,incrementPercentage,prodservCode,unitCode);
				
				//String pstr=code+" "+unit+" "+mark+" "+description;
				//String hash=product.getHash();//MD5.get(pstr);
				//product.setHash(hash);
				DBObject obj=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}");
				response+= (count>0?",":"");
				if(obj==null){
					//DBObject objByCode=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}");
					//if(objByCode==null){
						product.setId(new Long(new Mongoi().doIncrement(Mongoi.PRODUCTS_COUNTER, "{ \"unique\" : \"unique\" }","id")));
						new Mongoi().doInsert(Mongoi.PRODUCTS, new Gson().toJson(product));
						log.info("inserting new product -> "+new Gson().toJson(product));
				}
				else {
					log.object("product of code exists "+code,obj);
					float oldPrice=new Float(obj.get("unitPrice").toString());
					Object providerPriceO=obj.get("providerPrice");
					float oldProviderPrice= providerPriceO==null?-1:new Float(providerPriceO.toString());
					Object oldProviderOfferO=obj.get("providerOffer");
					float oldProviderOffer= oldProviderOfferO==null?-1:new Float(oldProviderOfferO.toString());
					String oldDescription=obj.get("description").toString();
					String oldProdservCode=obj.get("prodservCode")==null?"":obj.get("prodservCode").toString();
					String oldUnitCode=obj.get("unitCode")==null?"":obj.get("unitCode").toString();
					log.object("unit-price",oldPrice,"vs",unitPrice);
					if(oldPrice!=unitPrice){
						new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"unitPrice\" : "+unitPrice+" }");
						new Mongoi().doPush(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"priceHistory\" : {\"unitPrice\" : "+oldPrice+", \"deprecatedDate\" : "+new Date().getTime()+", \"updater\" : \""+onlineClient.getShopman().getLogin()+"\" }}");
						//response+="updating unitPrice("+unitPrice+") for -> "+obj+"\n";
						log.info("updating unitPrice("+unitPrice+") for -> "+obj);
					}
					if(oldProviderPrice!=providerPrice){
						new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"providerPrice\" : "+providerPrice+" }");
						new Mongoi().doPush(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"priceHistory\" : {\"providerPrice\" : "+oldProviderPrice+", \"deprecatedDate\" : "+new Date().getTime()+", \"updater\" : \""+onlineClient.getShopman().getLogin()+"\" }}");
						//response+="updating providerPrice("+providerPrice+") for -> "+obj+"\n";
						log.info("updating providerPrice("+providerPrice+") for -> "+obj);
					}
					if(oldProviderOffer!=providerOffer){
						new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"providerOffer\" : "+providerOffer+" }");
						new Mongoi().doPush(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"priceHistory\" : {\"providerOffer\" : "+oldProviderOffer+", \"deprecatedDate\" : "+new Date().getTime()+", \"updater\" : \""+onlineClient.getShopman().getLogin()+"\" }}");
						//response+="updating providerPrice("+providerOffer+") for -> "+obj+"\n";
						log.info("updating providerPrice("+providerOffer+") for -> "+obj);
					}
					if(!oldProdservCode.equals(prodservCode)){
						new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"prodservCode\" : \""+prodservCode+"\" }");
						log.info("updating prodservCode("+oldProdservCode+") for -> "+prodservCode);
					}
					if(!oldUnitCode.equals(unitCode)){
						new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"unitCode\" : \""+unitCode+"\" }");
						log.info("updating unitCode("+oldUnitCode+") for -> "+unitCode);
					}
					if(!oldDescription.equals(description)){
						new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}", "{\"description\" : \""+description+"\" }");
						log.info("updating description("+oldDescription+") for -> "+description);
					}
				}
				obj=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\"}");
				response += obj.toString();
				
			}
			//System.out.println(i+" -> "+str);
			i++;
			
		}
		response += "]}";
		//response+="total products checked -> "+(i-2)+"\n";
		return response;
		/*EntityManager em=EMF.get(EMF.UNIT_PRODUCT).createEntityManager();
		em.getTransaction().begin();
		List<Product> listStore = em.createNativeQuery("select * from Product",Product.class).getResultList();
		boolean[] keep = new boolean[listStore.size()];
		int rowCount=1;//sheet.getRowCount();
		while(true){
			try{
				String code=sheet.getCellAt("A"+rowCount).getTextValue();
				if(code.startsWith("<end>")){rowCount--;break;}
				else {
					rowCount++;					
				}
			}
			catch(IndexOutOfBoundsException e){}
		}
		System.out.println("updating "+rowCount+" products");
		try {
			response.getWriter().println("updating "+rowCount+" products");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 1; i <= rowCount; i++) {
			String code=sheet.getCellAt("A"+i).getTextValue();
			String mark=sheet.getCellAt("D"+i).getTextValue();
			String unit=sheet.getCellAt("B"+i).getTextValue();
			String description=sheet.getCellAt("C"+i).getTextValue();
			float unitPrice=new Float(sheet.getCellAt("F"+i).getTextValue());
			int productPriceKind=new Integer(sheet.getCellAt("H"+i).getTextValue());
			
			for (int j = 0; j < listStore.size(); j++) {
				if(listStore.get(j).getCode().equals(code)){
					keep[j]=true;
					Product productDB=listStore.get(j);
					if(!productDB.getMark().equals(mark)
							||!productDB.getUnit().equals(unit)
							||!productDB.getDescription().equals(description)
							||productDB.getUnitPrice()!=(unitPrice)
							||productDB.getProductPriceKind()!=productPriceKind
							){
						productDB.setMark(mark);
						productDB.setUnit(unit);
						productDB.setDescription(description);
						productDB.setUnitPrice(unitPrice);
						productDB.setProductPriceKind(productPriceKind);
						System.out.println("merging : "+ i+"\t "+code+"\t "+mark+"\t "+unit+"\t "+description+"\t "+unitPrice+"\t "+productPriceKind);
						try {
							response.getWriter().println("merging : "+ i+"\t "+code+"\t "+mark+"\t "+unit+"\t "+description+"\t "+unitPrice+"\t "+productPriceKind);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						em.merge(productDB);
					}
					break;
				}
				else if(j==listStore.size()-1){
					Product product=new Product(code, unitPrice, unit, mark, description, productPriceKind);
					System.out.println("persisting : "+ i+"\t "+code+"\t "+mark+"\t "+unit+"\t "+description+"\t "+unitPrice+"\t "+productPriceKind);
					try {
						response.getWriter().println("persisting : "+ i+"\t "+code+"\t "+mark+"\t "+unit+"\t "+description+"\t "+unitPrice+"\t "+productPriceKind);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					em.persist(product);
				}
			}
		}
		for (int i = 0; i < keep.length; i++) {
			if(!keep[i]){
				em.remove(listStore.get(i));
			}
		}
     	em.getTransaction().commit();
     	em.close();
		ProductsStore.refresh();
		try {
			response.getWriter().println("<b>succesfully updated : products</b>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
	}
	
	/*
	protected void oldDoGet(HttpServletRequest request, HttpServletResponse response){

		String db=request.getParameter("db");
		if(db==null){
			try {
				response.getWriter().print("<b>failed : db parameter null</b>");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		else if(db.equals("product")){
			File file = new File("/opt/workspace/com.ferremundo/db/DB.ods");
			Sheet sheet=null;
			try {
				sheet = SpreadSheet.createFromFile(file).getSheet(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EntityManager em=EMF.get(EMF.UNIT_PRODUCT).createEntityManager();
			em.getTransaction().begin();
			List<Product> listStore = em.createNativeQuery("select * from Product",Product.class).getResultList();
			boolean[] keep = new boolean[listStore.size()];
			int rowCount=1;//sheet.getRowCount();
			while(true){
				try{
					String code=sheet.getCellAt("A"+rowCount).getTextValue();
					if(code.startsWith("<end>")){rowCount--;break;}
					else {
						rowCount++;					
					}
				}
				catch(IndexOutOfBoundsException e){}
			}
			System.out.println("updating "+rowCount+" products");
			try {
				response.getWriter().println("updating "+rowCount+" products");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 1; i <= rowCount; i++) {
				String code=sheet.getCellAt("A"+i).getTextValue();
				String mark=sheet.getCellAt("D"+i).getTextValue();
				String unit=sheet.getCellAt("B"+i).getTextValue();
				String description=sheet.getCellAt("C"+i).getTextValue();
				float unitPrice=new Float(sheet.getCellAt("F"+i).getTextValue());
				int productPriceKind=new Integer(sheet.getCellAt("H"+i).getTextValue());
				
				for (int j = 0; j < listStore.size(); j++) {
					if(listStore.get(j).getCode().equals(code)){
						keep[j]=true;
						Product productDB=listStore.get(j);
						if(!productDB.getMark().equals(mark)
								||!productDB.getUnit().equals(unit)
								||!productDB.getDescription().equals(description)
								||productDB.getUnitPrice()!=(unitPrice)
								||productDB.getProductPriceKind()!=productPriceKind
								){
							productDB.setMark(mark);
							productDB.setUnit(unit);
							productDB.setDescription(description);
							productDB.setUnitPrice(unitPrice);
							productDB.setProductPriceKind(productPriceKind);
							System.out.println("merging : "+ i+"\t "+code+"\t "+mark+"\t "+unit+"\t "+description+"\t "+unitPrice+"\t "+productPriceKind);
							try {
								response.getWriter().println("merging : "+ i+"\t "+code+"\t "+mark+"\t "+unit+"\t "+description+"\t "+unitPrice+"\t "+productPriceKind);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							em.merge(productDB);
						}
						break;
					}
					else if(j==listStore.size()-1){
						Product product=new Product(code, unitPrice, unit, mark, description, productPriceKind);
						System.out.println("persisting : "+ i+"\t "+code+"\t "+mark+"\t "+unit+"\t "+description+"\t "+unitPrice+"\t "+productPriceKind);
						try {
							response.getWriter().println("persisting : "+ i+"\t "+code+"\t "+mark+"\t "+unit+"\t "+description+"\t "+unitPrice+"\t "+productPriceKind);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						em.persist(product);
					}
				}
			}
			for (int i = 0; i < keep.length; i++) {
				if(!keep[i]){
					em.remove(listStore.get(i));
				}
			}
	     	em.getTransaction().commit();
	     	em.close();
			ProductsStore.refresh();
			try {
				response.getWriter().println("<b>succesfully updated : products</b>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/*else if(db.equals("client")){
			File file = new File("/opt/workspace/com.ferremundo/db/DB.ods");
			Sheet sheet=null;
			try {
				sheet = SpreadSheet.createFromFile(file).getSheet(1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EntityManager em=EMF.get(EMF.UNIT_CLIENT).createEntityManager();
			em.getTransaction().begin();
			List<Client> listStore = em.createNativeQuery("select * from Client",Client.class).getResultList();
			boolean[] keep = new boolean[listStore.size()];
			int rowCount=1;//sheet.getRowCount();
			while(true){
				try{
					String code=sheet.getCellAt("A"+rowCount).getTextValue();
					if(code.startsWith("<end>")){rowCount--;break;}
					else {
						rowCount++;					
					}
				}
				catch(IndexOutOfBoundsException e){}
			}
			System.out.println("updating "+rowCount+" clients");
			try {
				response.getWriter().println("updating "+rowCount+" clients");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 1; i <= rowCount; i++) {
				String code=sheet.getCellAt("A"+i).getTextValue();
				String consummer=sheet.getCellAt("B"+i).getTextValue();
				String tel=sheet.getCellAt("C"+i).getTextValue();
				String address=sheet.getCellAt("D"+i).getTextValue();
				String cp=sheet.getCellAt("E"+i).getTextValue();
				String rfc=sheet.getCellAt("F"+i).getTextValue();
				int payment=(int)Math.round(new Float(sheet.getCellAt("G"+i).getTextValue()));
				int consummerType=(int)Math.round(new Float(sheet.getCellAt("H"+i).getTextValue()));
				String email=sheet.getCellAt("I"+i).getTextValue();
				String city=sheet.getCellAt("J"+i).getTextValue();
				String state=sheet.getCellAt("K"+i).getTextValue();
				String country=sheet.getCellAt("L"+i).getTextValue();
			
				for (int j = 0; j < listStore.size(); j++) {
					if(listStore.get(j).getCode().equals(code)){
						keep[j]=true;
						Client clientDB=listStore.get(j);
						if(!clientDB.getConsummer().equals(consummer)
								||!clientDB.getTel().equals(tel)
								||!clientDB.getAddress().equals(address)
								||!clientDB.getCp().equals(cp)
								||!clientDB.getRfc().equals(rfc)
								||clientDB.getPayment()!=payment
								||clientDB.getConsummerType()!=consummerType
								||!clientDB.getEmail().equals(email)
								||!clientDB.getCity().equals(city)
								||!clientDB.getState().equals(state)
								||!clientDB.getCountry().equals(country)
								){
							clientDB.setConsummer(consummer);
							clientDB.setTel(tel);
							clientDB.setAddress(address);
							clientDB.setCp(cp);
							clientDB.setRfc(rfc);
							clientDB.setPayment(payment);
							clientDB.setConsummerType(consummerType);
							clientDB.setEmail(email);
							clientDB.setCity(city);
							clientDB.setState(state);
							clientDB.setCountry(country);
							try {
								response.getWriter().println("merging : "+ i+"\t "+code+"\t "+consummer+"\t "+tel+"\t "+address+"\t "+cp+"\t "+rfc
										+"\t "+payment+"\t "+consummerType+"\t "+email+"\t "+city+"\t "+state+"\t "+country);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							em.merge(clientDB);
						}
						break;
					}
					else if(j==listStore.size()-1){
						Client client=new Client(code, consummer, consummerType, address, city, country, state, email, cp, rfc, tel, payment);
						try {
							response.getWriter().println("persisting : "+ i+"\t "+code+"\t "+consummer+"\t "+tel+"\t "+address+"\t "+cp+"\t "+rfc
									+"\t "+payment+"\t "+consummerType+"\t "+email+"\t "+city+"\t "+state+"\t "+country);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						em.persist(client);
					}
				}
			}
			for (int i = 0; i < keep.length; i++) {
				if(!keep[i]){
					em.remove(listStore.get(i));
				}
			}
	     	em.getTransaction().commit();
	     	em.close();
			ClientsStore.refresh();
			try {
				response.getWriter().println("<b>succesfully updated : "+db+"</b>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}* /

		else{
			try {
				response.getWriter().println("<b>failed : "+db+" parameter not allowed</b>");
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
		
	}
	*/
	/*
	public static void insertClient(Client client){
		File file = new File("/opt/workspace/com.ferremundo/db/DB.ods");
		Sheet sheet=null;
		SpreadSheet spreadSheet=null;
		try {
			spreadSheet = SpreadSheet.createFromFile(file);
			sheet = spreadSheet.getSheet(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClientsStore store=ClientsStore.instance();
		List<Client> list=store.search(client.getCode(), Integer.MAX_VALUE);
		//if(list.size()==0){
			int rowCount=1;//sheet.getRowCount();
			while(true){
				try{
					String codetmp=sheet.getCellAt("A"+rowCount).getTextValue();
					if(codetmp.startsWith("<end>")){rowCount--;break;}
					else {
						rowCount++;					
					}
				}
				catch(IndexOutOfBoundsException e){}
			}
			
			sheet.getCellAt("A"+(rowCount+1)).setValue(list.size()==0?client.getCode().toUpperCase():(client.getCode().toUpperCase()+"N"));
			sheet.getCellAt("B"+(rowCount+1)).setValue(client.getConsummer().toUpperCase());
			sheet.getCellAt("C"+(rowCount+1)).setValue(client.getTel().toUpperCase());
			sheet.getCellAt("D"+(rowCount+1)).setValue(client.getAddress().toUpperCase());
			sheet.getCellAt("E"+(rowCount+1)).setValue(client.getCp().toUpperCase());
			sheet.getCellAt("F"+(rowCount+1)).setValue(client.getRfc().toUpperCase());
			sheet.getCellAt("G"+(rowCount+1)).setValue(client.getPayment());
			sheet.getCellAt("H"+(rowCount+1)).setValue(client.getConsummerType());
			sheet.getCellAt("I"+(rowCount+1)).setValue(client.getEmail().toUpperCase());
			sheet.getCellAt("J"+(rowCount+1)).setValue(client.getCity().toUpperCase());
			sheet.getCellAt("K"+(rowCount+1)).setValue(client.getState().toUpperCase());
			sheet.getCellAt("L"+(rowCount+1)).setValue(client.getCountry().toUpperCase());
			
			sheet.setRowCount(rowCount+2);
			sheet.getCellAt("A"+(rowCount+2)).setValue("<end>");
			
			System.out.println("writing BD");
			try {
				spreadSheet.saveAs(new File("/opt/workspace/com.ferremundo/db/DB.ods"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JGet.get("http://localhost:8080/com.ferremundo/updater?db=client");
			ClientsStore.refresh();
		//}
	}
*/
	/// This method is to insert the field usoCFDI as 'G01' to the whole clients
	private static void updateClientsCFDIUse(){
		Mongo mongo=null;
		try {
			mongo = new Mongo("localhost",27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB db = mongo.getDB("globaldb");
		DBCollection collection = db.getCollection("clients");
		DBCursor cursor=collection.find();
		while(cursor.hasNext()){
			DBObject n=cursor.next();
			String code=n.get("code").toString();
			DBObject dbObject=(DBObject)JSON.parse("{ \"code\" : \""+code+"\"}");
			String js="{ \"$set\" : "+"{\"cfdiUse\" : \"\" }"+"}";
			DBObject dbObject2=(DBObject)JSON.parse(js);
			collection.update(dbObject, dbObject2);
			System.out.println(code);
		}
	}
	
	private static void fixProductHashToSimplyBeTheCode(){
		Mongo mongo=null;
		try {
			mongo = new Mongo("192.168.10.16",27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB db = mongo.getDB("globaldb");
		DBCollection collection = db.getCollection("products");
		DBCursor cursor=collection.find();
		while(cursor.hasNext()){
			DBObject n=cursor.next();
			String code=n.get("code").toString();
			DBObject dbObject=(DBObject)JSON.parse("{ \"code\" : \""+code+"\"}");
			String js="{ \"$set\" : "+"{\"hash\" : \""+code+"\" }"+"}";
			DBObject dbObject2=(DBObject)JSON.parse(js);
			collection.update(dbObject, dbObject2);
			System.out.println(code);
		}
	}
	
	public static void main(String[] args) {
		//fixProductHashToSimplyBeTheCode();
	}
}
