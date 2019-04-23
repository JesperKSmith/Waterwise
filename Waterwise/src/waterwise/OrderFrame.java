package waterwise;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

//Author Marcus Melgaard Jensen & Emil Møller Nielsen

public class OrderFrame extends JFrame {

    //Fields
    //this will be set to the order we are working on
    Order orderShown;
    //variable to determine wheter it is an incoming or outgoing order - 
    //true is incoming

    String orderID;
    String startDate;
    String closedDate;
    String deliveryType;
    String paymentType;
    String customerEmail;
    String deliveryAddress;
    String addressCity;
    String addressZip;
    String addressCountry;
    String customerName;
    int customerPhonenumber;

    String supplierName;
    String supplierEmail;
    String ownAddress;
    String ownCity;
    String ownZip;
    String ownCountry;
    int ownPhonenumber;

    String orderStatus;
    HashMap<Product, Integer> listOfProducts = new HashMap<>();

    //Dimensions
    Dimension buttonDimension = new Dimension(100, 30);
    Dimension productPaneDimension = new Dimension(0, 120);
    Dimension productTableDimension = new Dimension(200, 200);
    Dimension buttonPanelDimension = new Dimension(120, 100);
    Dimension totalpricePanelDimension = new Dimension(120, 100);

    //JPanels
    JPanel ofPanel = new JPanel();
    JPanel topPanel = new JPanel();
    JPanel middlePanel = new JPanel();
    JPanel customerPanel = new JPanel();
    JPanel customerPanelWEST = new JPanel();
    JPanel customerPanelEAST = new JPanel();
    JPanel productPanel = new JPanel();
    JPanel productTablePanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    JPanel supplierPanel = new JPanel();

    //Supplier
    JLabel supplierNameLabel = new JLabel("Supplier:");
    JTextField supplierNameField = new JTextField("Waterwise");

    JLabel supplierEmailLabel = new JLabel("Email:");
    JTextField supplierEmailField = new JTextField("waterwise@waterwise.com");

    JLabel ownAddressLabel = new JLabel("Addresse:");
    JTextField ownAddressField = new JTextField("vejnavn 1");

    JLabel ownCityLabel = new JLabel("By:");
    JTextField ownCityField = new JTextField("Copenhagen");

    JLabel ownZipLabel = new JLabel("Postnr:");
    JTextField ownZipField = new JTextField("1000");

    JLabel ownCountryLabel = new JLabel("Land:");
    JTextField ownCountryField = new JTextField("Denmark");

    JLabel ownPhonenumberLabel = new JLabel("Telefon:");
    JTextField ownPhonenumberField = new JTextField("21417007");

    //ScrollPane
    JTable productTable = new JTable();
    JScrollPane productTableScrollPane = new JScrollPane(productTable);
    ArrayList<Product> chosenProducts = new ArrayList<>(); // Hvad gør vi her?

    //OrdreID
    JPanel orderIDPanel = new JPanel();
    JLabel orderIDLabel = new JLabel("OrdreID:");
    JTextField orderIDField = new JTextField("", 5);

    //Delivery
    JPanel deliveryTypePanel = new JPanel();
    JLabel deliveryTypeLabel = new JLabel("Levering:");
    JTextField deliveryTypeField = new JTextField("", 15);

    //Paymenttype
    JPanel paymentTypePanel = new JPanel();
    JLabel paymentTypeLabel = new JLabel("Betalt via:");
    JTextField paymentTypeField = new JTextField();

    //Customer Email
    JPanel customerEmailPanel = new JPanel();
    JLabel customerEmailLabel = new JLabel("Email:");
    JTextField customerEmailField = new JTextField();

    //Delivery address
    JPanel deliveryAddressPanel = new JPanel();
    JLabel deliveryAddressLabel = new JLabel("Adresse:");
    JTextField deliveryAddressField = new JTextField();

    JLabel deliveryAddressCityLabel = new JLabel("By:");
    JTextField deliveryAddressCityField = new JTextField();

    JLabel deliveryAddressZipLabel = new JLabel("Postnr:");
    JTextField deliveryAddressZipField = new JTextField();

    JLabel deliveryAddressCountryLabel = new JLabel("Land:");
    JTextField deliveryAddressCountryField = new JTextField("Danmark");

    //CustomerName
    JPanel customerNamePanel = new JPanel();
    JLabel customerNameLabel = new JLabel("Kundenavn:");
    JTextField customerNameField = new JTextField();

