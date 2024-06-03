package pages;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import objects.User;
import pages.components.NavigationBar;

public class LoginPage extends Scene {
	public LoginPage() {
		super(new BorderPane(), 1366, 768);
		// init
		BorderPane mainPane = (BorderPane) this.getRoot();
		
		// form
		VBox formBox = new VBox();
		Label emailLbl = new Label("Email");
		emailLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		emailLbl.setPadding(new Insets(0, 0, 0, 15));
		Label passwordLbl = new Label("Password");
		passwordLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		passwordLbl.setPadding(new Insets(0, 0, 0, 15));
		TextField emailField = new TextField();
		emailField.setStyle("-fx-background-radius: 30");
		emailField.setMinSize(500, 50);
		emailField.setFont(Font.font("Arial", 20));
		emailField.setPadding(new Insets(0, 0, 0, 15));
		PasswordField passwordField = new PasswordField();
		passwordField.setMinSize(500, 50);
		passwordField.setStyle("-fx-background-radius: 30");
		passwordField.setFont(Font.font("Arial", 20));
		passwordField.setPadding(new Insets(0, 0, 0, 15));
		Button loginBtn = new Button("Login");
		loginBtn.setStyle("-fx-background-color: #FFCF00; -fx-background-radius: 30; -fx-cursor: hand");
		loginBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		loginBtn.setTextFill(Color.web("#000000"));
		loginBtn.setMinSize(500, 50);
		loginBtn.setOnAction(e -> login(emailField.getText(), passwordField.getText()));
		Hyperlink toRegister = new Hyperlink("Not registered yet? Register here!");
		toRegister.setFont(Font.font("Arial", 15));
		toRegister.setTextFill(Color.BLACK);
		toRegister.setOnMouseClicked(e -> Main.register());
		formBox.getChildren().addAll(emailLbl, emailField, passwordLbl, passwordField, loginBtn, toRegister);
		formBox.setMaxWidth(500);
		formBox.setAlignment(Pos.CENTER_LEFT);
		formBox.setSpacing(5);
		
		mainPane.setCenter(formBox);
	}
	
	public void login(String email, String password) {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Error");
		error.setHeaderText("Invalid Data");
		User user = fetchLogin(email);
		if(email.length() <= 0 || password.length() <= 0) {
			error.setContentText("Email and password must be filled!");
			error.show();
		}
		else if(user == null) {
			error.setContentText("Email isn't registered!");
			error.show();
		}
		else if(!user.getUserPassword().equals(password)) {
			error.setContentText("Password is incorrect!");
			error.show();
		}
		else {
			Main.currentUser = user;
			NavigationBar.getInstance().updateNavigation();
			if(Main.bufferLego != null) {
				Main.detail(Main.bufferLego);
				Main.bufferLego = null;
			}
			else {
				Main.home();
			}
		}
	}
	
	public User fetchLogin(String email) {
		User user = null;
		
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(String.format("select * from users where UserEmail LIKE '%s'", email));
			if(rs.next()) {
				String userID, userName, userEmail, userPassword;
				LocalDate userDOB;
				userID = rs.getString("UserID");
				userName = rs.getString("UserName");
				userEmail = rs.getString("UserEmail");
				userPassword = rs.getString("UserPassword");
				userDOB = LocalDate.parse(rs.getString("UserDOB"));
				user = new User(userID, userName, userEmail, userPassword, userDOB);
			}
			s.executeUpdate(String.format("insert into CurrentUser values('%s')", user.getUserID()));
			s.close();
			con.close();
			rs.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return user;
	}
}
