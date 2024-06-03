package pages.components;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.Main;
import objects.Lego;

public class ProductCard extends VBox{
	public Lego lego;
	public ProductCard(Lego lego) {
		super();
		this.lego = lego;
		init(lego);
	}

	public void init(Lego lego) {
		// init
		VBox imageBox = new VBox();
		File imageFile = new File("assets/" + lego.getLegoID() + ".png");
		Image image = new Image(imageFile.toURI().toString());
		ImageView imageView = new ImageView(image);
		VBox detailBox = new VBox();
		Label name = new Label(lego.getLegoName());
		name.setWrapText(true);
		name.setMaxWidth(200);
		Label id = new Label("#" + Integer.toString(lego.getLegoID()));
		Label price = new Label("$" + Double.toString(lego.getLegoPrice()));
		
		// image box
		imageView.setFitWidth(180);
		imageView.setFitHeight(180);
		imageView.setPreserveRatio(true);
		imageBox.getChildren().add(imageView);
		imageBox.setMinSize(200, 200);
		imageBox.setMaxSize(200, 200);
		imageBox.setAlignment(Pos.CENTER);
		
		// detail box
		id.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		name.setFont(Font.font("Arial", 14));
		price.setFont(Font.font("Arial", FontWeight.BOLD, 14));
		detailBox.getChildren().addAll(id, name, price);
		detailBox.setMaxWidth(250);
		detailBox.setSpacing(3);
		
		// product card
		DropShadow productCardShadow = new DropShadow();
		productCardShadow.setRadius(2);	
		setStyle("-fx-cursor: hand; -fx-background-color: #FAFAFA; -fx-background-radius: 15");
		setEffect(productCardShadow);
		getChildren().addAll(imageBox, detailBox);
		setMaxSize(250, 300);
		setPadding(new Insets(10));
		setSpacing(10);
		setAlignment(Pos.CENTER);
		
		setOnMouseClicked(e -> Main.detail(lego));
	}
}
