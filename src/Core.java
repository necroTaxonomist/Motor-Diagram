import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class Core extends Polygon
{
    private int direction;
    
    public Core(double radius,
                double thickness,
                double angle,
                double length,
                int dirIn,
                double x,
                double y)
    {
        super(createPoints(radius, thickness, Math.PI * angle / 180, length, -1*dirIn, x, y));
        direction = dirIn / Math.abs(dirIn);
        setStroke(Color.BLACK);
    }
    
    public void updateColor(double voltage)
    {
        voltage *= -direction;
        
        if (voltage > 1)
            throw new NullPointerException();
        
        double r = voltage <= 0 ? .85 - 0.15*voltage : .85 - 0.20*voltage;
        double g = voltage <= 0 ? .85 + 0.20*voltage : .85 - 0.20*voltage;
        double b = voltage <= 0 ? .85 + 0.20*voltage : .85 + 0.15*voltage;
        
        Color c = new Color(r,g,b,1.0);
        
        setFill(c);
    }
    
    private static double[] createPoints(double radius,
                                         double thickness,
                                         double angle,
                                         double length,
                                         int dir,
                                         double xDisp,
                                         double yDisp)
    {
        double inc = Math.asin((thickness/2) / (radius+thickness));
        int amplitude = (int)Math.floor(angle/2/inc);
        int numPoints = 4 * amplitude + 3;
        
        double[] points = new double[numPoints*2];
        
        int nextIndex= 0;
        
        for (int t = amplitude; t >= 1; --t)
        {
            double theta = inc * t;
            double x = (radius + thickness) * Math.cos(theta);
            double y = (radius + thickness) * Math.sin(theta);
            
            points[nextIndex] = x; ++nextIndex;
            points[nextIndex] = y; ++nextIndex;
            
            if (t == 1)
            {
                points[nextIndex] = radius + thickness + length; ++nextIndex;
                points[nextIndex] = y; ++nextIndex;
            }
        }
        
        for (int t = -1; t >= -amplitude; --t)
        {
            double theta = inc * t;
            double x = (radius + thickness) * Math.cos(theta);
            double y = (radius + thickness) * Math.sin(theta);
            
            if (t == -1)
            {
                points[nextIndex] = radius + thickness + length; ++nextIndex;
                points[nextIndex] = y; ++nextIndex;
            }
            
            points[nextIndex] = x; ++nextIndex;
            points[nextIndex] = y; ++nextIndex;
        }
        
        for (int t = -amplitude; t <= amplitude; ++t)
        {
            double theta = inc * t;
            double x = (radius) * Math.cos(theta);
            double y = (radius) * Math.sin(theta);
            
            points[nextIndex] = x; ++nextIndex;
            points[nextIndex] = y; ++nextIndex;
        }
        
        for (int i = 0; i < points.length; ++i)
        {
            if (i % 2 == 0)
                points[i] = points[i] * dir + xDisp;
            else
                points[i] = points[i] + yDisp;
        }
        
        return points;
    }
}