package com.wildhunt.librarian.ui

import androidx.lifecycle.*
import com.wildhunt.librarian.domain.models.Book
import com.wildhunt.librarian.domain.models.Message
import com.wildhunt.librarian.domain.models.Sender
import com.wildhunt.librarian.domain.use_cases.GetBooksUseCase
import com.wildhunt.librarian.domain.use_cases.GetKeywordsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel : ViewModel() {
  private val audioRecorder = AudioRecorder()
  private val audioPlayer = AudioPlayer()

  private val _messagesFlow = MutableStateFlow<List<Message>>(emptyList())
  val messagesFlow: Flow<List<Message>> = _messagesFlow

  private var conversationState: ConversationState = ConversationState.Greeting
  private var detectedKeywords = mutableSetOf<String>()
  private var cachedBooks = mutableListOf<Book>()
  private val KEYWORD_THRESHOLD = 5

  @Inject
  lateinit var getBooks: GetBooksUseCase

  @Inject
  lateinit var getKeywords: GetKeywordsUseCase

  fun startAudioRecording(fileName: String): String {
    audioRecorder.startRecording(fileName)
    return fileName
  }

  fun stopAudioRecording(fileName: String) {
    audioRecorder.stopRecording()
    viewModelScope.launch {
//      _messagesFlow.emit(listOf(AudioMessage(fileName)))
    }
  }

  fun startPlaying(fileName: String) {
    audioPlayer.startPlaying(fileName)
  }

  fun initConversation() {
    viewModelScope.launch {
      conversationState = ConversationState.Greeting
      detectedKeywords.clear()
      aiReply(Questions.greetings.random())
    }
  }

  fun newUserMessage(message: Message) {
    viewModelScope.launch {
      saveMessage(message)
      detectKeywords(message)
      reply()
    }
  }

  private suspend fun detectKeywords(message: Message) {
    detectedKeywords.addAll(getKeywords.get(message))
  }

  private suspend fun reply() {
    if (detectedKeywords.size > KEYWORD_THRESHOLD) {
      if (cachedBooks.isEmpty()) {
        val books = getBooks.getBooks(detectedKeywords.toList())
        cachedBooks.addAll(books.sortedByDescending { it.rating })
      }

      if (cachedBooks.isEmpty()) {
        aiReply(Questions.noBooks)
      } else {
        val book = cachedBooks.removeAt(0)
        val detailed = getBooks.getDetails(book)

        aiReply("Take a look at this book:")
        saveMessage(Message.Recommendation(
          imageUrl = detailed.imageLinks?.thumbnail,
          title = detailed.title,
          description = detailed.description,
          authors = detailed.authors.joinToString(", "),
          genre = detailed.categories.joinToString(", ")
        ))
      }
    } else {
      when (conversationState) {
        ConversationState.Greeting -> {
          conversationState = ConversationState.Genre
          aiReply(Questions.genres.random())
        }
        ConversationState.Genre -> {
          conversationState = ConversationState.Default
          aiReply(Questions.keywords.random())
        }
        ConversationState.Default -> {
          aiReply(Questions.keywords.random())
        }
      }
    }
  }

  private suspend fun aiReply(text: String) {
    saveMessage(Message.Text(sender = Sender.AI, text = text))
  }

  private suspend fun saveMessage(message: Message) {
    _messagesFlow.emit(listOf(message) + _messagesFlow.value)
  }

  enum class ConversationState {
    Greeting,
    Genre,
    Default,
  }
}