    //Customer Phone
    JPanel customerPhonenumberPanel = new JPanel();
    JLabel customerPhonenumberLabel = new JLabel("Telefon:");
    JTextField customerPhonenumberField = new JTextField();

    //Status 
    JPanel statusPanel = new JPanel();
    JLabel statusLabel = new JLabel("Status:");
    String[] statusdrop = {"Afsluttet", "Uafsluttet"};
    JComboBox statusmenu = new JComboBox(statusdrop);

    //ProductdropDown
    JPanel productListPanel = new JPanel();
    JLabel productLabel = new JLabel("Produkt:");
    ArrayList<String> productComboList = new ArrayList<>();
    String[] productarray = {" "};
    JComboBox<String> productbox = new JComboBox<String>(productarray);

    //Antal Produkter
    JLabel amountLabel = new JLabel("Antal:");
    JTextField amountField = new JTextField("", 5);

    //Buttons
    JPanel buttonPanel = new JPanel();
    JButton confirmJButton = new JButton("Bekræft");
    JButton cancelJButton = new JButton("Annullér");
    JButton addButton = new JButton("Tilføj");
    JButton removeProductButton = new JButton("Fjern vare");

    //Total pris
    JTextField totalPrice = new JTextField("", 6);

    //method that builds the frame and buttons
    private void frameBuild() {
        this.setTitle("WaterWise DB Project - OrderFrame");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);

        //Panels
        ofPanel.setLayout(new BorderLayout());

        ofPanel.add(topPanel, BorderLayout.NORTH);
        ofPanel.add(middlePanel, BorderLayout.CENTER);
        ofPanel.add(productTablePanel, BorderLayout.SOUTH);

        //TopPanel
        topPanel.setLayout(new BorderLayout());
        topPanel.add(orderIDPanel, BorderLayout.WEST);
        orderIDPanel.add(orderIDLabel);
        orderIDPanel.add(orderIDField);
        //orderIDField.setEditable(false);
        topPanel.add(statusPanel, BorderLayout.EAST);
        statusPanel.add(statusLabel);
        statusPanel.add(statusmenu);

        //MiddlePanel
        middlePanel.setLayout(new BorderLayout());

        middlePanel.add(productPanel, BorderLayout.SOUTH);

