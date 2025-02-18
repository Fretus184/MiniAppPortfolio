package com.nees.app.nesherportfolio

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import androidx.media3.ui.PlayerView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nees.app.nesherportfolio.sys.ui.composable.HomeUI
import com.nees.app.nesherportfolio.sys.utils.Routes
import com.nees.app.nesherportfolio.ui.theme.NesherPortfolioTheme
import com.nees.app.nesherportfolio.ui.theme.ciel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            NesherPortfolioTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Surface (
                        modifier = Modifier.fillMaxSize()
                            .background(color = ciel)
                            .padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Routes.HOME_VIEW,
                        ) {
                            composable(Routes.HOME_VIEW) {
                                HomeUI(getVideoUri())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getVideoUri() : Uri {
        return Uri.parse("android.resource://"+packageName+"/"+R.raw.cielavion)
    }
}

fun Context.buildExoPlayer(uri: Uri) : ExoPlayer =
    ExoPlayer.Builder(this).build().apply {
        setMediaItem(MediaItem.fromUri(uri))
        repeatMode =  Player.REPEAT_MODE_ALL
        playWhenReady = true
        prepare()
    }

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun Context.buildPlayerView (exoPlayer: ExoPlayer) =
    PlayerView(this).apply{
        player = exoPlayer
        layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        useController = false
        resizeMode = RESIZE_MODE_ZOOM
    }
