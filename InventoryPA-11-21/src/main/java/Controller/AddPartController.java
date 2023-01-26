package Controller;

import Model.*;
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

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AddPartController implements Initializable {

    Stage stage;
    Parent scene;

    public RadioButton inHouseRadio;
    public RadioButton outsourcedRadio;
    public Label changeLabel;
    public AnchorPane machineIdText;
    public TextField addPartID;
    public TextField nameTF;
    public TextField invTF;
    public TextField priceTF;
    public TextField maxTF;
    public TextField machineIdTF;
    public TextField minTF;
    @FXML
    private Button saveAddPart;
    @FXML
    private Button toMain1;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addPartID.setText(String.valueOf(Inventory.incrementPartId()));

    }

    //Shows machine ID label when in-house radio button is selected.
    @FXML
    void showInHouse(ActionEvent event) {
        boolean isInhouse;
     //   isInhouse = inHouseRadio.isSelected();
        changeLabel.setText("Machine ID");
    }

    //Shows Company Name label when Outsourced radio button is selected.
    public void showOutsourced(ActionEvent actionEvent) {
        boolean isOutsourced;
        isOutsourced = outsourcedRadio.isSelected();
        changeLabel.setText("Company Name");
    }


    //Method to add a new part.
    public void saveAddPart(ActionEvent actionEvent) throws IOException {
         //Validates that the input meets the requirements.
        try {
            ObservableList<Part> allParts = Inventory.getAllParts();

            int id = 0;
            String name = nameTF.getText();
            double priceStr = Double.parseDouble(priceTF.getText());
            int stockStr = Integer.parseInt(invTF.getText());
            int maxStr = Integer.parseInt(maxTF.getText());
            int minStr = Integer.parseInt(minTF.getText());

            if (!(maxStr > minStr) || !(minStr >= 1)) {
                throw new NumberFormatException("Min must be less than Max");
            }
            if ((stockStr > maxStr) || (stockStr < minStr)) {
                throw new NumberFormatException("Inventory Level must be between Min and Max");
            }
            for (int i = 0; i <= allParts.size(); ++i) {
                id = i + 1;
            }

          //Checks which radio button is selected to determine which class to add the new part to.
            boolean selectInHouse;
            selectInHouse = inHouseRadio.isSelected();

          //Checks that the correct type of data is input into the text fields and if not generates an alert detailing
          //which text field contains the error.

            boolean accept = true;


            if (selectInHouse) {
                Inventory.addPart(new InHouse(
                        Integer.parseInt(addPartID.getText()),
                        nameTF.getText(),
                        Double.parseDouble(priceTF.getText()),
                        Integer.parseInt(invTF.getText()),
                        Integer.parseInt(minTF.getText()),
                        Integer.parseInt(maxTF.getText()),
                        Integer.parseInt(machineIdTF.getText())));
            } else {
                Inventory.addPart(new OutSourced(
                        Integer.parseInt(addPartID.getText()),
                        nameTF.getText(),
                        Double.parseDouble(priceTF.getText()),
                        Integer.parseInt(invTF.getText()),
                        Integer.parseInt(minTF.getText()),
                        Integer.parseInt(maxTF.getText()),
                        machineIdTF.getText()));
            }
            //Returns to main screen.
            Parent root = FXMLLoader.load(getClass().getResource("/View/MainView2.fxml"));
            Stage stage = (Stage) toMain1.getScene().getWindow();
            Scene scene = new Scene(root, 1021, 363);
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();

        } catch (NumberFormatException e) {
            //Generates the error dialog box.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Exception: " + e.getMessage() + ". Please enter a valid value for each Text Field." +
                    "Valid inputs for the name and company name text fields are letters and words. All other inputs must be numbers.  ");
            alert.showAndWait();
        }
    }

    @FXML
    void onCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will cancel any changes, would you like to proceed?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            nameTF.clear();
            priceTF.clear();
            invTF.clear();
            minTF.clear();
            maxTF.clear();
            machineIdTF.getText();
            inHouseRadio.setSelected(false);
            outsourcedRadio.setSelected(false);

            Parent root = FXMLLoader.load(getClass().getResource("/View/MainView2.fxml"));
            Stage stage = (Stage) toMain1.getScene().getWindow();
            Scene scene = new Scene(root, 1021, 363);
            stage.setTitle("Main");
            stage.setScene(scene);
            stage.show();
        }
    }
}









