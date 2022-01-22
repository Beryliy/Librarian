package com.wildhunt.librarian

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wildhunt.librarian.chat.ChatViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val messages = mutableListOf(
//            Message(
//                isUser = false,
//                text = "Hello, how can i help you",
//            ),
//            Message(
//                isUser = true,
//                text = "Hello, I want to read some books",
//            ),
//            Message(
//                isUser = false,
//                text = "Tell me about you mood",
//            ),
//            Message(
//                isUser = true,
//                text = "I feel depressed",
//            ),
//            Message(
//                isUser = false,
//                text = "What's happened",
//            ),
//            Message(
//                isUser = true,
//                text = "I can't do anything",
//            ),
//            Message(
//                isUser = true,
//                text = "Just feel bad",
//            ),
//        )
        setContent {
            var route by remember { mutableStateOf<Routes>(Routes.Feed) }
            var messages by remember { mutableStateOf<List<Message>>(listOf()) }

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Yellow)
            ) {
                when (route) {
                    Routes.Feed -> Feed { route = Routes.Chat }
                    Routes.Chat -> Chat(
                        messages = messages,
                        onBack = { route = Routes.Feed },
                        onSend = { messages = messages + it + Message(isUser = false, text = "abs") }
                    )
                }
            }
        }
    }
}

sealed class Routes {
    object Feed : Routes()
    object Chat : Routes()
}

@Composable
fun Feed(
    onChatButtonClicked: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
                .background(Color.Green),
            onClick = onChatButtonClicked,
        ) {
            Text(text = "Click me!!!")
        }
    }
}

@Composable
fun Chat(
    messages: List<Message>,
    onBack: () -> Unit,
    onSend: (Message) -> Unit,
) {
    BackHandler(onBack = onBack)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier.fillMaxSize().weight(1f),
        ) {
            items(messages) { message ->
                Box(
                    Modifier
                        .background(Color.Green)
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = message.text,
                        modifier = Modifier
                            .background(Color.Yellow)
                            .align(if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
            }
        }

        MessageInput(onSend)
    }
}

@Composable
fun MessageInput(onSend: (Message) -> Unit) {
    var input by remember { mutableStateOf("") }

    Row {
        TextField(
            value = input,
            modifier = Modifier.weight(1f),
            onValueChange = { input = it }
        )
        Button(
            modifier = Modifier.height(56.dp),
            onClick = { onSend(Message(isUser = true, text = input)) },
            enabled = input.isNotBlank()
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send button"
            )
        }
    }
}

data class Message(
    val isUser: Boolean,
    val text: String,
)