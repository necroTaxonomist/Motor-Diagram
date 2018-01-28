import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public interface Generator
{
    DoubleProperty getVoltage();
    DoubleProperty getDirection();
    
    DoubleProperty getAmplitude();
    DoubleProperty getPhase();
}