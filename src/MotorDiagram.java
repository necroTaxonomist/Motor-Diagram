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
    
    private GeneralDiagram diagram;
    private GeneralBar sideBar;
    
    private GeneralDiagram fourCore;
    private RotateBar fourBar;
    
    private GeneralDiagram stator;
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
        
        // 2 CORE
        
        BorderPane normalPane = new BorderPane();
        Tab normalTab = new Tab("2 Core", normalPane);
        normalTab.setClosable(false);
        mainPane.getTabs().add(normalTab);
        
        diagram = new CoresDiagram(2, 90, 45, Math.PI / 3, 112.5, 300, 150);
        sideBar = new GeneralBar(diagram);
        
        normalPane.setCenter(diagram);
        normalPane.setLeft(sideBar);
        
        // 4 CORE
        BorderPane fourPane = new BorderPane();
        Tab fourTab = new Tab("4 Core", fourPane);
        fourTab.setClosable(false);
        mainPane.getTabs().add(fourTab);
        
        fourCore = new CapacitorDiagram(75, 35, Math.PI / 15, 50, 300, 200);
        fourBar = new RotateBar(fourCore);
        
        fourPane.setCenter(fourCore);
        fourPane.setLeft(fourBar);
        
        // STATOR
        BorderPane statorPane = new BorderPane();
        Tab statorTab = new Tab("Stator", statorPane);
        statorTab.setClosable(false);
        mainPane.getTabs().add(statorTab);
        
        stator = new StatorDiagram(90, 11.25, 55, 300, 200);
        statorBar = new StatorBar(stator);
        
        statorPane.setCenter(stator);
        statorPane.setLeft(statorBar);
        
        
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
        fourCore.getTransforms().add(windowScale);
        stator.getTransforms().add(windowScale);
        
        normalPane.setMargin(sideBar, new Insets(20,20,20,25));
        normalPane.setMargin(fourBar, new Insets(20,20,20,25));
        normalPane.setMargin(statorBar, new Insets(20,20,20,25));
        
        primaryStage.show();
    }
}