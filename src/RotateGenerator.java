import javafx.scene.layout.HBox;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.animation.AnimationTimer;

public class RotateGenerator extends ACGenerator
{
    public RotateGenerator()
    {
        super();
    }
    
    public DoubleProperty getVoltage()
    {
        return getAmplitude();
    }
    
    public DoubleProperty getDirection()
    {
        return getPhase();
    }
}