package pages;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import objects.Lego;
import pages.components.NavigationBar;

public class DetailPage extends Scene{
	Lego lego;
	public DetailPage(Lego lego) {
		super(new BorderPane(), 1366, 768);
		this.lego = lego;
		// init 
		BorderPane mainPane = (BorderPane) this.getRoot();
		
		// nav bar
		mainPane.setTop(NavigationBar.getInstance());
		
		// body
		HBox body = new HBox();
		VBox detail = new VBox();
		VBox imageBox = new VBox();
		Label id = new Label("#" + Integer.toString(lego.getLegoID()));
		id.setFont(Font.font("Arial", 20));
		Label name = new Label(lego.getLegoName());
		name.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		name.setMaxWidth(500);
		name.setWrapText(true);
		name.setPadding(new Insets(0, 0, 15, 0));
		Label minAge = new Label("Ages: " + Integer.toString(lego.getLegoMinAge()) + " and up");
		minAge.setFont(Font.font("Arial", 15));
		Label pieces = new Label("Pieces: " + Integer.toString(lego.getLegoPieces()));
		pieces.setFont(Font.font("Arial", 15));
		Label price = new Label("$" + Double.toString(lego.getLegoPrice()));
		price.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		price.setPadding(new Insets(15, 0, 15, 0));
		File file = new File("assets/" + lego.getLegoID() + ".png");
		Image image = new Image(file.toURI().toString());
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(400);
		imageView.setFitHeight(400);
		imageView.setPreserveRatio(true);
		DropShadow imageShadow = new DropShadow();
		
		// image box
		imageBox.getChildren().add(imageView);
		imageBox.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 15");
		imageBox.setEffect(imageShadow);
		imageBox.setMinSize(500, 500);
		imageBox.setMaxSize(500, 500);
		imageBox.setAlignment(Pos.CENTER);
		
		// add to cart
		Spinner<Integer> quantity = new Spinner<>(1, 99, 1);
		quantity.setEditable(true);
		quantity.setStyle("");
		Button addBtn = new Button("Add to cart");
		HBox btnPane = new HBox();
		addBtn.setStyle("-fx-background-color: #0067B2; -fx-background-radius: 30; -fx-cursor: hand");
		addBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		addBtn.setTextFill(Color.web("#FAFAFA"));
		addBtn.setMinSize(300, 50);
		btnPane.getChildren().add(addBtn);
		btnPane.setPadding(new Insets(15, 0, 0, 0));
		addBtn.setOnMouseClicked(e -> addToCart(lego, quantity.getValue()));
		
		// wish list
		File blackFile = new File("assets/heart-black.png");
		File redFile = new File("assets/heart-red.png");
		Image blackImg = new Image(blackFile.toURI().toString());
		Image redImg = new Image(redFile.toURI().toString());
		ImageView blackImgView = new ImageView(blackImg);
		ImageView redImgView = new ImageView(redImg);
		blackImgView.setFitHeight(40);
		redImgView.setFitHeight(40);
		blackImgView.setPreserveRatio(true);
		redImgView.setPreserveRatio(true);
		blackImgView.setStyle("-fx-cursor: hand");
		redImgView.setStyle("-fx-cursor: hand");
		if(wishlisted(lego)) btnPane.getChildren().add(redImgView);
		else btnPane.getChildren().add(blackImgView);
		blackImgView.setOnMouseClicked(e -> wishlist(lego));
		redImgView.setOnMouseClicked(e -> wishlist(lego));
		btnPane.setSpacing(15);
		btnPane.setAlignment(Pos.CENTER_LEFT);
		
		
		// detail
		detail.getChildren().addAll(id, name, minAge, pieces, price, quantity, btnPane);
		detail.setSpacing(3);
		detail.setAlignment(Pos.CENTER_LEFT);
		
		body.getChildren().addAll(imageBox, detail);
		body.setSpacing(100);
		body.setAlignment(Pos.CENTER);
		body.setPrefSize(1300, 500);
		
		
		mainPane.setCenter(body);
	}
	
	public static void addToCart(Lego lego, int quantity) {
		if(Main.currentUser == null) {
			Alert loginAlert = new Alert(AlertType.CONFIRMATION);
			loginAlert.setTitle("Confirm");
			loginAlert.setHeaderText("Login first!");
			loginAlert.setContentText("You must log in first before adding a product to cart!");
			Optional<ButtonType> options = loginAlert.showAndWait();
			if(options.get() == ButtonType.OK) {
				Main.bufferLego = lego;
				Main.login();
			}
			else {
				loginAlert.close();
			}
		}
		else if(Period.between(Main.currentUser.getUserDateOfBirth(), LocalDate.now()).getYears() < lego.getLegoMinAge()) {
			Alert ageAlert = new Alert(AlertType.ERROR);
			ageAlert.setTitle("Warning");
			ageAlert.setHeaderText("Too young!");
			ageAlert.setContentText("You don't reach the minimum age for this lego set!");
			ageAlert.show();
		}
		else {
			Connection con = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
				Statement s = con.createStatement();
				ResultSet rs = s.executeQuery(String.format("select * from carts where UserID like '%s' and LegoID like '%s'", Main.currentUser.getUserID(), lego.getLegoID()));
				if(rs.next()) {
					s.executeUpdate(String.format("update carts set Quantity = Quantity + %d where UserID like '%s' and LegoID like '%s'", quantity, Main.currentUser.getUserID(), lego.getLegoID()));
				}
				else {
					s.executeUpdate(String.format("insert into carts values('%s', '%s', '%d')", Main.currentUser.getUserID(), lego.getLegoID(), quantity));
				}
				s.close();
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Alert success = new Alert(AlertType.INFORMATION);
			success.setTitle("Success");
			success.setHeaderText("Action successful");
			success.setContentText("Item has been added to your cart!");
			success.show();
		}
	}
	
	public static boolean wishlisted(Lego lego) {
		if(Main.currentUser == null) return false;
		else {
			Connection c = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery(String.format("select * from wishlists where UserID like '%s' and LegoID like '%s'", Main.currentUser.getUserID(), lego.getLegoID()));
				if(rs.next()) {
					c.close();
					s.close();
					rs.close();
					return true;
				}
				else {
					c.close();
					s.close();
					rs.close();					
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public void wishlist(Lego lego) {
		if(Main.currentUser == null) {
			Alert loginAlert = new Alert(AlertType.CONFIRMATION);
			loginAlert.setTitle("Confirm");
			loginAlert.setHeaderText("Login first!");
			loginAlert.setContentText("You must log in first before adding a product to wish list!");
			Optional<ButtonType> options = loginAlert.showAndWait();
			if(options.get() == ButtonType.OK) {
				Main.bufferLego = lego;
				Main.login();
			}
			else {
				loginAlert.close();
			}
		}
		else {			
			Connection c = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
				Statement s = c.createStatement();
				if(wishlisted(lego)) {
					s.executeUpdate(String.format("delete from wishlists where UserID like '%s' and LegoID like '%s'", Main.currentUser.getUserID(), lego.getLegoID()));
				}
				else {
					s.executeUpdate(String.format("insert into wishlists values('%s', '%s')", Main.currentUser.getUserID(), lego.getLegoID()));
				}
				Main.detail(lego);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
