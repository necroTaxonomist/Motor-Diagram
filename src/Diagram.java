import javafx.scene.layout.Pane;

public class Diagram extends Pane
{
    private Core leftCore;
    private Core rightCore;
    private Spinner spinner;
    
    public Diagram()
    {
        leftCore = new Core(60,30,120, 50, 1, 200, 150);
        rightCore = new Core(60,30,120, 50, -1, 200, 150);
        spinner = new Spinner(200,150,60,20);
        
        getChildren().add(leftCore);
        getChildren().add(rightCore);
        getChildren().add(spinner);
        spinner.acknowledgeParent();
    }
    
    public void updateVoltage(double voltage)
    {
        leftCore.updateColor(voltage);
        rightCore.updateColor(voltage);
        spinner.updateVoltage(voltage);
    }
}