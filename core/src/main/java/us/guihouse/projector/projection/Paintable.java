package us.guihouse.projector.projection;

import us.guihouse.projector.projection.glfw.GLFWGraphicsAdapter;
import us.guihouse.projector.projection.models.VirtualScreen;

import java.awt.*;

public interface Paintable {
    void paintComponent(GLFWGraphicsAdapter g, VirtualScreen vs);
}
