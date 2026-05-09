package devices

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.system.MemoryUtil.NULL

class Window(title: String) {

    var width = 1280
        private set

    var height = 720
        private set

    val aspectRatio: Float
        get() = width.toFloat() / height.toFloat()

    private val handle: Long

    init {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit()) {
            throw RuntimeException("GLFW failed to init..")
        }

        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)

        handle = glfwCreateWindow(width, height, title, NULL, NULL)
        if (handle == NULL) {
            throw RuntimeException("Failed to create GLFW window..")
        }

        glfwSetWindowSizeCallback(handle) { _, newWidth: Int, newHeight: Int -> 
            width = newWidth
            height = newHeight
        }
        glfwMakeContextCurrent(handle)
        glfwSwapInterval(1)
        createCapabilities()

        glfwShowWindow(handle)
    }

    fun poll() {
        glfwPollEvents()
    }

    fun synchronize() {
        glfwSwapBuffers(handle)
    }

    fun close() {
        glfwSetWindowShouldClose(handle, true)
    }

    fun isClosed(): Boolean {
        return glfwWindowShouldClose(handle)
    }

    fun destroy() {
        glfwDestroyWindow(handle)
        glfwTerminate()
    }

}
