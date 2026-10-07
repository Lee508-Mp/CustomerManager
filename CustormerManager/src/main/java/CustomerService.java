import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * SERVICE (slide 28): "Application rules: create or delete customer."
 *
 * The controller (CustomerApp) never creates Customer objects or touches the
 * list directly. It asks this class to do it. Later, if you add a database,
 * only this class (plus a DAO) changes. The screen code stays the same.
 *
 * WHY ObservableList (slide 16)?
 * "An ObservableList tells listeners when its contents change."
 * The TableView listens to this list, so add/remove updates the rows
 * automatically. A plain ArrayList would NOT refresh the table.
 *
 * Data is in memory only, so closing the app loses it (slide 4).
 */
public class CustomerService {

    // "Keep this same list when adding and removing customers."
    // Never replace it with a new list, or the table loses its connection.
    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    // The table needs this list for setItems(). It is the one deliberate
    // exception to "hide your collections".
    public ObservableList<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(String name, String province) {
        customers.add(new Customer(name, province));
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }
}