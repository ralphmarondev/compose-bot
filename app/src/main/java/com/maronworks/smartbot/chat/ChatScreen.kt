package com.maronworks.smartbot.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maronworks.smartbot.chat.domain.Message
import com.maronworks.smartbot.chat.domain.MsgTag
import com.maronworks.smartbot.chat.domain.Query
import com.maronworks.smartbot.ui.theme.SmartBotTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getCurrentTimeString(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return currentDateTime.format(formatter)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    isDarkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {
    var message by rememberSaveable {
        mutableStateOf("")
    }
    val messageList = remember { mutableStateListOf<Message>() }

    messageList.add(
        Message(
            msg = "hi",
            tag = MsgTag.MESSAGE
        )
    )
    messageList.add(
        Message(
            msg = "hello",
            tag = MsgTag.RESPONSE
        )
    )


    fun onSend() {
        val trimmedMessage = message.trim()
        if (trimmedMessage.isNotBlank()) {
            messageList.add(
                Message(
                    trimmedMessage,
                    MsgTag.MESSAGE
                )
            )
            messageList.add(
                Message(
                    msg = Query.getResponse(message),
                    tag = MsgTag.RESPONSE
                )
            )
            Log.d("size", "Size of list: ${messageList.size}")

            // clear text-field
            message = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Smart Bot",
                        fontFamily = FontFamily.Monospace
                    )
                },
                actions = {
                    IconButton(onClick = { onToggleTheme(!isDarkTheme) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = ""
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Box(
                    modifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                        .navigationBarsPadding() // this adds padding for the keyboard
                ) {
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "Enter Message...",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        },
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 16.sp
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                onSend()
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Send,
                                    contentDescription = ""
                                )
                            }
                        },
                        maxLines = 2,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                onSend()
                            }
                        ),
                        // making the outline of the text field transparent
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                reverseLayout = true
            ) {
                // spacer on the bottom
                item {
                    Spacer(modifier = Modifier.height(5.dp))
                }

                // list of conversation
                items(messageList.size) { i ->
                    val index = messageList.size - 1 - i
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        if (messageList[index].tag == MsgTag.MESSAGE) {
                            MessageCard(
                                message = messageList[index].msg,
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .padding(start = 20.dp)
                            )
                        } else {
                            ResponseCard(
                                response = messageList[index].msg,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                                    .padding(end = 20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    message: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = message,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
            Text(
                text = getCurrentTimeString(),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
fun ResponseCard(
    modifier: Modifier = Modifier,
    response: String
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = response,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.W500,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
            Text(
                text = getCurrentTimeString(),
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                fontWeight = FontWeight.W400,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChatScreenPreview() {
    val isDarkTheme by rememberSaveable {
        mutableStateOf(false)
    }
    SmartBotTheme(darkTheme = isDarkTheme) {
        ChatScreen(
            isDarkTheme = isDarkTheme,
            onToggleTheme = { !isDarkTheme }
        )
    }
}