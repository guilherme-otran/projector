/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.guihouse.projector.projection;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import org.jetbrains.annotations.NotNull;
import us.guihouse.projector.projection.glfw.GLFWPreviewWindowCallback;
import us.guihouse.projector.projection.models.VirtualScreen;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;


/**
 *
 * @author guilherme
 */
public class PreviewImageView extends ImageView implements GLFWPreviewWindowCallback {
    private WritableImage fxTargetRender;
    private BufferedImage previewImage;
    private boolean rendering = false;

    public PreviewImageView() {
        setPreserveRatio(true);
    }

    @Override
    public void create(int width, int height) {
        Platform.runLater(() -> {
            if (fxTargetRender == null || fxTargetRender.getWidth() != width || fxTargetRender.getHeight() != height) {
                rendering = false;
                fxTargetRender = new WritableImage(width, height);
                previewImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                setImage(fxTargetRender);
            }
        });
    }

    @Override
    public void onDisplay(@NotNull byte[] data) {
        if (rendering) {
            return;
        }

        rendering = true;

        Platform.runLater(() -> {
            if (previewImage != null) {
                int width = previewImage.getWidth();
                int height = previewImage.getHeight();

                int[] imgData = ((DataBufferInt)previewImage.getRaster().getDataBuffer()).getData();

                if (width * height * 3 == data.length) {
                    for (int y = 0; y < height; y++) {
                        for (int x = 0; x < width; x++) {
                            int i = (((height - y - 1) * width) + x) * 3;
                            int imgI = (y * width) + x;

                            byte r = data[i];
                            byte g = data[i+1];
                            byte b = data[i+2];

                            imgData[imgI] = convertColor(r, g, b);
                        }
                    }
                }

                SwingFXUtils.toFXImage(previewImage, fxTargetRender);
            }

            rendering = false;
        });
    }

    private int convertColor(byte r, byte g, byte b) {
        return 0xFF << 24 | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }
}
