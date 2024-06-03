package pages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import main.Main;
import objects.Lego;
import pages.components.Wishlist;
import pages.components.NavigationBar;

public class WishlistPage extends Scene {
	public WishlistPage() {
		super(new BorderPane(), 1366, 768);
		// init
		BorderPane mainPane = (BorderPane) this.getRoot();
		VBox centerPane = new VBox();
		ScrollPane scrollPane = new ScrollPane(centerPane);
		scrollPane.setPadding(new Insets(0, 283, 0, 283));
		
		
		// nav bar
		mainPane.setCenter(scrollPane);
		mainPane.setTop(NavigationBar.getInstance());
		
		centerPane.setSpacing(15);
		centerPane.setPadding(new Insets(50, 0, 50, 0));
		
		ArrayList<Lego> legos = fetchWishlist();
		if(legos.size() <= 0) {
			Label emptyLbl = new Label("You haven't added any lego to your wishlist!");
			centerPane.getChildren().add(emptyLbl);
		}
		else {
			for(Lego l : legos) {
				centerPane.getChildren().add(new Wishlist(l));
			}
		}
	}
	
	public ArrayList<Lego> fetchWishlist(){
		ArrayList<Lego> legos = new ArrayList<>();
		
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(String.format("select * from legos l join wishlists w on l.LegoID = w.LegoID where UserID like '%s'", Main.currentUser.getUserID()));
			int legoID;
			String legoName;
			int legoMinAge, legoPieces;
			double legoPrice;
			while(rs.next()) {
				legoID = rs.getInt("LegoID");
				legoName = rs.getString("LegoName");
				legoMinAge = rs.getInt("LegoMinAge");
				legoPieces = rs.getInt("LegoPieces");
				legoPrice = rs.getInt("LegoPrice");
				legos.add(new Lego(legoID, legoName, legoMinAge, legoPieces, legoPrice));
			}
			c.close();
			s.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return legos;
	}
}
