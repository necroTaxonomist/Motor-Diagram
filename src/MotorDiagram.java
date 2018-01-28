import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.geometry.Side;
import javafx.geometry.Insets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.transform.Scale;

public class MotorDiagram extends Application
{
    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 400;
    private Scale windowScale;
    
    private Diagram diagram;
    private StatorDiagram sDiagram;
    private SideBar sideBar;
    private StatorBar statorBar;
    
    public static void main(String[] args)
    {
        launch(args);
    }
    
    public void start(Stage primaryStage)
    {
        windowScale = new Scale();

        TabPane mainPane = new TabPane();
        mainPane.setSide(Side.RIGHT);
        
        BorderPane normalPane = new BorderPane();
        BorderPane statorPane = new BorderPane();
        
        Tab normalTab = new Tab("2 Core", normalPane);
        normalTab.setClosable(false);
        
        Tab statorTab = new Tab("Stator", statorPane);
        statorTab.setClosable(false);
        
        mainPane.getTabs().add(normalTab);
        mainPane.getTabs().add(statorTab);
        
        diagram = new Diagram(90, 45, 120, 112.5, 300, 150);
        sDiagram = new StatorDiagram(90, 11.25, 55, 300, 200);
        sideBar = new SideBar();
        statorBar = new  StatorBar();
        
        sideBar.getVoltage().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
            {
                updateCoreVoltages(newVal.doubleValue());
                if (!sideBar.isAlternating())
                    updateSpinnerDC(newVal.doubleValue());
            }
        });
        sideBar.getACChanges().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
            {
                updateCoreVoltages(sideBar.getVoltage().getValue());
                if (sideBar.isAlternating())
                    updateSpinnerAC(sideBar.getAmplitude(), sideBar.getFrequency());
                else
                    updateSpinnerDC(sideBar.getAmplitude());
            }
        });
        
        statorBar.getVoltage().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
            {
                updateStatorVoltages(newVal.doubleValue());
            }
        });
        statorBar.getACChanges().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
            {
                updateStatorVoltages(statorBar.getVoltage().getValue());
                updateStatorAC(statorBar.getAmplitude(), statorBar.getFrequency());
            }
        });
        
        normalPane.setCenter(diagram);
        normalPane.setLeft(sideBar);
        statorPane.setCenter(sDiagram);
        statorPane.setLeft(statorBar);
        //mainPane.setBottom(grandDad);
        
        Scene scene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        
        scene.widthProperty().addListener(new ChangeListener<Number>()
            {
                public void changed(ObservableValue<? extends Number> observableValue,
                                    Number oldSceneWidth,
                                    Number newSceneWidth)
                {
                    windowScale.setX(newSceneWidth.doubleValue() / WINDOW_WIDTH);
                    windowScale.setY(newSceneWidth.doubleValue() / WINDOW_WIDTH);
                }
            });
            
        diagram.getTransforms().add(windowScale);
        sDiagram.getTransforms().add(windowScale);
        
        updateCoreVoltages(0);
        updateSpinnerDC(0);
        
        updateStatorVoltages(-1);
        
        normalPane.setMargin(sideBar, new Insets(20,20,20,25));
        statorPane.setMargin(statorBar, new Insets(20,20,20,25));
        
        primaryStage.show();
    }
    
    public void updateCoreVoltages(double voltage)
    {
        diagram.setCoreVoltages(voltage);
    }
    
    public void updateStatorVoltages(double voltage)
    {
        sDiagram.setToothVoltages(voltage);
        sDiagram.setAmplitude(statorBar.getAmplitude());
    }
    
    public void updateSpinnerDC(double voltage)
    {
        diagram.setSpinnerDC(voltage);
    }
    
    public void updateSpinnerAC(double voltage, double frequency)
    {
        diagram.setSpinnerAC(voltage, frequency);
    }
    
    public void updateStatorAC(double voltage, double frequency)
    {
        sDiagram.setSpinnerAC(voltage, frequency);
    }
}