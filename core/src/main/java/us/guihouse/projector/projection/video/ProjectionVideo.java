package us.guihouse.projector.projection.video;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;
import us.guihouse.projector.projection.CanvasDelegate;
import us.guihouse.projector.projection.Projectable;
import us.guihouse.projector.projection.glfw.GLFWGraphicsAdapter;
import us.guihouse.projector.projection.glfw.RGBImageCopy;
import us.guihouse.projector.projection.models.VirtualScreen;
import us.guihouse.projector.utils.VlcPlayerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class ProjectionVideo implements Projectable {
    private final CanvasDelegate delegate;

    protected MediaPlayer player;

    private int[] imageData;
    private int[] freezeImageData;

    private int videoTex = 0;
    private int texW;
    private int texH;

    protected boolean firstFrame = false;

    protected int videoW = 0;
    protected int videoH = 0;

    private final HashMap<String, Integer> width = new HashMap<>();
    private final HashMap<String, Integer> height = new HashMap<>();

    private final HashMap<String, Integer> projectionX = new HashMap<>();
    private final HashMap<String, Integer> projectionY = new HashMap<>();

    @Getter
    private boolean cropVideo = false;

    @Getter
    private final BooleanProperty render = new SimpleBooleanProperty(true);

    protected ProjectionVideo.MyRenderCallback renderCallback;
    protected ProjectionVideo.MyBufferFormatCallback bufferFormatCallback;
    protected CallbackVideoSurface videoSurface;

    public ProjectionVideo(CanvasDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void rebuildLayout() {
        if (videoW == 0 || videoH == 0) {
            return;
        }

        width.clear();
        height.clear();
        projectionX.clear();
        projectionY.clear();

        delegate.getVirtualScreens().forEach(vs -> {
            float scaleW = vs.getWidth() / (float) videoW;
            float scaleH = vs.getHeight() / (float) videoH;

            float scale;

            if (cropVideo) {
                scale = Math.max(scaleW, scaleH);
            } else {
                scale = Math.min(scaleW, scaleH);
            }

            int scaledWidth = Math.round(scale * videoW);
            int scaledHeight = Math.round(scale * videoH);

            width.put(vs.getVirtualScreenId(), scaledWidth);
            height.put(vs.getVirtualScreenId(), scaledHeight);

            projectionX.put(vs.getVirtualScreenId(), (vs.getWidth() - scaledWidth) / 2);
            projectionY.put(vs.getVirtualScreenId(), (vs.getHeight() - scaledHeight) / 2);
        });
    }

    @Override
    public void init() {
        renderCallback = new ProjectionVideo.MyRenderCallback();
        bufferFormatCallback = new ProjectionVideo.MyBufferFormatCallback();

        this.player = VlcPlayerFactory.getFactory().mediaPlayers().newMediaPlayer();
        videoSurface = VlcPlayerFactory.getFactory().videoSurfaces().newVideoSurface(bufferFormatCallback, renderCallback, true);
        this.videoSurface.attach(this.player);
        this.player.video().setAdjustVideo(false);

        rebuildLayout();
    }

    public void setCropVideo(boolean cropVideo) {
        this.cropVideo = cropVideo;
        rebuildLayout();
    }

    @Override
    public void paintComponent(GLFWGraphicsAdapter g, VirtualScreen vs) {
        if (imageData == null && freezeImageData == null) {
            return;
        }

        int rWidth = width.getOrDefault(vs.getVirtualScreenId(), 0);
        int rHeight = height.getOrDefault(vs.getVirtualScreenId(), 0);

        if (render.get() && rWidth > 0 && rHeight > 0) {
            int videoW = this.videoW;
            int videoH = this.videoH;
            int[] data = freezeImageData != null ? freezeImageData : imageData;

            if (videoW * videoH != data.length) {
                return;
            }

            int buffer = g.getProvider().dequeueGlBuffer();
            GL30.glBindBuffer(GL30.GL_PIXEL_UNPACK_BUFFER, buffer);

            GL30.glBufferData(
                    GL30.GL_PIXEL_UNPACK_BUFFER,
                    (long) data.length * 4,
                    GL30.GL_STREAM_DRAW
            );

            ByteBuffer destination = GL30.glMapBuffer(GL30.GL_PIXEL_UNPACK_BUFFER, GL30.GL_WRITE_ONLY);

            if (destination != null) {
                RGBImageCopy.copyImageToBuffer(data, destination, true);
            }

            GL30.glUnmapBuffer(GL30.GL_PIXEL_UNPACK_BUFFER);
            GL30.glBindBuffer(GL30.GL_PIXEL_UNPACK_BUFFER, 0);

            int rProjectionX = projectionX.getOrDefault(vs.getVirtualScreenId(), 0);
            int rProjectionY = projectionY.getOrDefault(vs.getVirtualScreenId(), 0);

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, vs.getWidth(), vs.getHeight());

            Composite composite = g.getComposite();

            g.getProvider().enqueueForDraw(() -> {
                GL11.glEnable(GL11.GL_BLEND);
                GL20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                if (videoTex == 0) {
                    videoTex = g.getProvider().dequeueTex();

                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, videoTex);

                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
                }

                GL11.glPushMatrix();
                g.adjustOrtho();
                g.updateAlpha(composite);

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, videoTex);

                GL30.glBindBuffer(GL30.GL_PIXEL_UNPACK_BUFFER, buffer);

                if (videoW != texW || videoH != texH) {
                    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, videoW, videoH, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0L);
                    texW = videoW;
                    texH = videoH;
                } else {
                    GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, videoW, videoH, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0L);
                }

                GL11.glBegin(GL11.GL_QUADS);

                GL11.glTexCoord2d(0, 0);
                GL11.glVertex2i(rProjectionX, rProjectionY);

                GL11.glTexCoord2d(0, 1);
                GL11.glVertex2i(rProjectionX, rProjectionY + rHeight);

                GL11.glTexCoord2d(1, 1);
                GL11.glVertex2i(rProjectionX + rWidth, rProjectionY + rHeight);

                GL11.glTexCoord2d(1, 0);
                GL11.glVertex2i(rProjectionX + rWidth, rProjectionY);

                GL11.glEnd();

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
                GL30.glBindBuffer(GL30.GL_PIXEL_UNPACK_BUFFER, 0);
                GL11.glPopMatrix();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            });
        }
    }

    public int[] getImageData() {
        return imageData;
    }

    public void freeze() {
        freezeImageData = imageData;
        firstFrame = true;
    }

    public void unfreeze() {
        freezeImageData = null;
        rebuildLayout();
    }

    protected void generateBuffer(int w, int h) {
        freeze();
        videoW = w;
        videoH = h;

        imageData = new int[w * h];
    }

    @Override
    public void finish() {
        this.player.release();
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    private final class MyRenderCallback implements RenderCallback {
        MyRenderCallback() {
        }

        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat) {
            if (firstFrame) {
                firstFrame = false;
            } else {
                nativeBuffers[0].asIntBuffer().get(imageData);
                unfreeze();
            }
        }
    }

    private final class MyBufferFormatCallback implements BufferFormatCallback {

        MyBufferFormatCallback() {
        }

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            generateBuffer(sourceWidth, sourceHeight);
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

        @Override
        public void allocatedBuffers(ByteBuffer[] buffers) {
            assert buffers[0].capacity() == videoW * videoH * 4;
        }
    }
}
