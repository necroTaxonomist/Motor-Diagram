import javafx.scene.layout.HBox;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.animation.AnimationTimer;

public class ACGenerator extends HBox implements Generator
{
    private static double MAX_VOLTAGE = 500;
    private static double NORMAL_VOLTAGE = 480;
    private static double FPS = 60;
    private static double CONST_SCALAR = 1;
    
    private double intVoltage;
    private DoubleProperty extVoltage;
    
    private DoubleProperty direction;
    
    private double intAmplitude;
    private DoubleProperty extAmplitude;
    
    private double intPhase;
    private DoubleProperty extPhase;
    
    private double frequency;
    
    private AnimationTimer clock;
    
    public ACGenerator()
    {
        super(20);
        
        intVoltage = 0;
        extVoltage = new SimpleDoubleProperty(intVoltage);
        
        direction = new SimpleDoubleProperty(0);
        
        intAmplitude = 0;
        extAmplitude = new SimpleDoubleProperty(intAmplitude);
        
        intPhase = 0;
        extPhase = new SimpleDoubleProperty(intPhase);
        
        frequency = 0;
        
        clock = new AnimationTimer()
            {
                public void handle(long now)
                {
                    step();
                }
            };
        
    
        HBox leftBox = new HBox();
        
        TextField voltageInput = new TextField("0");
        Label volts = new Label("V");
        
        voltageInput.setPrefColumnCount(3);
        
        leftBox.getChildren().add(voltageInput);
        leftBox.getChildren().add(volts);
        
        HBox rightBox = new HBox();
        
        TextField frequencyInput = new TextField("0");
        Label hertz = new Label("Hz");
        
        frequencyInput.setPrefColumnCount(2);
        
        rightBox.getChildren().add(frequencyInput);
        rightBox.getChildren().add(hertz);
        
        voltageInput.textProperty().addListener(new ChangeListener<String>()
            {
                public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal)
                {
                    double newVoltage, newFrequency;
                    try
                    {
                        newVoltage = Double.parseDouble(newVal);
                        if (newVoltage > MAX_VOLTAGE)
                            newVoltage = MAX_VOLTAGE;
                        else if (newVoltage < -MAX_VOLTAGE)
                            newVoltage = -MAX_VOLTAGE;
                        newFrequency = Double.parseDouble(frequencyInput.getText());
                    }
                    catch (Exception ex)
                    {
                        return;
                    }
                    
                    setParams(newVoltage, newFrequency);
                }
            });
        
        frequencyInput.textProperty().addListener(new ChangeListener<String>()
            {
                public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal)
                {
                    double newVoltage, newFrequency;
                    try
                    {
                        newFrequency = Double.parseDouble(newVal);
                        if (newFrequency <= 0)
                            throw new Exception();
                        newVoltage = Double.parseDouble(voltageInput.getText());
                    }
                    catch (Exception ex)
                    {
                        return;
                    }
                    
                    setParams(newVoltage, newFrequency);
                }
            });
        
        getChildren().add(leftBox);
        getChildren().add(rightBox);
        
        clock.start();
    }
    
    private void setParams(double newAmplitude, double newFrequency)
    {
        intAmplitude = newAmplitude;
        extAmplitude.setValue(intAmplitude / NORMAL_VOLTAGE);
        
        frequency = newFrequency;
        
        intVoltage = 0;
        extVoltage.setValue(intVoltage / NORMAL_VOLTAGE);
        
        intPhase = 0;
        extPhase.setValue(intPhase);
    }
    
    private void step()
    {
        intPhase += 2 * Math.PI * frequency * CONST_SCALAR / FPS;
        if (intPhase >= 2 * Math.PI)
            intPhase -= 2 * Math.PI;
        extPhase.setValue(intPhase);
        
        intVoltage = intAmplitude * Math.sin(intPhase);
        extVoltage.setValue(intVoltage / NORMAL_VOLTAGE);
    }
    
    public DoubleProperty getVoltage()
    {
        return extVoltage;
    }
    
    public DoubleProperty getDirection()
    {
        return direction;
    }
    
    public DoubleProperty getAmplitude()
    {
        return extAmplitude;
    }
    
    public DoubleProperty getPhase()
    {
        return extPhase;
    }
}