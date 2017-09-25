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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Box;
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
    private Button beginProjectionButton;

    @FXML
    private Button endProjectionButton;

    @FXML
    private AnchorPane playerPane;

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

        SwingNode node = new SwingNode();
        node.setContent(projectionPlayer.getPreviewPanel());

        playerPane.getChildren().add(node);

        playerContainer.widthProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                node.resize(playerPane.getWidth(), playerPane.getHeight());
                projectionPlayer.setPreviewPanelSize(playerPane.getWidth(), playerPane.getHeight());
            }
        });

        playerContainer.heightProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                node.resize(playerPane.getWidth(), playerPane.getHeight());
                projectionPlayer.setPreviewPanelSize(playerPane.getWidth(), playerPane.getHeight());
            }
        });



        //projectionPlayer.getPlayer().playMedia("/home/guilherme/projects/twns-demo.ogv");
    }

    @Override
    public void onEscapeKeyPressed() {
        if (!endProjectionButton.isDisabled()) {
            endProjectionButton.fire();
        }
    }

    @FXML
    public void beginButtonClick() {

    }

    @FXML
    public void playButtonClick() {

    }

    @FXML
    public void pauseButtonClick() {

    }

    @FXML
    public void stopButtonClick() {

    }

    @FXML
    public void withoutSoundButtonClick() {

    }

    @FXML
    public void withSoundButtonClick() {

    }
}
