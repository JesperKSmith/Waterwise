package waterwise;

import java.util.HashMap;

//Marcus Melgaard Jensen

public class Outgoing extends Order {

    //Outgoing specific fields
    private String supplierName;
    private String supplierEmail;
    private String ownAddress;
    private String ownCity;
    private String ownZip;
    private String ownCountry;
    private String ownPhonenumber;

    //Creates an Outgoing order object from the given parameters
    //The boolean determines whether to save it in the database immediately
    //Therefore it should be false when called from the database
    public Outgoing() {

        OrderFrame tempOF = new OrderFrame(this);
    }

    //Creates an order object from the given parameters
    //The boolean determines whether to save it in the database immediately
    //Therefore it should be false when called from the database
    public Outgoing(String orderID, String startDate, String closedDate,
            HashMap<Product, Integer> productMap, String paymentType,
            String deliveryType, String orderStatus, String suppName,
            String suppEmail, String ownAddress, String ownCity,
            String ownZip, String ownCountry, String ownPhone,
            boolean updateDB) {

        this.setOrderID(orderID);
        this.setStartDate(startDate);
        this.setClosedDate(closedDate);
        this.setListOfProducts(productMap);
        this.setPaymentType(paymentType);
        this.setDeliveryType(deliveryType);
        this.setOrderStatus(orderStatus);

        this.setSupplierName(suppName);
        this.setSupplierEmail(suppEmail);
        this.setOwnAddress(ownAddress);
        this.setOwnCity(ownCity);
        this.setOwnZip(ownZip);
        this.setOwnCountry(ownCountry);
        this.setOwnPhonenumber(ownPhone);

        if (productMap != null) {
            this.CalculatePriceTotal();
        }
        if (updateDB) {
            this.Update();
        }

    }

    //Override methods inherited from DataBaseElement
    //They call the correct methods in the FileWrappper    @Override
    public void Update() {
        FileWrapper fw = new FileWrapper();
        try {
            fw.createOutgoingOrder(this);
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - " + this.getClass().toString());
        }
    }

    @Override
    public void Delete() {
        FileWrapper fw = new FileWrapper();
        try {
            fw.deleteOrder("outgoingOrder", "ID", this.getOrderID());
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - " + this.getClass().toString());
        }
    }

    //Setter
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setSupplierEmail(String supplierEmail) {
        this.supplierEmail = supplierEmail;
    }

    public void setOwnAddress(String ownAddress) {
        this.ownAddress = ownAddress;
    }

    public void setOwnCity(String ownCity) {
        this.ownCity = ownCity;
    }

    public void setOwnZip(String ownZip) {
        this.ownZip = ownZip;
    }

    public void setOwnCountry(String ownCountry) {
        this.ownCountry = ownCountry;
    }

    public void setOwnPhonenumber(String ownPhonenumber) {
        this.ownPhonenumber = ownPhonenumber;
    }

    //Getter
    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public String getOwnAddress() {
        return ownAddress;
    }

    public String getOwnCity() {
        return ownCity;
    }

    public String getOwnZip() {
        return ownZip;
    }

    public String getOwnCountry() {
        return ownCountry;
    }

    public String getOwnPhonenumber() {
        return ownPhonenumber;
    }

}
