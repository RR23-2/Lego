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
import pages.components.NavigationBar;
import pages.components.Transaction;

public class TransactionPage extends Scene{
	public TransactionPage() {
		super(new BorderPane(), 1366, 768);
		BorderPane mainPane = (BorderPane) this.getRoot();
		
		// navbar
		mainPane.setTop(NavigationBar.getInstance());
		
		// transaction lists
		VBox transactionBox = new VBox();
		ScrollPane scrollBox = new ScrollPane(transactionBox);
		scrollBox.setPadding(new Insets(50, 433, 50, 433));
		transactionBox.setSpacing(15);
		
		ArrayList<String> transactions = fetchTransactions();
		
		if(transactions.isEmpty()) {
			transactionBox.getChildren().add(new Label("You haven't done any transaction!"));
		}
		for (String t : transactions) {
			transactionBox.getChildren().add(new Transaction(t));
		}
		
		mainPane.setCenter(scrollBox);
	}
	
	private ArrayList<String> fetchTransactions(){
		ArrayList<String> transactions = new ArrayList<>();
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(String.format("select TransactionID from transactions where UserID like '%s'", Main.currentUser.getUserID()));
			while(rs.next()) {
				transactions.add(rs.getString("TransactionID"));
			}
			c.close();
			s.close();
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return transactions;
	}
}
