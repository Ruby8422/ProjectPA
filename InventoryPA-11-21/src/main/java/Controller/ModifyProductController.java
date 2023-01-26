package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static Controller.MainViewController.getProductIndex;

public class ModifyProductController implements Initializable {

    Stage stage;
    Parent scene;

    private int index = getProductIndex();

    public TableView partsTable1;
    public TableColumn<Part, Integer> partIdCol1;
    public TableColumn<Part, String> partNameCol1;
    public TableColumn<Part, String> inventoryLevelCol1;
    public TableColumn<Part, Double> priceCostPerUnitCol1;
    public TableView<Part> partsTable2;
    public TableColumn<Part, Integer> partIdCol2;
    public TableColumn<Part, String> partNameCol2;
    public TableColumn<Part, Integer> inventoryLevelCol2;
    public TableColumn<Part, Double> priceCostPerUnitCol2;
    public TextField modifyProductSearch;
    public TextField modifyProductID;
    public TextField nameTF;
    public TextField invTF;
    public TextField priceTF;
    public TextField maxTF;
    public TextField minTF;
    @FXML
    private Button addModifyProduct;

    @FXML
    private Button cancelModify2;

    @FXML
    private Button removeAssociatedPart2;

    @FXML
    private Button saveModify2;

    public ObservableList<Part> tempList = FXCollections.observableArrayList();

    public ObservableList<Part> tempList2 = FXCollections.observableArrayList();

  //  public ObservableList<Part> associatedParts=FXCollections.observableArrayList();

    public class noAssocPartsException extends Exception {
        public noAssocPartsException(String message){
            super(message);
        }
    }

    private Product product = Inventory.getAllProducts().get(index);
    private int prodId = product.getId();

    private void updatePartsTable1() {
        partsTable1.setItems(Inventory.getAllParts());

        partIdCol1.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol1.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol1.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol1.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void updatePartsTable2(){
        partsTable2.setItems(product.getAllAssociatedParts());

        partIdCol2.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));
        inventoryLevelCol2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCostPerUnitCol2.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void updateTextFields(){
        modifyProductID.setText("Auto-Gen: " + Integer.toString(prodId));
        nameTF.setText(product.getName());
        invTF.setText(Integer.toString(product.getStock()));
        priceTF.setText(Double.toString(product.getStock()));
        minTF.setText(Integer.toString(product.getMin()));
        maxTF.setText(Integer.toString(product.getMax()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Loads all the data in the tables and text fields.
        updatePartsTable1();;
        updatePartsTable2();
        updateTextFields();

    }

    @FXML
    void addModify2(ActionEvent event) {
        Part selectedPart;

        selectedPart = (Part) partsTable1.getSelectionModel().getSelectedItem();
        product.addAssociatedPart(selectedPart);

        updatePartsTable1();
    }

    @FXML
    void onCancel2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource( "/View/MainView2.fxml"));
        Stage stage = (Stage) cancelModify2.getScene().getWindow();
        Scene scene = new Scene(root, 1021, 363);
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onRemove2(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You are trying to delete an associated part, would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){Part selectedPart;
            selectedPart = (Part) partsTable2.getSelectionModel().getSelectedItem();
            product.deleteAssociatedPart(selectedPart);

            updatePartsTable2();
        }
    }


    @FXML
    void onSave2(ActionEvent event) throws noAssocPartsException, IOException {

        try{
            if(product.getAllAssociatedParts().isEmpty()){
                throw new noAssocPartsException("All products must have at least one associated part.");
            }

            else {
                Product modProduct = new Product(
                        prodId,
                        nameTF.getText(),
                        Double.parseDouble(priceTF.getText()),
                        Integer.parseInt(invTF.getText()),
                        Integer.parseInt(minTF.getText()),
                        Integer.parseInt(maxTF.getText()));

                modProduct.getAllAssociatedParts().addAll(product.getAllAssociatedParts());

                Inventory.updateProduct(index, modProduct);

                Parent root = FXMLLoader.load(getClass().getResource( "/View/MainView2.fxml"));
                Stage stage = (Stage) cancelModify2.getScene().getWindow();
                Scene scene = new Scene(root, 1021, 363);
                stage.setTitle("Main");
                stage.setScene(scene);
                stage.show();
            }
        }

        catch(noAssocPartsException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Every product must have at least one associated part.");
            alert.showAndWait();
        }
    }

    public void onModifyProductSearch(ActionEvent actionEvent) {
        String q = modifyProductSearch.getText();

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
        }
        partsTable1.setItems(searchedParts);
        modifyProductSearch.clear();
    }

    //private static ObservableList<Part> allParts = FXCollections.observableArrayList();





    //Searches by Part Name
   /* private static ObservableList<Part> searchByPartName(String partialName) {
        ObservableList<Part> namedPart = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part pt : allParts) {
            if(pt.getName().contains(partialName)){
                namedPart.add(pt);
            }
        }


        return namedPart;
    }*/


    //Searches by Part ID

    /*private Part getPartNameWithID(int ID){
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(int i = 0; i < allParts.size(); i++) {
            Part pt = allParts.get(i);

            if(pt.getId() == ID){
                return pt;
            }
        }

        return null;
    }*/
    public void sendProduct(Product product){

        modifyProductID.setText(String.valueOf(Inventory.incrementPartId()));
        nameTF.setText(product.getName());
        invTF.setText(String.valueOf(product.getStock()));
        priceTF.setText(String.valueOf(product.getPrice()));


    }

}

