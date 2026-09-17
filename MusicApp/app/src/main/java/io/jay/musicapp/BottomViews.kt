package io.jay.musicapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun HomeView() {
    val categories = listOf("Hits", "Happy", "Workout", "Running", "TGIF", "Yoga")
    val grouped = listOf("New Release", "Favorites", "Top Rated").groupBy { it }

    LazyColumn {
        grouped.forEach { entry ->
            stickyHeader {
                Text(entry.value[0], modifier = Modifier.padding(16.dp))

                LazyRow {
                    items(categories) { c ->
                        BrowserItem(category = c, drawable = R.drawable.outline_music_note_add_24)
                    }
                }
            }
        }
    }
}

@Composable
fun BrowserItem(category: String, drawable: Int) {
    Card(modifier = Modifier.padding(16.dp).size(200.dp), border = BorderStroke(3.dp, color = Color.DarkGray), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(category)
            Image(painter = painterResource(drawable), contentDescription = category)
        }
    }
}

@Composable
fun LibraryView() {
    val items: List<Pair<Int, String>> = listOf(
        Pair(R.drawable.outline_playlist_play_24, "Playlist"),
        Pair(R.drawable.outline_artist_24, "Artists"),
        Pair(R.drawable.outline_album_24, "Album"),
        Pair(R.drawable.outline_music_note_24, "Songs"),
        Pair(R.drawable.outline_genres_24, "Genre"),
    )

    LazyColumn() {
        items(items) { (icon, name) ->
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row() {
                    Image(painter = painterResource(icon), contentDescription = name)
                    Text(name)
                }

                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
            Divider(color = Color.LightGray)
        }
    }
}

@Composable
fun BrowseView() {
    val categories = listOf("Hits", "Happy", "Workout", "Running", "TGIF", "Yoga")

    LazyVerticalGrid(GridCells.Fixed(2)) {
        items(categories) { c->
            BrowserItem(category = c, drawable = R.drawable.outline_browse_gallery_24)
        }
    }
}