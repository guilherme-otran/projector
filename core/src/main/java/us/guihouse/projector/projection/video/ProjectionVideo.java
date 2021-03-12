package us.guihouse.projector.projection.video;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.w3c.dom.css.Rect;
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
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProjectionVideo implements Projectable {
    private final CanvasDelegate delegate;

    protected MediaPlayer player;

    private int[] imageData;
    protected int videoW = 0;
    protected int videoH = 0;

    private final ConcurrentHashMap<String, Integer> texes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Rectangle> positions = new ConcurrentHashMap<>();

    protected boolean firstFrame = false;
    private boolean freeze = false;

    private int[] freezeImageData;
    private int freezeVideoW;
    private int freezeVideoH;

    private final ConcurrentHashMap<String, Rectangle> freezePositions = new ConcurrentHashMap<>();

    @Getter
    private boolean cropVideo = false;

    @Getter
    private final BooleanProperty render = new SimpleBooleanProperty(true);

    protected ProjectionVideo.MyRenderCallback renderCallback;
    protected ProjectionVideo.MyBufferFormatCallback bufferFormatCallback;
    protected CallbackVideoSurface videoSurface;

    private Queue<int[]> frameBuffer = new ConcurrentLinkedQueue<>();

    public ProjectionVideo(CanvasDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public void rebuildLayout() {
        if (videoW == 0 || videoH == 0) {
            return;
        }

        delegate.getVirtualScreens().forEach(vs -> {
            delegate.runOnProvider(vs, provider -> {
                freeze = false;
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
                int x = (vs.getWidth() - scaledWidth) / 2;
                int y = (vs.getHeight() - scaledHeight) / 2;

                Rectangle position = new Rectangle(x, y, scaledWidth, scaledHeight);
                positions.put(vs.getVirtualScreenId(), position);

                Integer oldTex = texes.get(vs.getVirtualScreenId());

                if (oldTex != null) {
                    provider.freeTex(oldTex);
                }

                int tex = provider.dequeueTex();
                texes.put(vs.getVirtualScreenId(), tex);

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);

                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, videoW, videoH, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0L);

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            });
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
        int[] data;

        Rectangle position;

        int videoW;
        int videoH;

        if (freeze) {
            data = freezeImageData;

            position = freezePositions.get(vs.getVirtualScreenId());

            videoW = this.freezeVideoW;
            videoH = this.freezeVideoH;
        } else {
            data = imageData;

            position = positions.get(vs.getVirtualScreenId());

            videoW = this.videoW;
            videoH = this.videoH;
        }

        Integer videoTex = texes.get(vs.getVirtualScreenId());

        if (render.get() && position != null && data != null && videoTex != null) {
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

            g.setColor(Color.BLACK);
            g.fillRect(0, 0, vs.getWidth(), vs.getHeight());

            Composite composite = g.getComposite();

            g.getProvider().enqueueForDraw(() -> {
                GL11.glEnable(GL11.GL_BLEND);
                GL20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                GL11.glPushMatrix();
                g.adjustOrtho();
                g.updateAlpha(composite);

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, videoTex);

                GL30.glBindBuffer(GL30.GL_PIXEL_UNPACK_BUFFER, buffer);

                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, videoW, videoH, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 0L);

                GL11.glBegin(GL11.GL_QUADS);

                GL11.glTexCoord2d(0, 0);
                GL11.glVertex2d(position.getX(), position.getY());

                GL11.glTexCoord2d(0, 1);
                GL11.glVertex2d(position.getX(), position.getMaxY());

                GL11.glTexCoord2d(1, 1);
                GL11.glVertex2d(position.getMaxX(), position.getMaxY());

                GL11.glTexCoord2d(1, 0);
                GL11.glVertex2d(position.getMaxX(), position.getY());

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
        if (freeze) {
            return;
        }

        freezeImageData = frameBuffer.peek();
        freezeVideoW = videoW;
        freezeVideoH = videoH;
        freezePositions.putAll(positions);
        freeze = true;
    }

    public void unfreeze() {
        rebuildLayout();
    }

    protected void generateBuffer(int w, int h) {
        freeze();
        firstFrame = true;
        videoW = w;
        videoH = h;

        frameBuffer.clear();

        for (int i=0; i<3; i++) {
            frameBuffer.add(new int[w * h]);
        }
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
            int[] imageData = frameBuffer.poll();
            nativeBuffers[0].asIntBuffer().get(imageData);
            frameBuffer.add(imageData);

            ProjectionVideo.this.imageData = imageData;

            if (firstFrame) {
                firstFrame = false;
            } else {
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
