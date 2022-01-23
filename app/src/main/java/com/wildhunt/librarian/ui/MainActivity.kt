package com.wildhunt.librarian.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.wildhunt.librarian.R
import com.wildhunt.librarian.di.AppComponent
import com.wildhunt.librarian.domain.models.Message
import com.wildhunt.librarian.domain.models.Sender
import java.util.*

val LocalLockedAudioPlayer = compositionLocalOf<AudioPlayer> { error("Uninitialized") }
val LocalLockedAudioRecorder = compositionLocalOf<AudioRecorder> { error("Uninitialized") }

@ExperimentalPermissionsApi
class MainActivity : AppCompatActivity() {
    private val viewModel: ChatViewModel by viewModels()

    private var audioRecorder = AudioRecorder()
    private var audioPlayer = AudioPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppComponent.get(this).inject(viewModel)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            var route by remember { mutableStateOf<Routes>(Routes.Feed) }
            val messages by viewModel.messagesFlow.collectAsState(initial = listOf())

            CompositionLocalProvider(
                LocalLockedAudioPlayer provides audioPlayer,
                LocalLockedAudioRecorder provides audioRecorder,
            ) {
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
        }
    }

    fun stopAudioRecording(fileName: String) {
        audioRecorder.stopRecording()
//        viewModelScope.launch {
//          _messagesFlow.emit(listOf(AudioMessage(fileName)))
//        }
    }

    override fun onDestroy() {
        super.onDestroy()

        audioRecorder.stopRecording()
        audioPlayer.stopPlaying()
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
            Text(
                text = "Back",
                color = Colors.blue,
                lineHeight = 22.sp,
                fontSize = 16.sp,
                fontFamily = robotoFamily,
                fontWeight = Normal,
                modifier = Modifier.padding(start = 4.dp),
            )
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

    val context = LocalLockedAudioPlayer.current
    val background = if (message.sender == Sender.Me) Colors.blue else Colors.greyGradient

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
                                color = background,
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
                    Box(
                        modifier = Modifier.clickable {
                            context.startPlaying(message.fileName)
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = null
                        )
                    }
                }
                is Message.Recommendation -> {
                    Column(
                        modifier = Modifier
                            .background(
                                color = background,
                                shape = RoundedCornerShape(14.dp),
                            )
                    ) {
                        AsyncImage(
                            model = message.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
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
                                    Image(
                                        painterResource(id = R.drawable.pic_placeholder),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }
                                is AsyncImagePainter.State.Error -> {
                                    state.result.throwable.printStackTrace()
                                    Image(
                                        painterResource(id = R.drawable.pic_placeholder),
                                        contentDescription = null,
                                        contentScale = ContentScale.FillWidth,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
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
                            fontWeight = Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )

                        if (message.authors.isNotEmpty()) {
                            Text(
                                text = message.authors.joinToString(", "),
                                color = Color.White,
                                fontStyle = FontStyle.Italic,
                                lineHeight = 22.sp,
                                fontSize = 16.sp,
                                fontFamily = robotoFamily,
                                fontWeight = Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        if (!message.description.isNullOrBlank()) {
                            Text(
                                text = message.description,
                                color = Color.White,
                                lineHeight = 22.sp,
                                fontSize = 16.sp,
                                fontFamily = robotoFamily,
                                fontWeight = Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        if (message.genre.isNotEmpty()) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                                Text(
                                    text = "Type: ",
                                    color = Color.White,
                                    lineHeight = 22.sp,
                                    fontSize = 16.sp,
                                    fontFamily = robotoFamily,
                                    fontWeight = Bold,
                                )

                                Text(
                                    text = message.genre.joinToString(", "),
                                    color = Color.White,
                                    lineHeight = 22.sp,
                                    fontSize = 16.sp,
                                    fontFamily = robotoFamily,
                                    fontWeight = Normal,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class ComponentState { Pressed, Released }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MessageInput(onSend: (Message) -> Unit) {
    var input by remember { mutableStateOf("") }
    var btnState by remember { mutableStateOf(ComponentState.Released) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    val recorder = LocalLockedAudioRecorder.current
    val context = LocalContext.current

    when {
        input.isBlank() && btnState == ComponentState.Pressed && !cameraPermissionState.hasPermission -> {
            SideEffect {
                cameraPermissionState.launchPermissionRequest()
            }
        }
        input.isBlank() && btnState == ComponentState.Released && recorder.isRecording -> {
            recorder.stopRecording()?.let {
                onSend(
                    Message.Audio(
                        sender = Sender.Me,
                        fileName = it,
                        length = 0,
                    )
                )
            }
        }
        input.isBlank() && btnState == ComponentState.Pressed && !recorder.isRecording -> {
            val fileName =
                "${context.externalCacheDir?.absolutePath}/${UUID.randomUUID()}_audio"
            recorder.startRecording(fileName)
        }
    }

    Box(
        modifier = Modifier
            .navigationBarsWithImePadding()
            .heightIn(min = 56.dp)
            .fillMaxWidth()
            .background(Colors.grey)
    ) {
        Row {
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
            if (input.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            onSend(Message.Text(sender = Sender.Me, text = input))
                            input = ""
                        }
                        .padding(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = null
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    btnState = ComponentState.Pressed
                                    awaitRelease()
                                    btnState = ComponentState.Released
                                }
                            )
                        }
                        .padding(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_mic),
                        contentDescription = null
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = btnState == ComponentState.Pressed && input.isBlank(),
            enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
            exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .height(56.dp)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xffA9FFD6), Colors.blue),
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = CenterStart,
            ) {
                Box(
                    modifier = Modifier.padding(16.dp).size(8.dp).background(Color.Red, CircleShape)
                )
            }
        }
    }
}
