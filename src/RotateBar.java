import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.beans.property.DoubleProperty;

import javafx.beans.property.IntegerProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.animation.AnimationTimer;

public class RotateBar extends VBox
{
    private GeneralDiagram diagram;
    
    private ModeSelector select;
    private RotateGenerator rot;
    private ACGenerator ac;
    
    private Voltmeter vm;
    private SineDiagram sd;
    
    private MultiSine ms;
    
    private AnimationTimer clock;
    
    public RotateBar(GeneralDiagram diagramIn)
    {
        super(10);
        
        diagram = diagramIn;
        
        select = new ModeSelector("Off", "On");
        rot = new RotateGenerator();
        ac = new ACGenerator();
        
        vm = new Voltmeter(50,160);
        sd = new SineDiagram(150,240,3);
        
        ms = new MultiSine(150,240,3,2);
        
        select.getSetting().addListener(new ChangeListener<Number>()
            {
                public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                {
                    updateDiagram();
                    updateBar();
                }
            });
        
        rot.getPhase().addListener(new ChangeListener<Number>()
            {
                public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                {
                    if (select.getSetting().getValue() == 1)
                        updateDiagram();
                }
            });
        
        ac.getVoltage().addListener(new ChangeListener<Number>()
            {
                public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                {
                    if (select.getSetting().getValue() == 0)
                        updateDiagram();
                }
            });
        
        clock = new AnimationTimer()
            {
                public void handle(long now)
                {
                    if (select.getSetting().getValue() == 0)
                        sd.addPoint(ac.getVoltage().getValue());
                    else
                    {
                        double p1 = rot.getAmplitude().getValue() * Math.cos(rot.getPhase().getValue());
                        double p2 = rot.getAmplitude().getValue() * Math.cos(rot.getPhase().getValue() - Math.PI / 2);
                        
                        ms.addPoint(p1,0);
                        ms.addPoint(p2,1);
                    }
                }
            };
        
        updateDiagram();
        updateBar();
        clock.start();
    }
    
    private void updateDiagram()
    {
        if (select.getSetting().getValue() == 0)
        {
            diagram.setVoltageClamped(ac.getVoltage().getValue(),
                                      ac.getDirection().getValue(),
                                      ac.getAmplitude().getValue());
            vm.setVoltage(ac.getVoltage().getValue());
        }
        else
        {
            diagram.setVoltageFollow(rot.getVoltage().getValue(),
                                     rot.getDirection().getValue());
            
        }
        
    }
    
    private void updateBar()
    {
        getChildren().clear();
        getChildren().add(select);
        if (select.getSetting().getValue() == 0)
        {
            getChildren().add(ac);
            getChildren().add(vm);
            getChildren().add(sd);
        }
        else
        {
            getChildren().add(rot);
            getChildren().add(ms);
        }
        
    }
}