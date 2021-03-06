import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.animation.AnimationTimer;

public class SideBar extends VBox
{
    private static final double MAX_VOLTAGE = 500;
    
    private DoubleProperty voltage;
    private boolean alternating;
    
    private HBox acdcSwitch;
    
    private HBox dcControls;
    private TextField dcVoltage;
    
    private HBox acControls;
    private TextField acVoltage;
    private double acAmplitude;
    private double acFrequency;
    private double phaseProgress;
    private IntegerProperty acChanges;
    
    private AnimationTimer altTimer;
    
    private Voltmeter meter;
    private SineDiagram sine;
    
    public SideBar()
    {
        super(10);
        
        voltage = new SimpleDoubleProperty(0);
        
        acChanges = new SimpleIntegerProperty(0);
        
        createACDCSwitch();
        getChildren().add(acdcSwitch);
        
        createDCControls();
        getChildren().add(dcControls);
        
        createACControls();
        
        meter = new Voltmeter(50,160);
        getChildren().add(meter);
        
        sine = new SineDiagram(150,240,1.5);
        getChildren().add(sine);
    }
    
    private void createACDCSwitch()
    {
        acdcSwitch = new HBox(20);
        ToggleGroup acdcGroup = new ToggleGroup();
        
        RadioButton acButton = new RadioButton("AC");
        RadioButton dcButton = new RadioButton("DC");
        
        dcButton.fire();
        
        acButton.setOnAction((e) ->
            {
                setAlternating(true);
            });
        
        dcButton.setOnAction((e) ->
            {
                setAlternating(false);
            });
        
        acButton.setToggleGroup(acdcGroup);
        dcButton.setToggleGroup(acdcGroup);
        
        acdcSwitch.getChildren().add(acButton);
        acdcSwitch.getChildren().add(dcButton);
    }
    
    private void createDCControls()
    {
        dcControls = new HBox(20);
        
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
                    voltage.setValue(newVoltage / MAX_VOLTAGE);
                    meter.setVoltage(voltage.getValue());
                    sine.setLine(voltage.getValue());
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
        
        dcControls.getChildren().add(leftBox);
        dcControls.getChildren().add(flipButton);
        
        dcVoltage = voltageInput;
    }
    
    private void createACControls()
    {
        acControls = new HBox(20);
        
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
                    beginAlternating(newVoltage / MAX_VOLTAGE, newFrequency);
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
                    beginAlternating(newVoltage / MAX_VOLTAGE, newFrequency);
                }
            });
        
        acControls.getChildren().add(leftBox);
        acControls.getChildren().add(rightBox);
        
        acVoltage = voltageInput;
        
        altTimer = new AnimationTimer()
            {
                public void handle(long now)
                {
                    voltage.setValue(Math.sin(phaseProgress) * acAmplitude);
                    sine.addPoint(voltage.getValue());
                    meter.setVoltage(voltage.getValue());
                    phaseProgress += (2*Math.PI) * acFrequency / 60;
                    while (phaseProgress > 2*Math.PI)
                        phaseProgress -= 2*Math.PI;
                    //System.out.print((Math.sin(phaseProgress) * acAmplitude) + "/");
                }
            };
        
        acAmplitude = 0;
        acFrequency = 0;
    }
    
    private void setAlternating(boolean alt)
    {
        alternating = alt;
        
        getChildren().clear();
        getChildren().add(acdcSwitch);
        
        if (alt)
        {
            getChildren().add(acControls);
            acVoltage.setText("" + (voltage.getValue() * MAX_VOLTAGE));
            beginAlternating(acAmplitude, acFrequency);
        }
        else
        {
            getChildren().add(dcControls);
            dcVoltage.setText(acVoltage.getText());
            altTimer.stop();
            sine.setLine(voltage.getValue());
            meter.setVoltage(voltage.getValue());
        }
        acChange();
        getChildren().add(meter);
        getChildren().add(sine);
    }
    
    private void beginAlternating(double amplitude, double frequency)
    {
        altTimer.stop();
        acAmplitude = amplitude;
        acFrequency = frequency;
        phaseProgress = 0;
        voltage.setValue(0);
        altTimer.start();
        //System.out.printf("AMP=%f, FREQ=%f\n", acAmplitude, acFrequency);
        acChange();
    }
    
    public DoubleProperty getVoltage()
    {
        return voltage;
    }
    
    public boolean isAlternating()
    {
        return alternating;
    }
    
    private void acChange()
    {
        acChanges.setValue(acChanges.getValue()+1);
    }
    
    public IntegerProperty getACChanges()
    {
        return acChanges;
    }
    
    public double getAmplitude()
    {
        return acAmplitude;
    }
    
    public double getFrequency()
    {
        return acFrequency;
    }
}