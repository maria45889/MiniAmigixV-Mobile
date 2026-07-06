package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miniamigixv.miniamigixv_app.ui.components.GlassCard
import com.miniamigixv.miniamigixv_app.ui.components.NeonBorderCard
import com.miniamigixv.miniamigixv_app.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch

private val NeonPurple = Color(0xFF8B5CF6)
private val NeonBlue = Color(0xFF06B6D4)
private val NeonCyan = Color(0xFF22D3EE)
private val NeonPink = Color(0xFFF472B6)
private val NeonGreen = Color(0xFF10B981)
private val NeonOrange = Color(0xFFF59E0B)

private data class ChatConversation(
    val id: Int,
    val title: String,
    val preview: String,
    val time: String,
    val isActive: Boolean = false
)

private data class ChatMessage(
    val id: Int,
    val text: String,
    val isUser: Boolean,
    val time: String,
    val imageUrl: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    themeViewModel: ThemeViewModel,
    onBack: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }
    var showEmojiPicker by remember { mutableStateOf(false) }
    var isTyping by remember { mutableStateOf(false) }
    var showNewChatDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<String?>(null) }

    val conversations = remember {
        mutableStateListOf(
            ChatConversation(1, "Principal", "Último mensaje...", "Hace 2 min", isActive = true),
            ChatConversation(2, "Proyecto App", "¿Cómo va el desarrollo?", "Hace 1 hora"),
            ChatConversation(3, "Estudio Mate", "repasar trigonometría", "Ayer")
        )
    }

    var activeConvId by remember { mutableStateOf(1) }
    val activeConv = conversations.find { it.id == activeConvId }

    val messages = remember {
        mutableStateListOf<ChatMessage>()
    }

    var hasMessages by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color(0xFF0F172A).copy(alpha = 0.95f),
                drawerContentColor = Color.White
            ) {
                DrawerContent(
                    conversations = conversations,
                    activeConvId = activeConvId,
                    onSelectConversation = { conv ->
                        activeConvId = conv.id
                        hasMessages = false
                        messages.clear()
                        scope.launch { drawerState.close() }
                    },
                    onNewChat = { showNewChatDialog = true },
                    onDeleteChat = { conv ->
                        conversations.remove(conv)
                        if (conv.id == activeConvId && conversations.isNotEmpty()) {
                            activeConvId = conversations.first().id
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Chat / ${activeConv?.title ?: "IA"}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "MiniAmigixV AI",
                                fontSize = 11.sp,
                                color = NeonPurple.copy(alpha = 0.7f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Filled.List,
                                contentDescription = "Chats",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF0F172A).copy(alpha = 0.9f)
                    )
                )
            },
            bottomBar = {
                InputBar(
                    inputText = inputText,
                    onInputChange = { inputText = it },
                    showEmojiPicker = showEmojiPicker,
                    onToggleEmoji = { showEmojiPicker = !showEmojiPicker },
                    selectedImageUri = selectedImageUri,
                    onRemoveImage = { selectedImageUri = null },
                    onSend = {
                        if (inputText.isNotBlank()) {
                            messages.add(
                                ChatMessage(
                                    id = messages.size + 1,
                                    text = inputText.trim(),
                                    isUser = true,
                                    time = "Ahora"
                                )
                            )
                            inputText = ""
                            hasMessages = true
                            isTyping = true
                            scope.launch {
                                kotlinx.coroutines.delay(1500)
                                isTyping = false
                                messages.add(
                                    ChatMessage(
                                        id = messages.size + 1,
                                        text = respuestas[respuestas.indices.random()],
                                        isUser = false,
                                        time = "Ahora"
                                    )
                                )
                            }
                        }
                    }
                )
            },
            containerColor = Color.Transparent
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF0F172A),
                                Color(0xFF1E293B)
                            )
                        )
                    )
            ) {
                if (showEmojiPicker) {
                    EmojiPickerSheet(
                        onEmojiSelected = { emoji ->
                            inputText += emoji
                        },
                        onDismiss = { showEmojiPicker = false }
                    )
                }

                if (!hasMessages) {
                    WelcomeScreen(
                        onSuggestion = { suggestion ->
                            inputText = suggestion
                        }
                    )
                } else {
                    MessageList(
                        messages = messages,
                        isTyping = isTyping,
                        onCopy = { /* clipboard */ },
                        onSpeak = { /* TTS */ },
                        onRegenerate = { /* redo */ }
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerContent(
    conversations: List<ChatConversation>,
    activeConvId: Int,
    onSelectConversation: (ChatConversation) -> Unit,
    onNewChat: () -> Unit,
    onDeleteChat: (ChatConversation) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val filteredConvs = remember(conversations, searchText) {
        if (searchText.isBlank()) conversations
        else conversations.filter { it.title.contains(searchText, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color.Transparent)
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Chats",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )

        Button(
            onClick = onNewChat,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            contentPadding = PaddingValues(horizontal = 18.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(listOf(NeonPurple, NeonBlue)),
                        RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nuevo Chat", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            placeholder = { Text("Buscar conversación...", color = Color(0xFF64748B), fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp)) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = NeonBlue.copy(alpha = 0.5f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                cursorColor = NeonBlue,
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(filteredConvs) { conv ->
                ConversationItem(
                    conversation = conv,
                    isActive = conv.id == activeConvId,
                    onClick = { onSelectConversation(conv) },
                    onDelete = { onDeleteChat(conv) }
                )
            }
        }
    }
}

@Composable
private fun ConversationItem(
    conversation: ChatConversation,
    isActive: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showActions by remember { mutableStateOf(false) }

    val bgModifier = if (isActive) {
        Modifier
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        NeonPurple.copy(alpha = 0.2f),
                        NeonBlue.copy(alpha = 0.1f)
                    )
                )
            )
            .border(
                width = 0.dp,
                color = Color.Transparent,
                shape = RoundedCornerShape(0.dp)
            )
    } else {
        Modifier
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(bgModifier)
                .clickable { onClick() }
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = conversation.title,
                    fontSize = 14.sp,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium,
                    color = if (isActive) Color.White else Color(0xFFE2E8F0),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = conversation.preview,
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 1
                )
                Text(
                    text = conversation.time,
                    fontSize = 11.sp,
                    color = Color(0xFF475569)
                )
            }
            IconButton(
                onClick = { showActions = !showActions },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "Acciones",
                    tint = Color.White.copy(alpha = 0.4f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        AnimatedVisibility(visible = showActions) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDelete() }
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFEF4444),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Eliminar chat", fontSize = 13.sp, color = Color(0xFFEF4444))
            }
        }
    }
}

