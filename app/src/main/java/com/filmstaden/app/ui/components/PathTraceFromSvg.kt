package com.filmstaden.app.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PathMeasure
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.delay
import org.xmlpull.v1.XmlPullParser
import kotlin.math.min
import android.graphics.Path as AndroidPath

private const val MIN_PATH_LENGTH = 0.0001f
private const val AUTO_THIN_FACTOR = 0.45f
private const val MIN_STROKE_WIDTH_PX = 0.75f
private const val COMPLETION_EPSILON = 1e-3f

data class StyledPath(
    val rawPath: String,
    val fillColor: Color? = null,
    val strokeColor: Color? = null,
    val strokeWidthPxAt1x: Float? = null,
    val fillAlpha: Float = 1f,
    val strokeAlpha: Float = 1f,
    val cap: StrokeCap = StrokeCap.Round,
    val join: StrokeJoin = StrokeJoin.Round,
    val fillTypeEvenOdd: Boolean = false
)

data class SvgPathSpec(
    val viewportWidth: Float,
    val viewportHeight: Float,
    val paths: List<StyledPath>
)

fun SvgPathSpec(
    viewportWidth: Float,
    viewportHeight: Float,
    d: String
): SvgPathSpec = SvgPathSpec(
    viewportWidth = viewportWidth,
    viewportHeight = viewportHeight,
    paths = listOf(StyledPath(rawPath = d))
)

private data class BuiltPath(
    val styled: StyledPath,
    val androidPath: AndroidPath,
    val length: Float
)

private fun buildPaths(spec: SvgPathSpec): List<BuiltPath> {
    return spec.paths.map { styledPath ->
        val composePath = PathParser()
            .parsePathString(styledPath.rawPath)
            .toPath()
            .apply {
                fillType = if (styledPath.fillTypeEvenOdd) PathFillType.EvenOdd
                else PathFillType.NonZero
            }

        val androidPath = composePath.asAndroidPath()
        val pathMeasure = PathMeasure(androidPath, false)

        var totalLength = 0f
        do {
            totalLength += pathMeasure.length
        } while (pathMeasure.nextContour())

        BuiltPath(
            styled = styledPath,
            androidPath = androidPath,
            length = if (totalLength <= 0f) MIN_PATH_LENGTH else totalLength
        )
    }
}

