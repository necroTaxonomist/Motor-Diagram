import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.beans.property.DoubleProperty;

import javafx.beans.property.IntegerProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.animation.AnimationTimer;

public class GeneralBar extends VBox
{
    private GeneralDiagram diagram;
    
    private ModeSelector select;
    private DCGenerator dc;
    private ACGenerator ac;
    
    private Voltmeter vm;
    private SineDiagram sd;
    
    private AnimationTimer clock;
    
    public GeneralBar(GeneralDiagram diagramIn)
    {
        super(10);
        
        diagram = diagramIn;
        
        select = new ModeSelector("AC", "DC");
        dc = new DCGenerator();
        ac = new ACGenerator();
        
        vm = new Voltmeter(50,160);
        sd = new SineDiagram(150,240,3);
        
        select.getSetting().addListener(new ChangeListener<Number>()
            {
                public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                {
                    updateDiagram();
                    updateBar();
                }
            });
        
        dc.getVoltage().addListener(new ChangeListener<Number>()
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
                    sd.addPoint(activeGenerator().getVoltage().getValue());
                }
            };
        
        getChildren().add(select);
        getChildren().add(dc);
        getChildren().add(vm);
        getChildren().add(sd);
        
        updateDiagram();
        clock.start();
    }
    
    public Generator activeGenerator()
    {
        if (select.getSetting().getValue() == 0)
            return ac;
        else
            return dc;
    }
    
    private void updateDiagram()
    {
        diagram.setVoltageClamped(activeGenerator().getVoltage().getValue(),
                                  activeGenerator().getDirection().getValue(),
                                  activeGenerator().getAmplitude().getValue());
        vm.setVoltage(activeGenerator().getVoltage().getValue());
    }
    
    private void updateBar()
    {
        getChildren().clear();
        getChildren().add(select);
        getChildren().add((HBox)activeGenerator());
        getChildren().add(vm);
        getChildren().add(sd);
    }
}