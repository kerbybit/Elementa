package club.sk1er.elementa.constraints.animation

import club.sk1er.elementa.UIComponent
import club.sk1er.elementa.constraints.*
import java.awt.Color
import kotlin.math.max
import kotlin.math.roundToInt

sealed class AnimationComponent<T>(
    private val strategy: AnimationStrategy,
    private val totalFrames: Int,
    private val delayFrames: Int
) : SuperConstraint<T> {
    private var elapsedFrames = 0

    override fun animationFrame() {
        super.animationFrame()

        if (complete()) return

        elapsedFrames++
    }

    fun complete() = elapsedFrames - delayFrames >= totalFrames

    fun getPercentComplete() = strategy.getValue(max(elapsedFrames - delayFrames, 0).toFloat() / totalFrames.toFloat())
}

class XAnimationComponent(
    strategy: AnimationStrategy,
    totalFrames: Int,
    private val oldConstraint: XConstraint,
    val newConstraint: XConstraint,
    delay: Int
) : AnimationComponent<Float>(strategy, totalFrames, delay), XConstraint {
    override var cachedValue = 0f
    override var recalculate = true

    override fun getXPositionImpl(component: UIComponent, parent: UIComponent): Float {
        val startX = oldConstraint.getXPosition(component, parent)
        val finalX = newConstraint.getXPosition(component, parent)

        return startX + ((finalX - startX) * getPercentComplete())
    }

    // TODO: This is gross, can probably be done in parent!
    override fun animationFrame() {
        super<AnimationComponent>.animationFrame()

        oldConstraint.animationFrame()
        newConstraint.animationFrame()
    }
}

class YAnimationComponent(
    strategy: AnimationStrategy,
    totalFrames: Int,
    private val oldConstraint: YConstraint,
    val newConstraint: YConstraint,
    delay: Int
) : AnimationComponent<Float>(strategy, totalFrames, delay), YConstraint {
    override var cachedValue = 0f
    override var recalculate = true

    override fun getYPositionImpl(component: UIComponent, parent: UIComponent): Float {
        val startX = oldConstraint.getYPosition(component, parent)
        val finalX = newConstraint.getYPosition(component, parent)

        return startX + ((finalX - startX) * getPercentComplete())
    }
    override fun animationFrame() {
        super<AnimationComponent>.animationFrame()

        oldConstraint.animationFrame()
        newConstraint.animationFrame()
    }
}

class WidthAnimationComponent(
    strategy: AnimationStrategy,
    totalFrames: Int,
    private val oldConstraint: WidthConstraint,
    val newConstraint: WidthConstraint,
    delay: Int
) : AnimationComponent<Float>(strategy, totalFrames, delay), WidthConstraint {
    override var cachedValue = 0f
    override var recalculate = true

    override fun getWidthImpl(component: UIComponent, parent: UIComponent): Float {
        val startX = oldConstraint.getWidth(component, parent)
        val finalX = newConstraint.getWidth(component, parent)

        return startX + ((finalX - startX) * getPercentComplete())
    }

    override fun animationFrame() {
        super<AnimationComponent>.animationFrame()

        oldConstraint.animationFrame()
        newConstraint.animationFrame()
    }
}

class HeightAnimationComponent(
    strategy: AnimationStrategy,
    totalFrames: Int,
    private val oldConstraint: HeightConstraint,
    val newConstraint: HeightConstraint,
    delay: Int
) : AnimationComponent<Float>(strategy, totalFrames, delay), HeightConstraint {
    override var cachedValue = 0f
    override var recalculate = true

    override fun getHeightImpl(component: UIComponent, parent: UIComponent): Float {
        val startX = oldConstraint.getHeight(component, parent)
        val finalX = newConstraint.getHeight(component, parent)

        return startX + ((finalX - startX) * getPercentComplete())
    }

    override fun animationFrame() {
        super<AnimationComponent>.animationFrame()

        oldConstraint.animationFrame()
        newConstraint.animationFrame()
    }
}

class ColorAnimationComponent(
    strategy: AnimationStrategy,
    totalFrames: Int,
    private val oldConstraint: ColorConstraint,
    val newConstraint: ColorConstraint,
    delay: Int
) : AnimationComponent<Color>(strategy, totalFrames, delay), ColorConstraint {
    override var cachedValue = Color.WHITE
    override var recalculate = true

    override fun getColorImpl(component: UIComponent, parent: UIComponent): Color {
        val startColor = oldConstraint.getColor(component, parent)
        val endColor = newConstraint.getColor(component, parent)
        val percentComplete = getPercentComplete()

        val newR = startColor.red + ((endColor.red - startColor.red) * percentComplete)
        val newG = startColor.green + ((endColor.green - startColor.green) * percentComplete)
        val newB = startColor.blue + ((endColor.blue - startColor.blue) * percentComplete)
        val newA = startColor.alpha + ((endColor.alpha - startColor.alpha) * percentComplete)

        return Color(newR.roundToInt(), newG.roundToInt(), newB.roundToInt(), newA.roundToInt())
    }

    override fun animationFrame() {
        super<AnimationComponent>.animationFrame()

        oldConstraint.animationFrame()
        newConstraint.animationFrame()
    }
}