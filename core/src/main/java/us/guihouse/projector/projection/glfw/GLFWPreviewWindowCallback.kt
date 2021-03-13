package us.guihouse.projector.projection.glfw

interface GLFWPreviewWindowCallback {
    fun create(width: Int, height: Int)
    fun onDisplay(data: ByteArray)
}