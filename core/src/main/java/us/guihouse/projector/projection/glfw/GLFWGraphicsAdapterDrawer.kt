package us.guihouse.projector.projection.glfw

import us.guihouse.projector.other.EventQueue
import us.guihouse.projector.projection.ProjectionCanvas
import us.guihouse.projector.projection.models.VirtualScreen
import java.awt.Rectangle
import java.util.LinkedList
import java.util.Queue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30
import java.util.concurrent.ConcurrentLinkedQueue


class GLFWGraphicsAdapterDrawer(private val glWindow: Long,
                                private val projectionCanvas: ProjectionCanvas,
                                bounds: Rectangle,
                                private val virtualScreen: VirtualScreen) : EventQueue(), GLFWGraphicsAdapterProvider {

    companion object {
        const val BUFFER_LIMIT = 3
    }

    internal data class Frame(val draws: Queue<Runnable>, val glBuffers: Queue<Int>)

    private var currentFrame: Frame? = null

    private val filledFrameBuffer: LinkedBlockingQueue<Frame> = LinkedBlockingQueue()
    private val freeFrameBuffer: LinkedBlockingQueue<Frame> = LinkedBlockingQueue()

    private val freeGlBuffers = ConcurrentLinkedQueue<Int>()
    private val allocatedGlBuffers = ArrayList<Int>()

    private val freeMultiFrameGlBuffers = ConcurrentLinkedQueue<Int>()

    private val graphicsAdapter = GLFWGraphicsAdapter(bounds, this)

    private val loopRun: Runnable = Runnable {
        run() {
            currentFrame = freeFrameBuffer.poll(500, TimeUnit.MILLISECONDS)

            currentFrame?.let {
                it.draws.clear()
                projectionCanvas.paintComponent(graphicsAdapter, virtualScreen)
                filledFrameBuffer.add(it)
            }
        }
    }

    override fun enqueueForDraw(runnable: Runnable) {
        currentFrame!!.draws.add(runnable)
    }

    private fun allocateGlBuffer(): Int {
        val buffer = GL30.glGenBuffers()
        allocatedGlBuffers.add(buffer)
        return buffer
    }

    override fun dequeueGlBuffer(): Int {
        val glBuffer = freeGlBuffers.poll() ?: allocateGlBuffer()
        currentFrame!!.glBuffers.add(glBuffer)
        return glBuffer
    }

    override fun dequeueMultiFrameGlBuffer(): Int {
        return freeMultiFrameGlBuffers.poll() ?: allocateGlBuffer()
    }

    override fun freeMultiFrameGlBuffer(glBuffer: Int) {
        freeMultiFrameGlBuffers.add(glBuffer)
    }

    fun drawNextFrame() {
        val frame = filledFrameBuffer.poll(500, TimeUnit.MILLISECONDS)

        frame?.let {
            var part = it.draws.poll()

            while (part != null) {
                part.run()
                part = it.draws.poll()
            }

            freeGlBuffers.addAll(it.glBuffers)
            it.glBuffers.clear()
            freeFrameBuffer.add(it)
        }
    }

    override fun init() {
        for (i in 1..BUFFER_LIMIT) {
            freeFrameBuffer.add(Frame(LinkedList(), LinkedList()))
        }

        super.enqueueContinuous(loopRun)
        super.init()
    }

    override fun onStart() {
        super.onStart()
        GLFW.glfwMakeContextCurrent(glWindow)
        GL.createCapabilities()
    }

    override fun onStop() {
        super.onStop()
        super.removeContinuous(loopRun)
        allocatedGlBuffers.forEach(GL30::glDeleteBuffers)
        allocatedGlBuffers.clear()
        GLFW.glfwDestroyWindow(glWindow)
    }
}