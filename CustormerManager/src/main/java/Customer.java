/**
 * MODEL : "The class groups the customer's details together."
 *
 * WHY final + private?
 * - private: other classes can't change the fields directly (encapsulation).
 * - final: once a Customer is created it can never change. The slide says
 *   this model is "intentionally immutable". That is also why the table never
 *   needs to watch individual fields: to "edit" a customer you would
 *   remove it and add a new one.
 *
 * WHY public getters?
 * - PropertyValueFactory (slides 18-19) finds getName() / getProvince()
 *   by reflection to fill the table columns. No getter = empty column.
 *
 * Tip: type the fields, then use Alt+Insert > Constructor and
 * Alt+Insert > Getter to see how IntelliJ generates them.
 */
public class Customer {

    private final String name;
    private final String province;

    public Customer(String name, String province) {
        this.name = name;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }
}