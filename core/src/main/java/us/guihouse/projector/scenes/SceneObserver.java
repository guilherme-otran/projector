/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.guihouse.projector.scenes;

import us.guihouse.projector.models.ProjectionListItem;

/**
 *
 * @author guilherme
 */
public interface SceneObserver {

    void titleChanged(ProjectionListItem item);
}
