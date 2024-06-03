package pages.components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.Main;

public class NavigationBar extends HBox{
	private static NavigationBar instance = null;
	private HBox loggedIn = loggedIn();
	private HBox loggedOut = loggedOut();
	private HBox leftBar = leftBar();
	public static NavigationBar getInstance() {
		if(instance == null) {
			instance = new NavigationBar();
		}
		return instance;
	}
	private NavigationBar() {
		init();
	}
	private HBox loggedIn() {
		HBox rightBar = new HBox();
		Button cartBtn = new Button("Your Cart");
		cartBtn.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 5; -fx-cursor: hand");
		cartBtn.setTextFill(Color.web("#FACF00"));
		cartBtn.setFont(Font.font("Arial", 15));
		cartBtn.setOnAction(e -> Main.cart());
		Button wishListBtn = new Button("Your Wish List");
		wishListBtn.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 5; -fx-cursor: hand");
		wishListBtn.setTextFill(Color.web("#FACF00"));
		wishListBtn.setFont(Font.font("Arial", 15));
		wishListBtn.setOnAction(e -> Main.wishlist());
		Button transactionBtn = new Button("Your Transaction");
		transactionBtn.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 5; -fx-cursor: hand");
		transactionBtn.setTextFill(Color.web("#FACF00"));
		transactionBtn.setFont(Font.font("Arial", 15));
		transactionBtn.setOnAction(e -> Main.transaction());
		Button logoutBtn = new Button("Logout");
		logoutBtn.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 5; -fx-cursor: hand");
		logoutBtn.setTextFill(Color.web("#FACF00"));
		logoutBtn.setFont(Font.font("Arial", 15));
		logoutBtn.setOnAction(e -> {
			Main.currentUser = null;
			Connection con = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
				
				Statement s;
				s = con.createStatement();
				s.executeUpdate("delete from CurrentUser");
				s.close();
				con.close();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			updateNavigation();
			Main.home();
		});
		rightBar.setAlignment(Pos.CENTER_RIGHT);
		rightBar.setMinWidth(625);
		rightBar.setSpacing(10);
		rightBar.getChildren().addAll(wishListBtn, cartBtn, transactionBtn, logoutBtn);
		return rightBar;
	}
	private HBox loggedOut() {
		HBox rightBar = new HBox();
		Button loginBtn = new Button("Login");
		loginBtn.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 5; -fx-cursor: hand");
		loginBtn.setTextFill(Color.web("#FACF00"));
		loginBtn.setFont(Font.font("Arial", 15));
		loginBtn.setOnAction(e -> Main.login());
		rightBar.getChildren().add(loginBtn);
		rightBar.setAlignment(Pos.CENTER_RIGHT);
		rightBar.setMinWidth(625);
		rightBar.setSpacing(10);
		return rightBar;
	}
	
	public HBox leftBar() {
		HBox leftBar = new HBox();
		Image legoLogo = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/LEGO_logo.svg/768px-LEGO_logo.svg.png");
		ImageView legoLogoView = new ImageView(legoLogo);
		legoLogoView.setFitHeight(50);
		legoLogoView.setFitWidth(50);
		legoLogoView.setOnMouseClicked(e -> Main.home());
		legoLogoView.setStyle("-fx-cursor: hand");
		leftBar.getChildren().add(legoLogoView);
		leftBar.setMinWidth(625);
		leftBar.setAlignment(Pos.CENTER_LEFT);
		return leftBar;
	}
	
	public void updateNavigation() {
		getChildren().clear();
		if(Main.currentUser == null) {
			getChildren().addAll(leftBar, loggedOut);
		}
		else {
			getChildren().addAll(leftBar, loggedIn);
		}
	}
	
	public void init() {
		DropShadow navBarShadow = new DropShadow();
		setStyle("-fx-background-color: #FFCF00;");
		setMinHeight(60);
		setPadding(new Insets(0, 58, 0, 58));
		setAlignment(Pos.CENTER_LEFT);
		setEffect(navBarShadow);
		updateNavigation();
	}
}
