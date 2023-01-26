package Controller;

import Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;

public class MainViewController implements Initializable {

   Inventory inv;

    Stage stage;
    Parent scene;

    private static int partIndex;
    private static int productIndex;





    public TableColumn ProductID;
    public TableColumn ProductName;
    public TableColumn InventoryLevel2;
    public TableColumn PriceCostPerUnit2;
    public AnchorPane anchorPane1;
    public AnchorPane anchorPane2;
    public TableView<Part> partsTable;
    public TableView<Product> productsTable;
    public TableColumn<Part, Integer> partIdCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> inventoryLevelCol1;
    public TableColumn<Part, Double> priceCostPerUnitCol1;
    public TableColumn<Product, Integer> productIdCol;
    public TableColumn<Product, String> productNameCol;
    public TableColumn<Product, Integer> inventoryLevelCol2;
    public TableColumn<Product, Double> priceCostPerUnitCol2;

    @FXML
    private TextField SearchText1;

    @FXML
    private TextField SearchText2;

    @FXML
    private Button delete1;

    @FXML
    private Button delete2;

    @FXML
    private Button toAddPart;

    @FXML
    private Button toAddProduct;

    @FXML
    private Button toModifyPart;

    @FXML
    private Button toModifyProduct;

    Part part;

    Alert noSearch = new Alert(Alert.AlertType.INFORMATION, "No items found.");
    Alert delDenied = new Alert(Alert.AlertType.ERROR, "Cannot delete product with an associated part.");

    //Alert delSuccess = new Alert(Alert.AlertType.INFORMATION, "Item successfully deleted.");

    Alert noSelection = new Alert(Alert.AlertType.ERROR, "Select an item.");

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException i) {
            return false;
        }
    }

    private void updateProductsTable() {
        productsTable.setItems(Inventory.getAllProducts());

        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol2.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void updatePartsTable(){
        partsTable.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol1.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        updatePartsTable();

        updateProductsTable();
    }

        public static int getPartIndex() {
            return partIndex;
        }

        public static int getProductIndex() {
            return productIndex;
        }

    public boolean confirmDel(String item) {
        boolean delete = false;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + item + "?",
                ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete Item?");


        Optional<ButtonType> response = alert.showAndWait();

        if (response.get() == ButtonType.YES) {
            delete = true;
        }

        return delete;
    }

    //REVIEW!!!
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Product> productInventorySearch = FXCollections.observableArrayList();

    public void MainScreenController(Inventory inv) {
        this.inv = inv;
    }


    @FXML
    private void searchHandler1(ActionEvent event) {

        String q = SearchText1.getText();

        ObservableList<Part> partName = Inventory.lookupPart(q);

     //Searches by Part Name first then if there are no results it goes to search by ID.
        if(partName.size() == 0) {
            try {

                int Id = Integer.parseInt(q);
                Part pt = Inventory.lookupPart(Id);
                if (pt != null)
                    partName.add(pt);
            } catch (NumberFormatException e) {
                //ignore
            }
            if (partName.size() == 0)
                noSearch.showAndWait();
        }

        partsTable.setItems(partName);
       // SearchText1.clear();




    }
    public void searchHandler2(ActionEvent actionEvent) {
        String q = SearchText2.getText();

        ObservableList<Product> productName = Inventory.lookProduct(q);

//Tells the application to look for the name first then to look for an ID.
        if(productName.size() == 0) {
            try {

                int Id = Integer.parseInt(q);
                Product pr = Inventory.lookupProduct(Id);
                if (pr != null)
                    productName.add(pr);
            } catch (NumberFormatException e) {
                //ignore
            }
            if(productName.size() == 0)
                noSearch.showAndWait();
        }

        productsTable.setItems(productName);
       // SearchText2.clear();
    }

    @FXML
    void addHandler1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource( "/view/AddPartView.fxml"));
        Stage stage = (Stage) toAddPart.getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    void addHandler2(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/View/AddProductView.fxml"));
        Stage stage = (Stage) toAddProduct.getScene().getWindow();
        Scene scene = new Scene(root, 1072, 744);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();

    }


  /*  private void errorWindow(int code) {
        if (code == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Empty Inventory.");
            alert.setContentText("There's nothing to select.");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("You must select an item.");
            alert.showAndWait();
        }
    }

    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Are you sure you want to delete: " + name + "?");
        alert.setContentText("Click ok to confirm");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
    private boolean verifyDelete(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete product");
        alert.setHeaderText("Are you sure you want to delete: " + name + " this product still has parts assigned to it!");
        alert.setHeaderText("Are you sure you want to delete: " + name );
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }*/
    //Deletes a part from the parts table.
    @FXML
    void deleteHandler1(ActionEvent event) {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if (selectedPart != null) {
            if (confirmDel(selectedPart.getName())) {
                if (Inventory.deletePart(selectedPart));
                   // delSuccess.show();
                else if (!Inventory.deletePart(selectedPart))
                    delDenied.show();
            }
        }
        else
            noSelection.showAndWait();
    }

        /*Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are trying to delete a part, would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Part removePart = partsTable.getSelectionModel().getSelectedItem();
            inv.deletePart(removePart);
            partInventory.remove(removePart);
            partsTable.refresh();
        }*/

    @FXML
    void deleteHandler2(ActionEvent event) {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            if (selectedProduct.getAllAssociatedParts().size() != 0) {
                Alert assocParts = new Alert(Alert.AlertType.ERROR, "Product cannot be deleted because a part is associated to it.");
                assocParts.showAndWait();
            } else if (confirmDel(selectedProduct.getName())) {
                if (Inventory.deleteProduct(selectedProduct));
                 //   delSuccess.show();
                else if (!Inventory.deleteProduct(selectedProduct))
                    delDenied.show();
            }
        }
        else
            noSelection.showAndWait();
    }

      /*  Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are trying to delete a product, would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Product removeProduct = productsTable.getSelectionModel().getSelectedItem();


            inv.deleteProduct(removeProduct);
            productInventory.remove(removeProduct);
            productsTable.refresh();
        }
    }*/

    @FXML
    void exitHandler(ActionEvent event) {
        Platform.exit();

    }

    @FXML
    public void modifyHandler1(ActionEvent event) throws IOException {

      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource("/View/ModifyPartView.fxml"));
      Parent toModifyPartParent = loader.load();
      Scene toModifyPartScene = new Scene(toModifyPartParent);

        ModifyPartController MPController = loader.getController();
        MPController.sendPart(partsTable.getSelectionModel().getSelectedItem());



        Stage toModifyPartStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        toModifyPartStage.setScene(toModifyPartScene);
        toModifyPartStage.show();
    }



    @FXML
    void modifyHandler2(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/View/ModifyProductView.fxml"));
        Parent toModifyProductParent = loader.load();
        Scene toModifyProductScene = new Scene(toModifyProductParent);

        ModifyProductController MPRController = loader.getController();
        MPRController.sendProduct(productsTable.getSelectionModel().getSelectedItem());



        Stage toModifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        toModifyProductStage.setScene(toModifyProductScene);
        toModifyProductStage.show();
    }



    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

   /* public static void selectedWarning() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ITEM NOT SELECTED");
        alert.setHeaderText("This action requires a selection");
        alert.setContentText("Please select an item from the table to continue");
        alert.showAndWait();
    }*/

    
}
