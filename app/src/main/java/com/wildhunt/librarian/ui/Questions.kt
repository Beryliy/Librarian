package com.wildhunt.librarian.ui

object Questions {
    val greetings = """
        Hi! Let me help you to choose right book. How do you feel?
        Hello! Do you want to read some new book? Describe it to me and I`ll try to find it.
        Greetings! How are you?
        Hello, let`s find some books for you. What`s on your mind?
        Hi! I`ll help you to choose perfect book to read. Can you describe me your mood?    
    """.trimIndent().split("\n")

    val genres = """
        Which genre do you prefer?
        Could you tell me which genres do you prefer?
        Describe your favourite books on this topic.
        Which books do you prefer?
    """.trimIndent().split("\n")

    val keywords = """
        Describe me book you want to read.
        Tell me about ideal book for you.
        Which problems should be covered in book?
        Do you want a book on particular topic or something?
        Give me more details on the book you wish, please.
    """.trimIndent().split("\n")

    val noBooks = "Sorry, I've found nothing for you :("
}