import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class SineDiagram extends Pane
{
    private static final double MAX_HEIGHT = .9;
    
    private Rectangle bounds;
    private Polyline line;
    private Line middle;
    
    private double height;
    private double width;
    private double duration;
    
    public SineDiagram(double heightIn, double widthIn, double durationIn)
    {
        height = heightIn;
        width = widthIn;
        duration = durationIn;
        
        bounds = new Rectangle(width,height);
        bounds.setFill(Color.WHITE);
        bounds.setStroke(Color.GRAY);
        
        middle = new Line(0, height/2, width, height/2);
        middle.setStroke(Color.GRAY);
        
        double[] points = new double[(int)(2 * duration * 60)];
        
        double widthIncr = width / (duration * 60);
        for (int i = 0; i < duration * 60; ++i)
        {
            points[2*i] = widthIncr * (i+1);
            points[2*i+1] = height/2;
        }
        
        line = new Polyline(points);
        line.setStroke(Color.RED);
        
        getChildren().add(bounds);
        getChildren().add(middle);
        getChildren().add(line);
    }
    
    public void addPoint(double value)
    {
        double cVal = height / 2 * (1 - value * MAX_HEIGHT);
        
        for(int i = line.getPoints().size()-1; i > 1; i -= 2)
        {
            Double prevVal = line.getPoints().get(i-2);
            line.getPoints().set(i, prevVal);
        }
        
        line.getPoints().set(1, cVal);
    }
    
    public void setLine(double value)
    {
        double cVal = height / 2 * (1 - value * MAX_HEIGHT);
        
        for(int i = 1; i < line.getPoints().size(); i += 2)
        {
            line.getPoints().set(i, cVal);
        }
    }
}