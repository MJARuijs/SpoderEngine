package devices

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.system.MemoryUtil.NULL

class Window(title: String, private val onWindowResized: (Int, Int) -> Unit = { _, _ ->  }) {

    var width = 1280
        private set

    var height = 720
        private set

    val aspectRatio: Float
        get() = width.toFloat() / height.toFloat()

    val handle: Long

    init {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit()) {
            throw RuntimeException("GLFW failed to init..")
        }

        glfwWindowHint(GLFW_DECORATED, GLFW_TRUE)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)

        handle = glfwCreateWindow(width, height, title, NULL, NULL)
        if (handle == NULL) {
            throw RuntimeException("Failed to create GLFW window..")
        }

        glfwSetWindowSizeCallback(handle) { _, newWidth: Int, newHeight: Int -> 
            // println("Window Resized $newWidth $newHeight")
            width = newWidth
            height = newHeight
            onWindowResized(width, height)
        }
        glfwMakeContextCurrent(handle)
        glfwSwapInterval(1)

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
