package com.demo.kassensystem.controller;

import com.demo.kassensystem.Repos.ItemRepo;
import com.demo.kassensystem.model.Customer;
import com.demo.kassensystem.model.Item;
import com.demo.kassensystem.model.OrderedItem;
import com.demo.kassensystem.utils.Utils;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class OrderController implements Initializable {

    @FXML
    private Pane orderPane;

    @FXML
    private HBox mainHBox;

    @FXML
    private VBox itemVBox;

    @FXML
    private HBox artikelHBox;

    @FXML
    private TableView<Item> itemsTable;

    @FXML
    private TableColumn<Item, String> itemNrCol;

    @FXML
    private TableColumn<Item, String> nameCol;

    @FXML
    private TableColumn<Item, String> priceCol;

    @FXML
    private TableColumn<Item, String> cateCol;

    @FXML
    private TableColumn<Item, String> descCol;

    @FXML
    private VBox billVBox;

    @FXML
    private VBox billArea;

    @FXML
    private HBox buttonHbox;

    @FXML
    private TextField searchBox;

    @FXML
    private TableView<OrderedItem> orderedItemsTable;

    @FXML
    private TableColumn<OrderedItem, String> orderedCountCol;

    @FXML
    private TableColumn<OrderedItem, String> orderedNrCol;

    @FXML
    private TableColumn<OrderedItem, String> orderedNameCol;

    @FXML
    private TableColumn<OrderedItem, String> orderedPriceCol;

    @FXML
    private HBox customerInfosHbox;

    @FXML
    private Label customerInfos1;

    @FXML
    private Label customerInfos2;

    @FXML
    private Label totalLabel;

    @FXML
    private Label jobStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainHBox.prefWidthProperty().bind(orderPane.widthProperty());
        mainHBox.prefHeightProperty().bind(orderPane.heightProperty());

        itemVBox.prefWidthProperty().bind(mainHBox.widthProperty().multiply(0.64));
        itemVBox.prefHeightProperty().bind(mainHBox.heightProperty());
        billVBox.prefWidthProperty().bind(mainHBox.widthProperty().multiply(0.356));
        billVBox.prefHeightProperty().bind(mainHBox.heightProperty());

        artikelHBox.prefHeightProperty().bind(itemVBox.heightProperty().multiply(0.1));
        itemsTable.prefHeightProperty().bind(itemVBox.heightProperty().multiply(0.89));
        //itemsTable.prefWidthProperty().bind(itemVBox.prefWidthProperty());

        billArea.prefHeightProperty().bind(billVBox.heightProperty().multiply(0.68));
        buttonHbox.prefHeightProperty().bind(billVBox.heightProperty().multiply(0.2));
        jobStatus.prefHeightProperty().bind(billVBox.heightProperty().multiply(0.1));

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                searchBox.requestFocus();
            }
        });

        getItems();
        updateItemsTable();
        getOrderedItems();

        itemsTable.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER){

                    Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
                    OrderedItem orderedItem = new OrderedItem(0, selectedItem.getNr(), selectedItem.getName(), selectedItem.getPrice());
                    creatInputDialogAndUpdateBill(selectedItem.getNr() + " ** " +selectedItem.getName(), orderedItem, 0);
            }
        });

        searchBox.setOnKeyPressed( event -> {
            if (event.getCode() == KeyCode.DOWN){
                itemsTable.requestFocus();
                itemsTable.getSelectionModel().selectFirst();
            }
            if( event.getCode() == KeyCode.ENTER ) {
                searchBox.selectAll();
                String textEntered = searchBox.getText();
                if (textEntered.toLowerCase().startsWith("e")){
                    String extra = textEntered.substring(1);
                    double extraValue = 0;
                    try {
                        extraValue = Double.parseDouble(extra);
                    }catch (NumberFormatException e){
                        notify("Achtung", "Fehlgeschlagen", "Nachdem E ist eine Zahl");
                        return;
                    }
                    creatInputDialogAndUpdateBill("Extra "+extra, null, extraValue);
                    return;
                }
                try {
                    Item selectedItem = ItemRepo.getItemRepo().getItemWithNr(textEntered);
                    if (selectedItem != null) {
                        OrderedItem orderedItem = new OrderedItem(0, selectedItem.getNr(), selectedItem.getName(), selectedItem.getPrice());
                        creatInputDialogAndUpdateBill(selectedItem.getNr() + " ** " + selectedItem.getName(), orderedItem, 0);
                    }
                } catch (SQLException e) {
                    notify("Achtung" , "Fehlgeschlagen", "Datenbank Fehler");
                }

            }

        } );
    }

    private void creatInputDialogAndUpdateBill(String headerText, OrderedItem orderedItem, double extra){
        Dialog<String> dialog = new TextInputDialog("1");
        dialog.setTitle("Anzahl eingeben");
        dialog.setHeaderText(headerText);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/style/style.css").toExternalForm());
        dialogPane.getStylesheets().add(getClass().getResource("/style/billStyle.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        dialogPane.getStyleClass().add("countDialog");

        Optional<String> result = dialog.showAndWait();
        String countEntered = "";
        if (result.isPresent()){
            countEntered = result.get();
        }
        int count = 0;
        try {
            count = Integer.parseInt(countEntered);
        }catch (NumberFormatException e){
            notify("Achtung", "Fehlgeschlagen", "ungültige Nummer !");
            return ;
        }
        if (count < 1){
            notify("Achtung", "Fehlgeschlagen", "ungültige Nummer !");
            return;
        }

        // update die Rechnung
        //1. extra
        if (extra != 0){
            OrderedItem newOrderedItem = new OrderedItem(count, "Extra "+ extra,"", extra );
            orderedItemsTable.getItems().add(newOrderedItem);
            updateBillTotal();
            System.out.println(totalLabel.getText());
        }
        // 2. normales Gericht aus Menu
        else {
            orderedItem.setCount(count);
            orderedItemsTable.getItems().add(orderedItem);
            updateBillTotal();
        }

    }

    private void updateBillTotal(){
        double total = 0;
        for (OrderedItem item : orderedItemsTable.getItems()){
            total += item.getCount()*item.getUnitPrice();
        }
        totalLabel.setText("Summe : " + String.format("%.2f", total));
    }

    private void getItems(){
        ObservableList<Item> items = null;
        try {
            items = ItemRepo.getItemRepo().getItems();
        } catch (SQLException e) {
            notify("Achtung !", "Fehlgeschlagen" , "Datenbank Fehler");
        }

        itemNrCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> item) {
                return new SimpleStringProperty(item.getValue().getNr());
            }
        });

        nameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> item) {
                return new SimpleStringProperty(item.getValue().getName());
            }
        });

        priceCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> item) {
                return new SimpleStringProperty(item.getValue().getPrice()+"");
            }
        });

        cateCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> item) {
                return new SimpleStringProperty(item.getValue().getCategory());
            }
        });

        descCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Item, String> item) {
                return new SimpleStringProperty(item.getValue().getDescription());
            }
        });

        itemNrCol.prefWidthProperty().bind(itemsTable.widthProperty().multiply(0.1));
        nameCol.prefWidthProperty().bind(itemsTable.widthProperty().multiply(0.19));
        priceCol.prefWidthProperty().bind(itemsTable.widthProperty().multiply(0.11));
        cateCol.prefWidthProperty().bind(itemsTable.widthProperty().multiply(0.21));
        descCol.prefWidthProperty().bind(itemsTable.widthProperty().multiply(0.37));

        itemsTable.setItems(items);
    }

    private void getOrderedItems (){
        orderedCountCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderedItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderedItem, String> orIt) {
                return new SimpleStringProperty(orIt.getValue().getCount()+"");
            }
        });
        orderedNrCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderedItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderedItem, String> orIt) {
                return new SimpleStringProperty(orIt.getValue().getItemNr());
            }
        });
        orderedNameCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderedItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderedItem, String> orIt) {
                return new SimpleStringProperty(orIt.getValue().getItemName());
            }
        });
        orderedPriceCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderedItem, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderedItem, String> orIt) {

                if (orIt.getValue().getCount() == 1) {
                    return new SimpleStringProperty(orIt.getValue().getUnitPrice()+"");
                }
                else return new SimpleStringProperty(orIt.getValue().getCount()+"*"+orIt.getValue().getUnitPrice());
            }
        });

        orderedItemsTable.prefWidthProperty().bind(billArea.widthProperty().multiply(0.99));
        orderedItemsTable.prefHeightProperty().bind(billArea.heightProperty().multiply(0.6));
        orderedCountCol.prefWidthProperty().bind(orderedItemsTable.widthProperty().multiply(0.175));
        orderedNrCol.prefWidthProperty().bind(orderedItemsTable.widthProperty().multiply(0.2));
        orderedNameCol.prefWidthProperty().bind(orderedItemsTable.widthProperty().multiply(0.40));
        orderedPriceCol.prefWidthProperty().bind(orderedItemsTable.widthProperty().multiply(0.19));

        ObservableList<OrderedItem> orderedItems = FXCollections.observableArrayList();
        orderedItemsTable.setItems(orderedItems);
    }

    public void setCustomer(Customer customer){
        customerInfos1.setText(customer.getLastName());
        if (!customer.getFirstName().equals("")){
            customerInfos1.setText(customerInfos1.getText() + ", "+ customer.getFirstName());
        }

        customerInfos1.setText(customerInfos1.getText() + "\n" + customer.getStrasse() + "." + customer.getHausNr());
        customerInfos1.setText(customerInfos1.getText() + "\n" + customer.getOrt());
        customerInfos1.setText(customerInfos1.getText() + "\nLetzte Bstl: " + customer.getLastOrder());
        customerInfos2.setText(customer.getTel1());
        if (!customer.getTel2().equals("")){
            customerInfos2.setText(customerInfos2.getText() + "\n" + customer.getTel2());
        }
        if (!customer.getTel3().equals("")){
            customerInfos2.setText(customerInfos2.getText() + "\n" + customer.getTel3());
        }

        customerInfos2.setText(customerInfos2.getText() + "\n" + Utils.getTimeNow());
        System.out.println(customerInfos2.getText());
    }

    private Predicate<Item> createPredicate (String searchText){
        return item -> {
            if (searchText == null || searchText.isEmpty())
                return true;
            return searchItem(item, searchText);
        };
    }

    private boolean searchItem(Item item, String searchText) {
        return item.getNr().toLowerCase().contains(searchText) || item.getName().toLowerCase().contains(searchText)
                || item.getCategory().toLowerCase().contains(searchText);
    }

    private void updateItemsTable(){
        ObservableList<Item> items = FXCollections.observableArrayList();
        try {
            items = ItemRepo.getItemRepo().getItems();
        } catch (SQLException e) {
            notify("Achtung" , "Fehlgeschlagen", "Datenbank Fehler");
        }
        FilteredList<Item> itemFilteredList = new FilteredList<>(items);
        itemsTable.setItems(itemFilteredList);
        searchBox.textProperty().addListener((observableValue, s, t1) -> {
            itemFilteredList.setPredicate(createPredicate(t1.toLowerCase()));
        });
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

    public void printBill() {
        // Create a printer job for the default printer
        try {
            orderedItemsTable.getSelectionModel().select(null);
            print(billArea);
        } catch (Exception e) {
            jobStatus.setText("Printen fehlgeschlagen");
        }
    }

    private void print(Node node) throws Exception {
        jobStatus.textProperty().unbind();
        jobStatus.setText("Creating a printer job...");

        // Create a printer job for the default printer
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            // Show the printer job status
            jobStatus.textProperty().
                    bind(job.jobStatusProperty().asString());

            // Print the node
            boolean printed = job.printPage(node);
            if (printed) {
                // End the printer job
                job.endJob();
            } else {
                jobStatus.textProperty().unbind();
                jobStatus.setText("Printing failed.");
            }
        } else {
            jobStatus.setText("Could not create a printer job.");
        }
    }

    public void deleteOrderedItem() {
        OrderedItem item = orderedItemsTable.getSelectionModel().getSelectedItem();
        if (item == null){
            notify("Achtung", "Fehlgeschlagen", "Wählen Sie ein Item aus");
            return;
        }
        orderedItemsTable.getItems().remove(item);
        updateBillTotal();
    }

    public void deletePressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE){
            deleteOrderedItem();
        }
    }
}