        //CustomerPanel
        //If sentence that checks whether the order we are showing is 
        //either an Incoming or an Outgoing and sets the frame accordingly
        if (orderShown instanceof Incoming) {

            middlePanel.add(customerPanel);
            customerPanel.setBorder(new TitledBorder("KundeOplysninger"));
            customerPanel.setLayout(null);
            customerPanel.add(customerNameLabel);
            customerPanel.add(customerNameField);
            customerPanel.add(customerEmailLabel);
            customerPanel.add(customerEmailField);
            customerPanel.add(customerPhonenumberLabel);
            customerPanel.add(customerPhonenumberField);
            customerPanel.add(deliveryAddressLabel);
            customerPanel.add(deliveryAddressField);
            customerPanel.add(deliveryAddressCityLabel);
            customerPanel.add(deliveryAddressCityField);
            customerPanel.add(deliveryAddressZipLabel);
            customerPanel.add(deliveryAddressZipField);
            customerPanel.add(deliveryAddressCountryLabel);
            customerPanel.add(deliveryAddressCountryField);
            customerPanel.add(paymentTypeLabel);
            customerPanel.add(paymentTypeField);
            customerPanel.add(deliveryTypeLabel);
            customerPanel.add(deliveryTypeField);

            //CustomerPanelBounds
            customerNameLabel.setBounds(8, 30, 75, 15);
            customerNameField.setBounds(78, 28, 150, 30);
            customerEmailLabel.setBounds(8, 60, 75, 15);
            customerEmailField.setBounds(78, 58, 150, 30);
            customerPhonenumberLabel.setBounds(8, 90, 75, 15);
            customerPhonenumberField.setBounds(78, 88, 150, 30);
            paymentTypeLabel.setBounds(8, 150, 75, 15);
            paymentTypeField.setBounds(78, 148, 150, 30);
            deliveryAddressLabel.setBounds(260, 30, 75, 10);
            deliveryAddressField.setBounds(318, 28, 150, 30);
            deliveryAddressCityLabel.setBounds(260, 60, 75, 15);
            deliveryAddressCityField.setBounds(318, 58, 150, 30);
            deliveryAddressZipLabel.setBounds(260, 90, 75, 15);
            deliveryAddressZipField.setBounds(318, 88, 150, 30);
            deliveryAddressCountryLabel.setBounds(260, 120, 75, 15);
            deliveryAddressCountryField.setBounds(318, 118, 150, 30);
            deliveryTypeLabel.setBounds(260, 150, 75, 25);

            deliveryTypeField.setBounds(318, 148, 150, 30);

        } else if (orderShown instanceof Outgoing) {

            middlePanel.add(supplierPanel);
            supplierPanel.setBorder(new TitledBorder("SupplierOplysninger"));
            supplierPanel.setLayout(null);
            supplierPanel.add(supplierNameLabel);
            supplierPanel.add(supplierNameField);
            supplierPanel.add(supplierEmailLabel);
            supplierPanel.add(supplierEmailField);
            supplierPanel.add(ownPhonenumberLabel);
            supplierPanel.add(ownPhonenumberField);
            supplierPanel.add(ownAddressLabel);
            supplierPanel.add(ownAddressField);
            supplierPanel.add(ownCityLabel);
            supplierPanel.add(ownCityField);
            supplierPanel.add(ownZipLabel);
            supplierPanel.add(ownZipField);
            supplierPanel.add(ownCountryLabel);
            supplierPanel.add(ownCountryField);
            supplierPanel.add(deliveryTypeLabel);
            supplierPanel.add(deliveryTypeField);
            supplierPanel.add(paymentTypeLabel);
            supplierPanel.add(paymentTypeField);

            //CustomerPanelBounds
            supplierNameLabel.setBounds(8, 30, 75, 15);
            supplierNameField.setBounds(78, 28, 150, 30);
            supplierEmailLabel.setBounds(8, 60, 75, 15);
            supplierEmailField.setBounds(78, 58, 150, 30);
            ownPhonenumberLabel.setBounds(8, 90, 75, 15);
            ownPhonenumberField.setBounds(78, 88, 150, 30);
            ownAddressLabel.setBounds(260, 30, 75, 10);
            ownAddressField.setBounds(318, 28, 150, 30);
            ownCityLabel.setBounds(260, 60, 75, 15);
            ownCityField.setBounds(318, 58, 150, 30);
            ownZipLabel.setBounds(260, 90, 75, 15);
            ownZipField.setBounds(318, 88, 150, 30);
            ownCountryLabel.setBounds(260, 120, 75, 15);
            ownCountryField.setBounds(318, 118, 150, 30);
            deliveryTypeLabel.setBounds(260, 150, 75, 15);
            deliveryTypeField.setBounds(318, 148, 150, 30);
            paymentTypeLabel.setBounds(8, 150, 75, 15);
            paymentTypeField.setBounds(78, 148, 150, 30);

            //Faktiske felter
            supplierNameField.setText("Waterwise");
            supplierEmailField.setText("waterwise@waterwise.com");
            ownPhonenumberField.setText("21417007");
            ownAddressField.setText("vejnavn");
            ownCityField.setText("Copenhagen");
            ownZipField.setText("1000");
            ownCountryField.setText("Denmark");
            paymentTypeField.setText("Overførsel");
            deliveryTypeField.setText("Airmail");

        }

        //ProductPanel
        productPanel.setPreferredSize(productPaneDimension);
        productPanel.setBorder(new TitledBorder("Produkter"));
        productPanel.setLayout(new FlowLayout());

        //Loop that removes already added products from the product dropdown
        ArrayList<Product> temporary = ElementListCollection.getPList();

        for (Product temp : temporary) {
            productComboList.add(temp.getProductName());
            for (Product tempp : listOfProducts.keySet()) {
                if (temp.getProductID() == tempp.getProductID()) {
                    productComboList.remove(temp.getProductName());
                }
            }

        }

        productbox.setModel(new DefaultComboBoxModel(productComboList.toArray()));

        productPanel.add(productLabel);
        productPanel.add(productbox);

        productPanel.add(amountLabel);
        productPanel.add(amountField);
        productPanel.add(addButton);
        addButton.setPreferredSize(buttonDimension);

        //BottomPanel
        ofPanel.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(productTableScrollPane, BorderLayout.CENTER);
        productTableScrollPane.setPreferredSize(productTableDimension);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(confirmJButton);
        buttonPanel.add(cancelJButton);
        buttonPanel.add(removeProductButton);

