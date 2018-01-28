import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

public class StatorDiagram extends GeneralDiagram
{
    private Tooth[] teeth;
    private Coil[] coils;
    
    public StatorDiagram(double radius,
                   double thickness,
                   double angle,
                   double x,
                   double y)
    {
        super(x,y);
        
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
        
        initSpinner(x,y);
    }
    
    public void setVoltageFollow(double voltage, double direction)
    {
        for (int i = 0; i < 6; ++i)
        {
            double rotation = 2 * Math.PI * i / 6;
            
            double scale = Math.cos(direction - rotation);
            
            teeth[i].updateColor(voltage * scale);
        }
        
        super.setVoltageFollow(voltage, direction);
    }
}