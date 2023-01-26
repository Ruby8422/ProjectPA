package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Controller.MainViewController.getProductIndex;


public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;
    private int index = getProductIndex();

    private final int prodId = Inventory.incrementProductId();

    private final Product product = new Product(prodId, "New Product", 0.0,0,0,0);

    public TableColumn<Part, Integer> partIdCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Integer> inventoryLevelCol1;
    public TableColumn<Part, Double> priceCostPerUnitCol1;
    public TableColumn<Part, Integer> partIdCol2;
    public TableColumn<Part, String> partNameCol2;
    public TableColumn<Part, Integer> inventoryLevelCol2;
    public TableColumn<Part, Double> priceCostPerUnitCol2;

    public TextField addProductSearch;
    public TextField addProductID;
    public TableView partsTable1;
    public TableView partsTable2;
    public TextField nameTF;
    public TextField invTF;
    public TextField priceTF;
    public TextField maxTF;
    public TextField minTF;
    @FXML
    private Button saveProduct;

    @FXML
    private Button toMain2;

    Alert noSearch = new Alert(Alert.AlertType.INFORMATION, "No items found.");


    //public ObservableList<Part> associatedParts=FXCollections.observableArrayList();

    public class noAssociatedPartsException extends Exception {
        public noAssociatedPartsException(String message) {
            super(message);
        }
    }


    //private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    @FXML
    public void addHandler2(ActionEvent event) {
        Part selectedPart;
        //program suggested casting to Part hence (Part)
        selectedPart = (Part) partsTable1.getSelectionModel().getSelectedItem();

        product.addAssociatedPart(selectedPart);
        updatePartsTable2();



    }

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will cancel any changes, would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK) {

            nameTF.clear();
            invTF.clear();
            priceTF.clear();
            minTF.clear();
            maxTF.clear();

            Parent root = FXMLLoader.load(getClass().getResource("/View/MainView2.fxml"));
            Stage stage = (Stage) toMain2.getScene().getWindow();
            Scene scene = new Scene(root, 1021, 363);
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();
        }

    }

    @FXML
    void onRemove(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are trying to delete an associated part, would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            Part selectedPart;
            selectedPart = (Part) partsTable2.getSelectionModel().getSelectedItem();
            product.deleteAssociatedPart(selectedPart);
            updatePartsTable1();

        }
    }

    @FXML
    void onSaveProduct(ActionEvent event) throws noAssociatedPartsException, IOException {
        try{
            if(product.getAllAssociatedParts().isEmpty()){
                throw new noAssociatedPartsException("All products must have at least one associated part.");
            } else {
                Product newProduct = new Product(
                        prodId,
                        nameTF.getText(),
                        Double.parseDouble(priceTF.getText()),
                        Integer.parseInt(invTF.getText()),
                        Integer.parseInt(minTF.getText()),
                        Integer.parseInt(maxTF.getText()));


                newProduct.getAllAssociatedParts().addAll(product.getAllAssociatedParts());

                Inventory.addProduct(newProduct);

                Parent root = FXMLLoader.load(getClass().getResource("/View/MainView2.fxml"));
                Stage stage = (Stage) saveProduct.getScene().getWindow();
                Scene scene = new Scene(root, 1021, 363);
                stage.setTitle("Main");
                stage.setScene(scene);
                stage.show();





            }
        }
        catch(noAssociatedPartsException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Every product must have at least one associated part.");
            alert.showAndWait();
        }
       /* String nameS = nameTF.getText();
        String inventoryS = invTF.getText();
        String priceS = priceTF.getText();
        String minS = minTF.getText();
        String maxS = maxTF.getText();



        System.out.println(nameS + " " + inventoryS + " " + priceS + " " + minS + " " + maxS);


        //Converts strings from input to integers and doubles.

        double price = Double.parseDouble(priceS);
        int inv = Integer.parseInt(inventoryS);
        int min = Integer.parseInt(minS);
        int max = Integer.parseInt(maxS);



        String error = " ";
        error = "Max";
        if (max <= min) {
            System.out.println(error + "  is less than minimum.");
            return;
        }

        if (inv > max) {
            System.out.println("Error inventory is greater than maximum allowed.");
            return;
        }

        if (inv < min) {
            System.out.println("Error inventory is less than minimum allowed.");
            return;
        }


        try {
            Parent root = FXMLLoader.load(getClass().getResource("/View/MainView2.fxml"));
            Stage stage = (Stage) saveProduct.getScene().getWindow();
            Scene scene = new Scene(root, 1021, 363);
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


    }

    @FXML
    /*void searchHandler2(ActionEvent event) {
        String searchText = addProductSearch.getText();

        ObservableList<Part> parts = Inventory.lookupPart(searchText);

        if (parts.isEmpty()) {
            try {
                int searchId = Integer.parseInt(searchText);
                Part searchPart = Inventory.lookupPart(searchId);
                if (searchPart != null)
                    parts.add(searchPart);
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        partsTable1.setItems(parts);
        addProductSearch.clear();
    }*/




    //private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    private void updatePartsTable1() {

        partsTable1.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol1.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    private void updatePartsTable2() {
        partsTable2.setItems(product.getAllAssociatedParts());

        partIdCol2.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol2.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
         partsTable1.setItems(Inventory.getAllParts());

        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol1.setCellValueFactory(new PropertyValueFactory<>("price"));


    }



    //Searches by Part Name
  /*  private static ObservableList<Part> searchByPartName(String partialName) {
        ObservableList<Part> namedPart = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part pt : allParts) {
            if(pt.getName().contains(partialName)){
                namedPart.add(pt);
            }
        }


        return namedPart;
    }*/
    public void onAddProductSearch(ActionEvent actionEvent) {
        String q = addProductSearch.getText();

        ObservableList<Part> searchedParts = Inventory.lookupPart(q);

        //Searches by Part Name first then if there are no results it goes to search by ID.
        if(searchedParts.size() == 0) {
            try {

                int Id = Integer.parseInt(q);
                Part pt = Inventory.lookupPart(Id);
                if (pt != null)
                    searchedParts.add(pt);
            } catch (NumberFormatException e) {
                //ignore
            }
            if (searchedParts.size() == 0)
                noSearch.showAndWait();
        }
        partsTable1.setItems(searchedParts);
        addProductSearch.clear();
    }

    //Searches by Part ID

   /* private Part getPartNameWithID(int ID){
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(int i = 0; i < allParts.size(); i++) {
            Part pt = allParts.get(i);

            if(pt.getId() == ID){
                return pt;
            }
        }

        return null;
    }*/



}