        buttonPanel.setPreferredSize(buttonPanelDimension);
        confirmJButton.setPreferredSize(buttonDimension);
        removeProductButton.setPreferredSize(buttonDimension);
        cancelJButton.setPreferredSize(buttonDimension);

        addButton.addActionListener(new Listener().new addProductButton(this));
        confirmJButton.addActionListener(new Listener().new SaveEditButton(this));
        cancelJButton.addActionListener(new Listener().new DisposeFrameButton(this));
        removeProductButton.addActionListener(new Listener().new RemoveFromTableButton(this));

        ofPanel.setVisible(true);
        this.add(ofPanel);
        updateProductList();
        this.setVisible(true);
    }

    //Method that updates the available products list
    public void updateProductComboBox() {
        productbox.setModel(new DefaultComboBoxModel(productComboList.toArray()));
    }

    public void updateProductList() {
        DefaultTableModel chosenProductsTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 2) {
                    return true;
                } else {
                    return false;
                }

            }
        };

        productTable.setAutoCreateRowSorter(true);

        chosenProductsTableModel.setColumnIdentifiers(new String[]{"ProduktID", "ProduktNavn", "Antal", "Pris"});
        chosenProductsTableModel.setRowCount(listOfProducts.keySet().size());

        //Loops through the list, setting the value in the table from Products
        int row = 0;
        for (Product products : listOfProducts.keySet()) {
            chosenProductsTableModel.setValueAt(products.getProductID(), row, 0);
            chosenProductsTableModel.setValueAt(products.getProductName(), row, 1);
            chosenProductsTableModel.setValueAt(listOfProducts.get(products), row, 2);
            chosenProductsTableModel.setValueAt(products.getUnitPrice(), row, 3);

            row++;
        }

        productTable.setModel(chosenProductsTableModel);
    }

    //Constructor that takes and order to edit and show
    public OrderFrame(Order orderToShow) {

        orderShown = orderToShow;
        listOfProducts = orderShown.getListOfProducts();
        frameBuild();
        if (orderToShow instanceof Outgoing) {
            setTextOutgoing((Outgoing) orderToShow);
        } else if (orderToShow instanceof Incoming) {
            setTextIncoming((Incoming) orderToShow);
        }

    }

    //Settext method that sets text in the fields, shared by incoming and outgoing
    private void setTextCommon(Order ots) {
        orderIDField.setText(ots.getOrderID());
        deliveryTypeField.setText(ots.getDeliveryType());
        paymentTypeField.setText(ots.getPaymentType());
        if (ots.getOrderStatus() != null) {

            switch (ots.getOrderStatus()) {
                case "Afsluttet":
                    statusmenu.setSelectedIndex(0);
                    break;
                case "Uafsluttet":
                    statusmenu.setSelectedIndex(1);
                    break;
            }

        }
        if (ots.getListOfProducts() != null) {
            listOfProducts = ots.getListOfProducts();
            updateProductList();

        }
    }
//Settext method that sets text in the fields for outgoing

    private void setTextOutgoing(Outgoing ots) {

        //supplier
        if (ots.getSupplierName() != null) {
            supplierEmailField.setText(ots.getSupplierEmail());
            supplierNameField.setText(ots.getSupplierName());
            ownAddressField.setText(ots.getOwnAddress());
            ownCityField.setText(ots.getOwnCity());
            ownZipField.setText(ots.getOwnZip());
            ownCountryField.setText(ots.getOwnCountry());
            ownPhonenumberField.setText(ots.getOwnPhonenumber());
            orderIDField.setEditable(false);

        }

        setTextCommon(ots);

    }

    //Settext method that sets text in the fields for incoming
    private void setTextIncoming(Incoming ots) {

        //Customer
        int phonenum = ots.getCustomerPhonenumber();
        if (phonenum != 0) {
            customerPhonenumberField.setText("" + phonenum);
            customerPhonenumberField.setEditable(false);
            FileWrapper fw = new FileWrapper();
            Customer c;

            try {

                c = fw.loadCustomer(phonenum);
                ownAddressField.setText(c.getDeliveryAddress());
                ownCityField.setText(c.getDeliveryCityAddress());
                ownZipField.setText(c.getDeliveryZipAddress());
                ownCountryField.setText(c.getDeliveryCountryAddress());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

        setTextCommon(ots);
    }
}