@Composable
private fun WelcomeScreen(onSuggestion: (String) -> Unit) {
    val suggestions = listOf(
        "📝" to "Resume un texto",
        "📚" to "Ayúdame a estudiar",
        "🎨" to "Crear una imagen",
        "💻" to "Explicar código"
    )
    val capabilities = listOf(
        "💬 Resolver dudas",
        "🎵 Recomendar música",
        "🌦 Consultar el clima",
        "📅 Organizar eventos",
        "🌍 Traducir textos"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "🤖",
            fontSize = 72.sp,
            modifier = Modifier.alpha(0.9f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿En qué puedo ayudarte hoy?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Puedo ayudarte con:",
            fontSize = 14.sp,
            color = Color(0xFF94A3B8)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            capabilities.chunked(3).forEach { row ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    row.forEach { cap ->
                        Text(
                            text = cap,
                            fontSize = 13.sp,
                            color = Color(0xFFE2E8F0),
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color.White.copy(alpha = 0.06f))
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.12f),
                                    RoundedCornerShape(24.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "────────────────────────",
            fontSize = 12.sp,
            color = Color(0xFF475569)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    row.forEach { (icon, text) ->
                        GlassCard(
                            modifier = Modifier.weight(1f),
                            cornerRadius = 16.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSuggestion(text) },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = icon, fontSize = 28.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = text,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFF1F5F9),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MessageList(
    messages: List<ChatMessage>,
    isTyping: Boolean,
    onCopy: (ChatMessage) -> Unit,
    onSpeak: (ChatMessage) -> Unit,
    onRegenerate: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(messages, key = { it.id }) { msg ->
            MessageBubble(
                message = msg,
                onCopy = { onCopy(msg) },
                onSpeak = { onSpeak(msg) },
                onRegenerate = onRegenerate
            )
        }

        if (isTyping) {
            item {
                TypingIndicator()
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    onCopy: () -> Unit,
    onSpeak: () -> Unit,
    onRegenerate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        Row(
            modifier = Modifier.widthIn(max = 320.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
        ) {
            if (!message.isUser) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.horizontalGradient(listOf(NeonPurple, Color(0xFF6366F1)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🤖", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .clip(
                        if (message.isUser) RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = 20.dp,
                            bottomEnd = 6.dp
                        )
                        else RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                            bottomStart = 6.dp,
                            bottomEnd = 20.dp
                        )
                    )
                    .then(
                        if (message.isUser) {
                            Modifier
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            NeonPurple.copy(alpha = 0.85f),
                                            NeonBlue.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.15f),
                                    RoundedCornerShape(
                                        topStart = 20.dp,
                                        topEnd = 20.dp,
                                        bottomStart = 20.dp,
                                        bottomEnd = 6.dp
                                    )
                                )
                        } else {
                            Modifier
                                .background(Color.White.copy(alpha = 0.04f))
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.08f),
                                    RoundedCornerShape(
                                        topStart = 20.dp,
                                        topEnd = 20.dp,
                                        bottomStart = 6.dp,
                                        bottomEnd = 20.dp
                                    )
                                )
                        }
                    )
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (message.isUser) "👤 Tú" else "🤖 MiniAmigixV",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (message.isUser) Color.White.copy(alpha = 0.8f) else NeonPurple
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = message.time,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 22.sp
                )

                if (!message.isUser) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(
                        color = Color.White.copy(alpha = 0.05f),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ActionButton(icon = Icons.Filled.ContentCopy, label = "Copiar", onClick = onCopy)
                        ActionButton(icon = Icons.Filled.VolumeUp, label = "Leer", onClick = onSpeak)
                        ActionButton(icon = Icons.Filled.Refresh, label = "Regenerar", onClick = onRegenerate)
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(15.dp)
        )
    }
}

@Composable
private fun TypingIndicator() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp))
            .background(Color.White.copy(alpha = 0.02f))
            .border(1.dp, Color.White.copy(alpha = 0.03f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 4.dp, bottomEnd = 16.dp))
            .padding(12.dp, 18.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(NeonBlue)
            )
        }
    }
}

