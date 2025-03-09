package com.example.gimmea

import android.graphics.DiscretePathEffect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gimmea.ui.theme.GimmeaTheme
import com.example.gimmea.ui.theme.modernGray
import com.example.gimmea.ui.theme.modernWhite
import com.example.gimmea.ui.theme.modernYellow
import com.example.gimmea.viewmodels.DebugViewModel
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val debugViewModel: DebugViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GimmeaTheme {
                val categories by debugViewModel.categories
                    .map { it.keys.toList() }
                    .collectAsState(emptyList())
                
                val currentSuggestion by debugViewModel.currentSuggestion.collectAsState("")

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().background(modernGray).padding(innerPadding),
                    ) {
                        SuggestionDisplay(currentSuggestion)
                        CategoriesGrid(
                            modifier = Modifier
                                .weight(1f)
                                .clip(
                                    RoundedCornerShape(14.dp)
                                ),
                            categories = categories,
                            onItemClick = { item -> debugViewModel.setCurrentSuggestion(debugViewModel.categories.value[item]!!.random())}
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoriesGrid() {
    GimmeaTheme {
        CategoriesGrid(
            categories = listOf("Locations", "Jobs", "Relationships", "Lines from famous Movies"),
            onItemClick = {},
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewSuggestionsDisplay() {
    GimmeaTheme {
        SuggestionDisplay("suggestion")
    }
}


@Composable
fun SuggestionDisplay(
    suggestion: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f) // Ensures a square shape
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = suggestion,
            color = modernWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoriesGrid(categories: List<String>, onItemClick: (String) -> Unit, modifier: Modifier) {
    Box(modifier = modifier){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2-column grid
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(categories) { category ->
                CategoryItem(name = category, onClick = { item -> onItemClick(item) })
            }
        }
    }
}

@Composable
fun CategoryItem(name: String, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(
                onClickLabel = name,
                role = Role.Button
            ) { onClick(name) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardColors(
            containerColor = modernYellow,
            contentColor = modernGray,
            disabledContainerColor = modernWhite,
            disabledContentColor = modernGray
        )
    ) {
        Text(
            text = name,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun DashedBorderLayout(
    modifier: Modifier = Modifier,
    dashLength: Float = 10f,
    gapLength: Float = 10f,
    strokeWidth: Dp = 2.dp,
    borderColor: Color = Color.Black,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.drawBehind {
            val strokePx = strokeWidth.toPx()
            // Create the dashed effect using the dashLength and gapLength
            val dashEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength, gapLength),
                phase = 0f
            )

            val discretePathEffect = object: PathEffect {

            }
            // Draw a rectangle that fits the entire layout bounds.
            drawRect(
                color = borderColor,
                style = Stroke(width = strokePx, pathEffect = dashEffect)
            )
        },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 200, heightDp = 100)
@Composable
fun HandDrawnRectanglePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        DashedBorderLayout(
            modifier = Modifier.fillMaxSize()

        ) {
            Text("Sample Text")
        }
    }
}

@Composable
fun RoundedStrokeBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .drawBehind {
                // Define the stroke width and convert it from dp to pixels.
                val strokeWidth = 4.dp.toPx()
                // Calculate an inset so that the stroke is drawn fully within the bounds.
                val inset = strokeWidth / 2

                // Draw the rounded rectangle as a stroke.
                drawRoundRect(
                    color = Color.Blue,
                    topLeft = Offset(inset, inset),
                    size = Size(size.width - strokeWidth, size.height - strokeWidth),
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            }
            .padding(16.dp) // Inner padding for the content.
    ) {
        content()
    }
}

@Composable
fun HandDrawnRoundedStrokeBox(
    baseStrokeWidth: Dp = 4.dp,
    strokeColor: Color,
    backgroundColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .drawBehind {
                // Convert the base stroke width to pixels.
                val strokePx = baseStrokeWidth.toPx()
                // Inset the rectangle so the strokes aren’t clipped.
                val inset = strokePx
                val rect = Rect(inset, inset, size.width - inset, size.height - inset)
                val cornerRadiusPx = 20.dp.toPx()

                // Draw the filled rounded rectangle first
                drawRoundRect(
                    color = backgroundColor,
                    topLeft = Offset.Zero + Offset(inset, inset),
                    size = Size(size.width - inset, size.height - inset),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )

                // Generate points approximating the outline of a rounded rectangle.
                val points = generateRoundedRectPoints(rect, cornerRadiusPx, steps = 100)

                // Draw segments between points with variable stroke width.
                for (i in 0 until points.size - 1) {
                    // Calculate a parameter (t) for variation (from 0 to 1)
                    val t = i / (points.size - 1).toFloat()
                    // Vary the stroke width using a sine wave (adjust factors as desired)
                    val variableStroke = strokePx * (1f + 0.5f * kotlin.math.sin(2 * Math.PI * t).toFloat())
                    drawLine(
                        color = strokeColor,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = variableStroke,
                        cap = StrokeCap.Round
                    )
                }
                // Close the shape by connecting the last point to the first.
                val t = 1f
                val variableStroke = strokePx * (1f + 0.5f * kotlin.math.sin(2 * Math.PI * t).toFloat())
                drawLine(
                    color = Color.Blue,
                    start = points.last(),
                    end = points.first(),
                    strokeWidth = variableStroke,
                    cap = StrokeCap.Round
                )
            }
            .padding(16.dp)
    ) {
        content()
    }
}

// This helper function generates a list of points along the perimeter of a rounded rectangle.
fun generateRoundedRectPoints(rect: Rect, cornerRadius: Float, steps: Int): List<Offset> {
    val points = mutableListOf<Offset>()
    // Divide the outline roughly into eight segments:
    // four straight sides and four rounded corners.
    val lineSteps = steps / 4
    val arcSteps = steps / 4

    // Top side (from left+cornerRadius to right-cornerRadius)
    for (i in 0..lineSteps) {
        val x = rect.left + cornerRadius + i * (rect.width - 2 * cornerRadius) / lineSteps
        points.add(Offset(x, rect.top))
    }

    // Top-right arc
    val topRightCenter = Offset(rect.right - cornerRadius, rect.top + cornerRadius)
    for (i in 0..arcSteps) {
        // Angle from -90° to 0°
        val angle = -90f + (90f * i / arcSteps)
        val rad = Math.toRadians(angle.toDouble())
        val x = topRightCenter.x + cornerRadius * kotlin.math.cos(rad.toFloat())
        val y = topRightCenter.y + cornerRadius * kotlin.math.sin(rad.toFloat())
        points.add(Offset(x, y))
    }

    // Right side (from top+cornerRadius to bottom-cornerRadius)
    for (i in 0..lineSteps) {
        val y = rect.top + cornerRadius + i * (rect.height - 2 * cornerRadius) / lineSteps
        points.add(Offset(rect.right, y))
    }

    // Bottom-right arc
    val bottomRightCenter = Offset(rect.right - cornerRadius, rect.bottom - cornerRadius)
    for (i in 0..arcSteps) {
        // Angle from 0° to 90°
        val angle = 0f + (90f * i / arcSteps)
        val rad = Math.toRadians(angle.toDouble())
        val x = bottomRightCenter.x + cornerRadius * kotlin.math.cos(rad.toFloat())
        val y = bottomRightCenter.y + cornerRadius * kotlin.math.sin(rad.toFloat())
        points.add(Offset(x, y))
    }

    // Bottom side (from right-cornerRadius to left+cornerRadius)
    for (i in 0..lineSteps) {
        val x = rect.right - cornerRadius - i * (rect.width - 2 * cornerRadius) / lineSteps
        points.add(Offset(x, rect.bottom))
    }

    // Bottom-left arc
    val bottomLeftCenter = Offset(rect.left + cornerRadius, rect.bottom - cornerRadius)
    for (i in 0..arcSteps) {
        // Angle from 90° to 180°
        val angle = 90f + (90f * i / arcSteps)
        val rad = Math.toRadians(angle.toDouble())
        val x = bottomLeftCenter.x + cornerRadius * kotlin.math.cos(rad.toFloat())
        val y = bottomLeftCenter.y + cornerRadius * kotlin.math.sin(rad.toFloat())
        points.add(Offset(x, y))
    }

    // Left side (from bottom-cornerRadius to top+cornerRadius)
    for (i in 0..lineSteps) {
        val y = rect.bottom - cornerRadius - i * (rect.height - 2 * cornerRadius) / lineSteps
        points.add(Offset(rect.left, y))
    }

    // Top-left arc
    val topLeftCenter = Offset(rect.left + cornerRadius, rect.top + cornerRadius)
    for (i in 0..arcSteps) {
        // Angle from 180° to 270°
        val angle = 180f + (90f * i / arcSteps)
        val rad = Math.toRadians(angle.toDouble())
        val x = topLeftCenter.x + cornerRadius * kotlin.math.cos(rad.toFloat())
        val y = topLeftCenter.y + cornerRadius * kotlin.math.sin(rad.toFloat())
        points.add(Offset(x, y))
    }

    return points
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 400, heightDp = 200)
@Composable
fun ExampleUsage() {
    HandDrawnRoundedStrokeBox(
        strokeColor = modernGray,
        backgroundColor = modernYellow
    ) {
        Text(
            text = "Hand-drawn effect!",
            modifier = Modifier.padding(16.dp)
        )
    }
}


