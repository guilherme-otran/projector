package us.guihouse.projector.projection.glfw

interface GLFWGraphicsAdapterProvider {
    fun enqueueForDraw(runnable: Runnable)
}
