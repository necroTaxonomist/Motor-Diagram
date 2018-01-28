import javafx.scene.layout.HBox;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class DCGenerator extends HBox implements Generator
{
    private static double MAX_VOLTAGE = 500;
    private static double NORMAL_VOLTAGE = 480;
    
    private double intVoltage;
    private DoubleProperty extVoltage;
    private DoubleProperty direction;
    
    public DCGenerator()
    {
        super(20);
        
        intVoltage = 0;
        extVoltage = new SimpleDoubleProperty(0);
        direction = new SimpleDoubleProperty(0);
    
        HBox leftBox = new HBox();
            
        TextField voltageInput = new TextField("0");
        Label volts = new Label("V");
            
        voltageInput.setPrefColumnCount(4);
            
        leftBox.getChildren().add(voltageInput);
        leftBox.getChildren().add(volts);
        
        voltageInput.textProperty().addListener(new ChangeListener<String>()
            {
                public void changed(ObservableValue<? extends String> ov, String oldVal, String newVal)
                {
                    double newVoltage;
                    try
                    {
                        newVoltage = Double.parseDouble(newVal);
                        if (newVoltage > MAX_VOLTAGE)
                            newVoltage = MAX_VOLTAGE;
                        else if (newVoltage < -MAX_VOLTAGE)
                            newVoltage = -MAX_VOLTAGE;
                    }
                    catch (Exception ex)
                    {
                        return;
                    }
                    
                    intVoltage = newVoltage;
                    extVoltage.setValue(newVoltage / NORMAL_VOLTAGE);
                }
            });
        
        Button flipButton = new Button("Flip");
        
        flipButton.setOnAction((e) ->
            {
                String txt = voltageInput.getText();
                if (txt.charAt(0) == '-')
                    voltageInput.setText(txt.substring(1));
                else
                    voltageInput.setText("-" + txt);
            });
        
        getChildren().add(leftBox);
        getChildren().add(flipButton);
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
        return extVoltage;
    }
    
    public DoubleProperty getPhase()
    {
        return null;
    }
}