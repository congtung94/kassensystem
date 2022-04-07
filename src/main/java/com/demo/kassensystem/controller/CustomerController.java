package com.demo.kassensystem.controller;

import com.demo.kassensystem.Repos.CustomerRepo;
import com.demo.kassensystem.model.Customer;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CustomerController implements Initializable {

    @FXML
    private Pane tablePane;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> customerNrCol;

    @FXML
    private TableColumn<Customer, String> nameCol;

    @FXML
    private TableColumn<Customer, String> strasseCol;

    @FXML
    private TableColumn<Customer, String> ortCol;

    @FXML
    private TableColumn<Customer, String> telCol;

    @FXML
    private TableColumn<Customer, String> lastOrderCol;

    @FXML
    private TableColumn<Customer, String> notizCol;

    @FXML
    private HBox searchHbox;

    @FXML
    private HBox functionHbox;

    @FXML
    private Label customerCountLabl;


    @FXML
    private TextField searchBox;

    private String newCusNr = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        searchHbox.prefWidthProperty().bind(tablePane.widthProperty().multiply(0.48));
        functionHbox.prefWidthProperty().bind(tablePane.widthProperty().multiply(0.48));

        getCustomers();
        updateCustomerTable();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                searchBox.requestFocus();
            }
        });

        searchBox.setOnKeyPressed( event -> {
            if (event.getCode() == KeyCode.DOWN){
                customerTable.requestFocus();
                customerTable.getSelectionModel().selectFirst();
            }
            if( event.getCode() == KeyCode.ENTER ) {
                searchBox.selectAll();
                Integer cusNr;
                try {
                   cusNr = Integer.parseInt(searchBox.getText());
                }catch (NumberFormatException e){
                    notify("Achtung" , "Fehlgeschlagen" , "Kundennummer war keine Nummer !");
                    return;
                }
                try {
                    if (!CustomerRepo.getCustomerRepo().existsCustomer(cusNr)){
                        newCusNr = cusNr+"";
                        addCustomer();
                    }
                } catch (SQLException e) {
                    notify("Neuen Kunden hinzufügen", "Fehlgeschlagen !" , "Datenbank Fehler ");
                }

            }

        } );

        customerTable.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){
                try {
                    loadOrderView();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadOrderView() throws IOException {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();

        Scene current = tablePane.getScene();
        BorderPane homePane =(BorderPane) current.getRoot();

        homePane.getChildren().remove(homePane.getCenter());
        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/fragments/order.fxml"));
        Pane orderView = loader1.load();

        orderView.prefWidthProperty().bind(homePane.widthProperty().multiply(0.932));
        orderView.prefHeightProperty().bind(homePane.heightProperty().multiply(0.95));

        OrderController orderController = loader1.getController();
        orderController.setCustomer(customer);

        homePane.setCenter(orderView);
    }

    private void getCustomers (){
        ObservableList<Customer> customers = null;
        try {
            customers = CustomerRepo.getCustomerRepo().getCustomers();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return;
        }

        customerNrCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> customer) {
                return new SimpleStringProperty(customer.getValue().getCusNr()+"");
            }
        });
        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> customer) {
                if (!customer.getValue().getFirstName().equals("")){
                    return new SimpleStringProperty
                            (customer.getValue().getLastName() + ", " + customer.getValue().getFirstName());
                }
                return new SimpleStringProperty(customer.getValue().getLastName());
            }
        });
        strasseCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> customer) {
                return new SimpleStringProperty(customer.getValue().getStrasse()
                        + ". " + customer.getValue().getHausNr());
            }
        });
        ortCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> customer) {
                return new SimpleStringProperty(customer.getValue().getOrt());
            }
        });
        telCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> customer) {
                String tel1 = customer.getValue().getTel1();
                String tel2 = customer.getValue().getTel2();
                String tel3 = customer.getValue().getTel3();
                if (tel2 != null && tel3 != null){
                    return new SimpleStringProperty(tel1 + "\n" + tel2 + "\n"+ tel3);
                }
                if (tel2 != null){
                    return new SimpleStringProperty(tel1 + "\n" + tel2);
                }
                if (tel3 != null){
                    return new SimpleStringProperty(tel1 + "\n" + tel3);
                }
                return new SimpleStringProperty(tel1);
            }
        });
        lastOrderCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> customer) {
                return new SimpleStringProperty(customer.getValue().getLastOrder());
            }
        });
        notizCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Customer, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Customer, String> customer) {
                return new SimpleStringProperty(customer.getValue().getNotiz());
            }
        });

        customerTable.prefWidthProperty().bind(tablePane.widthProperty());
        customerTable.prefHeightProperty().bind(tablePane.heightProperty());
        customerNrCol.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.08));
        nameCol.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.15));
        strasseCol.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.19));
        ortCol.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.1));
        telCol.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.15));
        lastOrderCol.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.1));
        notizCol.prefWidthProperty().bind(customerTable.widthProperty().multiply(0.22));

    }


    private Predicate<Customer> createPredicate (String searchText){
        return customer -> {
            if (searchText == null || searchText.isEmpty())
                return true;
            return searchCustomer(customer, searchText);
        };
    }

    private boolean searchCustomer (Customer customer, String searchText){
        return (customer.getCusNr()+"").startsWith(searchText) ||
                customer.getLastName().toLowerCase().contains(searchText)
                || customer.getFirstName().toLowerCase().contains(searchText)
                || customer.getOrt().toLowerCase().contains(searchText)
                || customer.getTel1().toLowerCase().replace(" ","").startsWith(searchText)
                || customer.getTel1().toLowerCase().replace(" ","").startsWith("089" + searchText)
                || customer.getTel2().toLowerCase().replace(" ","").startsWith(searchText)
                || customer.getTel2().toLowerCase().replace(" ","").startsWith("089" + searchText)
                || customer.getTel3().toLowerCase().replace(" ","").startsWith(searchText)
                || customer.getTel3().toLowerCase().replace(" ","").startsWith("089" + searchText)
                || customer.getStrasse().toLowerCase().contains(searchText);
    }

    private void updateCustomerTable(){
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        try {
            customers = CustomerRepo.getCustomerRepo().getCustomers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerCountLabl.setText("Kundenanzahl : " + customers.size());
        FilteredList<Customer> customerFilteredList = new FilteredList<>(customers);
        customerTable.setItems(customerFilteredList);
        searchBox.textProperty().addListener((observableValue, s, t1) -> {
            customerFilteredList.setPredicate(createPredicate(t1.toLowerCase()));
        });
    }

    public void addCustomer() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(tablePane.getScene().getWindow());
        dialog.setTitle("Neuen Kunde Hinzufügen");
        dialog.setHeaderText("Neuer Kunde");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/customerDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        CustomerDialogController controller = loader.getController();
        controller.setNewCusNr(newCusNr);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            Customer newCustomer = controller.getNewCustomer();
            if (newCustomer == null){
                notify("Achtung" , "Fehlgeschlagen" , "Kundennummer war keine Nummer !");
                return;
            }

            try {
                CustomerRepo.getCustomerRepo().addCustomer(newCustomer);
                notify( "Neuen Kunden hinzufügen", "Erfolgreich !", newCustomer.toString());
            } catch (SQLException e) {
                notify("Neuen Kunden hinzufügen", "Fehlgeschlagen !" , "Datenbank Fehler ");
                return;
            }
            updateCustomerTable();

        }
    }

    public void editCustomer() {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        int selectedIndex = customerTable.getSelectionModel().getSelectedIndex();

        if (customer == null){
            notify("Achtung !", "Keinen Kunde gewählt", "  wählen Sie einen aus !  ");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(tablePane.getScene().getWindow());
        dialog.setTitle("Kundendaten bearbeiten");
        dialog.setHeaderText("Bearbeiten");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/customerDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        CustomerDialogController controller = loader.getController();
        controller.editCustomer(customer);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK){
            controller.editedCustomer(customer);
            try {
                CustomerRepo.getCustomerRepo().updateCustomer(customer);
                updateCustomerTable();
                customerTable.getSelectionModel().select(selectedIndex);
            } catch (SQLException e) {
                notify("Achtung", "fehlgeschlagen", "Datenbank Fehler");
            }
        }
    }

    public void deleteCustomer() {
        Customer customer = customerTable.getSelectionModel().getSelectedItem();
        if (customer == null){
            notify("Achtung !", "Keinen Kunde gewählt", "  wählen Sie einen aus !  ");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Kunden löschen");
        alert.setHeaderText(null);
        alert.setContentText("  Dieser Kunden wird dann gelöscht : \n" + customer.toString());
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            try {
                CustomerRepo.getCustomerRepo().deleteCustomer(customer);
                updateCustomerTable();
            } catch (SQLException e) {
                notify("Achtung", "fehlgeschlagen", "Datenbank Fehler");
            }
        }

    }

    private void notify (String title, String header, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Text text = new Text(message);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setContent(text);
        dialogPane.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.setHeaderText(header);
        alert.setTitle(title);

        alert.show();
    }
}
