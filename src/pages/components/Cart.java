package pages.components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import objects.Lego;

public class Cart extends HBox {
	public Lego lego;
	public int quantity;
	public double price;
	public Cart(Lego lego, int quantity) {
		this.lego = lego;
		this.quantity = quantity;
		this.price = quantity * lego.getLegoPrice();
		this.price = Math.round(this.price * 100.0) / 100.0;
		init();
	}
	private void init() {
		HBox idBox = new HBox();
		idBox.setMinSize(150, 80);
		idBox.setAlignment(Pos.CENTER_LEFT);
		Label idLbl = new Label("#" + lego.getLegoID());
		idLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		idBox.getChildren().add(idLbl);
		HBox nameBox = new HBox();
		nameBox.setMinSize(400, 80);
		nameBox.setAlignment(Pos.CENTER);
		Hyperlink nameLink = new Hyperlink(lego.getLegoName());
		nameLink.setFont(Font.font("Arial", 20));
		nameLink.setTextFill(Color.BLACK);
		nameLink.setOnMouseClicked(e -> Main.detail(lego));
		nameBox.getChildren().add(nameLink);
		HBox rightBox = new HBox();
		rightBox.setMinSize(150, 80);
		rightBox.setAlignment(Pos.CENTER_RIGHT);
		Spinner<Integer> quantitySpinner = new Spinner<>(0, 99, this.quantity);
		quantitySpinner.setOnMouseClicked(e -> update(quantitySpinner.getValue()));
		quantitySpinner.setMaxWidth(52);
		Label priceLbl = new Label("$" + String.valueOf(price));
		priceLbl.setFont(Font.font("Arial", 15));
		
		rightBox.getChildren().addAll(quantitySpinner, priceLbl);
		rightBox.setSpacing(10);
		
		getChildren().addAll(idBox, nameBox, rightBox);
		setMinSize(800, 80);
		setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 10");
		setAlignment(Pos.BASELINE_CENTER);
	}
	
	public void update(int q) {
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = c.createStatement();
			if(q > 0) {
				s.executeUpdate(String.format("update carts set quantity = %d where UserID like '%s' and LegoID like '%s'", q, Main.currentUser.getUserID(), lego.getLegoID()));
			}
			else {
				s.executeUpdate(String.format("delete from carts where UserID like '%s' and LegoID like '%s'", Main.currentUser.getUserID(), lego.getLegoID()));
			}
			c.close();
			s.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		Main.cart();
	}
}
