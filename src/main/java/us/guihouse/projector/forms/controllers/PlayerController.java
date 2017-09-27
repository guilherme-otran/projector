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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
import javafx.scene.layout.VBox;
import us.guihouse.projector.other.ResizeableSwingNode;
import us.guihouse.projector.projection.ProjectionManager;
import us.guihouse.projector.projection.ProjectionPlayer;

/**
 * FXML Controller class
 *
 * @author guilherme
 */
public class PlayerController extends ProjectionController {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        endProjectionButton.disableProperty().set(true);
    }

    private ProjectionPlayer projectionPlayer;

    @FXML
    private VBox chooseFileBox;
    
    @FXML
    private VBox playerBox;
    
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
}
