package com.miniamigixv.miniamigixv_app.music.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.music.data.model.Track
import java.text.SimpleDateFormat
import java.util.*

private val AccentPurple = Color(0xFF8B5CF6)
private val AccentBlue = Color(0xFF22D3EE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    onBack: () -> Unit = {},
    musicViewModel: MusicViewModel = viewModel()
) {
    val state = musicViewModel.state

    // Time display
    val currentTime = remember { mutableStateOf(getCurrentTime()) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000)
            currentTime.value = getCurrentTime()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val isCompact = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            TopAppBar(
                title = { Text("Música", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    Text(
                        currentTime.value,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isCompact) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    LeftContent(isCompact, state, musicViewModel)
                    RightContent(state, musicViewModel)
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left Column: Now Playing, Video, Controls, Lyrics
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LeftContent(isCompact, state, musicViewModel)
                    }

                    // Right Column: Add Track, Sound Matrix, Downloader
                    Column(
                        modifier = Modifier.width(300.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        RightContent(state, musicViewModel)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun LeftContent(isCompact: Boolean, state: MusicUiState, musicViewModel: MusicViewModel) {
    val visualizerSize = if (isCompact) 80.dp else 120.dp
    val playButtonSize = if (isCompact) 48.dp else 56.dp
    val paddingInsideCard = if (isCompact) 16.dp else 20.dp

    // Now Playing Section
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(paddingInsideCard),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "REPRODUCIENDO AHORA",
                fontSize = 12.sp,
                color = AccentPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Circular Visualizer
            Box(
                modifier = Modifier
                    .size(visualizerSize)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                AccentPurple.copy(alpha = 0.3f),
                                AccentBlue.copy(alpha = 0.1f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.MusicNote,
                    contentDescription = null,
                    modifier = Modifier.size(if (isCompact) 32.dp else 48.dp),
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                state.currentTrackName,
                fontSize = if (isCompact) 16.sp else 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                state.currentArtist,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }

    // Video Player Section
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(paddingInsideCard),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "VIDEO",
                fontSize = 12.sp,
                color = AccentBlue,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompact) 120.dp else 150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF09090B)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.PlayCircleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(if (isCompact) 36.dp else 48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "El reproductor aparecerá aquí",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    // Playback Controls
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingInsideCard),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Previous */ }) {
                Icon(Icons.Filled.SkipPrevious, contentDescription = "Anterior", tint = MaterialTheme.colorScheme.onBackground)
            }
            Box(
                modifier = Modifier
                    .size(playButtonSize)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(AccentPurple, AccentBlue)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { /* Play/Pause */ },
                    modifier = Modifier.size(playButtonSize)
                ) {
                    Icon(
                        if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "Reproducir",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(if (isCompact) 24.dp else 32.dp)
                    )
                }
            }
            IconButton(onClick = { /* Next */ }) {
                Icon(Icons.Filled.SkipNext, contentDescription = "Siguiente", tint = MaterialTheme.colorScheme.onBackground)
            }
            IconButton(onClick = { /* Shuffle */ }) {
                Icon(Icons.Filled.Shuffle, contentDescription = "Aleatorio", tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

    // Lyric Stream Section
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(paddingInsideCard)
        ) {
            Text(
                "TRANSMISIÓN LÍRICA",
                fontSize = 12.sp,
                color = AccentPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "No encontré esta letra en la base de datos.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { musicViewModel.searchLyricsOnGoogle() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
            ) {
                Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("BUSCAR EN GOOGLE")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RightContent(state: MusicUiState, musicViewModel: MusicViewModel) {
    var addTrackName by remember { mutableStateOf("") }
    var addTrackArtist by remember { mutableStateOf("") }
    var addTrackYoutube by remember { mutableStateOf("") }
    var downloadUrl by remember { mutableStateOf("") }
    var downloadFormat by remember { mutableStateOf("MP3 (Audio)") }
    val formats = listOf("MP3 (Audio)", "MP4 (Video)", "WEBM")

    // Add Track Section
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "AÑADIR PISTA",
                fontSize = 12.sp,
                color = AccentPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = addTrackName,
                onValueChange = { addTrackName = it },
                label = { Text("Nombre de la canción", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentPurple,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = addTrackArtist,
                onValueChange = { addTrackArtist = it },
                label = { Text("Artista (opcional)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentPurple,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = addTrackYoutube,
                onValueChange = { addTrackYoutube = it },
                label = { Text("YouTube Link (opcional)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentPurple,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    musicViewModel.addTrack(addTrackName, addTrackArtist, addTrackYoutube.ifBlank { null })
                    addTrackName = ""
                    addTrackArtist = ""
                    addTrackYoutube = ""
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentPurple
                )
            ) {
                Text("VINCULAR A LA MATRIZ", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
            }
        }
    }

    // Sound Matrix Section
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "TU MATRIZ SONORA",
                fontSize = 12.sp,
                color = AccentBlue,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Not using LazyColumn here to prevent nested scrolling issues within the main column
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.tracks.forEach { track ->
                    TrackItem(
                        track = track,
                        onSelect = { musicViewModel.selectTrack(track.id) },
                        onDelete = { musicViewModel.deleteTrack(track.id) }
                    )
                }
            }
        }
    }

    // Music Downloader Section
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "DESCARGADOR DE MÚSICA",
                fontSize = 12.sp,
                color = AccentPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = downloadUrl,
                onValueChange = { downloadUrl = it },
                label = { Text("URL de YouTube", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentPurple,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
                ) {
                    Text(downloadFormat)
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    formats.forEach { format ->
                        DropdownMenuItem(
                            text = { Text(format, color = MaterialTheme.colorScheme.onBackground) },
                            onClick = {
                                downloadFormat = format
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Download */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentBlue
                )
            ) {
                Icon(Icons.Filled.Download, contentDescription = null, tint = MaterialTheme.colorScheme.background)
                Spacer(modifier = Modifier.width(8.dp))
                Text("DESCARGAR", color = MaterialTheme.colorScheme.background, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun TrackItem(
    track: Track,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (track.isSelected) AccentPurple.copy(alpha = 0.2f) else Color.Transparent)
            .clickable(onClick = onSelect)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = track.isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = AccentPurple,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = track.name,
                fontSize = 13.sp,
                fontWeight = if (track.isSelected) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = track.artist,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "NEURAL FREQUENCY: ${track.neuralFrequency}",
                fontSize = 10.sp,
                color = AccentBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun getCurrentTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}
