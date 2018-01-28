import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.beans.property.DoubleProperty;

import javafx.beans.property.IntegerProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.animation.AnimationTimer;

public class StatorBar extends VBox
{
    private GeneralDiagram diagram;
    
    private RotateGenerator rot;
    
    private MultiSine ms;
    
    private AnimationTimer clock;
    
    public StatorBar(GeneralDiagram diagramIn)
    {
        super(10);
        
        diagram = diagramIn;
        
        rot = new RotateGenerator();
        
        ms = new MultiSine(150,240,3,3);
        
        rot.getPhase().addListener(new ChangeListener<Number>()
            {
                public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                {
                    updateDiagram();
                }
            });
        
        clock = new AnimationTimer()
            {
                public void handle(long now)
                {
                    double p1 = rot.getAmplitude().getValue() * Math.cos(rot.getPhase().getValue());
                    double p2 = rot.getAmplitude().getValue() * Math.cos(rot.getPhase().getValue() - 2 * Math.PI / 3);
                    double p3 = rot.getAmplitude().getValue() * Math.cos(rot.getPhase().getValue() - 4 * Math.PI / 3);
                    
                    ms.addPoint(p1,0);
                    ms.addPoint(p2,1);
                    ms.addPoint(p3,2);
                }
            };
        
        getChildren().add(rot);
        getChildren().add(ms);
        
        updateDiagram();
        clock.start();
    }
    
    private void updateDiagram()
    {
        diagram.setVoltageFollow(rot.getVoltage().getValue(),
                                 rot.getDirection().getValue());
    }
}