@Composable
private fun InputBar(
    inputText: String,
    onInputChange: (String) -> Unit,
    showEmojiPicker: Boolean,
    onToggleEmoji: () -> Unit,
    selectedImageUri: String?,
    onRemoveImage: () -> Unit,
    onSend: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F172A).copy(alpha = 0.85f),
                        Color(0xFF1E293B).copy(alpha = 0.9f)
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        if (selectedImageUri != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonPurple.copy(alpha = 0.3f))
                        .border(2.dp, NeonPurple.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Image,
                        contentDescription = "Imagen",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Imagen adjunta", fontSize = 12.sp, color = Color(0xFF94A3B8))
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onRemoveImage, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggleEmoji) {
                Text("😊", fontSize = 20.sp)
            }

            IconButton(onClick = { /* image picker */ }) {
                Icon(
                    Icons.Filled.AttachFile,
                    contentDescription = "Adjuntar",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(22.dp)
                )
            }

            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text("Escribe un mensaje...", color = Color(0xFF64748B), fontSize = 14.sp)
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = NeonBlue.copy(alpha = 0.6f),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
                    cursorColor = NeonBlue,
                    focusedContainerColor = Color.White.copy(alpha = 0.06f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.06f)
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { /* voice */ }) {
                Icon(
                    Icons.Filled.Mic,
                    contentDescription = "Voz",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(22.dp)
                )
            }

            FilledIconButton(
                onClick = onSend,
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.Transparent
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(listOf(NeonPurple, NeonBlue)),
                            CircleShape
                        )
                        .border(2.dp, Color.White.copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Enviar",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmojiPickerSheet(
    onEmojiSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val categories = listOf(
        "😀 Emociones" to listOf("😊", "😂", "🤣", "😢", "😭", "😍", "🥰", "😘", "🤗", "🥺", "😎", "🤩", "😋", "🤪", "😴", "🥱", "😡", "🤯", "😱", "🤔", "🙏"),
        "👋 Gestos" to listOf("👍", "👋", "👌", "✌️", "🤞", "🤙", "👏", "🙌", "🤝", "💪"),
        "❤️ Corazones" to listOf("❤️", "💔", "💕", "💖", "💗", "💙", "💚", "💛", "💜", "🖤", "💝", "💞"),
        "🐶 Animales" to listOf("🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐨", "🦁", "🐯", "🦄", "🐸", "🐵", "🐔", "🐧", "🐦", "🦋", "🐙"),
        "🌸 Naturaleza" to listOf("🌸", "🌹", "🌻", "🌼", "🌷", "🌺", "🌴", "🌲", "🍀", "🌍", "🌎", "🌅", "🌇", "🎇", "🎆"),
        "🍎 Frutas" to listOf("🍎", "🍏", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🍒", "🍑", "🥭", "🍍", "🥝", "🍅"),
        "🍕 Comida" to listOf("🍕", "🍔", "🍟", "🌭", "🌮", "🌯", "🥗", "🍿", "🍱", "🍛", "🍝", "🍜", "🍲", "🥘"),
        "🍰 Postres" to listOf("🍦", "🍧", "🍨", "🍩", "🍪", "🎂", "🍰", "🧁", "🍫", "🍬", "🍭", "🍮"),
        "☕ Bebidas" to listOf("🍼", "🥛", "☕", "🍵", "🍶", "🍾", "🍷", "🍸", "🍹", "🍺", "🥤", "🧋"),
        "⚽ Deportes" to listOf("🏆", "🏅", "🥇", "⚽", "🏀", "🎾", "🎳", "🏓", "🏸", "🥊", "🎯", "🎿"),
        "🎮 Juegos" to listOf("🎮", "🕹️", "🎰", "🎲", "🧩", "🔮"),
        "🎵 Música" to listOf("🎸", "🎹", "🎺", "🎷", "🥁", "🎵", "🎶", "🎤", "🎧", "📻"),
        "🎨 Arte" to listOf("🎬", "🎨", "🎭", "🎪", "🎀", "🎟️", "🎫"),
        "✨ Símbolos" to listOf("🔥", "✨", "🎉", "🌟", "💡", "🚀", "💯", "⭐", "🌈", "🎁", "📚")
    )

    var selectedCategoryIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF0F172A).copy(alpha = 0.95f),
                        Color(0xFF1E293B).copy(alpha = 0.95f)
                    )
                )
            )
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            categories.take(7).forEachIndexed { index, (title, _) ->
                val isSelected = index == selectedCategoryIndex
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) NeonPurple.copy(alpha = 0.3f)
                            else Color.Transparent
                        )
                        .border(
                            1.dp,
                            if (isSelected) NeonPurple.copy(alpha = 0.5f)
                            else Color.White.copy(alpha = 0.08f),
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedCategoryIndex = index }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title.take(2),
                        fontSize = 16.sp
                    )
                }
            }
        }

        if (selectedCategoryIndex < categories.size) {
            val (_, emojis) = categories[selectedCategoryIndex]
            LazyColumn {
                items(emojis.chunked(8)) { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        row.forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onEmojiSelected(emoji) }
                                    .padding(6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 22.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

private val respuestas = listOf(
    "¡Claro! Permíteme ayudarte con eso. 😊",
    "Buena pregunta! Déjame pensar... 🤔",
    "Aquí tienes la información que necesitas:",
    "Excelente elección! Te recomiendo lo siguiente:",
    "Entiendo tu consulta. Te explico:",
    "Me encanta esa pregunta! La respuesta es:",
    "Claro que sí! Con gusto te ayudo.",
    "Interesante! Esto es lo que sé al respecto:"
)
