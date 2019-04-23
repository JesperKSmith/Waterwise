package waterwise;

//Author Jesper Smith

 // The Error class builds the ErrorFrame with an appropriate suggested solution
public class Error {    
    
    private String errorInput;
    private String errorCategory;
    private String inputFix;
    private String correctExample;
    
    // The Error class constructor has 2 formal parameters - An error and the related category
    public Error(String error, String category)  {
        
        ErrorFrame e;
        this.errorInput = error;
        this.errorCategory = category;
        switch (category) { // A switch that changes, depending on what category the reported error has. 
            case "Vægt": case "Produkt pris":
                                this.inputFix       = "heltal eller kommatal";
                                this.correctExample = "2 eller 2.5";                                // Integer og decimal number cases
                break; 
                
            case "Produkt ID": case "Antal": case "Genbestil":                                      // Integer only cases
                                this.inputFix       = "heltal";
                                this.correctExample = "2";
                break;
                                                                                                    // Remaining cases are unique
            case "Ordre ID"     :this.inputFix       = "heltal eller bogstaver";
                                this.correctExample = "2Xj";
                break;
                
            case "Navn"        :this.inputFix       = "Der er ikke indtastet noget navn";
                                this.correctExample = "Navnefeltet må ikke være tomt";
                break;
                
            case "Størrelse"   :this.inputFix       = "Der er ikke valgt nogen størrelse";
                                this.correctExample = "Der skal være valgt størrelse";
                break;
                
            case "Telefon"     :this.inputFix       = "8 cifret heltal";
                                this.correctExample = "20304050";
                break;
                
            case "Adresse"     :this.inputFix       = "Der er fejl i den indtastede adresse";
                                this.correctExample = "Adresse skal indeholde vejnavn og husnummer";
                break;
                
             case "Email"     :this.inputFix       = "med @ og domæne";
                                this.correctExample= "farlig@llan.dk";
                break;
                 
              case "Postnummer":this.inputFix       = "4 heltal";
                                this.correctExample = "2860";
                break;
                  
            case "Levering"     :this.inputFix       = "Der er ikke indtastet leveringstype";
                                this.correctExample= "Der kan kun oprettes ordre med indtastet leveringstype";
                break;
                
            case "Produkter"   :this.inputFix       = "Der er ikke tilføjet nogle produkter til ordren";
                                this.correctExample= "Der kan kun oprettes ordre med tilføjede produkter";
                break;
                
            case "Betalingstype":this.inputFix       = "Der er ikke indtastet betalingstype";
                                this.correctExample= "Der kan kun oprettes ordre med indtastet betalingstype";
                break;
        }
        e = new ErrorFrame(error, category, inputFix, correctExample);  // ErrorFrame object initiated after the 4 formal parameters it takes has been set.
    }   
    
}