@Composable
fun PathTraceStyled(
    spec: SvgPathSpec,
    progress: Float,
    modifier: Modifier = Modifier,
    defaultStrokeColor: Color = Color.White,
    defaultStrokeWidth: Dp = 2.dp,
    autoThin: Boolean = true
) {
    val builtPaths = remember(spec) { buildPaths(spec) }
    val totalLength = remember(builtPaths) {
        builtPaths.sumOf { it.length.toDouble() }.toFloat()
    }

    Canvas(modifier = modifier) {
        if (totalLength <= 0f) return@Canvas

        val clampedProgress = progress.coerceIn(0f, 1f)
        val targetLength = clampedProgress * totalLength

        val scaleX = size.width / spec.viewportWidth
        val scaleY = size.height / spec.viewportHeight
        val scale = min(scaleX, scaleY)

        val translateX = (size.width - spec.viewportWidth * scale) / 2f
        val translateY = (size.height - spec.viewportHeight * scale) / 2f

        val baseMatrix = Matrix().apply {
            setScale(scale, scale)
            postTranslate(translateX, translateY)
        }

        fun createStrokePaint(styledPath: StyledPath, finalStrokePx: Float): Paint {
            return Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeWidth = finalStrokePx
                color = (styledPath.strokeColor ?: defaultStrokeColor).toArgb()
                alpha = ((styledPath.strokeColor?.alpha ?: 1f) * 255)
                    .toInt().coerceIn(0, 255)

                strokeCap = when (styledPath.cap) {
                    StrokeCap.Butt -> Paint.Cap.BUTT
                    StrokeCap.Round -> Paint.Cap.ROUND
                    StrokeCap.Square -> Paint.Cap.SQUARE
                    else -> Paint.Cap.ROUND
                }

                strokeJoin = when (styledPath.join) {
                    StrokeJoin.Round -> Paint.Join.ROUND
                    StrokeJoin.Miter -> Paint.Join.MITER
                    StrokeJoin.Bevel -> Paint.Join.BEVEL
                    else -> Paint.Join.ROUND
                }

                strokeMiter = 1f
            }
        }

        fun createFillPaint(styledPath: StyledPath): Paint? {
            val color = styledPath.fillColor ?: return null
            return Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                this.color = color.toArgb()
                alpha = (styledPath.fillAlpha * 255).toInt().coerceIn(0, 255)
            }
        }

        drawIntoCanvas { canvas ->
            var accumulatedLength = 0f

            builtPaths.forEach { builtPath ->
                val isPathCompleted =
                    targetLength >= accumulatedLength + builtPath.length - COMPLETION_EPSILON
                if (isPathCompleted) {
                    val fillPath = AndroidPath(builtPath.androidPath)
                    fillPath.transform(baseMatrix)
                    createFillPaint(builtPath.styled)?.let { paint ->
                        canvas.nativeCanvas.drawPath(fillPath, paint)
                    }
                }
                accumulatedLength += builtPath.length
            }

            var remainingLength = targetLength

            builtPaths.forEach { builtPath ->
                if (remainingLength <= 0f) return@forEach

                val pathDrawLength = remainingLength.coerceAtMost(builtPath.length)
                if (pathDrawLength > 0f) {
                    val tracedPath = AndroidPath()
                    val pathMeasure = PathMeasure(builtPath.androidPath, false)
                    var lengthLeftOnPath = pathDrawLength

                    do {
                        val contourLength = pathMeasure.length
                        if (lengthLeftOnPath <= 0f) break

                        val segmentLength = lengthLeftOnPath.coerceAtMost(contourLength)
                        if (segmentLength > 0f) {
                            pathMeasure.getSegment(0f, segmentLength, tracedPath, true)
                        }
                        lengthLeftOnPath -= segmentLength
                    } while (pathMeasure.nextContour())

                    tracedPath.transform(baseMatrix)

                    val desiredStrokePx =
                        builtPath.styled.strokeWidthPxAt1x ?: defaultStrokeWidth.toPx()
                    val maxStrokePx = AUTO_THIN_FACTOR * scale
                    val finalStrokePx = if (autoThin) {
                        min(desiredStrokePx, maxStrokePx).coerceAtLeast(MIN_STROKE_WIDTH_PX)
                    } else {
                        desiredStrokePx
                    }

                    val strokePaint = createStrokePaint(builtPath.styled, finalStrokePx)
                    canvas.nativeCanvas.drawPath(tracedPath, strokePaint)
                }

                remainingLength -= pathDrawLength
            }
        }
    }
}

