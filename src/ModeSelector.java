import javafx.scene.layout.HBox;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ModeSelector extends HBox
{
    private IntegerProperty setting;
    
    public ModeSelector(String label1, String label2)
    {
        super(20);
        
        setting = new SimpleIntegerProperty(1);
        
        ToggleGroup acdcGroup = new ToggleGroup();
        
        RadioButton acButton = new RadioButton(label1);
        RadioButton dcButton = new RadioButton(label2);
        
        dcButton.fire();
        
        acButton.setOnAction((e) ->
            {
                setting.setValue(0);
            });
        
        dcButton.setOnAction((e) ->
            {
                setting.setValue(1);
            });
        
        acButton.setToggleGroup(acdcGroup);
        dcButton.setToggleGroup(acdcGroup);
        
        getChildren().add(acButton);
        getChildren().add(dcButton);
    }
    
    public IntegerProperty getSetting()
    {
        return setting;
    }
}