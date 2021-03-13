package us.guihouse.projector.projection.glfw

import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30
import org.lwjgl.system.MemoryUtil
import us.guihouse.projector.other.EventQueue
import us.guihouse.projector.projection.models.VirtualScreen
import java.awt.Rectangle

class GLFWPreviewWindow(val callback: GLFWPreviewWindowCallback) {
    private lateinit var bounds: Rectangle
    private val eventQueue = EventQueue(100)

    private var window: Long = 0

    private var writeBuffer: Int? = null
    private var readBuffer: Int? = null
    private var generatedBuffer: Int? = null

    internal inner class Starter : Runnable {
        override fun run() {
            GLFW.glfwMakeContextCurrent(window)
            GL.createCapabilities()

            callback.create(bounds.width, bounds.height)

            val glBuffer = GL30.glGenBuffers()

            glBuffer.let {
                GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, it)
                GL30.glBufferData(GL30.GL_PIXEL_PACK_BUFFER, bounds.width * bounds.height * 3L, GL30. GL_STREAM_READ)
                GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, 0)
            }

            generatedBuffer = glBuffer
            writeBuffer = glBuffer

            eventQueue.enqueueContinuous(Loop())
        }
    }

    internal inner class Finish : Runnable {
        override fun run() {
            writeBuffer = null
            readBuffer = null
            generatedBuffer?.let {
                GL30.glDeleteBuffers(it)
            }
            GLFW.glfwDestroyWindow(window)
        }
    }

    internal inner class Loop : Runnable {
        override fun run() {
            readBuffer?.let { glBuffer ->
                GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, glBuffer)
                val buffer = GL30.glMapBuffer(GL30.GL_PIXEL_PACK_BUFFER, GL30.GL_READ_ONLY)

                buffer?.let {
                    val size = bounds.width * bounds.height * 3
                    val data = ByteArray(size)
                    it.get(data)
                    callback.onDisplay(data)
                }

                GL30.glUnmapBuffer(GL30.GL_PIXEL_PACK_BUFFER)

                GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, 0)

                readBuffer = null
                writeBuffer = glBuffer
            }
        }

    }

    fun createWindow(glShare: Long) {
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE)

        window = GLFW.glfwCreateWindow(640, 480, "Projetor Preview", 0L, glShare)

        if (window == MemoryUtil.NULL) throw RuntimeException("Failed to create the GLFW window")
    }

    fun init(virtualScreen: VirtualScreen) {
        bounds = Rectangle(virtualScreen.width, virtualScreen.height)
        eventQueue.setStartRunnable(Starter())
        eventQueue.setStopRunnable(Finish())
        eventQueue.init()
    }

    fun loopCycle() {
        writeBuffer?.let {
            GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, it)
            GL30.glReadPixels(0, 0, bounds.width, bounds.height, GL30.GL_RGB, GL30.GL_UNSIGNED_BYTE, 0L)
            GL30.glBindBuffer(GL30.GL_PIXEL_PACK_BUFFER, 0)
            writeBuffer = null
            readBuffer = it
        }
    }

    fun shutdown() {
        eventQueue.stop()
    }
}