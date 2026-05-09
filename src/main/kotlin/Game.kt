import devices.Window

class Game {

    private val window = Window("ECS")

    fun run() {
        while (!window.isClosed()) {
            window.poll()
            window.synchronize()
        }

        window.destroy()
    }
}
