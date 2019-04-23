package waterwise;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

//Author Jesper Smith
// ErrorFrames goal is to display any errors to the user, to secure a correct format is saved in the database

public class ErrorFrame extends JFrame {    

    //JPanels
    JPanel ofPanel = new JPanel(); 
    JPanel middlePanel = new JPanel();
    JPanel mainPanel = new JPanel();
 
    
    // ErrorFrame - Panel and labels
    
    JPanel errorPanel = new JPanel();
    JLabel errorOne = new JLabel();    
    JLabel fixOne = new JLabel();    


    //frameBuild - Builds the frame
    
    private void frameBuild() {
        this.setTitle("WaterWise DB Project - Error!");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(325, 150);
        this.setLocation(540, 700);
                
        //ofPanel - Panel contains MiddlePanel
        
        ofPanel.setLayout(new BorderLayout());
        ofPanel.add(middlePanel, BorderLayout.CENTER);
        
        //MiddlePanel - Contains the mainPanel
        
        middlePanel.setLayout(new BorderLayout());
        
        //Error Panel - Displays the current recieved errors 
        
         middlePanel.add(mainPanel, BorderLayout.CENTER);
         mainPanel.setBorder(new TitledBorder("Fejl - detaljer"));
         mainPanel.setLayout(null);
         errorOne.setForeground(Color.RED);
         fixOne.setForeground(Color.BLUE);
         mainPanel.add(errorOne);
         mainPanel.add(fixOne);        
            
            //errorPanelBounds - Bounds limiting the 2 JLabels. Format is (x , y, width, height)        
            
        errorOne.setBounds(8, 30, 500, 15);
        fixOne.setBounds(8, 60, 500, 15);
        
        
        ofPanel.setVisible(true);
        this.add(ofPanel);
        this.setVisible(true);
    }
    
    // Constructor for ErrorFrame - it has 4 formal parameters: An error, a category, a correct format and a correct example
    public ErrorFrame(String error, String category, String inputFix, String correctExample) {
        
        switch (category) {     // A switch that changes, depending on what category the reported error has.
            case "Vægt": case "Produkt ID": case "Antal": case "Genbestil": case "Produkt pris" :
             case "Telefon" : case "Email" : case "Ordre ID": case "Postnummer" :                   // Format cases : where an error can occur in the format
                                  errorOne.setText("fejl: " + error + " i kategorien: " + category); 
                                  fixOne.setText("korrekt format er " + inputFix + " f.eks. " + correctExample);
                                break;           
            case "Navn": case "Størrelse": case "Produkter" : case "Betalingstype" :                // No input cases : where an error can occur if a field is left blank
            case "Levering" : case "Adresse" :   
                                     errorOne.setText(inputFix); 
                                     fixOne.setText(correctExample);
                                break;
           
        }
        frameBuild();  // Frame is build after the 4 formal parameters has been set.

    }
}
