import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Core extends Pane
{
    private int direction;
    
    private Polygon mainShape;
    
    public Core(double radius,
                double thickness,
                double angle,
                double length,
                int dirIn,
                double x,
                double y)
    {
        mainShape = createPoints(radius, thickness, Math.PI * angle / 180, length, -1*dirIn, x, y);
        direction = dirIn / Math.abs(dirIn);
        mainShape.setStroke(Color.BLACK);
        getChildren().add(mainShape);
        
        Line[] coil = createCoil(radius, thickness, Math.PI * angle / 180, 3*length/4, -1*dirIn, x, y, 5);
        
        for (int i = 0; i < coil.length; ++i)
            getChildren().add(coil[i]);
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
        
        mainShape.setFill(c);
    }
    
    private static Polygon createPoints(double radius,
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
        
        return new Polygon(points);
    }
    
    private static Line[] createCoil(double radius,
                                     double thickness,
                                     double angle,
                                     double length,
                                     int dir,
                                     double xDisp,
                                     double yDisp,
                                     int number)
    {
        Line[] lines = new Line[number+1];
        
        double y1 = thickness / 2;
        double y2 = thickness / -2;
        
        for (int i = 0; i < number; ++i)
        {
            double x1 = radius + thickness + i * (length / number);
            double x2 = radius + thickness + (i+1) * (length / number);
            
            lines[i] = new Line(dir * x1 + xDisp,
                                y1 + yDisp,
                                dir * x2 + xDisp,
                                y2 + yDisp);
            
            lines[i].setStrokeWidth(2.0);
        }
        
        double x = radius + thickness + length;
        
        lines[number] = new Line(dir * x + xDisp,
                                   y1 + yDisp,
                                   dir * x + xDisp,
                                   y1 + radius + yDisp);
        
        lines[number].setStrokeWidth(2.0);
        
        return lines;
    }
}