package pages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import pages.components.Cart;
import objects.Lego;
import pages.components.NavigationBar;

public class CartPage extends Scene{
	private double totalPrice = 0;
	private ArrayList<Cart> carts = fetchCart();
	public CartPage() {
		// init
		super(new BorderPane(), 1366, 768);
		// init
		BorderPane mainPane = (BorderPane) this.getRoot();
		VBox centerPane = new VBox();
		ScrollPane scrollPane = new ScrollPane(centerPane);
		scrollPane.setPadding(new Insets(0, 283, 0, 283));
		
		mainPane.setTop(NavigationBar.getInstance());
		mainPane.setCenter(scrollPane);
		

		centerPane.setSpacing(15);
		centerPane.setPadding(new Insets(50, 0, 50, 0));
		
		if(carts.size() <= 0) {
			Label emptyLbl = new Label("You haven't added any lego to your cart!");
			centerPane.getChildren().add(emptyLbl);
		}
		else {
			for(Cart c : carts) {
				centerPane.getChildren().add(c);
				totalPrice += c.price;
			}
		}
		
		// check out
		VBox checkOutBox = new VBox();
		Button checkOutBtn = new Button("Check out: $" + totalPrice);
		checkOutBtn.setStyle("-fx-background-color: #0067B2; -fx-background-radius: 30; -fx-cursor: hand");
		checkOutBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		checkOutBtn.setTextFill(Color.web("#FAFAFA"));
		checkOutBtn.setMinSize(300, 50);
		checkOutBtn.setOnAction(e -> checkOut());
		checkOutBox.getChildren().add(checkOutBtn);
		checkOutBox.setFillWidth(true);
		checkOutBox.setAlignment(Pos.CENTER);
		checkOutBox.setMinHeight(100);
		if(totalPrice > 0) {
			mainPane.setBottom(checkOutBox);
		}
		
	}
	
	private ArrayList<Cart> fetchCart(){
		ArrayList<Cart> carts = new ArrayList<>();
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(String.format("select l.LegoID, LegoName, LegoMinAge, LegoPieces, LegoPrice, Quantity from legos l join carts c on l.LegoID = c.LegoID where UserID like '%s'", Main.currentUser.getUserID()));
			int legoID;
			String legoName;
			int legoMinAge, legoPieces;
			double legoPrice;
			int quantity;
			while(rs.next()) {
				legoID = rs.getInt("LegoID");
				legoName = rs.getString("LegoName");
				legoMinAge = rs.getInt("LegoMinAge");
				legoPieces = rs.getInt("LegoPieces");
				legoPrice = rs.getDouble("LegoPrice");
				quantity = rs.getInt("quantity");
				Lego lego = new Lego(legoID, legoName, legoMinAge, legoPieces, legoPrice);
				carts.add(new Cart(lego, quantity));
			}
			c.close();
			s.close();
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return carts;
	}
	
	private void checkOut() {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setTitle("Confirmation");
		confirm.setHeaderText("Confirm Transaction?");
		confirm.setContentText("Please double check your cart: ");
		int i = 1;
		for (Cart c : carts) {
			confirm.setContentText(confirm.getContentText() + String.format("\n%d. %s (%d Set)", i, c.lego.getLegoName(), c.quantity));
			i++;
		}
		confirm.setContentText(confirm.getContentText() + String.format("\nYour total payment is $%.2f", totalPrice));
		confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> options = confirm.showAndWait();
		if(options.get() == ButtonType.YES) {
			Connection c = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("select count(*) as 'count' from transactions");
				int count = 0;
				if(rs.next()) {
					count = rs.getInt("count");
				}
				String id = String.format("TR%03d", (count + 1));
				s.executeUpdate(String.format("insert into transactions values('%s', '%s', '%s')", id, Main.currentUser.getUserID(), LocalDate.now()));
				for (Cart cart : carts) {
					s.executeUpdate(String.format("insert into transactiondetails values('%s', '%d', '%d')", id, cart.lego.getLegoID(), cart.quantity));
				}
				s.executeUpdate(String.format("delete from carts where UserID like '%s'", Main.currentUser.getUserID()));
				c.close();
				s.close();
				rs.close();
				Main.cart();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else {
			confirm.close();
		}
		
	}
}
