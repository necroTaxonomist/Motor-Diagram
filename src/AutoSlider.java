import javafx.scene.control.Slider;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class AutoSlider extends Slider
{
    public AutoSlider(double min, double max, double val)
    {
        super(min,max,val);
    }
    
    public void setOnChange(OnChangeMethod ocm)
    {
        valueProperty().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val)
            {
                ocm.onChange(new_val.doubleValue());
            }
        });
    }
    
    public static interface OnChangeMethod
    {
        public void onChange(double newValue);
    }
}