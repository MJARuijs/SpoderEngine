import devices.Keyboard
import devices.Mouse
import devices.Timer
import devices.Window
import graphics.Camera
import graphics.GraphicsContext
import graphics.Quad
import graphics.model.ModelLoader
import graphics.shaders.ShaderLoader
import graphics.shaders.ShaderProgram
import math.Color
import math.matrices.Matrix4
import math.vectors.Vector3
import org.lwjgl.glfw.GLFW

class Game {

    private val window = Window("ECS", ::onResize)
    private val keyboard = Keyboard()
    private val mouse = Mouse()
    private val timer = Timer()
    private val graphicsContext = GraphicsContext.getInstance()
    private val camera = Camera(window::aspectRatio, Vector3(0.0f, 0.0f, 2.0f))
    private val quad = Quad()
    private val quadProgram = ShaderProgram(listOf(
        ShaderLoader.getInstance().load("shaders/quad.vert"),
        ShaderLoader.getInstance().load("shaders/quad.frag")
    ))
    private val entityProgram = ShaderProgram(listOf(
        ShaderLoader.getInstance().load("shaders/entity.vert"),
        ShaderLoader.getInstance().load("shaders/entity.frag")
    ))

    private val modelLoader = ModelLoader()
    private val box = modelLoader.load("models/box.dae")

    private val ambientLight = AmbientLight(Color(0.25f, 0.25f, 0.25f))
    private val sun = DirectionalLight(Color(1.0f, 1.0f, 1.0f), Vector3(0.5f, 0.25f, 1.0f))

    fun onResize(w: Int, h: Int) {
        GraphicsContext.getInstance().resize(w, h)
    }

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

            camera.update(keyboard.downKeys, mouse.dx, mouse.dy, timer.delta)

            entityProgram.start()
            entityProgram.set("projection", camera.projectionMatrix)
            entityProgram.set("view", camera.viewMatrix)
            entityProgram.set("model", Matrix4())

            ambientLight.apply(entityProgram)
            sun.apply(entityProgram)

            box.render(entityProgram)
            entityProgram.stop()

            keyboard.update()
            mouse.update()
            window.synchronize()
            timer.update()
        }

        window.destroy()
    }
}