@SuppressLint("ResourceType")
fun loadPathSpecFromVectorDrawable(
    context: Context,
    @DrawableRes drawableResId: Int
): SvgPathSpec {
    val parser = context.resources.getXml(drawableResId)
    val androidNs = "http://schemas.android.com/apk/res/android"

    fun parseColorAttr(name: String): Color? {
        val resId = parser.getAttributeResourceValue(androidNs, name, 0)
        if (resId != 0) return Color(context.getColor(resId))
        val raw = parser.getAttributeValue(androidNs, name) ?: return null
        return try {
            Color(raw.toColorInt())
        } catch (_: Throwable) {
            null
        }
    }

    fun parseFloatAttr(name: String, fallback: Float? = null): Float? {
        val raw = parser.getAttributeValue(androidNs, name) ?: return fallback
        return raw.toFloatOrNull() ?: fallback
    }

    var viewportWidth = 24f
    var viewportHeight = 24f
    val styledPaths = mutableListOf<StyledPath>()

    var eventType = parser.eventType
    while (eventType != XmlPullParser.END_DOCUMENT) {
        if (eventType == XmlPullParser.START_TAG) {
            when (parser.name) {
                "vector" -> {
                    viewportWidth = parser
                        .getAttributeValue(androidNs, "viewportWidth")
                        ?.toFloatOrNull() ?: viewportWidth
                    viewportHeight = parser
                        .getAttributeValue(androidNs, "viewportHeight")
                        ?.toFloatOrNull() ?: viewportHeight
                }

                "path" -> {
                    val pathData = parser.getAttributeValue(androidNs, "pathData")
                    if (!pathData.isNullOrBlank()) {
                        val fillColor = parseColorAttr("fillColor")
                        val strokeColor = parseColorAttr("strokeColor")
                        val strokeWidth = parseFloatAttr("strokeWidth")
                        val fillAlpha = parseFloatAttr("fillAlpha", 1f) ?: 1f
                        val strokeAlpha = parseFloatAttr("strokeAlpha", 1f) ?: 1f

                        val cap = when (parser.getAttributeValue(androidNs, "strokeLineCap")) {
                            "butt" -> StrokeCap.Butt
                            "square" -> StrokeCap.Square
                            else -> StrokeCap.Round
                        }

                        val join = when (parser.getAttributeValue(androidNs, "strokeLineJoin")) {
                            "miter" -> StrokeJoin.Miter
                            "bevel" -> StrokeJoin.Bevel
                            else -> StrokeJoin.Round
                        }

                        val isFillTypeEvenOdd =
                            parser.getAttributeValue(androidNs, "fillType") == "evenOdd"

                        styledPaths += StyledPath(
                            rawPath = pathData,
                            fillColor = fillColor?.copy(alpha = fillAlpha),
                            strokeColor = strokeColor?.copy(alpha = strokeAlpha),
                            strokeWidthPxAt1x = strokeWidth,
                            fillAlpha = fillAlpha,
                            strokeAlpha = strokeAlpha,
                            cap = cap,
                            join = join,
                            fillTypeEvenOdd = isFillTypeEvenOdd
                        )
                    }
                }
            }
        }
        eventType = parser.next()
    }

    require(styledPaths.isNotEmpty()) {
        "No <path android:pathData=\"...\"> found in vector drawable #$drawableResId"
    }

    return SvgPathSpec(
        viewportWidth = viewportWidth,
        viewportHeight = viewportHeight,
        paths = styledPaths
    )
}

@Composable
fun PathTraceFromSvg(
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier,
    speedMs: Int = 3800,
    pauseMs: Int = 1000,
    easing: Easing = LinearEasing,
    stopSignal: Boolean = false,
    stopAtEndOfCurrentCycle: Boolean = true,
    strokeColor: Color = Color.White,
    onCycle: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val spec = remember(drawableId) { loadPathSpecFromVectorDrawable(context, drawableId) }

    val progress = remember { Animatable(0f) }
    val stopRef = rememberUpdatedState(stopSignal)

    LaunchedEffect(drawableId, speedMs, pauseMs, easing) {
        while (true) {
            progress.snapTo(0f)
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = speedMs, easing = easing)
            )

            val requestedStop = stopRef.value
            val stopAtEnd = requestedStop && stopAtEndOfCurrentCycle
            val shouldLoop = !requestedStop

            if (pauseMs > 0 && (shouldLoop || requestedStop)) {
                delay(pauseMs.toLong())
            }

            onCycle?.invoke()

            if (stopAtEnd || (requestedStop && !stopAtEndOfCurrentCycle)) {
                progress.snapTo(1f)
                break
            }
        }
    }

    PathTraceStyled(
        spec = spec,
        progress = progress.value,
        modifier = modifier,
        defaultStrokeColor = strokeColor
    )
}
