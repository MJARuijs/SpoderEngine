package graphics

import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glClear
import org.lwjgl.opengl.GL11.glClearColor
import org.lwjgl.opengl.GL11.glClearDepth

class GraphicsContext private constructor() {

    init {
        GL.createCapabilities()
    }

    fun clear(r: Float = 0.0f, g: Float = 0.0f, b: Float = 0.0f, a: Float = 1.0f) {
        glClear(GL11.GL_COLOR_BUFFER_BIT)
        glClearColor(r, g, b, a)
        // glClearDepth(1.0)
    }

    companion object {

        private var graphicsContext: GraphicsContext? = null

        fun getInstance(): GraphicsContext {
            if (graphicsContext == null) {
                graphicsContext = GraphicsContext()
            }
            return graphicsContext!!
        }
    }

}
