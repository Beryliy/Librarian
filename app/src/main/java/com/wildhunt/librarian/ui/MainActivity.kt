package com.wildhunt.librarian.ui

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Normal
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import coil.compose.AsyncImageContent
import coil.compose.AsyncImagePainter
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wildhunt.librarian.R
import com.wildhunt.librarian.domain.models.*
import com.wildhunt.librarian.di.AppComponent

class MainActivity : AppCompatActivity() {
    private val viewModel: ChatViewModel by viewModels()

    private val audioRecorder = AudioRecorder()
    private val audioPlayer = AudioPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppComponent.get(this).inject(viewModel)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            var route by remember { mutableStateOf<Routes>(Routes.Feed) }
            val messages by viewModel.messagesFlow.collectAsState(initial = listOf())

            ProvideWindowInsets {
                val systemUiController = rememberSystemUiController()

                SideEffect {
                    systemUiController.setSystemBarsColor(Colors.greyDark, darkIcons = false)
                }

                Box(
                    Modifier
                        .fillMaxSize()
                        .statusBarsPadding()
                ) {
                    when (route) {
                        Routes.Feed -> Feed { route = Routes.Chat }
                        Routes.Chat -> Chat(
                            messages = messages,
                            onBack = { route = Routes.Feed },
                            onSend = { viewModel.newUserMessage(it) }
                        )
                    }
                }
            }
        }

        viewModel.initConversation()
    }

    fun startAudioRecording(fileName: String): String {
        audioRecorder.startRecording(fileName)
        return fileName
    }

    fun stopAudioRecording(fileName: String) {
        audioRecorder.stopRecording()
//        viewModelScope.launch {
//          _messagesFlow.emit(listOf(AudioMessage(fileName)))
//        }
    }

    fun startPlaying(fileName: String) {
        audioPlayer.startPlaying(fileName)
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
    Image(
        painter = painterResource(id = R.drawable.bg_chat),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(onBack)

        LazyColumn(
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            items(messages) { message ->
                Message(message)
                Spacer(modifier = Modifier.height(2.dp))
            }
        }

        MessageInput(onSend)
    }
}

@Composable
fun TopBar(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .background(Colors.grey),
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onBack,
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = null
            )
            Text(text = "Back", color = Colors.blue)
        }
    }
}

@Composable
fun Message(message: Message) {
    val messageArrangement = if (message.sender == Sender.Me) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    val brush = if (message.sender == Sender.Me) {
        Brush.radialGradient(
            0.1f to Color.White,
            1f to Colors.blue,
            radius = 200f,
            center = Offset(-80f, -80f),
            tileMode = TileMode.Clamp,
        )
    } else {
        Brush.radialGradient(
            0.1f to Color.White,
            1f to Colors.greyGradient,
            radius = 200f,
            center = Offset(-80f, -80f),
            tileMode = TileMode.Clamp,
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = messageArrangement,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = messageArrangement,
        ) {

            when (message) {
                is Message.Text -> {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = brush,
                                shape = RoundedCornerShape(14.dp),
                            )
                            .padding(vertical = 6.dp, horizontal = 12.dp),
                        contentAlignment = CenterStart,
                    ) {
                        Box(modifier = Modifier.height(22.dp))
                        Text(
                            text = message.text,
                            color = Color.White,
                            lineHeight = 22.sp,
                            fontSize = 16.sp,
                            fontFamily = robotoFamily,
                            fontWeight = Normal,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
                is Message.Audio -> {

                }
                is Message.Recommendation -> {
                    Column(
                        modifier = Modifier
                            .background(
                                brush = brush,
                                shape = RoundedCornerShape(14.dp),
                            )
                    ) {
                        AsyncImage(
                            model = message.imageUrl,
                            contentDescription = null,
                            Modifier
                                .padding(
                                    start = 2.dp,
                                    top = 2.dp,
                                    end = 2.dp,
                                )
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 14.dp,
                                        topEnd = 14.dp,
                                    )
                                )
                        ) { state ->
                            when (state) {
                                is AsyncImagePainter.State.Loading -> {
                                    Text(text = "Loading...")
                                }
                                is AsyncImagePainter.State.Error -> {
                                    state.result.throwable.printStackTrace()
                                    Text(text = state.result.throwable.toString())
//                                    Image(painterResource(id = R.drawable.bg_chat), contentDescription = null)
                                }
                                is AsyncImagePainter.State.Success -> {
                                    AsyncImageContent()
                                }
                            }
                        }

                        Text(
                            text = message.title,
                            color = Color.White,
                            lineHeight = 22.sp,
                            fontSize = 16.sp,
                            fontFamily = robotoFamily,
                            fontWeight = Normal,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageInput(onSend: (Message) -> Unit) {
    var input by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .navigationBarsWithImePadding()
            .heightIn(min = 56.dp)
            .background(Colors.grey)
    ) {
        TextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.weight(1f),
            placeholder = @Composable {
                Text("Message")
            },
            shape = RectangleShape,
            maxLines = 10,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                cursorColor = Color.White,
                backgroundColor = Color.Transparent,
                placeholderColor = Colors.greyLight,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
        )
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    if (input.isNotBlank()) {
                        onSend(Message.Text(sender = Sender.Me, text = input))
                        input = ""
                    }
                }
                .padding(12.dp)
        ) {
            if (input.isNotBlank()) {
                Image(painter = painterResource(id = R.drawable.ic_mic), contentDescription = null)
            } else {
                Image(painter = painterResource(id = R.drawable.ic_mic), contentDescription = null)
            }
        }
    }
}
