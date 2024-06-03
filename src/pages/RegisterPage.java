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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;

public class RegisterPage extends Scene {
	public RegisterPage() {
		super(new BorderPane(), 1366, 768);
		// init
		BorderPane mainPane = (BorderPane) this.getRoot();
		
		// form
		VBox formBox = new VBox();
		Label usernameLbl = new Label("Username");
		usernameLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		usernameLbl.setPadding(new Insets(0, 0, 0, 15));
		Label emailLbl = new Label("Email");
		emailLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		emailLbl.setPadding(new Insets(0, 0, 0, 15));
		Label passwordLbl = new Label("Password");
		passwordLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		passwordLbl.setPadding(new Insets(0, 0, 0, 15));
		Label confPassLbl = new Label("Confirm Password");
		confPassLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		confPassLbl.setPadding(new Insets(0, 0, 0, 15));
		Label dobLbl = new Label("Date of Birth");
		dobLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		dobLbl.setPadding(new Insets(0, 0, 0, 15));
		TextField usernameField = new TextField();
		usernameField.setStyle("-fx-background-radius: 30");
		usernameField.setMinSize(500, 50);
		usernameField.setFont(Font.font("Arial", 20));
		usernameField.setPadding(new Insets(0, 0, 0, 15));
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
		PasswordField confPassField = new PasswordField();
		confPassField.setMinSize(500, 50);
		confPassField.setStyle("-fx-background-radius: 30");
		confPassField.setFont(Font.font("Arial", 20));
		confPassField.setPadding(new Insets(0, 0, 0, 15));
		DatePicker dobPicker = new DatePicker();
		dobPicker.setMinSize(500, 50);
		dobPicker.setEditable(true);
		dobPicker.setPadding(new Insets(0, 0, 0, 15));
		Button registerBtn = new Button("Register");
		registerBtn.setStyle("-fx-background-color: #FFCF00; -fx-background-radius: 30; -fx-cursor: hand");
		registerBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		registerBtn.setTextFill(Color.web("#000000"));
		registerBtn.setMinSize(500, 50);
		registerBtn.setOnAction(e -> register(usernameField.getText(), emailField.getText(), passwordField.getText(), confPassField.getText(), dobPicker.getValue()));
		Hyperlink toLogin = new Hyperlink("Already registered? Login here!");
		toLogin.setFont(Font.font("Arial", 15));
		toLogin.setTextFill(Color.BLACK);
		toLogin.setOnMouseClicked(e -> Main.login());
		
		formBox.getChildren().addAll(usernameLbl, usernameField, emailLbl, emailField, passwordLbl, passwordField, confPassLbl, confPassField, dobLbl, dobPicker, registerBtn, toLogin);
		formBox.setMaxWidth(500);
		formBox.setAlignment(Pos.CENTER_LEFT);
		formBox.setSpacing(5);
		
		mainPane.setCenter(formBox);
	}
	
	public void register(String username, String email, String password, String confPass, LocalDate dob) {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Error");
		error.setHeaderText("Invalid data!");
		if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confPass.isEmpty() || dob.toString().isEmpty()) {
			error.setContentText("All fields must be filled!");
			error.show();
		}
		else if(!email.endsWith("@gmail.com")) {
			error.setContentText("Email must end with '@gmail.com'");
			error.show();
		}
		else if(emailInDb(email) == true) {
			error.setContentText("Email is already registered!");
			error.show();
		}
		else if(password.length() < 5) {
			error.setContentText("Password must be at least 5 characters long!");
			error.show();
		}
		else if(!password.equals(confPass)) {
			error.setContentText("Password doesn't match!");
			error.show();
		}
		else {
			Connection c = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("select count(*) as 'count' from users");
				int count = 0;
				while(rs.next()) {
					count = rs.getInt("count");
				}
				String userID = "CU" + String.format("%03d", (count + 1));
				s.executeUpdate(String.format("insert into users values('%s', '%s', '%s', '%s', '%s')", userID, username, email, password, dob));
				c.close();
				s.close();
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Main.login();
		}
	}
	
	public boolean emailInDb(String email) {
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(String.format("select * from users where UserEmail like '%s'", email));
			if(rs.next()) return true;
			c.close();
			s.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
