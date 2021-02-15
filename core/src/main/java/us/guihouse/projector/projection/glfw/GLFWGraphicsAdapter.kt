package us.guihouse.projector.projection.glfw

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Composite
import java.awt.Font
import java.awt.FontMetrics
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.GraphicsConfiguration
import java.awt.Image
import java.awt.Paint
import java.awt.Rectangle
import java.awt.RenderingHints
import java.awt.Shape
import java.awt.Stroke
import java.awt.font.FontRenderContext
import java.awt.font.GlyphVector
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.image.BufferedImageOp
import java.awt.image.ImageObserver
import java.awt.image.RenderedImage
import java.awt.image.renderable.RenderableImage
import java.nio.ByteBuffer
import java.text.AttributedCharacterIterator
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

class GLFWGraphicsAdapter(private val bounds: Rectangle) : Graphics2D() {
    private var transform: AffineTransform = AffineTransform()
    private var color: Color = Color(0, 0, 0)
    private var composite: Composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)
    private var font: Font = Font(Font.SANS_SERIF, 0, 16)

    override fun create(): Graphics {
        return GLFWGraphicsAdapter(bounds)
    }

    override fun translate(x: Int, y: Int) {
        transform.translate(x.toDouble(), y.toDouble())
    }

    override fun translate(tx: Double, ty: Double) {
        transform.translate(tx, ty)
    }

    override fun getColor(): Color {
        return color;
    }

    override fun setColor(c: Color?) {
        this.color = c ?: Color(0, 0, 0)
    }

    private fun updateColor() {
        var alpha = color.alpha / 255.0f

        if (composite is AlphaComposite) {
            alpha *= (composite as AlphaComposite).alpha
        }

        GL11.glColor4f(color.red / 255.0f, color.green / 255.0f, color.blue / 255.0f, alpha)
    }

    private fun updateAlpha() {
        var alpha = 1.0f

        if (composite is AlphaComposite) {
            alpha *= (composite as AlphaComposite).alpha
        }

        GL11.glColor4f(1f, 1f, 1f, alpha)
    }

    private fun adjustOrtho() {
        GL11.glOrtho(bounds.x.toDouble(), bounds.width.toDouble(), bounds.height.toDouble(), bounds.y.toDouble(), 1.0, 0.0)
    }

    override fun setPaintMode() {
        TODO("Not yet implemented")
    }

    override fun setXORMode(c1: Color?) {
        TODO("Not yet implemented")
    }

    override fun getFont(): Font {
        return font
    }

    override fun setFont(font: Font?) {
        font?.let { this.font = it }
    }

    override fun getFontMetrics(f: Font?): FontMetrics {
        TODO("Not yet implemented")
    }

    override fun getClipBounds(): Rectangle {
        return Rectangle(bounds)
    }

    override fun clipRect(x: Int, y: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun setClip(x: Int, y: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun setClip(clip: Shape?) {
        TODO("Not yet implemented")
    }

    override fun getClip(): Shape {
        TODO("Not yet implemented")
    }

    override fun copyArea(x: Int, y: Int, width: Int, height: Int, dx: Int, dy: Int) {
        TODO("Not yet implemented")
    }

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        TODO("Not yet implemented")
    }

    override fun fillRect(x: Int, y: Int, width: Int, height: Int) {
        GL11.glPushMatrix()
        adjustOrtho()
        GL11.glEnable(GL11.GL_COLOR_MATERIAL)
        GL11.glEnable(GL11.GL_BLEND)
        updateColor()

        GL11.glBegin(GL11.GL_QUADS)
        GL11.glVertex2i(x, y)
        GL11.glVertex2i(x, y + height)
        GL11.glVertex2i(x + width, y + height)
        GL11.glVertex2i(x + width, y)
        GL11.glEnd()

        GL11.glDisable(GL11.GL_COLOR_MATERIAL)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPopMatrix()
    }

    override fun clearRect(x: Int, y: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun drawRoundRect(x: Int, y: Int, width: Int, height: Int, arcWidth: Int, arcHeight: Int) {
        TODO("Not yet implemented")
    }

    override fun fillRoundRect(x: Int, y: Int, width: Int, height: Int, arcWidth: Int, arcHeight: Int) {
        TODO("Not yet implemented")
    }

    override fun drawOval(x: Int, y: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun fillOval(x: Int, y: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun drawArc(x: Int, y: Int, width: Int, height: Int, startAngle: Int, arcAngle: Int) {
        TODO("Not yet implemented")
    }

    override fun fillArc(x: Int, y: Int, width: Int, height: Int, startAngle: Int, arcAngle: Int) {
        TODO("Not yet implemented")
    }

    override fun drawPolyline(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int) {
        TODO("Not yet implemented")
    }

    override fun drawPolygon(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int) {
        TODO("Not yet implemented")
    }

    override fun fillPolygon(xPoints: IntArray?, yPoints: IntArray?, nPoints: Int) {
        TODO("Not yet implemented")
    }

    override fun drawString(str: String, x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun drawString(str: String?, x: Float, y: Float) {
        TODO("Not yet implemented")
    }

    override fun drawString(iterator: AttributedCharacterIterator?, x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun drawString(iterator: AttributedCharacterIterator?, x: Float, y: Float) {
        TODO("Not yet implemented")
    }

    override fun drawImage(img: Image?, xform: AffineTransform?, obs: ImageObserver?): Boolean {
        TODO("Not yet implemented")
    }

    override fun drawImage(img: BufferedImage?, op: BufferedImageOp?, x: Int, y: Int) {
        TODO("Not yet implemented")
    }

    override fun drawImage(img: Image?, x: Int, y: Int, observer: ImageObserver?): Boolean {
        img ?: return false
        val width = img.getWidth(observer)
        val height = img.getHeight(observer)

        if (width <= 0 || height <= 0) {
            return false
        }

        return drawImage(img, x, y, width, height, observer)
    }

    override fun drawImage(img: Image?, x: Int, y: Int, width: Int, height: Int, observer: ImageObserver?): Boolean {
        return drawImage(img, x, y, width, height, null, observer)
    }

    override fun drawImage(img: Image?, x: Int, y: Int, bgcolor: Color?, observer: ImageObserver?): Boolean {
        img ?: return false
        val width = img.getWidth(observer)
        val height = img.getHeight(observer)

        if (width <= 0 || height <= 0) {
            return false
        }

        return drawImage(img, x, y, width, height, bgcolor, observer)
    }

    override fun drawImage(
        img: Image?,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        bgcolor: Color?,
        observer: ImageObserver?
    ): Boolean {
        img ?: return false
        val imgW = img.getWidth(observer)
        val imgH = img.getHeight(observer)

        if (width <= 0 || height <= 0) {
            return false
        }

        return drawImage(img, x, y, x + width, y + height, 0, 0, imgW, imgH, bgcolor, observer)
    }

    override fun drawImage(
        img: Image?,
        dx1: Int,
        dy1: Int,
        dx2: Int,
        dy2: Int,
        sx1: Int,
        sy1: Int,
        sx2: Int,
        sy2: Int,
        observer: ImageObserver?
    ): Boolean {
        return drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null, observer)
    }

    override fun drawImage(
        img: Image?,
        dx1: Int,
        dy1: Int,
        dx2: Int,
        dy2: Int,
        sx1: Int,
        sy1: Int,
        sx2: Int,
        sy2: Int,
        bgcolor: Color?,
        observer: ImageObserver?
    ): Boolean {
        img ?: return false

        val tempImg: BufferedImage

        if (img is BufferedImage && img.type == BufferedImage.TYPE_INT_ARGB) {
            tempImg = img
        } else {
            val width = img.getWidth(observer)
            val height = img.getHeight(observer)

            if (width <= 0 || height <= 0) {
                return false
            }

            tempImg = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
            with (tempImg.graphics) {
                drawImage(img, 0, 0, observer)
                dispose()
            }
        }

        bgcolor?.let {
            val oldColor = getColor()
            setColor(it)

            val x = min(dx1, dx2)
            val y = min(dy1, dy2)
            val width = max(dx1, dx2) - x
            val height = max(dy1, dy2) - y

            fillRect(x, y, width, height)

            setColor(oldColor)
        }

        val width = tempImg.width
        val height = tempImg.height

        val buffer = BufferUtils.createByteBuffer(width * height * 4)
        RGBImageCopy.copyImageToBuffer(tempImg,  buffer, true)

        GL11.glEnable(GL11.GL_BLEND)
        GL20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glEnable(GL11.GL_TEXTURE_2D)

        GL11.glPushMatrix()
        adjustOrtho()
        updateAlpha()

        val texId = GL11.glGenTextures()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId)

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer)

        GL11.glBegin(GL11.GL_QUADS)

        GL11.glTexCoord2d(sx1 / width.toDouble(), sy1 / height.toDouble())
        GL11.glVertex2i(dx1, dy1)

        GL11.glTexCoord2d(sx1 / width.toDouble(), sy2 / height.toDouble())
        GL11.glVertex2i(dx1, dy2)

        GL11.glTexCoord2d(sx2 / width.toDouble(), sy2 / height.toDouble())
        GL11.glVertex2i(dx2, dy2)

        GL11.glTexCoord2d(sx2 / width.toDouble(), sy1 / height.toDouble())
        GL11.glVertex2i(dx2, dy1)

        GL11.glEnd()

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0)
        GL11.glPopMatrix()
        GL11.glDeleteTextures(texId)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)

        return true
    }

    override fun dispose() {

    }

    override fun draw(s: Shape?) {
        TODO("Not yet implemented")
    }

    override fun drawRenderedImage(img: RenderedImage?, xform: AffineTransform?) {
        TODO("Not yet implemented")
    }

    override fun drawRenderableImage(img: RenderableImage?, xform: AffineTransform?) {
        TODO("Not yet implemented")
    }

    override fun drawGlyphVector(g: GlyphVector?, x: Float, y: Float) {
        TODO("Not yet implemented")
    }

    override fun fill(s: Shape?) {
        TODO("Not yet implemented")
    }

    override fun hit(rect: Rectangle?, s: Shape?, onStroke: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override fun getDeviceConfiguration(): GraphicsConfiguration {
        TODO("Not yet implemented")
    }

    override fun setComposite(comp: Composite?) {
        this.composite = comp ?: AlphaComposite.getInstance(AlphaComposite.SRC_OVER)
    }

    override fun setPaint(paint: Paint?) {
        TODO("Not yet implemented")
    }

    override fun setStroke(s: Stroke?) {
        TODO("Not yet implemented")
    }

    override fun setRenderingHint(hintKey: RenderingHints.Key?, hintValue: Any?) {
        TODO("Not yet implemented")
    }

    override fun getRenderingHint(hintKey: RenderingHints.Key?): Any {
        TODO("Not yet implemented")
    }

    override fun setRenderingHints(hints: MutableMap<*, *>?) {
        TODO("Not yet implemented")
    }

    override fun addRenderingHints(hints: MutableMap<*, *>?) {
        TODO("Not yet implemented")
    }

    override fun getRenderingHints(): RenderingHints {
        TODO("Not yet implemented")
    }

    override fun rotate(theta: Double) {
        transform.rotate(theta)
    }

    override fun rotate(theta: Double, x: Double, y: Double) {
        transform.rotate(theta, x, y)
    }

    override fun scale(sx: Double, sy: Double) {
        transform.scale(sx, sy)
    }

    override fun shear(shx: Double, shy: Double) {
        transform.shear(shx, shy)
    }

    override fun transform(Tx: AffineTransform?) {
        Tx?.let { transform.concatenate(it) }
    }

    override fun setTransform(Tx: AffineTransform?) {
        transform = Tx ?: AffineTransform()
    }

    override fun getTransform(): AffineTransform {
        return transform
    }

    override fun getPaint(): Paint {
        TODO("Not yet implemented")
    }

    override fun getComposite(): Composite {
        return composite
    }

    override fun setBackground(color: Color?) {
        TODO("Not yet implemented")
    }

    override fun getBackground(): Color {
        TODO("Not yet implemented")
    }

    override fun getStroke(): Stroke {
        TODO("Not yet implemented")
    }

    override fun clip(s: Shape?) {
        TODO("Not yet implemented")
    }

    override fun getFontRenderContext(): FontRenderContext {
        TODO("Not yet implemented")
    }

}
