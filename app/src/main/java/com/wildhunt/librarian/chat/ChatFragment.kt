package com.wildhunt.librarian.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.wildhunt.librarian.R

class ChatFragment : Fragment() {

  private val viewModel: ChatViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).apply {
      setContent {
        MaterialTheme {
          Surface(color = MaterialTheme.colors.background) {
            ChatScreen(chatViewModel = viewModel)
          }
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    viewLifecycleOwner.lifecycle.addObserver(viewModel)
  }

  @Composable
  fun ChatScreen(chatViewModel: ChatViewModel) {
    val messages by chatViewModel.messages.collectAsState(initial = emptyList())
    Box(
      modifier = Modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
    ) {
      LazyColumn(Modifier.fillMaxSize()) {
        items(messages) { message ->
          MessageItem(message)
          Divider()
        }
      }
    }
  }

  @Composable
  fun MessageItem(message: Message) {
    Row(
      modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 8.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Avatar(message.sender)
      Column(modifier = Modifier.padding(start = 8.dp)) {
        Text(
          text = if (message.sender == Persona.AI) "AI" else "Me",
          style = TextStyle(fontWeight = FontWeight.Bold),
          fontSize = 18.sp,
        )
        val lastMessageText = message.message
        Text(
          text = lastMessageText,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }

  @Composable
  fun Avatar(persona: Persona) {
    val icon = ImageVector.vectorResource(
      id = if (persona == Persona.AI) {
        R.drawable.ic_launcher_foreground
      } else {
        R.drawable.ic_launcher_foreground
      }
    )
    Icon(
      modifier = Modifier
        .size(24.dp)
        .clip(CircleShape),
      imageVector =icon,
      contentDescription = "Avatar",
    )
  }
}