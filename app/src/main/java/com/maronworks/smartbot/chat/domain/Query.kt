package com.maronworks.smartbot.chat.domain

class Query {
    companion object {
        fun getResponse(message: String): String {

            conversationList.forEachIndexed { _, item ->
                if (item.query == message.lowercase().trim().replace(" ", ""))
                    return item.response
            }

            return "Sorry, I don't understand you."
        }
    }
}

data class Conversation(
    val query: String,
    val response: String
)

val conversationList = listOf(
    Conversation(
        query = "hi",
        response = "hello"
    ),
    Conversation(
        query = "hello",
        response = "hi"
    ),
    Conversation(
        query = "whocreatedyou",
        response = "Ralph Maron Eda created me. All by himself. From Scratch."
    ),
    Conversation(
        query = "howoldareyou",
        response = "I don't count, but I'm great in my days."
    ),


)