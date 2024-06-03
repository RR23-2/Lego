package pages.components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TransactionDetail extends VBox {
	public TransactionDetail(String transactionID) {
		// header
		Label nameLbl = new Label("Lego Name");
		nameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		Label priceLbl = new Label("Price");
		priceLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		Label quantityLbl = new Label("Quantity");
		quantityLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		Label subtotalLbl = new Label("Subtotal");
		subtotalLbl.setFont(Font.font("Arial", FontWeight.BOLD, 12));
		HBox nameBox = new HBox();
		nameBox.getChildren().add(nameLbl);
		nameBox.setMinWidth(160);
		nameBox.setAlignment(Pos.CENTER_LEFT);
		HBox priceBox = new HBox();
		priceBox.getChildren().add(priceLbl);
		priceBox.setMinWidth(90);
		priceBox.setAlignment(Pos.CENTER_LEFT);
		HBox quantityBox = new HBox();
		quantityBox.getChildren().add(quantityLbl);
		quantityBox.setMinWidth(90);
		quantityBox.setAlignment(Pos.CENTER_LEFT);
		HBox subtotalBox = new HBox();
		subtotalBox.getChildren().add(subtotalLbl);
		subtotalBox.setMinWidth(90);
		subtotalBox.setAlignment(Pos.CENTER_LEFT);
		HBox header = new HBox();
		header.getChildren().addAll(nameBox, priceBox, quantityBox, subtotalBox);
		
		getChildren().add(header);
		
		double total = 0;
		
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(String.format("select LegoName, LegoPrice, Quantity from TransactionDetails t join Legos l on t.LegoID = l.LegoID where t.TransactionID like '%s'", transactionID));
			String name;
			double price, subtotal;
			int quantity;
			while(rs.next()) {
				name = rs.getString("LegoName");
				price = rs.getDouble("LegoPrice");
				quantity = rs.getInt("Quantity");
				subtotal = price * quantity;
				total += subtotal;
				getChildren().add(row(name, price, quantity, subtotal));
			}
			c.close();
			s.close();
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		HBox totalRow = new HBox();
		Label totalLbl = new Label("Total: $" + Double.toString(total));
		totalLbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
		totalRow.getChildren().add(totalLbl);
		totalRow.setPadding(new Insets(0, 35, 0, 0));
		totalRow.setAlignment(Pos.CENTER_RIGHT);
		
		DropShadow shadow = new DropShadow();
		
		setEffect(shadow);
		getChildren().add(totalRow);
		setMinWidth(500);
		setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 10");
		setAlignment(Pos.CENTER);
		setPadding(new Insets(15, 45 , 15, 45));
		setSpacing(5);
		setMaxWidth(500);
	}
	
	public HBox row(String name, double price, int quantity, double subtotal) {
		Label nameLbl = new Label(name);
		nameLbl.setFont(Font.font("Arial", 12));
		Label priceLbl = new Label("$" + Double.toString(price));
		priceLbl.setFont(Font.font("Arial", 12));
		Label quantityLbl = new Label(Integer.toString(quantity));
		quantityLbl.setFont(Font.font("Arial", 12));
		Label subtotalLbl = new Label("$" + Double.toString(subtotal));
		subtotalLbl.setFont(Font.font("Arial", 12));
		HBox nameBox = new HBox();
		nameBox.getChildren().add(nameLbl);
		nameBox.setMinWidth(160);
		nameBox.setAlignment(Pos.CENTER_LEFT);
		HBox priceBox = new HBox();
		priceBox.getChildren().add(priceLbl);
		priceBox.setMinWidth(90);
		priceBox.setAlignment(Pos.CENTER_LEFT);
		HBox quantityBox = new HBox();
		quantityBox.getChildren().add(quantityLbl);
		quantityBox.setMinWidth(90);
		quantityBox.setAlignment(Pos.CENTER_LEFT);
		HBox subtotalBox = new HBox();
		subtotalBox.getChildren().add(subtotalLbl);
		subtotalBox.setMinWidth(90);
		subtotalBox.setAlignment(Pos.CENTER_LEFT);
		HBox row = new HBox();
		row.getChildren().addAll(nameBox, priceBox, quantityBox, subtotalBox);
		return row;
	}
}
