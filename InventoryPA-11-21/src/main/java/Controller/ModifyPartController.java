package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.OutSourced;
import Model.Part;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static Controller.MainViewController.getPartIndex;
import static Model.Inventory.getAllParts;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {

    public TextField partIdTF;

    Stage stage;
    Parent scene;

  //  private boolean isInhouse;
    private final int index =  getPartIndex();

    @FXML
    private RadioButton inHouseRadio;

    @FXML
    private RadioButton outSourcedRadio;


    public Label changeLabel2;
    public ToggleGroup tGroup2;
    public TextField nameField;
    public TextField stockField;
    public TextField priceCostField;
    public TextField maxField;
    public TextField machineIdField;
    public TextField minField;
    @FXML
    private Button cancelModifyPart;

    @FXML
    private RadioButton inHouseModify;

    @FXML
    private AnchorPane modifyPart;

    @FXML
    private RadioButton outsourcedModify;




    @FXML
    private Button saveModifyPart1;
    Part part;

    private int partId = Inventory.incrementPartId();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


           Part part = Inventory.getAllParts().get(index);
            partId = Inventory.getAllParts().get(index).getId();
            partIdTF.setText(String.valueOf(part.getId()));
            nameField.setText(part.getName());
            stockField.setText(Integer.toString(part.getStock()));
            priceCostField.setText(Double.toString(part.getPrice()));
            minField.setText(Integer.toString(part.getMin()));
            maxField.setText(Integer.toString(part.getMax()));

            if (part instanceof InHouse) {
                changeLabel2.setText("Machine ID");
                machineIdField.setText(Integer.toString(((InHouse) getAllParts().get(index)).getMachineId()));
                inHouseRadio.setSelected(true);
            } else {
                changeLabel2.setText("Company Name");
                machineIdField.setText(((OutSourced) getAllParts().get(index)).getCompanyName());
                outSourcedRadio.setSelected(true);
            }
        }
        

    @FXML
    private void showInHouseModify(ActionEvent event) {
        //isInhouse = true;
        changeLabel2.setText("Machine ID");
        machineIdField.setText("");
        machineIdField.setPromptText("Mach ID");
    }

    @FXML
    private void showOutsourcedModify(ActionEvent event) {
     //   isInhouse = false;
        changeLabel2.setText("Company Name");
        machineIdField.setText("");
        machineIdField.setPromptText("Company Nm");
    }

    @FXML
    private void saveModifyPart(ActionEvent event) throws IOException {
        try {
            ObservableList<Part> allParts = Inventory.getAllParts();

            int id = 0;
            String name = nameField.getText();
            double priceStr = Double.parseDouble(priceCostField.getText());
            int stockStr = Integer.parseInt(stockField.getText());
            int maxStr = Integer.parseInt(maxField.getText());
            int minStr = Integer.parseInt(minField.getText());

            if (!(maxStr > minStr) || !(minStr >= 1)) {
                throw new NumberFormatException("Min must be less than Max");
            }
            if ((stockStr > maxStr) || (stockStr < minStr)) {
                throw new NumberFormatException("Inventory Level must be between Min and Max");
            }
            for (int i = 0; i <= allParts.size(); ++i) {
                id = i + 1;
            }

        boolean selectInHouse;
        selectInHouse = inHouseRadio.isSelected();

        if (selectInHouse) {
            Part inPart = new InHouse(
                    partId,
                    nameField.getText(),
                    Double.parseDouble(priceCostField.getText()),
                    Integer.parseInt(stockField.getText()),
                    Integer.parseInt(minField.getText()),
                    Integer.parseInt(maxField.getText()),
                    Integer.parseInt(machineIdField.getText()));

            Inventory.updatePart(index, inPart);
        } else {
            Part outPart = new OutSourced(
                    partId,
                    nameField.getText(),
                    Double.parseDouble(priceCostField.getText()),
                    Integer.parseInt(stockField.getText()),
                    Integer.parseInt(minField.getText()),
                    Integer.parseInt(maxField.getText()),
                    machineIdField.getText());

            Inventory.updatePart(index, outPart);
        }

        Stage stage;
        Parent root;
        stage = (Stage) saveModifyPart1.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        root = loader.load(getClass().getResource("/View/MainView2.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    } catch (NumberFormatException e) {
            //Generates the error dialog box.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Exception: " + e.getMessage() + ". Please enter a valid value for each Text Field!"
            + "Valid inputs for the name and company name text fields are letters and words. All other inputs must be numbers.  ");
            alert.showAndWait();
        }
    }


    @FXML



    public void onCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will cancel any changes, would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            partIdTF.clear();
            nameField.clear();
            stockField.clear();
            priceCostField.clear();
            minField.clear();
            maxField.clear();
            machineIdField.clear();
            inHouseRadio.setSelected(false);
            outSourcedRadio.setSelected(false);

            Parent root = FXMLLoader.load(getClass().getResource("/View/MainView2.fxml"));
            Stage stage = (Stage) cancelModifyPart.getScene().getWindow();
            Scene scene = new Scene(root, 1021, 363);
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();
        }
    }


//Sends information of selected part to modify part screen.
  public void sendPart(Part part){

        partIdTF.setText(String.valueOf(partId));
        nameField.setText(part.getName());
        stockField.setText(String.valueOf(part.getStock()));
        priceCostField.setText(String.valueOf(part.getPrice()));


    }



}
