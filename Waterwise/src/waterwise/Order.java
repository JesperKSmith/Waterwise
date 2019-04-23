package waterwise;

import java.util.HashMap;

//Author Marcus Melgaard Jensen

public abstract class Order extends DataBaseElement {

    //Fields
    private String orderID;
    private String startDate;
    private String closedDate;
    private HashMap<Product, Integer> listOfProducts = new HashMap<>();
    private double priceTotal;
    private String paymentType;
    private String deliveryType;
    private String orderStatus;
    private int customerID;

    //Methods
    //This method will calculate the total for order
    public void CalculatePriceTotal() {

        double sumPrice = 0.0;
        //Loops over all the products in the order
        for (Product key : listOfProducts.keySet()) {

            //Sets the value to how many of current product there is
            Integer value = listOfProducts.get(key);

            //Add products unit price multiplied by amount to the total sum
            sumPrice = sumPrice + (key.getUnitPrice() * value);

        }

        priceTotal = sumPrice;
    }

    //Setters
    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public void setListOfProducts(HashMap<Product, Integer> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    //Getters
    public String getOrderID() {
        return this.orderID;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getClosedDate() {
        return this.closedDate;
    }

    public double getPriceTotal() {
        return this.priceTotal;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public String getDeliveryType() {
        return this.deliveryType;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public HashMap<Product, Integer> getListOfProducts() {
        return this.listOfProducts;
    }

}
