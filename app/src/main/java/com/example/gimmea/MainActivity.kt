package com.example.gimmea

import android.graphics.DiscretePathEffect
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.util.lerp
import com.example.gimmea.ui.theme.GimmeaTheme
import com.example.gimmea.ui.theme.modernGray
import com.example.gimmea.ui.theme.modernWhite
import com.example.gimmea.ui.theme.modernYellow
import com.example.gimmea.viewmodels.DebugViewModel
import kotlinx.coroutines.flow.map
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow

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
                        modifier = Modifier
                            .fillMaxSize()
                            .background(modernGray)
                            .padding(innerPadding),
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

@Preview(showBackground = false)
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
        modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(modernGray)
    ){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(modernYellow)
                .aspectRatio(1f) // Ensures a square shape
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = suggestion,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                },
                label = "SuggestionFadeAnimation"
            ) { targetSuggestion ->
                Text(
                    text = targetSuggestion,
                    color = modernGray,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CategoriesGrid(categories: List<String>, onItemClick: (String) -> Unit, modifier: Modifier) {
    Box(modifier = modifier){
        val listState = rememberLazyGridState() // Track scroll position
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2-column grid
            modifier = Modifier
                .fillMaxSize()
                .fillMaxHeight(),
            state = listState,
            contentPadding = PaddingValues(4.dp)
        ) {
            itemsIndexed(categories) { index, category ->
                val itemOffset by remember {
                    derivedStateOf {
                        val layoutInfo = listState.layoutInfo
                        val viewportHeight = layoutInfo.viewportSize.height.toFloat()

                        // Get the item offset within the viewport
                        layoutInfo.visibleItemsInfo.find { it.index == index }?.let { itemInfo ->
                            val centerY = (itemInfo.offset.y + itemInfo.size.height / 2).toFloat()
                            val distanceFromCenter = abs(centerY - viewportHeight / 2) / (viewportHeight / 2)
                            distanceFromCenter.coerceIn(0f, 1f)
                        } ?: 0f
                    }
                }

                val threshold = 0.8f
                val effectStrength = easeInEffect(itemOffset, threshold) // 0 when near center, strong near edges

                val scale = 1f - (0.5f * effectStrength)
                val alpha = max(1f - (1.5f * effectStrength), 0f)

                CategoryItem(
                    name = category,
                    modifier = modifier.graphicsLayer {
                        this.scaleX = scale
                        this.scaleY = scale
                        this.alpha = alpha
                    },
                    onClick = { item -> onItemClick(item) })
            }
        }
    }
}

fun easeInEffect(distance: Float, threshold: Float): Float {
    return if (distance > threshold) {
        ((distance - threshold) / (1f - threshold)).pow(2) // Quadratic ease-in
    } else {
        0f
    }
}

@Composable
fun CategoryItem(name: String, modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    Card(
        modifier = modifier
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


