package club.sk1er.elementa.components

import club.sk1er.elementa.UIComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.opengl.GL11

/**
 * "Root" component. All components MUST have a Window in their hierarchy in order to do any rendering
 * or animating.
 */
class Window(val animationFPS: Int = 244) : UIComponent() {
    private var systemTime = -1L
    var scaledResolution = ScaledResolution(Minecraft.getMinecraft())

    init {
        super.parent = this
    }

    override fun draw() {
        GL11.glClear(GL11.GL_STENCIL_BUFFER_BIT)
        GL11.glClearStencil(0)

        scaledResolution = ScaledResolution(Minecraft.getMinecraft())

        if (systemTime == -1L) systemTime = System.currentTimeMillis()

        while (this.systemTime < System.currentTimeMillis() + 1000 / animationFPS) {
            animationFrame()

            this.systemTime += 1000 / animationFPS;
        }

        super.draw()
    }

    override fun getLeft(): Float {
        return 0f
    }

    override fun getTop(): Float {
        return 0f
    }

    override fun getWidth(): Float {
        return scaledResolution.scaledWidth.toFloat()
    }

    override fun getHeight(): Float {
        return scaledResolution.scaledHeight.toFloat()
    }

    override fun getRight() = getWidth()
    override fun getBottom() = getHeight()

    companion object {
        fun of(component: UIComponent): Window {
            var current = component

            try {
                while (current !is Window && current.parent != current) {
                    current = current.parent
                }
            } catch (e: UninitializedPropertyAccessException) {
                throw IllegalStateException("No window parent? It's possible you haven't called Window.addChild() at this point in time.")
            }

            return current as? Window ?: throw IllegalStateException("No window parent? It's possible you haven't called Window.addChild() at this point in time.")
        }
    }
}