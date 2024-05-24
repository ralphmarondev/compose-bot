package com.maronworks.smartbot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.maronworks.smartbot.chat.ChatScreen
import com.maronworks.smartbot.ui.theme.SmartBotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by rememberSaveable {
                mutableStateOf(false)
            }
            SmartBotTheme(darkTheme = darkTheme) {
                ChatScreen(
                    isDarkTheme = darkTheme,
                    onToggleTheme = { darkTheme = it }
                )
            }
        }
    }
}