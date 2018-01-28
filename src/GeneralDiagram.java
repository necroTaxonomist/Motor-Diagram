import javafx.scene.layout.Pane;

public class GeneralDiagram extends Pane
{
    private Spinner spinner;
    
    private static final double SPINNER_SIZE = 90;
    
    public GeneralDiagram(double x, double y)
    {
    }
    
    public void setVoltage(double voltage, double direction)
    {
        spinner.dcVoltage(voltage, direction);
    }
    
    public void setVoltageClamped(double voltage, double direction, double amplitude)
    {
        if (voltage > 0)
            spinner.dcVoltage(Math.abs(amplitude), direction);
        else if (voltage < 0)
            spinner.dcVoltage(-Math.abs(amplitude), direction);
        else
            spinner.dcVoltage(0, direction);
    }
    
    public void setVoltageFollow(double voltage, double direction)
    {
        spinner.acVoltage(voltage, direction);
    }
    
    protected void initSpinner(double x, double y)
    {
        spinner = new Spinner(x,y,SPINNER_SIZE,SPINNER_SIZE/3);
        getChildren().add(spinner);
        spinner.acknowledgeParent();
    }
}