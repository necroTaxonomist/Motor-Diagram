import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class StatorDiagram extends Pane
{
    private Tooth[] teeth;
    private Coil[] coils;
    private Spinner spinner;
    private double amplitude;
    
    public StatorDiagram(double radius,
                   double thickness,
                   double angle,
                   double x,
                   double y)
    {
        teeth = new Tooth[6];
        for (int i = 0; i < 6; ++i)
        {
            /*teeth[2*i] = new Tooth(radius + i * 3*thickness/2,
                                  thickness, angle,
                                  i * 120, x, y);
            teeth[2*i+1] = new Tooth(radius + i * 3*thickness/2,
                                  thickness, angle,
                                  i * 120 + 180, x, y);*/
            teeth[i] = new Tooth(radius, thickness, angle, -i * 60, x, y);
            
            getChildren().add(teeth[i]);
        }
        
        coils = new Coil[6];
        for (int i = 0; i < 3; ++i)
        {
            coils[2*i] = new Coil(radius + i * 3*thickness/2 + 2*thickness,
                                  thickness/2, 160,
                                  i * 120, x, y);
            coils[2*i+1] = new Coil(radius + i * 3*thickness/2 + 2*thickness,
                                  thickness/2, 160,
                                  i * 120 + 180, x, y);
            
            getChildren().add(coils[2*i]);
            getChildren().add(coils[2*i+1]);
        }
        
        
        spinner = new Spinner(x,y,radius,radius/3);
        
        
        getChildren().add(spinner);
        spinner.acknowledgeParent();
        
        amplitude = 0;
    }

    public void setToothVoltages(double phase)
    {
        
        //teeth[0].updateColor(Math.sin(phase));
        //teeth[1].updateColor(Math.sin(phase + Math.PI));
        for (int i = 0; i < 6; ++i)
        {
            if (phase < 0)
                teeth[i].updateColor(0);
            else
                teeth[i].updateColor(amplitude * Math.sin(phase + i*Math.PI/3));
        }
    }
    
    public void setSpinnerAC(double voltage, double frequency)
    {
        spinner.acVoltage(voltage !=0 ? frequency : 0, 0, Math.PI/2);
    }
    
    public void setAmplitude(double amp)
    {
        amplitude = amp;
    }
}