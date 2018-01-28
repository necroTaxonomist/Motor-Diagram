import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class RotatableCore extends Pane
{
    private Polygon mainShape;
    
    private Line[] coil;
    
    public RotatableCore(double radius,
                         double thickness,
                         double angle,
                         double length,
                         double rotation,
                         double x,
                         double y)
    {
        mainShape = createPoints(radius, thickness, angle, length, rotation, x, y);
        mainShape.setStroke(Color.BLACK);
        getChildren().add(mainShape);
        
        coil = createCoil(radius, thickness, angle, 3*length/4, rotation, x, y, 5);
        
        for (int i = 0; i < coil.length; ++i)
            getChildren().add(coil[i]);
    }
    
    public void updateColor(double voltage)
    {
        if (voltage > 1)
            voltage = 1;
        else if (voltage < -1)
            voltage = -1;
        
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
                                        double rotation,
                                        double xDisp,
                                        double yDisp)
    {
        System.out.println("PART 0");
        
        double inc = Math.asin((thickness/2) / (radius+thickness));
        int amplitude = (int)Math.floor(angle/2/inc);
        int numPoints = 4 * amplitude + 3;
        
        System.out.println("amplitude=" + amplitude);
        System.out.println("numPoints=" + numPoints);
        
        double[] points = new double[numPoints*2];
        
        int nextIndex= 0;
        
        System.out.println("PART 1");
        
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
        
        System.out.println("PART 2");
        
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
        
        System.out.println("PART 3");
        
        for (int t = -amplitude; t <= amplitude; ++t)
        {
            double theta = inc * t;
            double x = (radius) * Math.cos(theta);
            double y = (radius) * Math.sin(theta);
            
            points[nextIndex] = x; ++nextIndex;
            points[nextIndex] = y; ++nextIndex;
        }
        
        System.out.println("PART 4");
        
        for (int i = 0; i < points.length; i += 2)
        {
            double oldX = points[i];
            double oldY = points[i+1];
            
            double newX = oldX * Math.cos(rotation) - oldY * Math.sin(rotation) + xDisp;
            double newY = oldX * Math.sin(rotation) + oldY * Math.cos(rotation) + yDisp;
            
            points[i] = newX;
            points[i+1] = newY;
        }
        
        return new Polygon(points);
    }
    
    private static Line[] createCoil(double radius,
                                     double thickness,
                                     double angle,
                                     double length,
                                     double rotation,
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
            
            lines[i] = new Line(x1,
                                y1,
                                x2,
                                y2);
            
            lines[i].setStrokeWidth(2.0);
        }
        
        double x = radius + thickness + length;
        
        lines[number] = new Line(x,
                                 y1,
                                 x,
                                 y1 + radius);
        
        lines[number].setStrokeWidth(2.0);
        
        double mirror = 1;
        if (rotation == -Math.PI)
            mirror = -1;
        
        for (int i = 0; i < lines.length; ++i)
        {
            double oldX = lines[i].getStartX();
            double oldY = lines[i].getStartY() * mirror;
            
            double newX = oldX * Math.cos(rotation) - oldY * Math.sin(rotation) + xDisp;
            double newY = oldX * Math.sin(rotation) + oldY * Math.cos(rotation) + yDisp;
            
            lines[i].setStartX(newX);
            lines[i].setStartY(newY);
            
            oldX = lines[i].getEndX();
            oldY = lines[i].getEndY() * mirror;
            
            newX = oldX * Math.cos(rotation) - oldY * Math.sin(rotation) + xDisp;
            newY = oldX * Math.sin(rotation) + oldY * Math.cos(rotation) + yDisp;
            
            lines[i].setEndX(newX);
            lines[i].setEndY(newY);
        }
        
        return lines;
    }
}