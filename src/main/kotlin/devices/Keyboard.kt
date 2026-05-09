package devices

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import org.lwjgl.glfw.GLFW.GLFW_REPEAT
import org.lwjgl.glfw.GLFW.glfwSetKeyCallback
import java.util.*

class Keyboard {

    private val events = ArrayDeque<Event>()
    private val pressedKeys = HashSet<Int>()
    private val releasedKeys = HashSet<Int>()
    private val repeatedKeys = HashSet<Int>()
    private val downKeys = HashSet<Int>()

    fun attachToWindow(windowHandle: Long) {
        glfwSetKeyCallback(windowHandle) { _, key: Int, _, action: Int, _ ->
            events.push(Event(key, action))
        }
    }

    fun update() {
        pressedKeys.clear()
        releasedKeys.clear()
        repeatedKeys.clear()

        while (events.isNotEmpty()) {
            val event = events.pop()
            when (event.action) {
                GLFW_PRESS -> {
                    pressedKeys.add(event.key)
                    downKeys.add(event.key)
                }
                GLFW_RELEASE -> {
                    releasedKeys.add(event.key)
                    downKeys.remove(event.key)
                }
                GLFW_REPEAT -> {
                    repeatedKeys.add(event.key)
                }
            }
        }
    }

    private data class Event(val key: Int, val action: Int)

}
