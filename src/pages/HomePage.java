package pages;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import pages.components.HomeSideBar;
import pages.components.NavigationBar;
import pages.components.ProductCard;
import pages.components.ProductCardManager;

public class HomePage extends Scene{
	public HomePage() {
		super(new BorderPane(), 1366, 768);
		BorderPane mainPane = (BorderPane) this.getRoot();
		
		// product list
		FlowPane productPane = new FlowPane();
		productPane.setMinWidth(1185);
		productPane.setMaxWidth(1185);
		productPane.setPadding(new Insets(20));
		productPane.setHgap(10);
		productPane.setVgap(10);
		
		// using singleton to minimize processing time
		ArrayList<ProductCard> legos = ProductCardManager.getInstance().productCards;
		for(VBox l : legos) {
			productPane.getChildren().add(l);
		}
		
		ScrollPane productScrollPane = new ScrollPane(productPane);
		
		
		// set main pane
		mainPane.setCenter(productScrollPane);
		mainPane.setLeft(HomeSideBar.getInstance());
		mainPane.setTop(NavigationBar.getInstance());
	}
}
