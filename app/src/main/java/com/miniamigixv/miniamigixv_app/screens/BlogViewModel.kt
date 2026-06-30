package com.miniamigixv.miniamigixv_app.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.miniamigixv.miniamigixv_app.screens.data.model.BlogPost
import java.util.UUID

class BlogViewModel : ViewModel() {
    var myPosts by mutableStateOf(listOf<BlogPost>())
        private set

    var showCreatePostDialog by mutableStateOf(false)
        private set

    init {
        loadMockPosts()
    }

    private fun loadMockPosts() {
        myPosts = emptyList()
    }

    fun toggleCreatePostDialog(show: Boolean) {
        showCreatePostDialog = show
    }

    fun addPost(
        title: String,
        content: String,
        category: String,
        imageUrl: String?,
        visibleToAll: Boolean,
        isOfficial: Boolean,
        isPinned: Boolean
    ) {
        val newPost = BlogPost(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            category = category,
            imageUrl = imageUrl,
            visibleToAll = visibleToAll,
            isOfficial = isOfficial,
            isPinned = isPinned,
            author = "Usuario"
        )
        myPosts = myPosts + newPost
        showCreatePostDialog = false
    }

    fun deletePost(postId: String) {
        myPosts = myPosts.filter { it.id != postId }
    }
}
