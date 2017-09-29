/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.guihouse.projector.forms.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import us.guihouse.projector.other.ResizeableSwingNode;
import us.guihouse.projector.projection.ProjectionManager;
import us.guihouse.projector.projection.ProjectionPlayer;
import us.guihouse.projector.services.FileDragDropService;

/**
 * FXML Controller class
 *
 * @author guilherme
 */
public class PlayerController extends ProjectionController {
    private FileDragDropService service;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        endProjectionButton.disableProperty().set(true);
    }

    private ProjectionPlayer projectionPlayer;
    
    // Drag and drop
    @FXML
    private Label dragDropLabel;
    
    @FXML
    private VBox chooseFileBox;
    
    @FXML
    private VBox playerBox;
    
    private String oldLabelText;
    
    // Controls
    @FXML
    private Button beginProjectionButton;

    @FXML
    private Button endProjectionButton;
    
    @FXML
    private Group playerGroup;

    @FXML
    private Pane playerContainer;

    // Player controls
    @FXML
    private Button beginButton;

    @FXML
    private Button playButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;

    @FXML
    private Slider timeBar;

    @FXML
    private Label timeLabel;

    @FXML
    private ToggleButton withoutSoundButton;

    @FXML
    private ToggleButton withSoundButton;

    private SwingNode node;

    @FXML
    public void onBeginProjection() {
        beginProjectionButton.disableProperty().set(true);
        endProjectionButton.disableProperty().set(false);
        getProjectionManager().setProjectable(projectionPlayer);
    }

    @FXML
    public void onEndProjection() {
        beginProjectionButton.disableProperty().set(false);
        endProjectionButton.disableProperty().set(true);
        getProjectionManager().setProjectable(null);
    }

    @Override
    public void initWithProjectionManager(ProjectionManager projectionManager) {
        super.initWithProjectionManager(projectionManager);
        this.oldLabelText = dragDropLabel.getText();
        this.projectionPlayer = projectionManager.createPlayer();

        playerGroup.setAutoSizeChildren(false);

        node = new ResizeableSwingNode();
        node.setContent(projectionPlayer.getPreviewPanel());
        playerGroup.getChildren().add(node);

        ChangeListener listener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                node.resize(playerContainer.getWidth(), playerContainer.getHeight());
                projectionPlayer.setPreviewPanelSize(playerContainer.getWidth(), playerContainer.getHeight());
            }
        };

        playerContainer.widthProperty().addListener(listener);
        playerContainer.heightProperty().addListener(listener);

        //projectionPlayer.getPlayer().prepareMedia("/Users/guilherme/Desktop/ABERTURA.mp4");
        playerBox.setVisible(false);
        chooseFileBox.setVisible(true);
    }

    @Override
    public void onEscapeKeyPressed() {
        if (!endProjectionButton.isDisabled()) {
            endProjectionButton.fire();
        }
    }

    @FXML
    public void beginButtonClick() {
        projectionPlayer.getPlayer().setPosition(0);
    }

    @FXML
    public void playButtonClick() {
        projectionPlayer.getPlayer().play();
    }

    @FXML
    public void pauseButtonClick() {
        projectionPlayer.getPlayer().pause();
    }

    @FXML
    public void stopButtonClick() {
        projectionPlayer.getPlayer().stop();
    }

    @FXML
    public void withoutSoundButtonClick() {

    }

    @FXML
    public void withSoundButtonClick() {

    }
    
    // Drag and drop
    @FXML
    public void onDragOver(DragEvent event) {
        service.onDragOver(event);
    }
    
    @FXML
    public void onDragExit() {
        setOriginal();
        dragDropLabel.setVisible(false);        
        service.onDragExit();
    }
    
    @FXML
    public void onDragDropped(DragEvent event) {
        service.onDragDropped(event);
    }
        
    private void setError(String error) {
        dragDropLabel.setVisible(true);
        dragDropLabel.setText(error);
        chooseFileBox.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
    }
    
    private void setOriginal() {
        dragDropLabel.setText(oldLabelText);
        chooseFileBox.setBorder(null);
    }
}
