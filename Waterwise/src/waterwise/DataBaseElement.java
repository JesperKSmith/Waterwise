package waterwise;

//Author Marcus Melgaard Jensen

public abstract class DataBaseElement {

    //Update and Delete methods that all classes inheriting from DataBaseElement will have
    //They do nothing
    //They has to be overridden in each subclass
    
    abstract void Update();

    abstract void Delete();
}
