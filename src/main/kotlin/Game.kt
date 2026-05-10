import devices.Keyboard
import devices.Mouse
import devices.Window
import graphics.GraphicsContext
import org.lwjgl.glfw.GLFW

class Game {

    private val window = Window("ECS")
    private val keyboard = Keyboard()
    private val mouse = Mouse()
    private val graphicsContext = GraphicsContext.getInstance()

    init {
        keyboard.attachToWindow(window.handle)
        mouse.attachToWindow(window.handle, window.width, window.height)
    }

    fun run() {
        mouse.capture(window.handle)
        while (!window.isClosed()) {
            window.poll()

            graphicsContext.clear(0.25f, 0.25f, 0.25f)
            if (keyboard.isKeyPressed(GLFW.GLFW_KEY_Q)) {
                window.close()
            }

            keyboard.update()
            mouse.update()
            window.synchronize()
        }

        window.destroy()
    }
}
