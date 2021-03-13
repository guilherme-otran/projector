package us.guihouse.projector.projection.glfw

interface GLFWGraphicsAdapterProvider {
    fun enqueueForDraw(runnable: Runnable)
    fun dequeueGlBuffer(): Int
    fun dequeueMultiFrameGlBuffer(): Int
    fun freeMultiFrameGlBuffer(glBuffer: Int)
    fun dequeueTex(): Int
    fun freeTex(videoTex: Int)

    interface Callback {
        fun run(provider: GLFWGraphicsAdapterProvider)
    }
}