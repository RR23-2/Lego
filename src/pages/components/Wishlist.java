package pages.components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import objects.Lego;

public class Wishlist extends HBox{
	Lego lego;
	public Wishlist(Lego lego) {
		this.lego = lego;
		init();
	}
	public void init() {
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
		HBox removeBox = new HBox();
		removeBox.setMinSize(150, 80);
		removeBox.setAlignment(Pos.CENTER_RIGHT);
		Button removeBtn = new Button("Remove");
		removeBtn.setStyle("-fx-background-color: #FACF00; -fx-background-radius: 5; -fx-cursor: hand");
		removeBtn.setTextFill(Color.web("#FAFAFA"));
		removeBtn.setFont(Font.font("Arial", 15));
		removeBtn.setOnAction(e -> remove(lego));
		removeBox.getChildren().add(removeBtn);
		
		getChildren().addAll(idBox, nameBox, removeBox);
		setMinSize(800, 80);
		setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 10");
		setAlignment(Pos.BASELINE_CENTER);
	}
	
	public void remove(Lego lego) {
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = c.createStatement();
			s.executeUpdate(String.format("delete from wishlists where UserID like '%s' and LegoID like '%s'", Main.currentUser.getUserID(), lego.getLegoID()));
			c.close();
			s.close();
			Main.wishlist();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
