package waterwise;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

//Author Marcus Melgaard, Jesper Smith & Emil Møller Nielsen
public class Listener {

    Controller controller = new Controller();

    public Listener() {

    }

    //Inner classes
    public class ResetViewButton extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {

            controller.resetView();
        }

    }

    public class ResetOutgoingViewButton extends AbstractAction {

        Gui gui;

        public ResetOutgoingViewButton(Gui gui) {
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            gui.updateStockOrderList();
        }

    }

    public class EditOrderButton extends AbstractAction {

        JTable orderList;
        String cTTF;

        public EditOrderButton(JTable orders, String classToTestFor) {

            this.orderList = orders;
            this.cTTF = classToTestFor;

        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            if (orderList.getSelectedRowCount() != 1) {
                JOptionPane.showMessageDialog(null, "Du skal vælge et element i listen.");

            } else {

                Order c = (Order) controller.getElementFromTable(orderList, cTTF);
                OrderFrame editOrder = new OrderFrame(c);
            }
        }

    }

    public class EditCustomerButton extends AbstractAction {

        String cTTF;
        JTable customerList;

        public EditCustomerButton(JTable customers, String category) {
            customerList = customers;
            cTTF = category;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            if (customerList.getSelectedRowCount() != 1) {
                JOptionPane.showMessageDialog(null, "Du skal vælge et element i listen.");
            } else {
                Customer c = (Customer) controller.getElementFromTable(customerList, cTTF);
                NewCustomerFrame editCustomer = new NewCustomerFrame();
                editCustomer.setTextCustomer(c);
            }
        }

    }

    public class EditProductButton extends AbstractAction {

        String productToEdit;
        JTable productList;

        public EditProductButton(JTable products, String category) {
            productList = products;
            productToEdit = category;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (productList.getSelectedRowCount() != 1) {
                JOptionPane.showMessageDialog(null, "Du skal vælge et element i listen.");
            } else {
                Product p = (Product) controller.getElementFromTable(productList, productToEdit);
                NewProductFrame editProduct = new NewProductFrame();
                editProduct.setTextProduct(p);
            }
        }
    }

    public class DeleteElementButton extends AbstractAction {

        JTable elementList;
        String cTTF;

        public DeleteElementButton(JTable elements, String classToTestFor) {

            this.elementList = elements;
            this.cTTF = classToTestFor;

        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            if (elementList.getSelectedRowCount() != 1) {
                JOptionPane.showMessageDialog(null, "Du skal vælge et element i listen.");
            } else {
                DataBaseElement c = (DataBaseElement) controller.getElementFromTable(elementList, cTTF);
                c.Delete();
                controller.resetView();
            }

        }

    }

    public class ChangeStatusButton extends AbstractAction {

        JTable table = null;
        String cTTF;
        Gui gui;

        public ChangeStatusButton(Gui gui, String classToTestFor) {

            cTTF = classToTestFor;
            this.gui = gui;
            switch (cTTF) {
                case "Outgoing":
                    table = gui.stockOrderTable;
                    break;
                case "Incoming":
                    table = gui.orderTable;
                    break;
            }

        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {

                if (table.getSelectedRowCount() > 1) {
                    JOptionPane.showMessageDialog(null, "Du skal vælge ét element i listen.");
                } else {
                    Order c = (Order) controller.getElementFromTable(table, cTTF);
                    controller.changeStatusMethod(c);
                    controller.resetView();
                }

            } catch (IndexOutOfBoundsException iob) {
                JOptionPane.showMessageDialog(null, "Du skal vælge et element i listen.");
            }

        }

    }

    public class PrintLabelButton extends AbstractAction {

        JTable elementList;
        String cTTF;
        Gui gui;

        public PrintLabelButton(Gui gui, JTable elementList, String cTTF) {

            this.elementList = elementList;
            this.cTTF = cTTF;
            this.gui = gui;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                Order c = (Order) controller.getElementFromTable(elementList, cTTF);

                gui.printLabelFrame(c);
            } catch (IndexOutOfBoundsException iob) {
                JOptionPane.showMessageDialog(null, "Du skal vælge et element.");

            }

        }

    }

    public class PrintEmailButton extends AbstractAction {

        JTable elementList;
        String cTTF;
        Gui gui;

        public PrintEmailButton(Gui gui, JTable elementList, String cTTF) {

            this.elementList = elementList;
            this.cTTF = cTTF;
            this.gui = gui;

        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {

                Order c = (Order) controller.getElementFromTable(elementList, cTTF);

                gui.printEmailFrame(c);
            } catch (IndexOutOfBoundsException iob) {
                JOptionPane.showMessageDialog(null, "Du skal vælge et element.");

            }
        }
    }

    public class copyText extends AbstractAction {

        String textToClipboard;

        public copyText(String text) {
            textToClipboard = text;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            StringSelection stringSelection = new StringSelection(textToClipboard);
            Clipboard copy = Toolkit.getDefaultToolkit().getSystemClipboard();
            copy.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(null, "tekst kopieret");
        }
    }

    public class newProductFrameConfirmButton extends AbstractAction {

        NewProductFrame npf;

        public newProductFrameConfirmButton(NewProductFrame npf) {
            this.npf = npf;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Error er;

            npf.eh = new ErrorChecker();

            int appID = 0;
            int appAmount = 0;
            double appPrice = 0.0;
            double appWeight = 0.0;
            int appReorder = 0;
            String tempID = npf.productIDField.getText();
            String tempName = npf.productNameField.getText();
            String tempAmount = npf.productAmountField.getText();
            String tempWeight = npf.productWeightField.getText();
            String tempSize = npf.productSize.getSelectedItem().toString();
            String tempPrice = npf.productPriceField.getText();
            String tempReorder = npf.reorderField.getText();

            if (npf.eh.isNameValid(tempName)) {
                if (npf.eh.isPriceValid(tempPrice)) {
                    appPrice = npf.eh.StringToDouble(tempPrice);
                    if (npf.eh.isAmountValid(tempAmount)) {
                        appAmount = npf.eh.StringToInt(tempAmount);
                        if (npf.eh.isWeightValid(tempWeight)) {
                            appWeight = npf.eh.StringToDouble(tempWeight);
                            if (npf.eh.isProductIDValid(tempID)) {
                                appID = npf.eh.StringToInt(tempID);
                                if (npf.eh.isSizeValid(tempSize)) {
                                    if (npf.eh.isProductReorderValid(tempReorder)) {
                                        appReorder = npf.eh.StringToInt(tempReorder);
                                        System.out.println("Alt godkendt - der kan konverteres");
                                        Product appProduct = new Product(appID, tempName, appAmount, appWeight, tempSize, appPrice, appReorder, true);
                                        System.out.println("Produkt konverteret - der kan nu skrives til DB");
                                        npf.dispose();
                                    } else {
                                        er = new Error(tempReorder, "Genbestil");
                                    }
                                } else {
                                    er = new Error(tempSize, "Størrelse");
                                }
                            } else {
                                er = new Error(tempID, "Produkt ID");
                            }
                        } else {
                            er = new Error(tempWeight, "Vægt");
                        }
                    } else {
                        er = new Error(tempAmount, "Antal");
                    }
                } else {
                    er = new Error(tempPrice, "Produkt pris");
                }
            } else {
                er = new Error(tempName, "Navn");
            }
            controller.resetView();
        }

    }

    public class addProductButton extends AbstractAction {

        OrderFrame addProductAmount;
        String selectedItem;
        String selectedAmountString;
        Integer selectedAmount;

        public addProductButton(OrderFrame addProduct) {
            addProductAmount = addProduct;
        }

        @Override

        public void actionPerformed(ActionEvent ae) {

            try {

                selectedItem = addProductAmount.productbox.getSelectedItem().toString();
                selectedAmountString = addProductAmount.amountField.getText();
                selectedAmount = Integer.parseInt(selectedAmountString);

                Product temp = new Product("emil");

                for (Product p : ElementListCollection.getPList()) {
                    if (p.getProductName().equals(selectedItem)) {
                        temp = p;
                    }

                }
                addProductAmount.chosenProducts.add(temp);
                addProductAmount.listOfProducts.put(temp, selectedAmount);
                addProductAmount.productComboList.remove(temp.getProductName());
                addProductAmount.updateProductList();
                addProductAmount.updateProductComboBox();

            } catch (NullPointerException npe) {
                System.out.println("nullPointer");
                JOptionPane.showMessageDialog(null, "Ingen produkter valgt eller tilgængelige.");
            } catch (NumberFormatException nfe) {
                System.out.println("numberformat");
                JOptionPane.showMessageDialog(null, "Du skal vælge et antal vare.");
            }

        }
    }

    public class addProductFrameButton extends AbstractAction {

        AddProductFrame addProductFrameAmount;
        String selectedItem;
        String selectedAmountString;
        Integer selectedAmount;

        public addProductFrameButton(AddProductFrame addProduct) {
            addProductFrameAmount = addProduct;
        }

        @Override

        public void actionPerformed(ActionEvent ae) {

            try {

                selectedItem = addProductFrameAmount.productbox.getSelectedItem().toString();
                selectedAmountString = addProductFrameAmount.amountField.getText();
                selectedAmount = Integer.parseInt(selectedAmountString);

                Product temp = new Product("emil");

                for (Product p : ElementListCollection.getPList()) {
                    if (p.getProductName().equals(selectedItem)) {
                        temp = p;
                    }

                }
                addProductFrameAmount.chosenProducts.add(temp);
                addProductFrameAmount.listOfProducts.put(temp, selectedAmount);
                addProductFrameAmount.productComboList.remove(temp.getProductName());
                addProductFrameAmount.updateProductList();
                addProductFrameAmount.updateProductComboBox();

            } catch (NullPointerException npe) {
                System.out.println("nullPointer");
                JOptionPane.showMessageDialog(null, "Ingen produkter valgt eller tilgængelige.");
            } catch (NumberFormatException nfe) {
                System.out.println("numberformat");
                JOptionPane.showMessageDialog(null, "Du skal vælge et antal vare.");
            }

        }
    }

    public class confirmCustomerButton extends AbstractAction {

        NewCustomerFrame ec;

        public confirmCustomerButton(NewCustomerFrame editCustomer) {
            ec = editCustomer;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            Error er;
            ErrorChecker eh = new ErrorChecker();
            int appPhone = 0;

            String tempID = ec.customerPhonenumberField.getText();
            String tempName = ec.customerNameField.getText();
            String tempPhone = ec.customerPhonenumberField.getText();
            String tempEmail = ec.customerEmailField.getText();
            String tempStreet = ec.customerAddressField.getText();
            String tempZip = ec.zipField.getText();
            String tempCity = ec.cityField.getText();
            String tempCountry = ec.countryField.getText();
            String tempAddress = tempStreet + tempZip + tempCity + tempCountry;

            if (eh.isNameValid(tempName)) {
                if (eh.isPhonenumberValid(tempPhone)) {
                    appPhone = eh.StringToInt(tempPhone);
                    if (eh.isEmailValid(tempEmail)) {
                        if (eh.isAddressValid(tempAddress)) {
                            System.out.println("Alt godkendt - Opretter kunde objekt");
                            Customer c = new Customer(appPhone, tempEmail, tempName, tempStreet, tempCity, tempZip, tempCountry);
                            System.out.println("Kunde gemt i DB");
                            ec.dispose();
                            controller.resetView();

                        } else {
                            er = new Error(tempAddress, "Adresse");
                        }
                    } else {
                        er = new Error(tempEmail, "Email");
                    }

                } else {
                    er = new Error(tempPhone, "Telefon");
                }
            } else {
                er = new Error(tempName, "Navn");
            }

        }

    }

    public class checkReset extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            controller.resetView();
        }
    }

    public class RemoveAmountFromTableButton extends AbstractAction {

        AddProductFrame removeAmountOfProducts;
        int selectedRow;
        String selectedProduct;

        public RemoveAmountFromTableButton(AddProductFrame removeAmountFromTable) {
            removeAmountOfProducts = removeAmountFromTable;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {

                selectedRow = removeAmountOfProducts.productTable.getSelectedRow();
                if (removeAmountOfProducts.productTable.getValueAt(selectedRow, 1) == null) {
                    JOptionPane.showMessageDialog(null, "Du har markeret et tomt felt.");
                }
                for (Product p : removeAmountOfProducts.listOfProducts.keySet()) {
                    if (p.getProductName().equals(removeAmountOfProducts.productTable.getValueAt(selectedRow, 1))) {
                        removeAmountOfProducts.listOfProducts.remove(p);
                        selectedProduct = p.getProductName();
                    }
                }
                removeAmountOfProducts.productComboList.add(selectedProduct);
                removeAmountOfProducts.updateProductComboBox();
                removeAmountOfProducts.updateProductList();
            } catch (IndexOutOfBoundsException iob) {
                JOptionPane.showMessageDialog(null, "Du skal markere et produkt.");
            }
        }
    }

    public class RemoveFromTableButton extends AbstractAction {

        OrderFrame removeProduct;
        int selectedRow;
        String selectedProduct;

        public RemoveFromTableButton(OrderFrame removeProductFromTable) {
            removeProduct = removeProductFromTable;

        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            try {

                selectedRow = removeProduct.productTable.getSelectedRow();
                if (removeProduct.productTable.getValueAt(selectedRow, 1) == null) {
                    JOptionPane.showMessageDialog(null, "Du har markeret et tomt felt.");

                }

                for (Product p : removeProduct.listOfProducts.keySet()) {
                    if (p.getProductName().equals(removeProduct.productTable.getValueAt(selectedRow, 1))) {
                        removeProduct.listOfProducts.remove(p);
                        selectedProduct = p.getProductName();
                    }

                }
                removeProduct.productComboList.add(selectedProduct);
                removeProduct.updateProductComboBox();
                removeProduct.updateProductList();

            } catch (IndexOutOfBoundsException iob) {
                JOptionPane.showMessageDialog(null, "Du skal markere et produkt.");
            }

        }
    }

    public class SaveEditButton extends AbstractAction {

        OrderFrame ofSaveFrom;

        public SaveEditButton(OrderFrame ofSaveFrom) {
            this.ofSaveFrom = ofSaveFrom;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            controller.saveEditMethod(ofSaveFrom);
            controller.resetView();

        }

    }

    public class DisposeFrameButton extends AbstractAction {

        JFrame ftd;

        public DisposeFrameButton(JFrame frameToDispose) {
            ftd = frameToDispose;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {

            ftd.dispose();

        }

    }

    public class newCustomerFrame extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            NewCustomerFrame cF = new NewCustomerFrame();
        }

    }

    public class createNewProduct extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            NewProductFrame pF = new NewProductFrame();
        }
    }

    public class addProduct extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            AddProductFrame aPF = new AddProductFrame();
        }
    }

    public class createNewIncoming extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Incoming i = new Incoming();
        }

    }

    public class AddProductAmountButton extends AbstractAction {

        AddProductFrame apf;

        public AddProductAmountButton(AddProductFrame modtagetApf) {
            this.apf = modtagetApf;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            apf.addProductsToStock();
            controller.resetView();
            apf.dispose();
        }
    }

    public class createNewOutgoing extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent ae) {
            Outgoing o = new Outgoing();
        }

    }

}
