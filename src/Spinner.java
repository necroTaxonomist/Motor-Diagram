import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.scene.transform.Rotate;
import javafx.scene.input.MouseEvent;

public class Spinner extends Pane
{
    private static final double FRICTION = 0;
    private static final double MAX_ACC = 5 + FRICTION;
    
    private double theta;
    private double omega;
    private double voltage;
    
    private long prevTime;
    
    private AnimationTimer anim;
    private Rotate rot;
    
    private int grab;
    
    private double x;
    private double y;
    
    public Spinner(double x, double y, double length, double width)
    {
        this.x = x;
        this.y = y;
        
        Rectangle outline = new Rectangle(x - length/2 - 1.25, y - width/2 - 1.25, length + 2.5, width + 2.5);
        outline.setFill(Color.BLACK);
        
        Rectangle middle = new Rectangle(x - length/2, y - width/2, length, width);
        middle.setFill(new Color(.85,.85,.85,1.0));
        
        Rectangle northEnd = new Rectangle(x + length/6, y - width/2, length/3, width);
        northEnd.setFill(new Color(1.0,.65,.65,1.0));
        
        Rectangle southEnd = new Rectangle(x - length/2, y - width/2, length/3, width);
        southEnd.setFill(new Color(.65,.65,1.0,1.0));
        
        northEnd.setOnMousePressed((e) ->
            {
                grab = 1;
            });
        
        southEnd.setOnMousePressed((e) ->
            {
                grab = -1;
            });
        
        setOnMouseReleased((e) ->
            {
                if (grab != 0)
                    grab = 0;
            });
        
        rot = new Rotate(-90,x,y);
        anim = new AnimationTimer()
            {
                public void handle(long now)
                {
                    step(now);
                }
            };
        
        theta = Math.PI/2;
        omega = 0;
        voltage = 0;
        grab = 0;
        
        getChildren().add(outline);
        getChildren().add(middle);
        getChildren().add(northEnd);
        getChildren().add(southEnd);
        
        getTransforms().add(rot);
        anim.start();
    }
    
    private void step(long now)
    {
        double dt;
        if (now < prevTime)
        {
            prevTime = now;
            System.out.println("skipped");
            return;
        }
        else
        {
            dt = (now - prevTime) / 1000000000.0;
            prevTime = now;
        }
        
        if (grab == 0)
        {
            double driving = -MAX_ACC * Math.sin(theta) * voltage;
            double friction;
            if (omega != 0)
                friction = omega < 0 ? FRICTION : -FRICTION;
            else
                friction = 0;
            
            double alpha = driving + friction;
            
            if (omega != 0 && omega + driving*dt > 0 != omega + driving*dt + friction*dt > 0)
            {
                theta += omega*dt - omega*dt*dt/2;
                omega = 0;
            }
            else
            {
                theta += omega*dt + alpha*dt*dt/2;
                omega += alpha*dt;
            }
        }
        else
        {
            if (omega != 0)
                omega = 0;
        }
        
        rot.setAngle(-180 * theta / Math.PI);
    }
    
    private void getGrabbed(MouseEvent e)
    {
        double dx = e.getX() - x;
        double dy = e.getY() - y;
        theta = Math.atan2(-dy*grab,dx*grab);
    }
    
    public void updateVoltage(double newVoltage)
    {
        voltage = newVoltage;
    }
    
    public void acknowledgeParent()
    {
        if (getParent() != null)
        {
            getParent().setOnMouseDragged((e) ->
            {
                if (grab != 0)
                    getGrabbed(e);
            });
        }
    }
}