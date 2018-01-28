import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Tooth extends Pane
{
    private Polygon mainShape;
    
    public Tooth(double radius,
                double thickness,
                double angle,
                double rotation,
                double x,
                double y)
    {
        mainShape = createPoints(radius, thickness, Math.PI * angle / 180, Math.PI * rotation / 180, x, y);
        mainShape.setStroke(Color.BLACK);
        getChildren().add(mainShape);
    }
    
    public void updateColor(double voltage)
    {
        if (voltage > 1)
            throw new NullPointerException();
        
        double r = voltage <= 0 ? .85 - 0.15*voltage : .85 - 0.20*voltage;
        double g = voltage <= 0 ? .85 + 0.20*voltage : .85 - 0.20*voltage;
        double b = voltage <= 0 ? .85 + 0.20*voltage : .85 + 0.15*voltage;
        
        Color c = new Color(r,g,b,1.0);
        
        mainShape.setFill(c);
    }
    
    private static Polygon createPoints(double radius,
                                        double thickness,
                                        double angle,
                                        double rotation,
                                        double xDisp,
                                        double yDisp)
    {
        double length = 5*thickness;
        
        double inc = Math.asin((thickness/16) / (radius+thickness));
        int amplitude = (int)Math.floor(angle/2/inc);
        int numPoints = 4 * amplitude - 91;
        
        double[] points = new double[numPoints*2];
        
        int nextIndex= 0;
        
        for (int t = amplitude; t >= 48; --t)
        {
            double theta = inc * t;
            double x = (radius + thickness) * Math.cos(theta);
            double y = (radius + thickness) * Math.sin(theta);
            
            points[nextIndex] = x; ++nextIndex;
            points[nextIndex] = y; ++nextIndex;
            
            if (t == 48)
            {
                points[nextIndex] = radius + thickness + length; ++nextIndex;
                points[nextIndex] = y; ++nextIndex;
            }
        }
        
        for (int t = -48; t >= -amplitude; --t)
        {
            double theta = inc * t;
            double x = (radius + thickness) * Math.cos(theta);
            double y = (radius + thickness) * Math.sin(theta);
            
            if (t == -48)
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
        
        double cos = Math.cos(rotation);
        double sin = Math.sin(rotation);
        
        for (int i = 0; i < points.length; i += 2)
        {
            double newX = cos*points[i] - sin*points[i+1];
            double newY = sin*points[i] + cos*points[i+1];
            
            points[i] = newX + xDisp;
            points[i+1] = newY + yDisp;
        }
        
        return new Polygon(points);
    }
}