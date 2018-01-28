import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.scene.transform.Scale;

public class MotorDiagram extends Application
{
    public static final int WINDOW_WIDTH = 600;
    public static final int WINDOW_HEIGHT = 400;
    private Scale windowScale;
    
    private Diagram diagram;
    private SideBar sideBar;
    
    public static void main(String[] args)
    {
        launch(args);
    }
    
    public void start(Stage primaryStage)
    {
        windowScale = new Scale();

        BorderPane mainPane = new BorderPane();
        
        diagram = new Diagram();
        sideBar = new SideBar();
        
        sideBar.getVoltage().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
            {
                updateVoltage(newVal.doubleValue());
            }
        });
        
        mainPane.setCenter(diagram);
        mainPane.setLeft(sideBar);
        
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
        
        updateVoltage(0);
        
        primaryStage.show();
    }
    
    public void updateVoltage(double voltage)
    {
        diagram.updateVoltage(voltage);
    }
}