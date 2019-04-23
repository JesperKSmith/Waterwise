package waterwise;

import java.util.HashMap;


//Author Marcus Melgaard Jensen
public class Incoming extends Order {

    //Incoming specific fields
    private int customerPhonenumber;

    //Override methods inherited from DataBaseElement
    //They call the correct methods in the FileWrappper
    @Override
    public void Update() {
        FileWrapper fw = new FileWrapper();
        try {
            fw.createIncomingOrder(this);
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - " + this.getClass().toString());
        }
    }

    @Override
    public void Delete() {
        FileWrapper fw = new FileWrapper();
        try {
            fw.deleteOrder("incomingOrder", "ID", this.getOrderID());
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - " + this.getClass().toString());
        }
    }

    //Creates an incoming object
    //When confirm is pressed in OrderFrame the variables will be set
    //And saved to the database
    public Incoming() {

        OrderFrame tempOF = new OrderFrame(this);

    }

    //Creates an Incoming order object from the given parameters
    //The boolean determines whether to save it in the database immediately
    //Therefore it should be false when called from the database
    public Incoming(String orderID, String startDate, String closedDate,
            HashMap<Product, Integer> productMap, String paymentType,
            String deliveryType, String orderStatus, int phonenumber,
            boolean updateDB) {

        this.setOrderID(orderID);
        this.setStartDate(startDate);
        this.setClosedDate(closedDate);
        this.setListOfProducts(productMap);
        this.setPaymentType(paymentType);
        this.setDeliveryType(deliveryType);
        this.setOrderStatus(orderStatus);
        this.setCustomerPhonenumber(phonenumber);

        if (productMap != null) {
            this.CalculatePriceTotal();
        }

        if (updateDB) {
            this.Update();
        }

    }

    //Setter
    public void setCustomerPhonenumber(int customerPhonenumber) {
        this.customerPhonenumber = customerPhonenumber;
    }

    //Getter
    public int getCustomerPhonenumber() {
        return customerPhonenumber;
    }
}
