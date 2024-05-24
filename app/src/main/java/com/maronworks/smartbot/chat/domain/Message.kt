package com.maronworks.smartbot.chat.domain

data class Message(
    val msg: String,
    val tag: Int
)

class MsgTag {
    companion object {
        const val MESSAGE = 1
        const val RESPONSE = 2
    }
}