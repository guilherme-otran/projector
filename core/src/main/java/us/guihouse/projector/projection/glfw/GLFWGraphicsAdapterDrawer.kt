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


class GLFWGraphicsAdapterDrawer(private val glWindow: Long,
                                private val projectionCanvas: ProjectionCanvas,
                                bounds: Rectangle,
                                private val virtualScreen: VirtualScreen) : EventQueue(), GLFWGraphicsAdapterProvider {

    companion object {
        const val BUFFER_LIMIT = 3
    }

    private var currentFrame: Queue<Runnable>? = null

    private val filledBuffer: LinkedBlockingQueue<Queue<Runnable>> = LinkedBlockingQueue()
    private val freeBuffer: LinkedBlockingQueue<Queue<Runnable>> = LinkedBlockingQueue()

    private val graphicsAdapter = GLFWGraphicsAdapter(bounds, this)

    private val loopRun: Runnable = Runnable {
        run() {
            currentFrame = freeBuffer.poll(500, TimeUnit.MILLISECONDS)

            currentFrame?.let {
                it.clear()
                projectionCanvas.paintComponent(graphicsAdapter, virtualScreen)
                filledBuffer.add(it)
            }
        }
    }

    override fun enqueueForDraw(runnable: Runnable) {
        currentFrame!!.add(runnable)
    }

    fun drawNextFrame() {
        val frame = filledBuffer.poll(500, TimeUnit.MILLISECONDS)

        frame?.let {
            var part = it.poll()

            while (part != null) {
                part.run()
                part = it.poll()
            }

            freeBuffer.add(it)
        }
    }

    override fun init() {
        for (i in 1..BUFFER_LIMIT) {
            freeBuffer.add(LinkedList())
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
        GLFW.glfwDestroyWindow(glWindow)
    }
}