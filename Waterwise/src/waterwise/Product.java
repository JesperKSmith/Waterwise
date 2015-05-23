package waterwise;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Product extends DataBaseElement
{

    //Fields
    private int productID;
    private String productName;
    private int amountInStorage;
    private double weight;
    private String size;
    private double unitPrice;
    private int reorderAmount;
    private String tempPrice;
    private String tempAmount;
    private String tempWeight;
    private String tempReorder;
    private String updateDB;
    private String tempID;

    //Constructor
    //Creates dummy product with only name, meant for testing
    public Product(String nameToBe)
    {
        productName = nameToBe;
    }

    //Creates an entire product, 
    //The boolean should be false if called from the database so as to not save it again
    //The boolean should be true if the object should be put in the database
    Product(int ID, String name, int amount, double weight, String size, double unitPrice, int reOrderAmount, boolean updateDB)
    {
        this.productID = ID;
        this.productName = name;
        this.amountInStorage = amount;
        this.weight = weight;
        this.size = size;
        this.unitPrice = unitPrice;
        this.reorderAmount = reOrderAmount;
        if(updateDB){
            this.Update();
        }
        
    }
    
    Product (String tempID, String name, String amount,String weight, String size, String price,String reOrder ,String updateDB) {
        this.tempID = tempID;
        this.productName = name;        
        this.tempAmount = amount;
        this.tempWeight = weight;
        this.size = size;
        this.tempPrice = price;
        this.tempReorder = reOrder;
        this.updateDB = updateDB;  
    }
    
    @Override
    public void Update(){
       FileWrapper fw = new FileWrapper();
        try {
            fw.createProduct(this);
        } catch (Exception ex) {
            System.out.println( ex + " thrown from - " + this.getClass().toString());
        }
    }
    
    @Override
    public void Delete() {
        FileWrapper fw = new FileWrapper();
        try {
            fw.deleteProduct(productID);
        } catch (Exception ex) {
            System.out.println( ex + " thrown from - " + this.getClass().toString());
        }
    }

    //Getters    
    public int getProductID()
    {
        return this.productID;
    }

    public String getProductName()
    {
        return this.productName;
    }

    public int getAmountInStorage()
    {
        return this.amountInStorage;
    }

    public double getWeight()
    {
        return this.weight;
    }

    public String getSize()
    {
        return this.size;
    }

    public double getUnitPrice()
    {
        return this.unitPrice;
    }

    public int getReorderAmount()
    {
        return this.reorderAmount;
    }
    
    public void setProductID(int productID)
    {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAmountInStorage(int amountInStorage) {
        this.amountInStorage = amountInStorage;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setSize(String size) {
        this.size = size;
    }    
    
    public String getTempPrice() {
        return tempPrice;
    }

    public String getTempAmount() {
        return tempAmount;
    }

    public String getTempWeight() {
        return tempWeight;
    }

    public String getTempReorder() {
        return tempReorder;
    }

    public String getUpdateDB() {
        return updateDB;
    }

    public String getTempID() {
        return tempID;
    }
    //Setters
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setReorderAmount(int reorderAmount) {
        this.reorderAmount = reorderAmount;
    }

}
