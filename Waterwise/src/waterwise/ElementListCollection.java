package waterwise;

import java.util.ArrayList;

/**
 *
 * @author BlottoG
 */
public class ElementListCollection {

    //Field
    private static ArrayList<Product> pList = new ArrayList<>();
    private static ArrayList<Order> oList = new ArrayList<>();
    private static ArrayList<Customer> cList = new ArrayList<>();
    private static ArrayList<Order> stockList = new ArrayList<>();

    //Setter
    public static void addPList(Product pToAdd) {
        pList.add(pToAdd);
    }

    public static void addOrder(Order newOrder) {
        oList.add(newOrder);
    }

    public static void addCList(Customer cToAdd) {
        cList.add(cToAdd);
    }

    public static void addStock(Order newOrder) {
        stockList.add(newOrder);
    }

    //Getter
    public static ArrayList<Product> getPList() {
        FileWrapper fw = new FileWrapper();
        try {
            pList = fw.loadProductList();
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - ElementListCollection");
            ex.printStackTrace();
        }
        return pList;
    }

    public static ArrayList<Order> getOList() {
        FileWrapper fw = new FileWrapper();
        try {
            oList = fw.loadIncomingOrderList();
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - ElementListCollection");
            ex.printStackTrace();
        }
        return oList;
    }

    public static ArrayList<Customer> getCList() {
        FileWrapper fw = new FileWrapper();
        try {
            cList = fw.loadCustomerList();
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - ElementListCollection");
            ex.printStackTrace();
        }
        return cList;
    }

    public static ArrayList<Order> getStockList() {
        FileWrapper fw = new FileWrapper();
        try {
            stockList = fw.loadOutgoingOrderList();
        } catch (Exception ex) {
            System.out.println(ex + " thrown from - ElementListCollection");
            ex.printStackTrace();
        }
        return stockList;
    }

}
