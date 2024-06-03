package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.stage.Stage;
import objects.Lego;
import objects.User;
import pages.CartPage;
import pages.DetailPage;
import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;
import pages.TransactionPage;
import pages.WishlistPage;
import pages.components.HomeSideBar;
import pages.components.ProductCardManager;

public class Main extends Application{
	
	public static User currentUser = getCurrentUser();
	public static Lego bufferLego = null;
	public static Stage stage = new Stage();
	
	// routing
	public static void home() {
		stage.setScene(new HomePage());
	}
	
	public static void detail(Lego lego) {
		resetSorting();
		stage.setScene(new DetailPage(lego));
	}
	
	public static void login() {
		resetSorting();
		stage.setScene(new LoginPage());
	}
	
	public static void register() {
		resetSorting();
		stage.setScene(new RegisterPage());
	}
	
	public static void cart() {
		resetSorting();
		stage.setScene(new CartPage());
	}
	
	public static void wishlist() {
		resetSorting();
		stage.setScene(new WishlistPage());
	}
	
	public static void transaction() {
		resetSorting();
		stage.setScene(new TransactionPage());
	}
	// reset sorting
	public static void resetSorting() {
		HomeSideBar.getInstance().resetSorting();
		ProductCardManager.getInstance().resetSorting();
	}
	
	
	//get current user
	public static User getCurrentUser() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			
			Statement s;
			s = con.createStatement();
			ResultSet rs = s.executeQuery("select u.UserID, Username, UserEmail, UserPassword, UserDoB from users u join CurrentUser cu on u.UserID = cu.UserID");
			String userID, username, userEmail, userPassword;
			LocalDate userDoB;
			if(rs.next()) {
				userID = rs.getString("UserID");
				username = rs.getString("Username");
				userEmail = rs.getString("UserEmail");
				userPassword = rs.getString("UserPassword");
				userDoB = LocalDate.parse(rs.getString("UserDoB"));
				return new User(userID, username, userEmail, userPassword, userDoB);
			}
			con.close();
			s.close();
			rs.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage = stage;
		stage.setTitle("Lego");
		resetSorting();
		home();
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
