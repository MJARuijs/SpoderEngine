package devices

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.glfwGetTime

class Timer {
    
    private var initial = glfwGetTime()
    var currentTime = 0.0
        private set

    var delta = 0.1
        private set

    fun update() {
        val previousTime = currentTime
        currentTime = glfwGetTime() - initial
        delta = currentTime - previousTime
    }

    fun reset() {
        initial = glfwGetTime()
        currentTime = 0.0
        delta = 0.1
    }

}
