package com.nispax.fileexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.nispax.fileexplorer.ui.NispaxApp
import com.nispax.fileexplorer.ui.theme.NispaxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NispaxTheme {
                NispaxApp()
            }
        }
    }
}
