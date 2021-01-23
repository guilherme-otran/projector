package us.guihouse.projector.models;

import java.awt.*;
import java.util.List;
import lombok.Data;

@Data
public class WindowConfig {

    private String displayId;
    private String virtualScreenId;
    private Rectangle displayBounds;
    private boolean project;

    private int x;
    private int y;
    private int width;
    private int height;

    private int bgFillX;
    private int bgFillY;
    private int bgFillWidth;
    private int bgFillHeight;

    private List<WindowConfigHelpLine> helpLines;

    private List<WindowConfigBlend> blends;

    private WindowConfigBlackLevelAdjust blackLevelAdjust;

    private double scaleX;
    private double scaleY;

    private double shearX;
    private double shearY;

    private double rotate;
}
