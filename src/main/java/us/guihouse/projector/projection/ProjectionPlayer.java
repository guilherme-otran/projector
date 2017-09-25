/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package us.guihouse.projector.projection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.*;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

/**
 *
 * @author guilherme
 */
public class ProjectionPlayer implements Projectable {

    private final CanvasDelegate delegate;

    private PlayerPanel panel;

    private DirectMediaPlayer player;

    private BufferedImage image;
    private final MediaPlayerFactory factory;

    private int width;
    private int height;

    private int previewW;
    private int previewH;
    private int previewY;

    public ProjectionPlayer(CanvasDelegate delegate) {
        this.delegate = delegate;

        factory = new MediaPlayerFactory();
    }

    @Override
    public void paintComponent(Graphics2D g) {
        g.drawImage(image, null, 0, 0);
    }

    @Override
    public CanvasDelegate getCanvasDelegate() {
        return delegate;
    }

    @Override
    public void rebuildLayout() {
        if (this.player != null) {
            this.player.release();
        }

        GraphicsDevice device = delegate.getCurrentDevice();

        if (device == null) {
            device = delegate.getDefaultDevice();
            this.width = device.getDefaultConfiguration().getBounds().width;
            this.height = device.getDefaultConfiguration().getBounds().height;
        } else {
            this.width = delegate.getWidth();
            this.height = delegate.getHeight();
        }

        image = device.getDefaultConfiguration().createCompatibleImage(width, height);
        image.setAccelerationPriority(1.0f);

        this.player = factory.newDirectMediaPlayer(new MyBufferFormatCallback(width, height), new MyRenderCallback());
    }

    @Override
    public void init() {
        rebuildLayout();

        if (panel == null) {
            panel = new PlayerPanel();
            panel.setLayout(new FlowLayout());
        }

        panel.setImage(image);
    }

    public DirectMediaPlayer getPlayer() {
        return player;
    }

    public JComponent getPreviewPanel() {
        return panel;
    }

    public void setPreviewPanelSize(double dw, double dh) {
        int w = (int) Math.round(dw);
        int h = (int) Math.round(dh);

        previewW = w;
        previewH = (int) Math.round((this.height / (double) this.width) * w);

        previewY = (h - previewH) / 2;

        panel.setBounds(0,0, w, h);
        panel.repaint();
    }

    private final class MyRenderCallback extends RenderCallbackAdapter {

        public MyRenderCallback() {
            super(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        }

        @Override
        public void onDisplay(DirectMediaPlayer mediaPlayer, int[] data) {
            panel.repaint();
        }
    }

    private final class MyBufferFormatCallback implements BufferFormatCallback {
        private int w;
        private int h;

        MyBufferFormatCallback(int w, int h) {
            this.w = w;
            this.h = h;
        }
        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            return new RV32BufferFormat(w, h);
        }
    }

    private final class PlayerPanel extends JComponent {

        private BufferedImage image;

        private void setImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(image, 0, previewY, previewW, previewH, null);
        }
    }
}
