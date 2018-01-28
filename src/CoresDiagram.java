import javafx.scene.layout.Pane;

public class CoresDiagram extends GeneralDiagram
{
    private RotatableCore[] cores;
    
    private static final double SPINNER_SIZE = 90;
    
    private boolean onlyEvens;
    
    public CoresDiagram(int numCores,
                        double radius,
                        double thickness,
                        double angleBetween,
                        double length,
                        double x,
                        double y)
    {
        super(x,y);
        
        double angle = 2 * Math.PI / numCores - angleBetween;
        
        cores = new RotatableCore[numCores];
        
        for (int i = 0; i < numCores; ++i)
        {
            System.out.println("LOOP");
            
            double rotation = -i * (angle + angleBetween);
            
            cores[i] = new RotatableCore(radius, thickness, angle, length, rotation, x, y);
            
            getChildren().add(cores[i]);
        }
        
        onlyEvens = false;
        
        initSpinner(x,y);
    }
    
    public void setVoltage(double voltage, double direction)
    {
        
        
        for (int i = 0; i < cores.length; ++i)
        {
            double rotation = 2 * Math.PI * i / cores.length;
            
            double scale = Math.cos(direction - rotation);
            
            if (!onlyEvens || i % 2 == 0)
                cores[i].updateColor(voltage * scale);
            else
                cores[i].updateColor(0);
        }
        
        super.setVoltage(voltage, direction);
    }
    
    public void setVoltageClamped(double voltage, double direction, double amplitude)
    {
        for (int i = 0; i < cores.length; ++i)
        {
            double rotation = 2 * Math.PI * i / cores.length;
            
            double scale = Math.cos(rotation - direction);
            
            if (!onlyEvens || i % 2 == 0)
                cores[i].updateColor(voltage * scale);
            else
                cores[i].updateColor(0);
        }
        
        super.setVoltageClamped(voltage, direction, amplitude);
    }
    
    public void setVoltageFollow(double voltage, double direction)
    {
        for (int i = 0; i < cores.length; ++i)
        {
            double rotation = 2 * Math.PI * i / cores.length;
            
            double scale = Math.cos(direction - rotation);
            
            if (!onlyEvens || i % 2 == 0)
                cores[i].updateColor(voltage * scale);
            else
                cores[i].updateColor(0);
        }
        
        super.setVoltageFollow(voltage, direction);
    }
    
    public void setOnlyEvens(boolean oe)
    {
        onlyEvens = oe;
    }
}