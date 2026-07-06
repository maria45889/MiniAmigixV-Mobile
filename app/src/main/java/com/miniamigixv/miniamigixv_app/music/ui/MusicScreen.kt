package com.miniamigixv.miniamigixv_app.music.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miniamigixv.miniamigixv_app.music.data.model.Track
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    onBack: () -> Unit = {},
    musicViewModel: MusicViewModel = viewModel()
) {
    val state = musicViewModel.state
    val favoriteIds = remember { mutableStateListOf<String>() }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF0B0F1A), Color(0xFF0F172A))
                )
            )
            .systemBarsPadding()
    ) {
        val isCompact = maxWidth < 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(
                title = {
                    Text(
                        "Música",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
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
                    NowPlayingSection(state, musicViewModel, isCompact)
                    VideoLyricsGrid(state, musicViewModel, isCompact)
                    LibrarySection(state, musicViewModel, favoriteIds)
                    FavoritesSection(state, favoriteIds)
                    StatsSection(state, favoriteIds)
                    RecommendationsSection()
                    AddTrackCard(musicViewModel)
                    DownloaderSection()
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        NowPlayingSection(state, musicViewModel, isCompact)
                        VideoLyricsGrid(state, musicViewModel, isCompact)
                    }
                    Column(
                        modifier = Modifier.width(300.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        LibrarySection(state, musicViewModel, favoriteIds)
                        FavoritesSection(state, favoriteIds)
                        StatsSection(state, favoriteIds)
                        RecommendationsSection()
                        AddTrackCard(musicViewModel)
                        DownloaderSection()
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun NowPlayingSection(
    state: MusicUiState,
    viewModel: MusicViewModel,
    isCompact: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
    val vinylRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "vinylRotation"
    )

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                "\uD83C\uDFB5 REPRODUCIENDO AHORA",
                fontSize = 12.sp,
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isCompact) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    VinylRecord(isPlaying = state.isPlaying, rotation = vinylRotation)
                    NowPlayingInfoContent(state, viewModel, modifier = Modifier.fillMaxWidth())
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VinylRecord(isPlaying = state.isPlaying, rotation = vinylRotation)
                    NowPlayingInfoContent(state, viewModel, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun VinylRecord(isPlaying: Boolean, rotation: Float) {
    val displayRotation = if (isPlaying) rotation else 0f

    Box(
        modifier = Modifier
            .size(140.dp)
            .rotate(displayRotation)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A1A1A),
                        Color.Black,
                        Color(0xFF111111),
                        Color.Black,
                        Color(0xFF111111),
                        Color.Black,
                        Color(0xFF111111),
                        Color.Black,
                        Color(0xFF111111),
                        Color.Black
                    )
                )
            )
            .border(2.dp, Color.White.copy(alpha = 0.1f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFF111111))
                .border(2.dp, NeonPurple.copy(alpha = 0.7f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            )
        }
    }
}

@Composable
private fun NowPlayingInfoContent(
    state: MusicUiState,
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    var volume by remember { mutableStateOf(0.7f) }

    Column(modifier = modifier) {
        Text(
            state.currentTrackName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            state.currentArtist,
            fontSize = 15.sp,
            color = Color(0xFF94A3B8),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("0:00", fontSize = 11.sp, color = Color(0xFF94A3B8))
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(state.progress)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(NeonBlue, NeonPurple)
                            )
                        )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("0:00", fontSize = 11.sp, color = Color(0xFF94A3B8))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { viewModel.toggleShuffle() }) {
                Icon(
                    Icons.Filled.Shuffle,
                    contentDescription = "Aleatorio",
                    tint = if (state.isShuffleEnabled) NeonPurple else Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(22.dp)
                )
            }
            IconButton(onClick = { viewModel.playPrevious() }) {
                Icon(
                    Icons.Filled.SkipPrevious,
                    contentDescription = "Anterior",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(NeonPurple, NeonBlue)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { viewModel.togglePlayPause() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        if (state.isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = "Reproducir",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            IconButton(onClick = { viewModel.playNext() }) {
                Icon(
                    Icons.Filled.SkipNext,
                    contentDescription = "Siguiente",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp)
                )
            }
            IconButton(onClick = { viewModel.toggleRepeat() }) {
                Icon(
                    Icons.Filled.Repeat,
                    contentDescription = "Repetir",
                    tint = if (state.isRepeatEnabled) NeonPurple else Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("\uD83D\uDD0A", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Slider(
                value = volume,
                onValueChange = { volume = it },
                colors = SliderDefaults.colors(
                    thumbColor = NeonPurple,
                    activeTrackColor = NeonBlue,
                    inactiveTrackColor = Color.White.copy(alpha = 0.1f)
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun VideoLyricsGrid(
    state: MusicUiState,
    viewModel: MusicViewModel,
    isCompact: Boolean
) {
    if (isCompact) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            VideoSection()
            LyricsSection(state, viewModel)
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            VideoSection(modifier = Modifier.weight(1.5f))
            LyricsSection(state, viewModel, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun VideoSection(modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "\uD83C\uDFAC VIDEO",
                    fontSize = 12.sp,
                    color = NeonBlue,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    "\uD83D\uDC41\u200D",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonPurple.copy(alpha = 0.15f))
                        .border(1.dp, NeonPurple.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .clickable { }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF050505)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("\uD83C\uDFAC", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Selecciona una canci\u00F3n",
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0A0F1E).copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text("\u23EE", fontSize = 20.sp, color = Color.White.copy(alpha = 0.6f))
                Text("\u25B6\uFE0F", fontSize = 24.sp, color = NeonBlue)
                Text("\uD83D\uDCFA", fontSize = 20.sp, color = Color.White.copy(alpha = 0.6f))
                Text("\u23ED", fontSize = 20.sp, color = Color.White.copy(alpha = 0.6f))
                Text("\uD83D\uDD0A", fontSize = 20.sp, color = Color.White.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
private fun LyricsSection(
    state: MusicUiState,
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(NeonBlue)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "TRANSMISI\u00D3N L\u00CDRICA",
                        fontSize = 12.sp,
                        color = NeonBlue,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
                Text(
                    "A ESPA\u00D1OL \uD83D\uDD04",
                    fontSize = 11.sp,
                    color = NeonBlue,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(NeonBlue.copy(alpha = 0.15f))
                        .border(1.dp, NeonBlue.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .clickable { }
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("\u270D\uFE0F", fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No encontr\u00E9 esta letra en la base de datos.",
                        fontSize = 14.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { viewModel.searchLyricsOnGoogle() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonBlue),
                        border = BorderStroke(1.dp, NeonBlue.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            "BUSCAR EN GOOGLE \uD83D\uDD0D",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LibrarySection(
    state: MusicUiState,
    viewModel: MusicViewModel,
    favoriteIds: MutableList<String>
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredTracks = remember(state.tracks, searchQuery) {
        if (searchQuery.isBlank()) state.tracks
        else state.tracks.filter { track ->
            track.name.contains(searchQuery, ignoreCase = true) ||
                track.artist.contains(searchQuery, ignoreCase = true)
        }
    }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "\uD83C\uDFB6 TU BIBLIOTECA",
                    fontSize = 13.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    "${state.tracks.size}",
                    fontSize = 12.sp,
                    color = Color(0xFFA78BFA),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            NeonPurple.copy(alpha = 0.15f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        "\uD83D\uDD0D Buscar canci\u00F3n...",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = NeonBlue
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (filteredTracks.isEmpty()) {
                    Text(
                        "\uD83C\uDFB5 Tu biblioteca est\u00E1 vac\u00EDa",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    filteredTracks.forEach { track ->
                        TrackItem(
                            track = track,
                            isFavorite = favoriteIds.contains(track.id),
                            onSelect = { viewModel.selectTrack(track.id) },
                            onToggleFavorite = {
                                if (favoriteIds.contains(track.id)) {
                                    favoriteIds.remove(track.id)
                                } else {
                                    favoriteIds.add(track.id)
                                }
                            },
                            onEdit = { },
                            onDelete = { viewModel.deleteTrack(track.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrackItem(
    track: Track,
    isFavorite: Boolean,
    onSelect: () -> Unit,
    onToggleFavorite: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (track.isSelected) NeonPurple.copy(alpha = 0.1f)
                else Color.Transparent
            )
            .border(
                if (track.isSelected) BorderStroke(1.dp, NeonPurple.copy(alpha = 0.3f))
                else BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onSelect)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .border(
                    2.dp,
                    if (track.isSelected) NeonPurple else Color.White.copy(alpha = 0.3f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (track.isSelected) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(NeonPurple)
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                track.name,
                fontSize = 13.sp,
                fontWeight = if (track.isSelected) FontWeight.Bold else FontWeight.Normal,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                track.artist,
                fontSize = 11.sp,
                color = Color(0xFF94A3B8),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                "NEURAL FREQUENCY: ${track.neuralFrequency}",
                fontSize = 10.sp,
                color = NeonBlue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        IconButton(onClick = onToggleFavorite, modifier = Modifier.size(28.dp)) {
            Icon(
                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favorito",
                tint = if (isFavorite) Color(0xFFEF4444) else Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
        }
        IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Editar",
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
        }
        IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Eliminar",
                tint = Color.White.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun FavoritesSection(
    state: MusicUiState,
    favoriteIds: List<String>
) {
    val favoriteTracks = state.tracks.filter { favoriteIds.contains(it.id) }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "\u2764\uFE0F FAVORITAS",
                    fontSize = 13.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Text(
                    "${favoriteTracks.size}",
                    fontSize = 12.sp,
                    color = Color(0xFFA78BFA),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            NeonPurple.copy(alpha = 0.15f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (favoriteTracks.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                ) {
                    Text("\uD83D\uDC9C", fontSize = 32.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "A\u00F1ade canciones a tus favoritas",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    favoriteTracks.forEach { track ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.03f))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    track.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    track.artist,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatsSection(
    state: MusicUiState,
    favoriteIds: List<String>
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                "\uD83D\uDCCA TUS ESTAD\u00CDSTICAS",
                fontSize = 13.sp,
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        icon = "\uD83C\uDFB5",
                        value = "${state.tracks.size}",
                        label = "Canciones",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "\u23F1",
                        value = "--",
                        label = "Tiempo",
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        icon = "\u2764\uFE0F",
                        value = "${favoriteIds.size}",
                        label = "Favoritas",
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "\uD83D\uDCE5",
                        value = "0",
                        label = "Descargas",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: String,
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                label,
                fontSize = 10.sp,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun RecommendationsSection() {
    val genres = listOf("Pop", "Rock", "Lo-fi", "Jazz", "Instrumental")

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                "\uD83C\uDFA7 RECOMENDACIONES",
                fontSize = 13.sp,
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                genres.forEach { genre ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(NeonPurple.copy(alpha = 0.15f))
                            .border(
                                1.dp,
                                NeonPurple.copy(alpha = 0.3f),
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            genre,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA78BFA)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddTrackCard(viewModel: MusicViewModel) {
    var name by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var youtubeId by remember { mutableStateOf("") }

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                "A\u00D1ADIR PISTA",
                fontSize = 12.sp,
                color = NeonPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = {
                    Text(
                        "Nombre de la canci\u00F3n",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = NeonCyan
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = artist,
                onValueChange = { artist = it },
                placeholder = {
                    Text(
                        "Artista (opcional)",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = NeonCyan
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = youtubeId,
                onValueChange = { youtubeId = it },
                placeholder = {
                    Text(
                        "YouTube Link (opcional)",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = NeonCyan
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(NeonBlue, NeonPurple)
                        )
                    )
            ) {
                Button(
                    onClick = {
                        viewModel.addTrack(
                            name,
                            artist,
                            youtubeId.ifBlank { null }
                        )
                        name = ""
                        artist = ""
                        youtubeId = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "VINCULAR A LA MATRIZ",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun DownloaderSection() {
    var downloadUrl by remember { mutableStateOf("") }
    var downloadFormat by remember { mutableStateOf("MP3 (Audio)") }
    val formats = listOf("MP3 (Audio)", "MP4 (Video)", "WEBM")

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(
                "\uD83D\uDCE5 DESCARGADOR DE M\u00DASICA",
                fontSize = 12.sp,
                color = NeonPurple,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = downloadUrl,
                onValueChange = { downloadUrl = it },
                placeholder = {
                    Text(
                        "URL de YouTube",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonBlue,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = NeonBlue
                ),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        downloadFormat,
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp
                    )
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.background(Color(0xFF12121A))
                ) {
                    formats.forEach { format ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    format,
                                    fontSize = 13.sp,
                                    color = Color.White
                                )
                            },
                            onClick = {
                                downloadFormat = format
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(NeonPurple, NeonBlue)
                        )
                    )
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Filled.Download,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "DESCARGAR",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
