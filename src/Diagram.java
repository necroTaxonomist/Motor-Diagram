import javafx.scene.layout.Pane;

public class Diagram extends Pane
{
    private RotatableCore leftCore;
    private RotatableCore rightCore;
    private Spinner spinner;
    
    public Diagram(double radius,
                   double thickness,
                   double angle,
                   double length,
                   double x,
                   double y)
    {
        leftCore = new RotatableCore(radius, thickness, angle, length, Math.PI, x, y);
        rightCore = new RotatableCore(radius, thickness, angle, length, 0, x, y);
        spinner = new Spinner(x,y,radius,radius/3);
        
        getChildren().add(leftCore);
        getChildren().add(rightCore);
        getChildren().add(spinner);
        spinner.acknowledgeParent();
    }

    public void setCoreVoltages(double voltage)
    {
        leftCore.updateColor(-voltage);
        rightCore.updateColor(voltage);
    }
    
    public void setSpinnerDC(double voltage)
    {
        spinner.dcVoltage(Math.abs(voltage), voltage >= 0 ? 0 : Math.PI);
    }
    
    public void setSpinnerAC(double voltage, double frequency)
    {
        //spinner.acVoltage(voltage !=0 ? frequency : 0, 0, Math.PI/4);
    }
}