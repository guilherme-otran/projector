/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.guihouse.projector;

import java.net.URL;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import us.guihouse.projector.forms.controllers.SceneManager;
import us.guihouse.projector.forms.controllers.WorkspaceController;
import us.guihouse.projector.other.SQLiteJDBCDriverConnection;

/**
 *
 * @author 15096134
 */
public class Projector extends Application {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        WorkspaceController controller;
        
        primaryStage.setTitle("Projector");
        primaryStage.setMaxWidth(Double.MAX_VALUE);
        primaryStage.setMaxHeight(Double.MAX_VALUE);
        
        SQLiteJDBCDriverConnection.connect();
        SQLiteJDBCDriverConnection.migrate();
        
        URL url = getClass().getClassLoader().getResource("fxml/workspace.fxml");
        
        FXMLLoader loader = new FXMLLoader(url);
        
        Parent workspaceRoot = loader.load();
        controller = loader.getController();
        
        Scene workspaceScene = new Scene(workspaceRoot, 1000, 700);
        
        controller.setSceneManager(new SceneManager() {
            @Override
            public void goToParent(Parent scene) {
                workspaceScene.setRoot(scene);
            }

            @Override
            public void goToWorkspace() {
                workspaceScene.setRoot(workspaceRoot);
            }

            @Override
            public Stage getStage() {
                return primaryStage;
            }

            @Override
            public Window getWindow() {
                return workspaceScene.getWindow();
            }
        });
        
        primaryStage.setScene(workspaceScene);
        
        primaryStage.xProperty().addListener(controller.getPreviewPaneBoundsListener());
        primaryStage.yProperty().addListener(controller.getPreviewPaneBoundsListener());
        primaryStage.widthProperty().addListener(controller.getPreviewPaneBoundsListener());
        primaryStage.heightProperty().addListener(controller.getPreviewPaneBoundsListener());
        
        primaryStage.show();
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (controller != null) {
                    controller.stop();
                }
            }
        });
    }
}
