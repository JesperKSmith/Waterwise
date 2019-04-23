package waterwise;


import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Author Jesper Smith

    // The ErrorChecker Class contains various methods, used to check different formats, or convert from one format to another
public class ErrorChecker {

    // Methods for checking validity of an input
    
    public boolean isAmountValid(String amount) {       // Returns true if the amount input is valid
        if (isInt(amount) == true) {
            return true;
        }
        return false;
    }
        public boolean isInt(String str) {              // Method to check if a string input can be parsed to an Integer
            try {
                Integer.parseInt(str);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    
    public boolean isDouble(String str) {               // Method to check if a string input can be parsed to a double
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    

    

    public boolean isProductIDValid(String amount) {        // Checks if product is Valid
        if (isInt(amount) == true) {
            return true;
        }
        return false;
    }



    public boolean isPriceValid(String price) {             // Checks if a price is valid, by using the isDouble method above
        if (isDouble(price) == true) {
            return true;
        }
        return false;
    }

    public boolean isWeightValid(String weight) {           // checks if a string input weight is valid, using the isDouble method
        if (isDouble(weight) == true) {
            return true;
        }
        return false;
    }

    public boolean isNameValid(String name) {               // Following 7 methods returns true, as long as the string input is not empty or ""
        if (name != "" && !name.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isDeliveryValid(String name) {
        if (name != "" && !name.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isOrderIDValid(String name) {
        if (name != "" && !name.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public boolean isProductReorderValid(String id) {       
        if (id != "" && !id.isEmpty()) {
            return true;
        }
        return false;
    }
    
        public boolean isPaymentValid(String name) {
        if (name != "" && !name.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isSuppNameValid(String name) {
        if (name != "" && !name.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean isCityValid(String name) {
        if (name != "" && !name.isEmpty()) {
            return true;
        }
        return false;
    }
    public String noProducts(boolean exists) {          // method that takes a boolean input, and returns a string depending on true/false
        if (exists) {
            return "ingen produkter valgt";
        } else {
            return "der er valgt produkter";
        }

    }

    

    public boolean doesListExist(HashMap listOfProducts) {      // method that takes a HashMap input, and checks if the map == null
        if (listOfProducts == null) {
            return false;
        }
        return true;
    }

    public boolean isDateValid(String date) {                   // method that checks an input string for a date pattern, and returns true if it is found
        if (date != "" && !date.isEmpty()) {
            String zipPattern = "\\d\\d\\d\\d-\\d\\d-\\d\\d";
            Pattern zip = Pattern.compile(zipPattern);
            Matcher m = zip.matcher(date);
            if (m.find()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean isAddressValid(String address) {              // method that checks an input string for a zip pattern, and returns true if it is found
        if (address != "" && !address.isEmpty()) {
            String zipPattern = "\\d\\d\\d\\d";
            Pattern zip = Pattern.compile(zipPattern);
            Matcher m = zip.matcher(address);
            if (m.find()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean isStreetnameValid(String streetname) {        // method that checks an input string for a streetname pattern, and returns true if it is found
        if (streetname != "" && !streetname.isEmpty()) {
            String zipPattern = "(.)*(\\d)";
            Pattern zip = Pattern.compile(zipPattern);
            Matcher m = zip.matcher(streetname);
            if (m.find()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean isSizeValid(String size) {                   // method that checks an input string for being a matching size
        if (size.equals("Stor") || size.equals("Lille") || size.equals("Standard")) {
            return true;
        }
        return false;
    }

    // Email format checker
    public boolean isEmailValid(String email) {                 // method that checks a string input for an email pattern, containing a domain and @

        if (email.contains(".de")
                || email.contains(".se")
                || email.contains(".com")
                || email.contains(".dk")
                && email.contains("@")) {
            return true;
        }
        return false;
    }   
    
    public boolean isPhonenumberValid(String phoneNumber) {     // Checks an input string for having the length 8 and being an int
        if (phoneNumber.length() == 8 && isInt(phoneNumber)){
            return true;
        }
        return false;
    }
    
    
    //converters 

    public String intToString(int number) {         // method converting input int to a string
        String convNumber;
        convNumber = "" + number;
        return convNumber;
    }

    public String doubleToString(double number) {   // method converting a double to a string
        String convNumber;
        convNumber = "" + number;
        return convNumber;
    }

    public int StringToInt(String number) {         // method converting a string to an int
        int convNumber;
        convNumber = Integer.parseInt(number);
        return convNumber;
    }

    public double StringToDouble(String number) {   // method converting a string to a double
        double convNumber;
        convNumber = Double.parseDouble(number);
        return convNumber;
    }

    public boolean incomingCheck(OrderFrame ofcheck) {              // method that takes an OrderFrame input, and checks its inputs for correct format - If everything is format, the order is created.

        Error er;
        boolean bool = false;

        if (isOrderIDValid(ofcheck.orderIDField.getText())) {             // Various method calls in this class, for checking the input OrderFrame                                                  
            if (!ofcheck.chosenProducts.isEmpty()) {                                                    
                if (isDeliveryValid(ofcheck.deliveryTypeField.getText())) {
                    if (isPaymentValid(ofcheck.paymentTypeField.getText())) {
                        if (isNameValid(ofcheck.customerNameField.getText())) {
                            if (isEmailValid(ofcheck.customerEmailField.getText())) {
                                if (isAddressValid(ofcheck.deliveryAddressZipField.getText())) {
                                    if (isStreetnameValid(ofcheck.deliveryAddressField.getText())) {
                                        if (isPhonenumberValid(ofcheck.customerPhonenumberField.getText())) {

                                            System.out.println("Alle felter godkendt - der kan skrives til DB");

                                            bool = true;                                                                    // If every input passes the check, bool is set to true.
                                            ofcheck.dispose();
                                        } else { 
                                            er = new Error(ofcheck.customerPhonenumberField.getText(), "Telefon");          // Depending on what check went wrong, the corresponding error is reported
                                        }
                                    } else {
                                        er = new Error(ofcheck.deliveryAddressField.getText(), "Adresse");
                                    }
                                } else {
                                    er = new Error(ofcheck.deliveryAddressZipField.getText(), "Postnummer");
                                }
                            } else {
                                er = new Error(ofcheck.customerEmailField.getText(), "Email");
                            }
                        } else {
                            er = new Error(ofcheck.customerNameField.getText(), "Navn");
                        }
                    } else {
                        er = new Error(ofcheck.paymentTypeField.getText(), "Betalingstype");
                    }
                } else {
                    er = new Error(ofcheck.deliveryTypeField.getText(), "Levering");
                }
            } else {
                er = new Error("Intet produkt valgt", "Produkter");
            }
        } else {
            er = new Error(ofcheck.orderIDField.getText(), "Ordre ID");
        }
        return bool;                                                                                        // if bool returns true, the order is complete and sent to database
    }

    public boolean outgoingCheck(OrderFrame ofcheck) {                      // method for checking outgoing orders

        Outgoing oc = (Outgoing) ofcheck.orderShown;
        Error er;
        boolean bool = false;

        String orderID = ofcheck.orderIDField.getText();
        HashMap<Product, Integer> productlist = ofcheck.listOfProducts;
        String paymentType = ofcheck.paymentTypeField.getText();
        String deliveryType = ofcheck.deliveryTypeField.getText();
        String suppname = ofcheck.supplierNameField.getText();
        String suppemail = ofcheck.supplierEmailField.getText();

        if (isOrderIDValid(orderID)) {
            if (!productlist.isEmpty()) {
                if (isPaymentValid(paymentType)) {
                    if (isDeliveryValid(deliveryType)) {
                        if (isSuppNameValid(suppname)) {
                            if (isEmailValid(suppemail)) {
                                System.out.println("Alle felter godkendt - der kan skrives til DB");
                                bool = true;
                                ofcheck.dispose();
                            } else {
                                er = new Error(ofcheck.supplierEmailField.getText(), "Email");
                            }
                        } else {
                            er = new Error(ofcheck.supplierNameField.getText(), "Navn");
                        }
                    } else {
                        er = new Error(ofcheck.deliveryTypeField.getText(), "Lev.Type");
                    }
                } else {
                    er = new Error(ofcheck.paymentTypeField.getText(), "Betalingstype");
                }
            } else {
                er = new Error("Intet produkt valgt", "Produkter");
            }
        } else {
            er = new Error(ofcheck.orderIDField.getText(), "Ordre ID");
        }

        return bool;
    }
}
