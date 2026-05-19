package graphics

import math.matrices.Matrix4
import math.vectors.Vector3
import org.lwjgl.glfw.GLFW
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.min
import kotlin.math.tan

class Camera(var aspectRatio: () -> Float,
             var position: Vector3 = Vector3(),
             var rotation: Vector3 = Vector3(),
             var fov: Float = 70.0f, 
             var zNear: Float = 0.01f, 
             var zFar: Float = 1000.0f) {

    val projectionMatrix: Matrix4
        get() = Matrix4(floatArrayOf(
            1.0f / (aspectRatio() * tan((PI.toFloat() / 180.0f) * fov / 2.0f)), 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f / tan((PI.toFloat() / 180.0f) * fov / 2.0f), 0.0f, 0.0f,
                0.0f, 0.0f, -(zFar + zNear) / (zFar - zNear), -(2.0f * zFar * zNear) / (zFar - zNear),
                0.0f, 0.0f, -1.0f, 0.0f
        ))

    val viewMatrix: Matrix4
        get() = Matrix4()
            .rotate(rotation)
            .translate(-position)

    val rotationMatrix: Matrix4
        get() = Matrix4().rotateY(-rotation.y).rotateX(-rotation.x)


    fun update(downKeys: HashSet<Int>, mouseXDelta: Double, mouseYDelta: Double, delta: Double) {
        val translation = Vector3()

        val mouseSpeed = 1.75f
        var moveSpeed = 5.0f

        if (downKeys.contains(GLFW.GLFW_KEY_LEFT_SHIFT)) {
            moveSpeed = 10.0f
        }

        if (downKeys.contains(GLFW.GLFW_KEY_W)) {
            translation.z += 1.0f
        }

        if (downKeys.contains(GLFW.GLFW_KEY_S)) {
            translation.z -= 1.0f
        }

        if (downKeys.contains(GLFW.GLFW_KEY_D)) {
            translation.x -= 1.0f
        }

        if (downKeys.contains(GLFW.GLFW_KEY_A)) {
            translation.x += 1.0f
        }

        if (downKeys.contains(GLFW.GLFW_KEY_LEFT_CONTROL)) {
            translation.y += 1.0f
        }

        if (downKeys.contains(GLFW.GLFW_KEY_SPACE)) {
            translation.y -= 1.0f
        }

        if (translation.length() > 0.0f) {
            val rotationMatrix = Matrix4().rotateY(-rotation.y)
            position += rotationMatrix.dot(-translation.unit()) * delta.toFloat() * moveSpeed
        }

        rotation.x += (-mouseYDelta.toFloat() * mouseSpeed) % (2.0f * PI.toFloat())
        rotation.x = min(max(-PI.toFloat() / 2.0f, rotation.x), PI.toFloat() / 2.0f)
        rotation.y += (mouseXDelta.toFloat() * mouseSpeed) % (2.0f * PI.toFloat())
    }

}
