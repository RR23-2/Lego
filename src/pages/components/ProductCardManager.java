package pages.components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import objects.Lego;

public class ProductCardManager {
	public ArrayList<ProductCard> productCards = fetchCards();
	public static ProductCardManager instance = null;
	public static ProductCardManager getInstance() {
		if(instance == null) {
			instance = new ProductCardManager();
		}
		return instance;
	}
	private ProductCardManager() {
		
	}
	private ArrayList<ProductCard> fetchCards(){
		ArrayList<ProductCard> cards = new ArrayList<ProductCard>();
		
		ArrayList<Lego> legos = new ArrayList<>();
				
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			
			Statement s;
			s = con.createStatement();
			ResultSet rs;
			rs = s.executeQuery("select * from legos");
			String legoName;
			int legoID, legoMinAge, legoPieces;
			double legoPrice;
			while(rs.next()) {
				legoID = rs.getInt("LegoID");
				legoName = rs.getString("LegoName");
				legoMinAge = rs.getInt("LegoMinAge");
				legoPieces = rs.getInt("LegoPieces");
				legoPrice = rs.getDouble("LegoPrice");
				legos.add(new Lego(legoID, legoName, legoMinAge, legoPieces, legoPrice));
			}
			
			rs.close();
			s.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (Lego l : legos) {
			cards.add(new ProductCard(l));
		}
		
		return cards;
	}
	public void sortByID(boolean asc) {
        Collections.sort(this.productCards, Comparator.comparingInt(o -> o.lego.getLegoID()));
        if(!asc) Collections.reverse(this.productCards);
	}
	
	public void sortByName(boolean asc) {
		Collections.sort(this.productCards, Comparator.comparing(o -> o.lego.getLegoName()));
        if(!asc) Collections.reverse(this.productCards);
	}
	
	public void sortByPrice(boolean asc) {
		Collections.sort(this.productCards, Comparator.comparingDouble(o -> o.lego.getLegoPrice()));
        if(!asc) Collections.reverse(this.productCards);
	}
	
	public void resetSorting() {
		sortByID(true);
	}
}
