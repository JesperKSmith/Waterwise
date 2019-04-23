package waterwise;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


//Author Markus Sørensen

public class FileWrapper
{

    
    //Global attributes initiated
    //SQL Attributes
    private Connection connect = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    //Format for DB->client conversion of date
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    //Controller objetct for conversion of collected strings
    Controller c = new Controller();
    
    //Private methods
    //Method for defining connnection to database
    private void createConnection() throws SQLException, ClassNotFoundException
    {
        //JBDC driver is defined
        Class.forName("com.mysql.jdbc.Driver");
        //driver is used to getting and setting connection using location(local), user and password
        connect = DriverManager.getConnection("jdbc:mysql://localhost/waterwise", "sqluser", "sqluserpw");
    }
    //Method for closing all used DB attributes
    private void closeConnection()
    {
        try
        {
            if (resultSet != null)
            {
                resultSet.close();
            }
            if (connect != null)
            {
                connect.close();
            }
            if (preparedStatement != null)
            {
                preparedStatement.close();
            }
        }
        catch (Exception e)
        {

        }
    }
    //Method for checking if a String-value is allready used in a table. Returns a boolean
    private boolean checkIfDuplicateString(String table, String column, String value) throws Exception
    {
        boolean duplicate = false;
        try
        {
            //Connection made
            createConnection();
            int numOfMatch = 0;
            //Statement is prepared (Select Count)
            preparedStatement = connect.prepareStatement("SELECT COUNT(" + column + ") as keyCount FROM waterWise." + table + " WHERE " + column + " like ?;");
            //Prepared statement is filled
            preparedStatement.setString(1, value);
            //PreparedStatement is executed and resultSet is set
            resultSet = preparedStatement.executeQuery();
            //boolean is set
            if (resultSet.next())
            {
                numOfMatch = resultSet.getInt(1);
            }
            if (numOfMatch >= 1)
            {
                duplicate = true;
            }
            else if (numOfMatch == 0)
            {
                duplicate = false;
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            //Connection is closed
            closeConnection();
        }
        //boolean is returned
        return duplicate;
    }
    //Method for checking if an int-value is allready used in a table. Returns a boolean
    private boolean checkIfDuplicateInt(String table, String column, int value) throws Exception
    {
        boolean duplicate = false;
        try
        {
            //Connection is made
            createConnection();
            int numOfMatch = 0;
            //Statement is prepared (Select Count)
            preparedStatement = connect.prepareStatement("SELECT COUNT(" + column + ") as keyCount FROM waterWise." + table + " WHERE " + column + " = ?;");
            //Preparedstatement is filled
            preparedStatement.setInt(1, value);
            //PreparedStatement is executed, and resultSet is set
            resultSet = preparedStatement.executeQuery();
            //boolean is set
            if (resultSet.next())
            {
                numOfMatch = resultSet.getInt(1);
            }
            if (numOfMatch >= 1)
            {
                duplicate = true;
            }
            else if (numOfMatch == 0)
            {
                duplicate = false;
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            //Connection is closed
            closeConnection();
        }
        //boolean is returned
        return duplicate;
    }
    //Method for checking if an order(outgoing or incoming) is done. Returns boolean
    private boolean checkIfDone(String table, String ID) throws Exception
    {
        boolean done = false;
        try
        {
            //connection made
            createConnection();
            int numOfMatch = 0;
            //Statement prepared (Select Count)
            preparedStatement = connect.prepareStatement("SELECT COUNT(status) as keyCount FROM waterWise." + table + " WHERE ID like ? and status like ?;");
            //PreparedStatement filled
            preparedStatement.setString(1, ID);
            preparedStatement.setString(2, "Afsluttet");
            //PreparedStatement is executed and resultSet set
            resultSet = preparedStatement.executeQuery();
            //boolean is set
            if (resultSet.next())
            {
                numOfMatch = resultSet.getInt(1);
            }
            if (numOfMatch == 1)
            {
                done = true;
            }
            else if (numOfMatch == 0)
            {
                done = false;
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            //connection is closed
            closeConnection();
        }
        //boolean is returned
        return done;
    }

    // Incoming Order
    //Method for creating new order
    public void createIncomingOrder(Incoming order) throws Exception
    {
        String ID = order.getOrderID();
        //Check if PK "ID" is allready in use
        //if PK not in use
        if (checkIfDuplicateString("incomingOrder", "ID", order.getOrderID()) == false)
        {
            try
            {
                createConnection();
                //Statement is prepared (INSERT INTO)
                preparedStatement = connect.prepareStatement("insert into  waterWise.incomingOrder values (? , ?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, ID);
                preparedStatement.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                //only set closed date if order is done
                if (order.getOrderStatus().equals("Uafsluttet"))
                {
                    preparedStatement.setDate(3, null);
                }
                else
                {
                    preparedStatement.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
                }
                preparedStatement.setDouble(4, order.getPriceTotal());
                preparedStatement.setString(5, order.getPaymentType());
                preparedStatement.setString(6, order.getDeliveryType());
                preparedStatement.setString(7, order.getOrderStatus());
                preparedStatement.setInt(8, order.getCustomerPhonenumber());
                preparedStatement.executeUpdate();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            finally
            {
                closeConnection();
            }
            HashMap<Product, Integer> map = new HashMap<>(order.getListOfProducts());
            for (Product key : map.keySet())
            {
                int value = map.get(key);
                createIncomingOrderLine(ID, key.getProductID(), value);
            }
        }
        //If PK is in use
        else
        {
            //update existing entry
            updateIncomingOrder(order);
        }
    }
    //Method for updating existing order
    public void updateIncomingOrder(Incoming order) throws SQLException
    {
        String ID = order.getOrderID();
        String status = order.getOrderStatus();
        //Delete orderlines belonging to order
        deleteOrderLines("incomingOrderLine", ID);
        try
        {
            boolean done = checkIfDone("incomingOrder", ID);
            createConnection();
            //only set date, if status has changed
            if ((done == true && status.equals("Afsluttet")) || (done == false && status.equals("Uafsluttet")))
            {
                //Statement is prepared (UPDATE)
                preparedStatement = connect.prepareStatement("UPDATE  waterWise.incomingOrder SET priceTotal = ?, paymentType = ?, deliveryType = ?, status = ? WHERE ID = ?");
                preparedStatement.setDouble(1, order.getPriceTotal());
                preparedStatement.setString(2, order.getPaymentType());
                preparedStatement.setString(3, order.getDeliveryType());
                preparedStatement.setString(4, order.getOrderStatus());
                preparedStatement.setString(5, ID);
                preparedStatement.executeUpdate();
            }
            else if (status.equals("Afsluttet"))
            {
                //Statement is prepared (UPDATE)
                preparedStatement = connect.prepareStatement("UPDATE  waterWise.incomingOrder SET closedDate = ?, priceTotal = ?, paymentType = ?, deliveryType = ?, status = ? WHERE ID = ?");
                preparedStatement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
                preparedStatement.setDouble(2, order.getPriceTotal());
                preparedStatement.setString(3, order.getPaymentType());
                preparedStatement.setString(4, order.getDeliveryType());
                preparedStatement.setString(5, order.getOrderStatus());
                preparedStatement.setString(6, ID);
                preparedStatement.executeUpdate();
            }
            else if (status.equals("Uafsluttet"))
            {
                //Statement is prepared (UPDATE)
                preparedStatement = connect.prepareStatement("UPDATE  waterWise.incomingOrder SET closedDate = ?, priceTotal = ?, paymentType = ?, deliveryType = ?, status = ? WHERE ID = ?");
                preparedStatement.setDate(1, null);
                preparedStatement.setDouble(2, order.getPriceTotal());
                preparedStatement.setString(3, order.getPaymentType());
                preparedStatement.setString(4, order.getDeliveryType());
                preparedStatement.setString(5, order.getOrderStatus());
                preparedStatement.setString(6, ID);
                preparedStatement.executeUpdate();
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
        //HashMap is looped through
        HashMap<Product, Integer> map = new HashMap<>(order.getListOfProducts());
        for (Product key : map.keySet())
        {
            int value = map.get(key);
            //Entry in incomingOrderLine is made
            createIncomingOrderLine(ID, key.getProductID(), value);
        }
    }
    //Method for retrieving specific order
    public Incoming loadIncomingOrder(String ID) throws Exception
    {
        Incoming order;
        //Belonging orderLines are loaded
        HashMap map = loadOrderLines("incomingOrderLine", ID);
        try
        {
            createConnection();
            //Statement is prepared (SELECT * FROM ... WHERE ...)
            preparedStatement = connect.prepareStatement("SELECT * from waterWise.incomingOrder WHERE ID like ?");
            preparedStatement.setString(1, ID);
            resultSet = preparedStatement.executeQuery();
            String startDate = null;
            String closedDate = null;
            String paymentType = null;
            String deliveryType = null;
            String orderStatus = null;
            int customerPhone = 0;
            if (resultSet.next())
            {
                startDate = df.format(resultSet.getDate("startDate"));
                //only apply format, if date exists
                if (resultSet.getDate("closedDate") == null)
                {
                    closedDate = "";
                }
                else
                {
                    closedDate = df.format(resultSet.getDate("closedDate"));
                }
                paymentType = resultSet.getString("paymentType");
                deliveryType = resultSet.getString("deliveryType");
                orderStatus = resultSet.getString("status");
                customerPhone = resultSet.getInt("customerPhone");
            }
            //Order is set
            order = new Incoming(ID, startDate, closedDate, map, paymentType, deliveryType, orderStatus, customerPhone, false);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //order is returned
        return order;
    }
    //Method for retrieving complete list of incomingOrders
    public ArrayList<Order> loadIncomingOrderList() throws Exception
    {
        //ArrayList is made
        ArrayList<Order> list = new ArrayList<Order>();
        try
        {
            createConnection();
            //Statement is prepared (SELECT *)
            preparedStatement = connect.prepareStatement("SELECT * from waterWise.incomingOrder");
            resultSet = preparedStatement.executeQuery();
            //repeat as long as there are more orders
            while (resultSet.next())
            {
                //Information is retrieved from resultSet
                String orderID = resultSet.getString("ID");
                String startDate = df.format(resultSet.getDate("startDate"));
                String closedDate = null;
                //only apply format, if date exists
                if (resultSet.getDate("closedDate") == null)
                {
                    closedDate = "";
                }
                else
                {
                    closedDate = df.format(resultSet.getDate("closedDate"));
                }
                String paymentType = resultSet.getString("paymentType");
                String deliveryType = resultSet.getString("deliveryType");
                String orderStatus = resultSet.getString("status");
                int customerPhone = resultSet.getInt("customerPhone");
                //Order is set and added to ArrayList, with no map
                Order order = new Incoming(orderID, startDate, closedDate, null, paymentType, deliveryType, orderStatus, customerPhone, false);
                list.add(order);
            }
            //for every order in the list, load belonging orderlines and add them to the order
            for (Order order : list)
            {
                HashMap<Product, Integer> map = loadOrderLines("incomingOrderLine", order.getOrderID());
                order.setListOfProducts(map);
                //calculate and set total price
                order.CalculatePriceTotal();
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //ArrayList is returned
        return list;
    }
    //Method for creating incomingOrderLine
    private void createIncomingOrderLine(String orderID, int productID, int amount) throws SQLException
    {
        try
        {
            createConnection();
            //Statement is prepared (INSERT INTO)
            preparedStatement = connect.prepareStatement("insert into  waterWise.incomingOrderLine values (? , ?, ?)");
            preparedStatement.setString(1, orderID);
            preparedStatement.setInt(2, productID);
            preparedStatement.setInt(3, amount);
            preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }

    // Outgoing Order
    //Method for creating outgoingOrder
    public void createOutgoingOrder(Outgoing order) throws Exception
    {
        String ID = order.getOrderID();
        //Check if PK "ID" is allready in use
        //if PK not in use
        if (checkIfDuplicateString("outgoingOrder", "ID", ID) == false)
        {
            try
            {
                createConnection();
                //Statement isprepared (INSERT INTO)
                preparedStatement = connect.prepareStatement("insert into  waterWise.outgoingOrder values (? , ?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, ID);
                preparedStatement.setDate(2, new java.sql.Date(new java.util.Date().getTime()));
                //only give date, if order is done
                if (order.getOrderStatus().equals("Uafsluttet"))
                {
                    preparedStatement.setDate(3, null);
                }
                else
                {
                    preparedStatement.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
                }
                preparedStatement.setDouble(4, order.getPriceTotal());
                preparedStatement.setString(5, order.getPaymentType());
                preparedStatement.setString(6, order.getDeliveryType());
                preparedStatement.setString(7, order.getOrderStatus());
                preparedStatement.setString(8, c.packageSupplier(order.getSupplierName(), order.getSupplierEmail(), order.getOwnAddress(), order.getOwnCity(), order.getOwnZip(), order.getOwnCountry(), order.getOwnPhonenumber()));
                preparedStatement.executeUpdate();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            finally
            {
                closeConnection();
            }
            HashMap<Product, Integer> map = new HashMap<>(order.getListOfProducts());
            //for every product on the productList, make an outgoingOrderLine entry
            for (Product key : map.keySet())
            {
                int value = map.get(key);
                createOutgoingOrderLine(ID, key.getProductID(), value);
            }
        }
        //if PK in use
        else
        {
            //update existing order
            updateOutgoingOrder(order);
        }

    }
    //Method for updating outgoing order
    public void updateOutgoingOrder(Outgoing order)
            throws SQLException
    {
        String ID = order.getOrderID();
        String status = order.getOrderStatus();
        //belonging orderlines are deleted
        deleteOrderLines("outgoingOrderLine", ID);
        try
        {
            boolean done = checkIfDone("outgoingOrder", ID);
            createConnection();
            //only set the date, if status has changed
            if ((done == true && status.equals("Afsluttet")) || (done == false && status.equals("Uafsluttet")))
            {
                preparedStatement = connect.prepareStatement("UPDATE  waterWise.outgoingOrder SET priceTotal = ?, paymentType = ?, deliveryType = ?, status = ?, supplier = ? WHERE ID = ?");
                preparedStatement.setDouble(1, order.getPriceTotal());
                preparedStatement.setString(2, order.getPaymentType());
                preparedStatement.setString(3, order.getDeliveryType());
                preparedStatement.setString(4, order.getOrderStatus());
                preparedStatement.setString(5, c.packageSupplier(order.getSupplierName(), order.getSupplierEmail(), order.getOwnAddress(), order.getOwnCity(), order.getOwnZip(), order.getOwnCountry(), order.getOwnPhonenumber()));
                preparedStatement.setString(6, ID);
                preparedStatement.executeUpdate();
            }
            else if (status.equals("Afsluttet"))
            {
                preparedStatement = connect.prepareStatement("UPDATE  waterWise.outgoingOrder SET closedDate = ?, priceTotal = ?, paymentType = ?, deliveryType = ?, status = ?, supplier = ? WHERE ID = ?");
                preparedStatement.setDate(1, new java.sql.Date(new java.util.Date().getTime()));
                preparedStatement.setDouble(2, order.getPriceTotal());
                preparedStatement.setString(3, order.getPaymentType());
                preparedStatement.setString(4, order.getDeliveryType());
                preparedStatement.setString(5, order.getOrderStatus());
                preparedStatement.setString(6, c.packageSupplier(order.getSupplierName(), order.getSupplierEmail(), order.getOwnAddress(), order.getOwnCity(), order.getOwnZip(), order.getOwnCountry(), order.getOwnPhonenumber()));
                preparedStatement.setString(7, ID);
                preparedStatement.executeUpdate();
            }
            else if (status.equals("Uafsluttet"))
            {
                preparedStatement = connect.prepareStatement("UPDATE  waterWise.outgoingOrder SET closedDate = ?, priceTotal = ?, paymentType = ?, deliveryType = ?, status = ?, supplier = ? WHERE ID = ?");
                preparedStatement.setDate(1, null);
                preparedStatement.setDouble(2, order.getPriceTotal());
                preparedStatement.setString(3, order.getPaymentType());
                preparedStatement.setString(4, order.getDeliveryType());
                preparedStatement.setString(5, order.getOrderStatus());
                preparedStatement.setString(6, c.packageSupplier(order.getSupplierName(), order.getSupplierEmail(), order.getOwnAddress(), order.getOwnCity(), order.getOwnZip(), order.getOwnCountry(), order.getOwnPhonenumber()));
                preparedStatement.setString(7, ID);
                preparedStatement.executeUpdate();
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
        HashMap<Product, Integer> map = new HashMap<>(order.getListOfProducts());
        //for every product in productList
        for (Product key : map.keySet())
        {
            int value = map.get(key);
            //create new outgoingOrderLine entry
            createOutgoingOrderLine(ID, key.getProductID(), value);
        }
    }
    //Method for retrieving specific outgoingOrder
    public Outgoing loadOutgoingOrder(String ID) throws Exception
    {
        Outgoing order;
        //belonging productList is loaded and set
        HashMap map = loadOrderLines("outgoingOrderLine", ID);
        try
        {
            createConnection();
            //statement is prepared
            preparedStatement = connect.prepareStatement("SELECT * from waterWise.outgoingOrder WHERE ID like ?");
            preparedStatement.setString(1, ID);
            resultSet = preparedStatement.executeQuery();
            String startDate = null;
            String closedDate = null;
            String paymentType = null;
            String deliveryType = null;
            String orderStatus = null;
            String suppName = null;
            String suppEmail = null;
            String ownAddress = null;
            String ownCity = null;
            String ownZip = null;
            String ownCountry = null;
            String ownPhone = null;
            if (resultSet.next())
            {
                //information is retrieved
                startDate = df.format(resultSet.getDate("startDate"));
                if (resultSet.getDate("closedDate") == null)
                {
                    closedDate = "";
                }
                else
                {
                    closedDate = df.format(resultSet.getDate("closedDate"));
                }
                paymentType = resultSet.getString("paymentType");
                deliveryType = resultSet.getString("deliveryType");
                orderStatus = resultSet.getString("status");
                //supplier string is retrived
                String completeSupplier = resultSet.getString("supplier");
                //supplier is unpacked and set as array of strings
                String[] supplier = c.unpackage(completeSupplier);
                suppName = supplier[0];
                suppEmail = supplier[1];
                ownAddress = supplier[2];
                ownCity = supplier[3];
                ownZip = supplier[4];
                ownCountry = supplier[5];
                ownPhone = supplier[6];
            }
            //order is set
            order = new Outgoing(ID, startDate, closedDate, map, paymentType, deliveryType, orderStatus, suppName, suppEmail, ownAddress, ownCity, ownZip, ownCountry, ownPhone, false);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //order is returned
        return order;
    }
    //Method for returning a list of all outgoungOrders
    public ArrayList<Order> loadOutgoingOrderList() throws Exception
    {
        ArrayList<Order> list = new ArrayList<>();
        try
        {
            createConnection();
            //statement is prepared (SELECT *)
            preparedStatement = connect.prepareStatement("SELECT * from waterWise.outgoingOrder");
            resultSet = preparedStatement.executeQuery();
            //as long as there are more orders in the resultSet
            while (resultSet.next())
            {
                String orderID = resultSet.getString("ID");
                String startDate = df.format(resultSet.getDate("startDate"));
                String closedDate = null;
                //only apply format, if date is not null
                if (resultSet.getDate("closedDate") == null)
                {
                    closedDate = "";
                }
                else
                {
                    closedDate = df.format(resultSet.getDate("closedDate"));
                }
                String paymentType = resultSet.getString("paymentType");
                String deliveryType = resultSet.getString("deliveryType");
                String orderStatus = resultSet.getString("status");
                String completeSupplier = resultSet.getString("supplier");
                String[] supplier = c.unpackage(completeSupplier);
                String suppName = supplier[0];
                String suppEmail = supplier[1];
                String ownAddress = supplier[2];
                String ownCity = supplier[3];
                String ownZip = supplier[4];
                String ownCountry = supplier[5];
                String ownPhone = supplier[6];
                //order is set and added to list
                Order order = new Outgoing(orderID, startDate, closedDate, null, paymentType, deliveryType, orderStatus, suppName, suppEmail, ownAddress, ownCity, ownZip, ownCountry, ownPhone, false);
                list.add(order);
            }
            //for every order on the list
            for (Order order : list)
            {
                //load and set productList
                HashMap<Product, Integer> map = loadOrderLines("outgoingOrderLine", order.getOrderID());
                order.setListOfProducts(map);
                //calculate and set total price
                order.CalculatePriceTotal();
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //return ArrayList
        return list;
    }
    // Method for ceating outgoingOrderLine
    public void createOutgoingOrderLine(String orderID, int productID, int amount) throws SQLException
    {
        try
        {
            createConnection();
            //statement is prepared (INSERT INTO)
            preparedStatement = connect.prepareStatement("insert into  waterWise.outgoingOrderLine values (? , ?, ?)");
            preparedStatement.setString(1, orderID);
            preparedStatement.setInt(2, productID);
            preparedStatement.setInt(3, amount);
            preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }
    
    // General Order
    //Method for deleting specific orders
    public void deleteOrder(String table, String column, String idValue) throws SQLException
    {
        //if incoming, delete orderlines first (contain orderID FK)
        if (table.equals("incomingOrder"))
        {
            deleteOrder("incomingOrderLine", "orderID", idValue);
        }
        //if outgoing, delete orderlines first (contain orderID FK)
        else if (table.equals("outgoingOrder"))
        {
            deleteOrder("outgoingOrderLine", "orderID", idValue);
        }
        try
        {
            createConnection();
            //prepare statement (DELETE FROM... WHERE...)
            preparedStatement = connect.prepareStatement("delete from waterWise." + table + " WHERE " + column + " like ?;");
            preparedStatement.setString(1, idValue);
            preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }
    //Method for retrieving Hashmap for order(orderLines)
    public HashMap<Product, Integer> loadOrderLines(String table, String orderID) throws Exception
    {
        //productMap is made
        HashMap<Product, Integer> productMap = new HashMap<>();
        try
        {
            //HashMap of productID and amount is made
            HashMap<Integer, Integer> intMap = new HashMap<>();
            createConnection();
            //Statement is prepared (SELECT * FROM ... WHERE ...)
            preparedStatement = connect.prepareStatement("SELECT * from waterWise." + table + " where orderID like ?");
            preparedStatement.setString(1, orderID);
            resultSet = preparedStatement.executeQuery();
            //while there are more orderlines
            while (resultSet.next())
            {
                //add information to intMap
                int productID = resultSet.getInt("productID");
                int amount = resultSet.getInt("amount");
                intMap.put(productID, amount);
            }
            //for all productID's in HashMap
            for (int key : intMap.keySet())
            {
                //use productID's to get product-information
                createConnection();
                //prepar statement (SELECT * FROM ... WHERE...)
                preparedStatement = connect.prepareStatement("SELECT * from waterWise.product where ID = ?");
                preparedStatement.setInt(1, key);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next())
                {
                    int ID = resultSet.getInt("ID");
                    String name = resultSet.getString("name");
                    int amount = resultSet.getInt("amount");
                    double weight = resultSet.getDouble("weight");
                    String size = resultSet.getString("size");
                    double unitPrice = resultSet.getDouble("unitPrice");
                    int reOrderAmount = resultSet.getInt("reOrderAmount");
                    //product is made
                    Product product = new Product(ID, name, amount, weight, size, unitPrice, reOrderAmount, false);
                    //product and amount is added to list of products
                    productMap.put(product, intMap.get(product.getProductID()));
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //return HashMap
        return productMap;
    }
    //Method for deleting all orderlines belonging to an order
    public void deleteOrderLines(String table, String orderID) throws SQLException
    {
        try
        {
            createConnection();
            //statement is prepared (DELETE FROM ... WHERE ...)
            preparedStatement = connect.prepareStatement("delete from waterWise." + table + " WHERE orderID like ?;");
            preparedStatement.setString(1, orderID);
            preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }
    
    // Customer
    //Method for creating new customer
    public void createCustomer(Customer customer) throws SQLException
    {
        try
        {
            //if PK (phoneNumber) is not allready in use
            if (checkIfDuplicateInt("customer", "phoneNumber", customer.getPhoneNumber()) == false)
            {
                createConnection();
                //prepare statement (INSERT INTO)
                preparedStatement = connect.prepareStatement("insert into  waterWise.customer values (? , ?, ?, ?, ?)");
                preparedStatement.setInt(1, customer.getPhoneNumber());
                preparedStatement.setString(2, customer.getCustomerEmail());
                preparedStatement.setString(3, c.packageCustomerAddress(customer.getDeliveryAddress(), customer.getDeliveryCityAddress(), customer.getDeliveryZipAddress(), customer.getDeliveryCountryAddress()));
                preparedStatement.setString(4, customer.getCustomerName());
                preparedStatement.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
                preparedStatement.executeUpdate();
            }
            //if PK is allready in use
            else
            {
                //update instead
                updateCustomer(customer);
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }
    //Method for updating existing customer
    public void updateCustomer(Customer customer) throws SQLException
    {
        try
        {
            createConnection();
            //prepare statement (UPDATE ... SET ... WHERE ...)
            preparedStatement = connect
                    .prepareStatement("UPDATE  waterWise.customer SET email = ?, deliveryAddress = ?, name = ? WHERE phoneNumber = ?");
            preparedStatement.setString(1, customer.getCustomerEmail());
            preparedStatement.setString(2, c.packageCustomerAddress(customer.getDeliveryAddress(), customer.getDeliveryCityAddress(), customer.getDeliveryZipAddress(), customer.getDeliveryCountryAddress()));
            preparedStatement.setString(3, customer.getCustomerName());
            preparedStatement.setInt(4, customer.getPhoneNumber());
            preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }
    //Method for retrieving list of all customers
    public ArrayList<Customer> loadCustomerList() throws Exception
    {
        //ArrayList is made
        ArrayList<Customer> list = new ArrayList<Customer>();
        try
        {
            createConnection();
            //statement is prepared
            preparedStatement = connect.prepareStatement("SELECT * from waterWise.customer");
            resultSet = preparedStatement.executeQuery();
            //as long as there are more customers
            while (resultSet.next())
            {
                int phoneNumber = resultSet.getInt("phoneNumber");
                String email = resultSet.getString("email");
                String completeAddress = resultSet.getString("deliveryAddress");
                String name = resultSet.getString("name");
                String creationDate = df.format(resultSet.getDate("creationDate"));
                //completeAddress is split into an array
                String[] address = c.unpackage(completeAddress);
                String deliveryAddress = address[0];
                String deliveryCityAddress = address[1];
                String deliveryZipAddress = address[2];
                String deliveryCountryAddress = address[3];
                //customer is set
                Customer customer = new Customer(phoneNumber, email, name, deliveryAddress, deliveryCityAddress, deliveryZipAddress, deliveryCountryAddress, creationDate);
                //customer is added to ArrayList
                list.add(customer);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //ArrayList is returned
        return list;
    }
    //Method for retrieving specific customer
    public Customer loadCustomer(int phone) throws Exception
    {
        Customer customer;
        try
        {
            createConnection();
            //statement is prepared (SELECT * FROM ... WHERE...)
            preparedStatement = connect.prepareStatement("SELECT * from waterWise.customer where phoneNumber = ?");
            preparedStatement.setInt(1, phone);
            resultSet = preparedStatement.executeQuery();
            int phoneNumber = 0;
            String email = null;
            String completeAddress = null;
            String name = null;
            String creationDate = null;
            if (resultSet.next())
            {
                phoneNumber = resultSet.getInt("phoneNumber");
                email = resultSet.getString("email");
                completeAddress = resultSet.getString("deliveryAddress");
                name = resultSet.getString("name");
                creationDate = df.format(resultSet.getDate("creationDate"));
            }
            //completeAddress is split into an array
            String[] address = c.unpackage(completeAddress);
            String deliveryAddress = address[0];
            String deliveryCityAddress = address[1];
            String deliveryZipAddress = address[2];
            String deliveryCountryAddress = address[3];
            //customer is set
            customer = new Customer(phoneNumber, email, name, deliveryAddress, deliveryCityAddress, deliveryZipAddress, deliveryCountryAddress, creationDate);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //customer is returned
        return customer;
    }
    //Method for deleting customer
    public void deleteCustomer(int phone) throws SQLException
    {
        //ArrayList is made
        ArrayList<String> orderList = new ArrayList<String>();
        try
        {
            createConnection();
            //Prepare statement (SELECT... FROM... WHERE...)
            preparedStatement = connect.prepareStatement("SELECT ID from waterWise.incomingOrder where customerPhone = ?");
            preparedStatement.setInt(1, phone);
            resultSet = preparedStatement.executeQuery();
            //while there are more orderID's
            while (resultSet.next())
            {
                //add orderID's to ArrayList
                String order = resultSet.getString("ID");
                orderList.add(order);
            }
        }
        catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
        }
        //for all orderID's on list
        for (String order : orderList)
        {
            //delete order
            deleteOrder("incomingOrder", "ID", order);
        }
        try
        {
            createConnection();
            //prepare statement (DELETE FROM... WHERE...)
            preparedStatement = connect.prepareStatement("delete from waterWise.customer WHERE phoneNumber = ?;");
            preparedStatement.setInt(1, phone);
            preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }

    // Product
    //Method for creating a new product
    public void createProduct(Product product) throws SQLException
    {
        try
        {
            //if the PK "ID" is not used
            if (checkIfDuplicateInt("product", "ID", product.getProductID()) == false)
            {
                createConnection();
                //prepar statement (INSERT INTO)
                preparedStatement = connect.prepareStatement("insert into  waterWise.product values (?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(1, product.getProductID());
                preparedStatement.setString(2, product.getProductName());
                preparedStatement.setInt(3, product.getAmountInStorage());
                preparedStatement.setDouble(4, product.getWeight());
                preparedStatement.setString(5, product.getSize());
                preparedStatement.setDouble(6, product.getUnitPrice());
                preparedStatement.setInt(7, product.getReorderAmount());
                preparedStatement.executeUpdate();
            }
            //if PK is used
            else
            {
                //update existing product
                updateProduct(product);
            }
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }
    //Method for updating existing product
    public void updateProduct(Product product) throws SQLException
    {
        try
        {
            createConnection();
            //prepare statement (UPDATE... SET... WHERE...)
            preparedStatement = connect
                    .prepareStatement("UPDATE  waterWise.product SET ID = ?, name = ?, amount = ?, weight = ?, size = ?, unitPrice = ?, reOrderAmount = ? WHERE ID = ?");

            preparedStatement.setInt(1, product.getProductID());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setInt(3, product.getAmountInStorage());
            preparedStatement.setDouble(4, product.getWeight());
            preparedStatement.setString(5, product.getSize());
            preparedStatement.setDouble(6, product.getUnitPrice());
            preparedStatement.setInt(7, product.getReorderAmount());
            preparedStatement.setInt(8, product.getProductID());
            preparedStatement.executeUpdate();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }
    //Method for loading complete list of products
    public ArrayList<Product> loadProductList() throws Exception
    {
        //ArrayList is made
        ArrayList<Product> list = new ArrayList<Product>();
        try
        {
            createConnection();
            //prepare statement (SELECT * FROM...)
            preparedStatement = connect.prepareStatement("SELECT * from waterWise.product");
            resultSet = preparedStatement.executeQuery();
            //while there are more products
            while (resultSet.next())
            {
                //retrieve information
                int ID = resultSet.getInt("ID");
                String name = resultSet.getString("name");
                int amount = resultSet.getInt("amount");
                double weight = resultSet.getDouble("weight");
                String size = resultSet.getString("size");
                double unitPrice = resultSet.getDouble("unitPrice");
                int reOrderAmount = resultSet.getInt("reOrderAmount");
                //create product
                Product product = new Product(ID, name, amount, weight, size, unitPrice, reOrderAmount, false);
                //add product to list
                list.add(product);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //return ArrayList
        return list;
    }
    //method for retrieving specific product
    public Product loadProduct(int productID) throws Exception
    {
        Product product;
        try
        {
            createConnection();
            //prepare statement (SELECT * FROM... WHERE...)
            preparedStatement = connect.prepareStatement("SELECT * from waterWise.product where ID = ?");
            preparedStatement.setInt(1, productID);
            resultSet = preparedStatement.executeQuery();
            //initialize values
            int ID = 0;
            String name = null;
            int amount = 0;
            double weight = 0;
            String size = null;
            double unitPrice = 0;
            int reOrderAmount = 0;
            if (resultSet.next())
            {
                //set values
                ID = resultSet.getInt("ID");
                name = resultSet.getString("name");
                amount = resultSet.getInt("amount");
                weight = resultSet.getDouble("weight");
                size = resultSet.getString("size");
                unitPrice = resultSet.getDouble("unitPrice");
                reOrderAmount = resultSet.getInt("reOrderAmount");
            }
            //set product
            product = new Product(ID, name, amount, weight, size, unitPrice, reOrderAmount, false);
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            closeConnection();
        }
        //product is returned
        return product;
    }

    //Method for deleting a specific product
    public void deleteProduct(int productID) throws SQLException
    {
        try
        {
            // Delete from incomingOrderLine
            createConnection();
            //preparestatement (DELETE.. FROM... WHERE...)
            preparedStatement = connect.prepareStatement("delete from waterWise.incomingOrderLine WHERE productID = ?;");
            preparedStatement.setInt(1, productID);
            preparedStatement.executeUpdate();
            closeConnection();
            // Delete from outgoingOrderLine
            createConnection();
            //preparestatement (DELETE.. FROM... WHERE...)
            preparedStatement = connect.prepareStatement("delete from waterWise.outgoingOrderLine WHERE productID = ?;");
            preparedStatement.setInt(1, productID);
            preparedStatement.executeUpdate();
            closeConnection();
            // Delete from product
            createConnection();
            //preparestatement (DELETE.. FROM... WHERE...)
            preparedStatement = connect.prepareStatement("delete from waterWise.product WHERE ID = ?;");
            preparedStatement.setInt(1, productID);
            preparedStatement.executeUpdate();
            closeConnection();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
