package devices

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE
import org.lwjgl.glfw.GLFW.GLFW_REPEAT
import org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback
import org.lwjgl.glfw.GLFW.glfwSetInputMode
import org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback
import org.lwjgl.glfw.GLFW.glfwSetScrollCallback
import java.util.ArrayDeque

class Mouse {

    var x = 0.0
        internal set

    var y = 0.0
        internal set

    var dx = 0.0
        internal set

    var dy = 0.0
        internal set

    var xScroll = 0.0
        private set

    var yScroll = 0.0
        private set

    var moved = false
        internal set

    var captured = false
        internal set

    private val events = ArrayDeque<Event>()

    private val pressed = HashSet<Int>()
    private val released = HashSet<Int>()
    private val down = HashSet<Int>()

    fun attachToWindow(windowHandle: Long, windowWidth: Int, windowHeight: Int) {
        glfwSetMouseButtonCallback(windowHandle) { _, button: Int, action: Int, _ ->
            events.push(Event(button, action))
        }

        glfwSetScrollCallback(windowHandle) { _, xScrollNew: Double, yScrollNew: Double ->
            xScroll = xScrollNew
            yScroll = yScrollNew
        }

        glfwSetCursorPosCallback(windowHandle) { _, xPixelNew: Double, yPixel: Double ->
            val scaledX = (xPixelNew - windowWidth / 2) / windowWidth
            val scaledY = -(yPixel - windowHeight / 2) / windowHeight

            moved = (x != scaledX) || (y != scaledY)

            dx = scaledX - x
            dy = scaledY - y

            x = scaledX
            y = scaledY
        }
    }

    fun isPressed(button: Int) = pressed.contains(button)

    fun isReleased(button: Int) = released.contains(button)

    fun isDown(button: Int) = down.contains(button)

    internal fun post(button: Int, action: Int) = events.push(Event(button, action))

    fun capture(windowHandle: Long) {
        captured = true
        glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED)
    }

    fun release(windowHandle: Long) {
        captured = false
        glfwSetInputMode(windowHandle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL)
    }

    fun toggle(windowHandle: Long) {
        if (captured) {
            release(windowHandle)
        } else {
            capture(windowHandle)
        }
    }

    fun isCaptured() = captured

    fun update() {
        pressed.clear()
        released.clear()

        xScroll = 0.0
        yScroll = 0.0

        dx = 0.0
        dy = 0.0

        while (events.isNotEmpty()) {
            val event = events.pop()
            when (event.action) {
                GLFW_PRESS -> {
                    pressed.add(event.id)
                    down.add(event.id)
                }
                GLFW_RELEASE -> {
                    released.add(event.id)
                    down.remove(event.id)
                }
                GLFW_REPEAT -> {
                    // ignore
                }
            }
        }
    }

}
