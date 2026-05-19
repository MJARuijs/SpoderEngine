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
    val downKeys = HashSet<Int>()

    fun attachToWindow(windowHandle: Long) {
        glfwSetKeyCallback(windowHandle) { _, key: Int, _, action: Int, _ ->
            events.push(Event(key, action))
        }
    }

    fun isKeyPressed(key: Int): Boolean {
        return pressedKeys.contains(key)
    }

    fun update() {
        pressedKeys.clear()
        releasedKeys.clear()
        repeatedKeys.clear()

        while (events.isNotEmpty()) {
            val event = events.pop()
            when (event.action) {
                GLFW_PRESS -> {
                    pressedKeys.add(event.id)
                    downKeys.add(event.id)
                }
                GLFW_RELEASE -> {
                    releasedKeys.add(event.id)
                    downKeys.remove(event.id)
                }
                GLFW_REPEAT -> {
                    repeatedKeys.add(event.id)
                }
            }
        }
    }

}
