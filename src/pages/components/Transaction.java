package pages.components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Transaction extends VBox {
	TransactionDetail detail;
	LocalDate transactionDate;
	private boolean isDetailed = false;
	public Transaction(String transactionID) {
		HBox headerBox = new HBox();
		detail = new TransactionDetail(transactionID);
		Connection c = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			c = DriverManager.getConnection("jdbc:mysql://localhost:3306/lego", "root", "");
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(String.format("select TransactionDate from transactions where TransactionID like '%s'", transactionID));
			while(rs.next()) {
				transactionDate = LocalDate.parse(rs.getString("TransactionDate"));
			}
			c.close();
			s.close();
			rs.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Label idLbl = new Label("ID: " + transactionID);
		idLbl.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		Label dateLbl = new Label("Date: " + transactionDate);
		dateLbl.setFont(Font.font("Arial", 20));
		Button detailBtn = new Button("View Detail");
		detailBtn.setStyle("-fx-background-color: #FACF00; -fx-background-radius: 5; -fx-cursor: hand");
		detailBtn.setTextFill(Color.web("#FAFAFA"));
		detailBtn.setFont(Font.font("Arial", 15));
		detailBtn.setOnAction(e -> {
			if(isDetailed) {
				detailBtn.setText("View Detail");
				getChildren().remove(detail);
			}
			else {
				detailBtn.setText("Close Detail");
				getChildren().add(detail);
			}
			isDetailed = isDetailed == true ? false : true;
		});
		HBox idBox = new HBox();
		idBox.getChildren().add(idLbl);
		idBox.setMinSize(133,  80);
		idBox.setAlignment(Pos.CENTER_LEFT);
		HBox dateBox = new HBox();
		dateBox.getChildren().add(dateLbl);
		dateBox.setMinSize(133, 80);
		dateBox.setAlignment(Pos.CENTER);
		HBox detailBox = new HBox();
		detailBox.getChildren().add(detailBtn);
		detailBox.setMinSize(133, 80);
		detailBox.setAlignment(Pos.CENTER_RIGHT);
		
		headerBox.getChildren().addAll(idBox, dateBox, detailBox);
		headerBox.setMinSize(500, 80);
		headerBox.setStyle("-fx-background-color: #FAFAFA; -fx-background-radius: 10");
		headerBox.setAlignment(Pos.CENTER);
		getChildren().add(headerBox);
		setAlignment(Pos.BASELINE_CENTER);
		setSpacing(5);
	}
}
