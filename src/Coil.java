import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

public class Coil extends Pane
{
    private Polygon mainShape;
    
    public Coil(double radius,
                double thickness,
                double angle,
                double rotation,
                double x,
                double y)
    {
        mainShape = createPoints(radius, thickness, Math.PI * angle / 180, Math.PI * rotation / 180, x, y);
        mainShape.setStroke(Color.BLACK);
        mainShape.setFill(Color.ORANGE);
        getChildren().add(mainShape);
    }
    
    private static Polygon createPoints(double radius,
                                        double thickness,
                                        double angle,
                                        double rotation,
                                        double xDisp,
                                        double yDisp)
    {
        double inc = Math.asin((thickness/16) / (radius+thickness));
        int amplitude = (int)Math.floor(angle/2/inc);
        int numPoints = 4 * amplitude + 2;
        
        double[] points = new double[numPoints*2];
        
        int nextIndex= 0;
        
        for (int t = amplitude; t >= -amplitude; --t)
        {
            double theta = inc * t;
            double x = (radius) * Math.cos(theta);
            double y = (radius) * Math.sin(theta);
            
            points[nextIndex] = x; ++nextIndex;
            points[nextIndex] = y; ++nextIndex;
        }
        
        for (int t = -amplitude; t <= amplitude; ++t)
        {
            double theta = inc * t;
            double x = (radius+thickness) * Math.cos(theta);
            double y = (radius+thickness) * Math.sin(theta);
            
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