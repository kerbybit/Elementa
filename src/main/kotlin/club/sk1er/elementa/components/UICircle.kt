package club.sk1er.elementa.components

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.constraints.ConstantColorConstraint
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class UICircle @JvmOverloads constructor(private val steps: Int = 40) : UIComponent() {
    init {
        setColor(ConstantColorConstraint(Color(0, 0, 0, 0)))
    }

    override fun draw() {
        beforeDraw()

        val x = getLeft().toDouble()
        val y = getTop().toDouble()
        val r = getWidth().toDouble() / 2

        val color = this.getColor()
        if (color.alpha == 0) return super.draw()


        GL11.glPushMatrix()

        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)

        GlStateManager.color(color.red.toFloat() / 255f, color.green.toFloat() / 255f, color.blue.toFloat() / 255f, color.alpha.toFloat() / 255f)

        worldRenderer.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION)

        var theta = 0.0
        for (i in 0 until steps) {
            worldRenderer.pos(cos(theta) * r + x,  -sin(theta) * r + y, 0.0).endVertex()
            theta += (2 * PI) / steps
        }

        tessellator.draw()

        GlStateManager.color(1f, 1f, 1f, 1f)

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()

        GL11.glPopMatrix()

        super.draw()
    }
}