import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class MultiSine extends Pane
{
    private static final double MAX_HEIGHT = .9;
    
    private Rectangle bounds;
    private Polyline[] lines;
    private Line middle;
    
    private double height;
    private double width;
    private double duration;
    
    public MultiSine(double heightIn, double widthIn, double durationIn, int numLines)
    {
        height = heightIn;
        width = widthIn;
        duration = durationIn;
        
        bounds = new Rectangle(width,height);
        bounds.setFill(Color.WHITE);
        bounds.setStroke(Color.GRAY);
        
        getChildren().add(bounds);
        
        middle = new Line(0, height/2, width, height/2);
        middle.setStroke(Color.GRAY);
        
        getChildren().add(middle);
        
        lines = new Polyline[numLines];
        for (int l = 0; l < numLines; ++l)
        {
            double[] points = new double[(int)(2 * duration * 60)];
            
            double widthIncr = width / (duration * 60);
            for (int i = 0; i < duration * 60; ++i)
            {
                points[2*i] = widthIncr * (i+1);
                points[2*i+1] = height/2;
            }
            
            lines[l] = new Polyline(points);
            
            getChildren().add(lines[l]);
            
            lines[l].setStroke(new Color(l % 3 == 0 ? 1.0 : 0,
                                         l % 3 == 1 ? 1.0 : 0,
                                         l % 3 == 2 ? 1.0 : 0,
                                         1));
        }
    }
    
    public void addPoints(double ... values)
    {
        for (int i = 0; i < values.length; ++i)
        {
            addPoint(values[i], i);
        }
    }
    
    public void addPoint(double value, int index)
    {
        double cVal = height / 2 * (1 - value * MAX_HEIGHT);
        
        for(int i = lines[index].getPoints().size()-1; i > 1; i -= 2)
        {
            Double prevVal = lines[index].getPoints().get(i-2);
            lines[index].getPoints().set(i, prevVal);
        }
        
        lines[index].getPoints().set(1, cVal);
    }
    
    public void setLines(double ... values)
    {
        for (int i = 0; i < values.length; ++i)
        {
            setLine(values[i], i);
        }
    }
    
    public void setLine(double value, int index)
    {
        double cVal = height / 2 * (1 - value * MAX_HEIGHT);
        
        for(int i = 1; i < lines[index].getPoints().size(); i += 2)
        {
            lines[index].getPoints().set(i, cVal);
        }
    }
}