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

//Author Jesper Smith
public class AddProductFrame extends JFrame {

    //Fields
    HashMap<Product, Integer> listOfProducts = new HashMap<Product, Integer>();

    //Dimensions
    Dimension buttonDimension = new Dimension(100, 30);
    Dimension productPaneDimension = new Dimension(0, 120);
    Dimension productTableDimension = new Dimension(200, 200);
    Dimension buttonPanelDimension = new Dimension(120, 100);

    //JPanels
    JPanel ofPanel = new JPanel();
    JPanel middlePanel = new JPanel();
    JPanel productPanel = new JPanel();
    JPanel productTablePanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    JPanel supplierPanel = new JPanel();

    //ScrollPane
    JTable productTable = new JTable();
    JScrollPane productTableScrollPane = new JScrollPane(productTable);
    ArrayList<Product> chosenProducts = new ArrayList<>(); // Hvad gør vi her?

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
    JButton confirmJButton = new JButton("Committest");
    JButton cancelJButton = new JButton("Cancel");
    JButton addButton = new JButton("Add");
    JButton removeProductButton = new JButton("Fjern vare");

    //method that builds the frame and buttons
    private void frameBuild() {
        this.setTitle("WaterWise DB Project - Tilføj Produkter");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);

        //ProductArrayList
        updateProductList();

        //Panels
        ofPanel.setLayout(new BorderLayout());

        ofPanel.add(middlePanel, BorderLayout.NORTH);
        ofPanel.add(productTablePanel, BorderLayout.SOUTH);

        //MiddlePanel
        middlePanel.setLayout(new BorderLayout());

        middlePanel.add(productPanel, BorderLayout.CENTER);

        //ProductPanel
        productPanel.setPreferredSize(productPaneDimension);
        productPanel.setBorder(new TitledBorder("Produkter"));
        productPanel.setLayout(new FlowLayout());

        for (Product temp : ElementListCollection.getPList()) {
            productComboList.add(temp.getProductName());
        }
        productbox.setModel(new DefaultComboBoxModel(productComboList.toArray()));

        productPanel.add(productLabel);
        productPanel.add(productbox);

        productPanel.add(amountLabel);
        productPanel.add(amountField);
        productPanel.add(addButton);
        addButton.setPreferredSize(buttonDimension);

        //BottomPanel
        ofPanel.add(bottomPanel, BorderLayout.CENTER);
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

        addButton.addActionListener(new Listener().new addProductFrameButton(this));
        confirmJButton.addActionListener(new Listener().new AddProductAmountButton(this));
        cancelJButton.addActionListener(new Listener().new DisposeFrameButton(this));
        removeProductButton.addActionListener(new Listener().new RemoveAmountFromTableButton(this));

        ofPanel.setVisible(true);
        this.add(ofPanel);
        this.setVisible(true);
    }

    public void addProductsToStock() {
        for (Product key : listOfProducts.keySet()) {
            int x = listOfProducts.get(key);
            x += key.getAmountInStorage();
            key.setAmountInStorage(x);
            key.Update();
        }
    }

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

        chosenProductsTableModel.setColumnIdentifiers(new String[]{"ProduktID", "ProduktNavn", "Antal"});
        chosenProductsTableModel.setRowCount(listOfProducts.keySet().size());

        int row = 0;
        for (Product products : listOfProducts.keySet()) {
            chosenProductsTableModel.setValueAt(products.getProductID(), row, 0);
            chosenProductsTableModel.setValueAt(products.getProductName(), row, 1);
            chosenProductsTableModel.setValueAt(listOfProducts.get(products), row, 2);

            row++;
        }

        productTable.setModel(chosenProductsTableModel);
    }

    public AddProductFrame() {
        frameBuild();
    }

}
