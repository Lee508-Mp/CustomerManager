import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * VIEW + CONTROLLER.
 *  - View: start() and buildTable() build the layout (controls on screen).
 *  - Controller: handleSave() and handleDelete() "respond to UI actions".
 *  - Service: CustomerService holds the rules and the list.
 *  - DAO: not needed yet. Data is in memory.
 */
public class CustomerApp extends Application {

    // The service owns the data. The UI only asks it to add or remove.
    private final CustomerService service = new CustomerService();

    // Fields are class-level so the handlers can read them.
    private TextField nameField;
    private ComboBox<String> provinceBox;
    private Label status;
    private TableView<Customer> table;

    @Override
    public void start(Stage stage) {

        // ---------- Navigation ----------
        Menu fileMenu = new Menu("File");
        MenuItem closeItem = new MenuItem("Close");
        closeItem.setOnAction(e -> stage.close());
        fileMenu.getItems().add(closeItem);
        MenuBar menuBar = new MenuBar(fileMenu);

        // ---------- Name input ----------
        // WHY a visible label? "the prompt disappears once the user types."
        Label nameLabel = new Label("Customer.java name");
        nameField = new TextField();
        nameField.setPromptText("e.g., Mary Banda");
        nameLabel.setLabelFor(nameField);          //  accessibility

        // ---------- Province input  ----------
        // WHY ComboBox? "Known choices" - it forces a consistent value.
        // This is a short demo list, not all of Zambia's provinces.
        Label provinceLabel = new Label("Province");
        provinceBox = new ComboBox<>();
        provinceBox.getItems().addAll("Central", "Lusaka", "Copperbelt", "Western", "Northern", "Luapula", "NorthWestern" );
        provinceBox.setPromptText("Choose a province");
        provinceLabel.setLabelFor(provinceBox);

        // ---------- Buttons ----------
        Button saveButton = new Button("Save customer");
        saveButton.setDefaultButton(true);   // Enter activates Save
        saveButton.setOnAction(event -> handleSave());

        Button deleteButton = new Button("Delete selected");
        deleteButton.setOnAction(event -> handleDelete());

        HBox buttons = new HBox(10, saveButton, deleteButton);

        // ---------- Feedback label  ----------
        // "Routine success" and input problems go to a Label.
        status = new Label();

        // ---------- Table ----------
        table = buildTable();

        // Tab order follows the order controls are added here:
        // name -> province -> Save -> Delete -> table.
        VBox form = new VBox(8,
                nameLabel, nameField,
                provinceLabel, provinceBox,
                buttons, status,
                table);
        form.setPadding(new Insets(12));

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(form);

        stage.setTitle("Customer.java Manager");   //  a clear title
        stage.setScene(new Scene(root, 460, 520));
        stage.show();

        nameField.requestFocus();             //  start in the name field
    }

    /** Builds the TableView and connects it to the service's list. */
    private TableView<Customer> buildTable() {
        // "TableView<Customer.java> means each item is a Customer.java."
        TableView<Customer> t = new TableView<>();

        // setItems() connects the table to the ObservableList, so the
        // table updates itself when the list changes.
        t.setItems(service.getCustomers());

        //  "name" -> the factory calls getName() by reflection.
        TableColumn<Customer, String> nameCol = new TableColumn<>("Customer.java name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(220);

        // Slide 19: each column reads one part of the same Customer.java.
        TableColumn<Customer, String> provinceCol = new TableColumn<>("Province");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
        provinceCol.setPrefWidth(180);

        t.getColumns().add(nameCol);
        t.getColumns().add(provinceCol);
        return t;
    }

    // ================= CONTROLLER LOGIC =================

    /** Validate, then save.  */
    private void handleSave() {
        // trim() removes spaces at the ends , so "   " counts as empty.
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            status.setText("Enter the customer name.");
            nameField.requestFocus();   // send the user to the problem field
            return;                     // stop here; nothing is cleared (slide 11)
        }

        // A ComboBox can be left unselected, so check for null (slide 9).
        String province = provinceBox.getValue();
        if (province == null) {
            status.setText("Choose a province.");
            provinceBox.requestFocus();
            return;                     // the typed name is kept (slide 27)
        }

        service.addCustomer(name, province);

        // Slide 20: "Clear fields only after the save succeeds."
        nameField.clear();
        provinceBox.setValue(null);
        status.setText("Customer.java saved.");
        nameField.requestFocus();
    }

    /** Check selection, confirm, then delete. Slide 23. */
    private void handleDelete() {
        Customer selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            status.setText("Select a customer first.");
            return;
        }

        // Use a clear Delete button and a Cancel option.
        ButtonType delete = new ButtonType("Delete");
        Alert ask = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete the selected customer?",
                delete, ButtonType.CANCEL);
        ask.setHeaderText("Confirm deletion");

        // Closing the dialog gives an empty result, so orElse(CANCEL)
        // makes "closed" behave like "cancelled" and the record stays.
        // We compare with the exact ButtonType instance we passed in.
        if (ask.showAndWait().orElse(ButtonType.CANCEL) == delete) {
            service.removeCustomer(selected);
            status.setText("Customer.java deleted.");
        } else {
            status.setText("Deletion cancelled.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}