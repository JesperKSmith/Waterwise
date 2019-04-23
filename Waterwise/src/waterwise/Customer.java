package waterwise;

//Author Marcus Melgaard Jensen

public class Customer extends DataBaseElement {

    //Fields
    private int phoneNumber;
    private String customerEmail;
    private String customerName;
    private String deliveryAddress;
    private String deliveryCityAddress;
    private String deliveryZipAddress;
    private String deliveryCountryAddress;
    private String creationDate;

    //Override methods inherited from DataBaseElement
    //They call the correct methods in the FileWrappper
    @Override
    public void Update() {
        FileWrapper fw = new FileWrapper();
        try {
            fw.createCustomer(this);
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - " + this.getClass().toString());
        }
    }

    @Override
    public void Delete() {
        FileWrapper fw = new FileWrapper();
        try {
            fw.deleteCustomer(phoneNumber);
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - " + this.getClass().toString());
        }
    }

    //Constructor
    //To create an entirely new customer, and update to the database
    public Customer(int phone, String email, String name, String deliveryaddress,
            String city, String zip, String country) {

        this.phoneNumber = phone;
        this.customerEmail = email;
        this.customerName = name;
        this.deliveryAddress = deliveryaddress;
        this.deliveryCityAddress = city;
        this.deliveryZipAddress = zip;
        this.deliveryCountryAddress = country;

        this.Update();
    }

    //To create customer objects from database
    //This doesnt update to the database
    public Customer(int phone, String email, String name, String deliveryaddress,
            String city, String zip, String country, String creation) {

        this.phoneNumber = phone;
        this.customerEmail = email;
        this.customerName = name;
        this.deliveryAddress = deliveryaddress;
        this.deliveryCityAddress = city;
        this.deliveryZipAddress = zip;
        this.deliveryCountryAddress = country;
        creationDate = creation;

    }

    //Getters
    public String getCustomerEmail() {
        return this.customerEmail;
    }

    public String getDeliveryAddress() {
        return this.deliveryAddress;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getCreationDate() {
        return this.creationDate;
    }

    public String getDeliveryCityAddress() {
        return deliveryCityAddress;
    }

    public String getDeliveryZipAddress() {
        return deliveryZipAddress;
    }

    public String getDeliveryCountryAddress() {
        return deliveryCountryAddress;
    }

    //Setters
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
