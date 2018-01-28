import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.scene.transform.Rotate;

public class Voltmeter extends Pane
{
    private static final double NEEDLE_LENGTH = .9;
    private static final double NEEDLE_WIDTH = .02;
    private static final double FULL_ANGLE = Math.PI/2;
    private static final int RESOLUTION = 10;
    private static final double NEEDLE_SPEED = 5;
    private static final double NEEDLE_RANGE = .9;
    
    private Polygon bg;
    private Polygon needle;
    
    private AnimationTimer anim;
    private Rotate rot;
    
    private double currentAngle;
    private double targetAngle;
    
    public Voltmeter(double r1, double r2)
    {
        double xDisp = r2 * Math.sin(FULL_ANGLE / 2);
        double yDisp = r2;
        
        bg = createBG(r1, r2, xDisp, yDisp);
        needle = createNeedle(r1, r2, xDisp, yDisp);
        
        getChildren().add(bg);
        getChildren().add(needle);
        
        anim = new AnimationTimer()
            {
                public void handle(long now)
                {
                    step();
                }
            };
        anim.start();
        
        currentAngle = 0;
        targetAngle = 0;
        
        rot = new Rotate(0,xDisp,yDisp);
        
        needle.getTransforms().add(rot);
    }
    
    private static Polygon createBG(double r1, double r2, double xDisp, double yDisp)
    {
        double startAngle = Math.PI/2 - FULL_ANGLE/2;
        double endAngle = Math.PI - startAngle;
        double increment = FULL_ANGLE / RESOLUTION;
        
        double[] points = new double[4 * (RESOLUTION + 1)];
        
        int nextIndex = 0;
        
        for (double angle = startAngle; angle <= endAngle; angle += increment)
        {
            double x = r1 * Math.cos(angle) + xDisp;
            double y = yDisp - r1 * Math.sin(angle);
            
            points[nextIndex] = x; ++nextIndex;
            points[nextIndex] = y; ++nextIndex;
        }
        
        for (double angle = endAngle; angle >= startAngle; angle -= increment)
        {
            double x = r2 * Math.cos(angle) + xDisp;
            double y = yDisp - r2 * Math.sin(angle);
            
            points[nextIndex] = x; ++nextIndex;
            points[nextIndex] = y; ++nextIndex;
        }
        
        Polygon p = new Polygon(points);
        p.setFill(Color.WHITE);
        p.setStroke(Color.GRAY);
        
        return p;
    }
    
    private static Polygon createNeedle(double r1, double r2, double xDisp, double yDisp)
    {
        double[] points = new double[6];
        
        points[0] = r2 * NEEDLE_WIDTH / 2 + xDisp;
        points[1] = yDisp - r1;
        
        points[2] = r2 * NEEDLE_WIDTH / -2 + xDisp;
        points[3] = yDisp - r1;
        
        points[4] = xDisp;
        points[5] = -NEEDLE_LENGTH * (r2 - r1) - r1 + yDisp;
        
        Polygon p = new Polygon(points);
        p.setFill(Color.RED);
        p.setStroke(Color.GRAY);
        
        return p;
    }
    
    private void step()
    {
        if (Math.abs(targetAngle - currentAngle) <= NEEDLE_SPEED)
            currentAngle = targetAngle;
        else
        {
            double direction = Math.signum(targetAngle - currentAngle);
            currentAngle += direction * NEEDLE_SPEED;
        }
        
        //System.out.println("ANGLE = " + currentAngle);
        
        rot.setAngle(currentAngle);
    }
    
    public void setVoltage(double voltage)
    {
        targetAngle = voltage * (180 * FULL_ANGLE / Math.PI) / 2 * NEEDLE_RANGE;
    }
}