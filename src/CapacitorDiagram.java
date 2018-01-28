import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class CapacitorDiagram extends CoresDiagram
{
    public CapacitorDiagram(double radius,
                            double thickness,
                            double angleBetween,
                            double length,
                            double x,
                            double y)
    {
        super(4,
              radius,
              thickness,
              angleBetween,
              length,
              x,
              y);
              
        double topX = x + radius + thickness / 2;
        double topY = y - radius - thickness - 3*length/4;
        
        double bottomX = x + radius + thickness + 3*length/4;
        double bottomY = y + radius / 2 + thickness / 2;
        
        double strokeWidth = 2.25;
        
        Circle c = new Circle(topX, topY, 2.5);
        Circle d = new Circle(bottomX, bottomY, 2.5);
        getChildren().add(c);
        getChildren().add(d);
        
        Line l = new Line(bottomX, bottomY, bottomX + radius, bottomY);
        l.setStrokeWidth(strokeWidth);
        getChildren().add(l);
        
        double capX = bottomX + radius;
        double capY =  bottomY - 3*radius/2;
        
        Line m = new Line(capX, bottomY, capX, capY);
        m.setStrokeWidth(strokeWidth);
        getChildren().add(m);
        
        Line cap1 = new Line(capX - thickness / 2, capY, capX + thickness / 2, capY);
        cap1.setStrokeWidth(strokeWidth);
        getChildren().add(cap1);
        
        Line cap2 = new Line(capX - thickness / 2, capY - thickness / 2, capX + thickness / 2, capY - thickness / 2);
        cap2.setStrokeWidth(strokeWidth);
        getChildren().add(cap2);
        
        Line n = new Line(capX, capY - thickness / 2, capX, topY);
        n.setStrokeWidth(strokeWidth);
        getChildren().add(n);
        
        Line o = new Line(capX, topY, topX, topY);
        o.setStrokeWidth(strokeWidth);
        getChildren().add(o);
    }
}