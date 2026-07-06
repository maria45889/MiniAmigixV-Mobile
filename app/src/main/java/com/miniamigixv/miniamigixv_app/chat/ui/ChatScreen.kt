package com.miniamigixv.miniamigixv_app.chat.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.miniamigixv.miniamigixv_app.chat.data.model.ChatConversation
import com.miniamigixv.miniamigixv_app.chat.data.model.ChatMessage
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.components.GlassPanel
import android.net.Uri
import android.speech.tts.TextToSpeech
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*

// Colors to match the new Neon Web Chat UI
private val UserBubbleGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF8B5CF6), Color(0xFF06B6D4))
)
private val AIBubble = Color(0xFF111827).copy(alpha = 0.7f)
private val UserAvatar = Color(0xFF06B6D4) // Cyan avatar
private val AIAvatar = Color(0xFF8B5CF6) // Purple avatar
private val InputBg = Color(0xFF111827) // Very dark input background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onBack: () -> Unit = {},
    chatViewModel: ChatViewModel = viewModel()
) {
    val messages = chatViewModel.messages
    val conversations = chatViewModel.conversations
    val selectedConversationId = chatViewModel.selectedConversationId
    val state = chatViewModel.uiState
    val inputText = chatViewModel.inputText
    val listState = rememberLazyListState()
    var showEmojiPicker by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isSpeaking by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    val textToSpeech = remember { TextToSpeech(context, null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let { chatViewModel.sendMessageWithImage(it) }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .imePadding() // CRITICAL: So keyboard doesn't overlap input bar
    ) {
        val isCompact = maxWidth < 600.dp
        var showSidebar by remember(isCompact) { mutableStateOf(!isCompact) }
        val screenWidth = maxWidth

        Row(modifier = Modifier.fillMaxSize()) {
            // Sidebar
            AnimatedVisibility(
                visible = showSidebar,
                enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                exit = slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .width(if (isCompact) 280.dp else 320.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, Color.White.copy(alpha = 0.05f))
                ) {
                    // Sidebar Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chats",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        IconButton(onClick = { chatViewModel.createNewConversation() }) {
                            Icon(Icons.Filled.Add, contentDescription = "Nuevo chat", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }

                    Divider(color = Color.White.copy(alpha = 0.05f))

                    // Chat List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(conversations) { conversation ->
                            ConversationItem(
                                conversation = conversation,
                                isSelected = conversation.id == selectedConversationId,
                                onClick = {
                                    chatViewModel.selectConversation(conversation.id)
                                    if (isCompact) showSidebar = false
                                }
                            )
                        }
                    }
                }
            }

            // Main Chat Area
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                // Top Bar
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (isCompact) {
                                IconButton(onClick = { showSidebar = !showSidebar }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onBackground)
                                }
                            }
                            Text("Chat / ", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)
                            val selectedChat = conversations.find { it.id == selectedConversationId }
                            Text(
                                selectedChat?.name ?: "Chat Principal",
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    },
                    actions = {
                        if (messages.isNotEmpty()) {
                            IconButton(onClick = { chatViewModel.clearMessages() }) {
                                Icon(Icons.Filled.DeleteSweep, contentDescription = "Limpiar chat", tint = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                        IconButton(onClick = { /* Settings */ }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Configuración", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )

                Divider(color = Color.White.copy(alpha = 0.05f))

                if (state is ChatUiState.Error) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.ErrorOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = state.message,
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            TextButton(onClick = { chatViewModel.retryLastMessage() }) {
                                Text("Reintentar", fontWeight = FontWeight.Bold)
                            }
                            TextButton(onClick = { chatViewModel.clearError() }) {
                                Text("OK")
                            }
                        }
                    }
                }

                if (messages.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.SmartToy,
                                contentDescription = null,
                                modifier = Modifier.size(72.dp),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Inicia una conversación con MiniAmigixV",
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(messages, key = { it.id }) { message ->
                            ChatBubbleWithAvatar(message, screenWidth)
                        }
                        if (state is ChatUiState.Sending) {
                            item { TypingIndicator(screenWidth) }
                        }
                    }
                }

                // Bottom Input Bar
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp)
                            .navigationBarsPadding(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Input TextField
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(24.dp))
                                .background(InputBg)
                                .border(1.dp, Color.White.copy(alpha=0.1f), RoundedCornerShape(24.dp))
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = chatViewModel::updateInputText,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Escribe un mensaje...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                leadingIcon = {
                                    IconButton(onClick = { showEmojiPicker = !showEmojiPicker }) {
                                        Icon(Icons.Filled.EmojiEmotions, contentDescription = "Emoji", tint = Color(0xFFF59E0B))
                                    }
                                },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(
                                    onSend = { chatViewModel.sendMessage() }
                                ),
                                maxLines = 4,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                    cursorColor = Color(0xFF3B82F6)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Photo Icon
                        IconButton(onClick = { photoPickerLauncher.launch("image/*") }) {
                            Icon(Icons.Filled.Image, contentDescription = "Foto", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        // Voice / Audio Icon
                        IconButton(onClick = { 
                            // Get last AI message and speak it
                            val lastAIMessage = messages.lastOrNull { !it.isUser }
                            if (lastAIMessage != null && lastAIMessage.text.isNotEmpty()) {
                                textToSpeech.speak(lastAIMessage.text, TextToSpeech.QUEUE_FLUSH, null, null)
                                isSpeaking = true
                            }
                        }) {
                            Icon(
                                Icons.Filled.VolumeUp,
                                contentDescription = "Escuchar IA",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Send Button
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF8B5CF6), Color(0xFF3b82f6))
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { chatViewModel.sendMessage() },
                                enabled = inputText.isNotBlank(),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(Icons.Filled.Send, contentDescription = "Enviar", tint = Color.White)
                            }
                        }
                    }

                    // Emoji Picker
                    if (showEmojiPicker) {
                        EmojiPicker(
                            onEmojiSelected = { emoji ->
                                chatViewModel.updateInputText(inputText + emoji)
                            },
                            onDismiss = { showEmojiPicker = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatBubbleWithAvatar(message: ChatMessage, screenWidth: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!message.isUser) {
            // AI Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AIAvatar),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.SmartToy, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Bubble with glassmorphism
        val shape = RoundedCornerShape(20.dp)
        // User bubble: max 75%, AI bubble: max 85%
        val maxWidthFraction = if (message.isUser) 0.75f else 0.85f

        Box(
            modifier = Modifier
                .widthIn(max = screenWidth * maxWidthFraction)
                .clip(shape)
                .then(
                    if (message.isUser) {
                        Modifier.background(UserBubbleGradient)
                    } else {
                        Modifier
                            .background(AIBubble)
                            .border(1.dp, Color.White.copy(alpha = 0.08f), shape)
                    }
                )
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                message.imageUrl?.let { imageUrl ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen del mensaje",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (message.text.isNotEmpty()) {
                    Text(
                        text = message.text,
                        fontSize = 15.sp,
                        color = Color.White,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = formatTimestamp(message.timestamp),
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        if (message.isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // User Avatar
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(UserAvatar),
                contentAlignment = Alignment.Center
            ) {
                Text("M", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun TypingIndicator(screenWidth: Dp) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        // AI Avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(AIAvatar),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.SmartToy, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AIBubble,
            modifier = Modifier.widthIn(max = screenWidth * 0.85f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}

@Composable
private fun ConversationItem(
    conversation: ChatConversation,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                if (isSelected) Color(0xFF1E3A8A) else Color.Transparent
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color(0xFF8B5CF6) else Color(0xFF374151)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Filled.Chat,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = conversation.name,
                fontSize = 15.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = conversation.lastMessage,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatTimestamp(conversation.timestamp),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (conversation.unreadCount > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8B5CF6)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (conversation.unreadCount > 9) "9+" else conversation.unreadCount.toString(),
                        fontSize = 11.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}

@Composable
private fun EmojiPicker(
    onEmojiSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val commonEmojis = listOf(
        "😀", "😂", "😍", "🥰", "😎", "🤔", "😢", "😡",
        "👍", "👎", "❤️", "💔", "🔥", "⭐", "🎉", "🎈",
        "🌟", "💯", "✨", "💪", "🙏", "👋", "🤝", "👏",
        "🎵", "🎶", "☀️", "🌈", "🌸", "🍕", "🍔", "☕"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = InputBg,
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Emojis",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar", tint = MaterialTheme.colorScheme.onBackground)
                }
            }
            Divider(color = Color.White.copy(alpha = 0.1f))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(commonEmojis.chunked(8)) { rowEmojis ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowEmojis.forEach { emoji ->
                            Text(
                                text = emoji,
                                fontSize = 32.sp,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable { onEmojiSelected(emoji) }
                            )
                        }
                    }
                }
            }
        }
    }
}
