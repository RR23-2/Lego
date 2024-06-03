package pages.components;

import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import main.Main;

public class HomeSideBar extends VBox {
	Hyperlink sortByID;
	Hyperlink sortByName;
	Hyperlink sortByPrice;
	boolean ascID, ascName, ascPrice = true;
	public static HomeSideBar instance = null;
	public static HomeSideBar getInstance() {
		if(instance == null) {
			instance = new HomeSideBar();
		}
		return instance;
	}
	private HomeSideBar() {
		// init
		sortByID = new Hyperlink("Sort by ID");
		sortByID.setStyle("-fx-border-color: transparent; -fx-underline: false; -fx-text-fill: black");
		sortByID.setFont(Font.font("Arial", 15));
		sortByID.setOnAction(e -> sortByID());
		sortByName = new Hyperlink("Sort by Name");
		sortByName.setStyle("-fx-border-color: transparent; -fx-underline: false; -fx-text-fill: black");
		sortByName.setFont(Font.font("Arial", 15));
		sortByName.setOnAction(e -> sortByName());
		sortByPrice = new Hyperlink("Sort by Price");
		sortByPrice.setStyle("-fx-border-color: transparent; -fx-underline: false; -fx-text-fill: black");
		sortByPrice.setFont(Font.font("Arial", 15));
		sortByPrice.setOnAction(e -> sortByPrice());
		
		// home side bar
		getChildren().addAll(sortByID, sortByName, sortByPrice);
		setPrefWidth(166);
		setStyle("-fx-background-color: #FFCF00;");
		setPadding(new Insets(20, 5, 20, 10));
		setSpacing(10);
	}
	
	public void sortByID() {
		resetLinks();
		ascName = true;
		ascPrice = true;
		if(ascID) sortByID.setText("Sort by ID (1 - 9)");
		else sortByID.setText("Sort by ID (9 - 1)");
		ProductCardManager.getInstance().sortByID(ascID);
		ascID = ascID == true ? false : true;
		Main.home();
	}
	
	public void sortByName() {
		resetLinks();
		ascID = true;
		ascPrice = true;
		if(ascName) sortByName.setText("Sort by Name (A - Z)");
		else sortByName.setText("Sort by Name (Z - A)");
		ProductCardManager.getInstance().sortByName(ascName);
		ascName = ascName == true ? false : true;
		Main.home();
	}
	
	public void sortByPrice() {
		resetLinks();
		ascID = true;
		ascName = true;
		if(ascPrice) sortByPrice.setText("Sort by Price (1 - 9)");
		else sortByPrice.setText("Sort by Price (9 - 1)");
		ProductCardManager.getInstance().sortByPrice(ascPrice);
		ascPrice = ascPrice == true ? false : true;
		Main.home();
	}
	
	public void resetLinks() {
		sortByID.setText("Sort by ID");
		sortByName.setText("Sort by Name");
		sortByPrice.setText("Sort by Price");
	}
	
	public void resetSorting() {
		resetLinks();
		ascID = true;
		ascName = true;
		ascPrice = true;
	}
}
