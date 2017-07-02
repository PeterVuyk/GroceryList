package groceryList;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GroceryListFrame extends Application
{
    private static final String APPLICATION_TITLE = "Grocery List";
    private static final String MENU_BAR_OPTIONS = "_Options";
    private static final String MENU_BAR_EXIT = "_Exit";
    
    private static final String TABLE_COLUMN_PRODUCT_NAME = "Product";
    private static final String TABLE_COLUMN_PRICE = "Price";
    private static final String TABLE_COLUMN_QUANTITY = "Quantity";
    private static final String TABLE_COLUMN_TOTAL_PRICE = "Total price";
    private static final String BUTTON_ADD = "Add";
    private static final String BUTTON_DELETE = "Delete";
    
    private static final String FORM_OVERVIEW_TOTAL_QUANTITY = "Total quantity: %d";    
    private static final String FORM_OVERVIEW_TOTAL_PRICE = "Total Price: $ %s";
    
    private static final String ALERT_INVALID_INPUT_TITLE = "Invalid input";
    private static final String ALERT_INVALID_INPUT_MESSAGE = "Please fill in the required fields.";
    
    Stage window;
    VBox vBoxLayout;
    TableView<Product> groceryTable;
    TextField nameInput, priceInput, quantityInput;
    Button addButton, deleteButton;
    Label totalQuantityLabel, totalPriceLabel;
    Double totalPrice = 0.0;
    Integer totalQuantity = 0;
    
    @Override
    public void start(Stage groceryList) throws Exception
    {
	this.createFrame(groceryList);
	this.applicationMenuBar();
	this.inputGroceryForm();
	this.groceryTable();
	this.totalFormOverview();
    }

    private void createFrame(Stage groceryList)
    {
	window = groceryList;
	window.setTitle(APPLICATION_TITLE);
	
	vBoxLayout = new VBox();
	
	Scene scene = new Scene(vBoxLayout, 540, 490);
	scene.getStylesheets().add("groceryList/Stylesheet.css");
	
	window.setScene(scene);
	window.show();	
    }
    
    private void applicationMenuBar()
    {
	Menu optionMenu = new Menu(MENU_BAR_OPTIONS);
	MenuItem closeApplication = new MenuItem(MENU_BAR_EXIT);
	closeApplication.setOnAction(e -> {
	    window.close();
	});
	optionMenu.getItems().add(closeApplication);
	
	MenuBar menuBar = new MenuBar();
	menuBar.getMenus().add(optionMenu);
	vBoxLayout.getChildren().add(menuBar);
    }
        
    private void inputGroceryForm()
    {
	nameInput = new TextField();
	nameInput.setPromptText(TABLE_COLUMN_PRODUCT_NAME);
	nameInput.setMinWidth(150);
	
	priceInput = new TextField();
	priceInput.setPromptText(TABLE_COLUMN_PRICE);
	priceInput.setMaxWidth(100);
	
	quantityInput = new TextField();
	quantityInput.setPromptText(TABLE_COLUMN_QUANTITY);
	quantityInput.setMaxWidth(100);
	
	addButton = new Button(BUTTON_ADD);
	addButton.setOnAction(e -> {
	    addProduct();
	    recalculateTotal();
	});
	deleteButton = new Button(BUTTON_DELETE);
	deleteButton.setOnAction(e -> {
	    deleteProduct();
	    recalculateTotal();
	});
	
	HBox inputFormBox = new HBox();
	inputFormBox.setPadding(new Insets(10, 10, 10, 10));
	inputFormBox.setSpacing(10);
	inputFormBox.getChildren().addAll(nameInput, priceInput, quantityInput, addButton, deleteButton);
	
	vBoxLayout.getChildren().add(inputFormBox);
    }
        
    private void addProduct()
    {
	try {
	    String name = nameInput.getText();
	    Double price = Double.parseDouble(priceInput.getText().replace(",", "."));
	    Integer quantity = Integer.parseInt(quantityInput.getText());
	    Product product = new Product(name, price, quantity);
	    groceryTable.getItems().add(product);
	} catch (Exception e) {
	    AlertBox.display(
		    ALERT_INVALID_INPUT_TITLE,
		    ALERT_INVALID_INPUT_MESSAGE
		    );
	    return;
	}
	
	nameInput.clear();
	priceInput.clear();
	quantityInput.clear();
    }
    
    private void deleteProduct()
    {
	ObservableList<Product> selectedProduct, products;
	products = groceryTable.getItems();
	selectedProduct = groceryTable.getSelectionModel().getSelectedItems();
	selectedProduct.forEach(products::remove);
    }
    
    private void recalculateTotal()
    {
	ObservableList<Product> products;
	products = groceryTable.getItems();
	
	this.totalQuantity = 0;
	this.totalPrice = 0.0;
	for (Product product : products) {
	    totalQuantity += product.getQuantity();
	    totalPrice += product.getTotalPrice();
	}
	
	this.totalQuantityLabel.setText(
		String.format(FORM_OVERVIEW_TOTAL_QUANTITY, totalQuantity)
		);
	DecimalFormat decimalFormat = new DecimalFormat("#.##");
	this.totalPriceLabel.setText(
		String.format(FORM_OVERVIEW_TOTAL_PRICE, decimalFormat.format(totalPrice))
		);
    }
    
    @SuppressWarnings("unchecked")
    private void groceryTable()
    {
	TableColumn<Product, String> nameColumn = new TableColumn<>(TABLE_COLUMN_PRODUCT_NAME);
	nameColumn.setMinWidth(200);
	nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		
	TableColumn<Product, Double> priceColumn = new TableColumn<>(TABLE_COLUMN_PRICE);
	priceColumn.setMinWidth(115);
	priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
	
	TableColumn<Product, Integer> quantityColumn = new TableColumn<>(TABLE_COLUMN_QUANTITY);
	quantityColumn.setMinWidth(100);
	quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

	TableColumn<Product, Double> totalPriceColumn = new TableColumn<>(TABLE_COLUMN_TOTAL_PRICE);
	totalPriceColumn.setMinWidth(120);
	totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

	groceryTable = new TableView<>();
	groceryTable.getColumns().addAll(nameColumn, priceColumn, quantityColumn, totalPriceColumn);
	
	vBoxLayout.getChildren().add(groceryTable);
    }
    
    private void totalFormOverview()
    {
	totalQuantityLabel = new Label(
		String.format(FORM_OVERVIEW_TOTAL_QUANTITY, totalQuantity)
		);
	totalQuantityLabel.getStyleClass().add("form-result");
	
	DecimalFormat decimalFormat = new DecimalFormat("#.##");
	totalPriceLabel = new Label(
		String.format(FORM_OVERVIEW_TOTAL_PRICE, decimalFormat.format(totalPrice))
		);
	totalPriceLabel.getStyleClass().add("form-result");
	
	HBox totalFormBox = new HBox();
	totalFormBox.getStyleClass().add("total-form-box");
	totalFormBox.setPadding(new Insets(10, 10, 10, 10));
	totalFormBox.setSpacing(120);
	totalFormBox.getChildren().addAll(totalQuantityLabel, totalPriceLabel);
	
	vBoxLayout.getChildren().add(totalFormBox);
    }
}
