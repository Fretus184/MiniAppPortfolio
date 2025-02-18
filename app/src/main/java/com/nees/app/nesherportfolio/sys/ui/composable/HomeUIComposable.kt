package com.nees.app.nesherportfolio.sys.ui.composable

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.nees.app.nesherportfolio.R
import com.nees.app.nesherportfolio.buildExoPlayer
import com.nees.app.nesherportfolio.buildPlayerView
import com.nees.app.nesherportfolio.ui.theme.ciel
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue
import kotlin.math.min

private val pagerItem = listOf(
    "-----------------------",
    "-------------------------",
    "--------------------------"
)

@SuppressLint("OpaqueUnitKey")
@Composable
fun HomeUI (uri: Uri) {
    val context         = LocalContext.current
    val exoPlayer       = remember { context.buildExoPlayer(uri) }
    var showModal by rememberSaveable { mutableStateOf(false) }

    DisposableEffect (
        key1 = AndroidView(
            factory = { it.buildPlayerView(exoPlayer) },
            modifier = Modifier.fillMaxSize()
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val pagerState = rememberPagerState { pagerItem.size }

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect {
                    delay(5000)
                    val item = if (pagerState.currentPage == pagerItem.size - 1) {
                        0
                    } else {
                        pagerState.currentPage + 1
                    }
                    pagerState.animateScrollToPage(item)
                }
            }
            Column (
                modifier = Modifier.fillMaxSize(0.8f)
            ) {
                Text("Nesher | AKF", fontFamily = FontFamily.Monospace,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold, fontSize = 10.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(10.dp))

                ItemScreen(
                    compactScreen = {
                        ItemContent(pagerState)
                    },
                    mediumScreen = {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ItemContent(pagerState)
                        }
                    }
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(17.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ItemContent(pagerState)
                    }
                }
            }
            Image(
                painter = painterResource(R.drawable.nesher_logo),
                contentDescription = "logo",
                modifier = Modifier.size(55.dp)
                    .aspectRatio(1f, matchHeightConstraintsFirst = true)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .clip(CircleShape)
                    .clickable {
                        showModal = !showModal
                    }
            )

            if (showModal) {
                ShowModalBottomSheet(close = { showModal = it }, showModalBottomSheet = { showModal })
            }
        }
    }
}

@Composable
private fun ItemContent (pagerState: PagerState) {
    HorizontalPager(
        modifier = Modifier.size(200.dp)
            .pagerFadeTransition(pagerState.currentPage, pagerState),
        state = pagerState
    ) {
        Box (modifier = Modifier.fillMaxSize()
            .background(ciel.copy(0.2f), shape = RoundedCornerShape(16.dp))
            .border(1.dp, ciel, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center) {
            Text(
                pagerItem[it],
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold, fontSize = 10.sp,
                color = Color.White
            )
        }
    }
    Column (verticalArrangement = Arrangement.spacedBy(10.dp)) {
        InfoView()
    }
}

@Composable
private fun InfoView () {
    var viewMessage by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current

    Spacer(modifier = Modifier.height(20.dp))
    Text("Pour mes contacts appuyer sur le l'icon tout en bas",
        fontFamily = FontFamily.Monospace, fontSize = 15.sp
    )
    Spacer(modifier = Modifier.height(5.dp))
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        androidx.compose.animation.AnimatedVisibility(!viewMessage) {
            Image(
                painter = painterResource(id = R.drawable.icons8_whatsapp_48),
                contentDescription = "Send Message",
                modifier = Modifier.size(40.dp)
                    .clickable {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    String.format(
                                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                                        "+22870146448", message.text
                                    )
                                    /*"https://wa.me/22870146448?text${
                                message.text.replace(
                                    " ",
                                    "%20"
                                )
                            }"*/
                                )
                            )
                        )
                    }
            )
        }

        Image(
            painter = painterResource(id = R.drawable.img_icons8),
            contentDescription = "Send Message",
            modifier = Modifier.size(40.dp)
                .clickable { viewMessage = !viewMessage }
        )
        androidx.compose.animation.AnimatedVisibility(!viewMessage) {
            Text("Cliquez pour m'envoyer un message", fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        androidx.compose.animation.AnimatedVisibility(viewMessage) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                value = message,
                onValueChange = { message = it },
                placeholder = {
                    Text("Send Message") },
                trailingIcon = {
                    Row {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = "Send Message",
                            modifier = Modifier.clickable {
                                postVolleyRequest(context, message.text)
                                message = TextFieldValue("")
                            }
                        )
                    } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {
                    //localFocusManager.clearFocus()
                    //message += '\n'
                    message = insertText(message)
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShowModalBottomSheet (close: (Boolean) -> Unit, showModalBottomSheet: () -> Boolean) {
    var show by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(showModalBottomSheet()) {
        show = showModalBottomSheet()
    }

    ModalBottomSheet(
        onDismissRequest = {
            show = false
            close.invoke(false)
        },
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 16.dp,
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    "EMAIL: ", color = Color.Black,
                    fontWeight = FontWeight.Bold, fontSize = 11.sp
                )
                Text(
                    "example@gmail.com",
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold, fontSize = 10.sp,
                    color = Color.DarkGray
                )
            }
            Row {
                Text(
                    "NUMBER: ", color = Color.Black,
                    fontWeight = FontWeight.Bold, fontSize = 11.sp
                )
                Text(
                    "(+XXX) XXXXXXXX | XXXXXXXX",
                    textDecoration = TextDecoration.Underline,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold, fontSize = 10.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

private fun PagerState.calculateCurrentOffsetForPage (page: Int) : Float {
    return (currentPage - page) + currentPageOffsetFraction
}

private fun PagerState.startOffsetForPage(page: Int) : Float {
    return calculateCurrentOffsetForPage(page).coerceAtLeast(0f)
}

private fun PagerState.endOffsetForPage(page: Int) : Float {
    return calculateCurrentOffsetForPage(page).coerceAtMost(0f)
}

private fun Modifier.pagerFadeTransition (page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
        val offScreenRight = pageOffset < 0f
        val deg = 150f
        val interpolated = FastOutLinearInEasing.transform(pageOffset.absoluteValue)
        rotationY = min(interpolated * if (offScreenRight) deg else -deg, 90f)

        transformOrigin = TransformOrigin(
            pivotFractionX = if (offScreenRight) 0f else 1f,
            pivotFractionY = .5f
        )
        //translationX = pageOffset * size.width
        //alpha = 1 - pageOffset.absoluteValue
    }

private fun insertText(textFieldValue: TextFieldValue, insertText: String = "\n"): TextFieldValue {
    val maxChars = textFieldValue.text.length
    val textBeforeSelection = textFieldValue.getTextBeforeSelection(maxChars)
    val textAfterSelection = textFieldValue.getTextAfterSelection(maxChars)
    val newText = "$textBeforeSelection$insertText$textAfterSelection"
    val newCursorPosition = textBeforeSelection.length + insertText.length
    return TextFieldValue(
        text = newText,
        selection = TextRange(newCursorPosition)
    )
}

private fun postVolleyRequest(
    context: Context,
    message: String
) {
    val queue = Volley.newRequestQueue(context)
    val TELEGRAM_TOKEN = "************************************************"
    val TELEGRAM_CHAT_ID = "***********"
    val url = "https://api.telegram.org/bot$TELEGRAM_TOKEN/sendMessage"

    val stringRequest = object : StringRequest(Method.POST, url,
        {response ->
            Log.d("SERVICE", response)
            Toast.makeText(context, "Message envoye...", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(context, "Message non envoye reessayer...", Toast.LENGTH_SHORT).show()
            Log.d("SERVICE", it.toString())
        }) {
        override fun getParams(): Map<String, String> {
            val params          = HashMap<String, String>()
            params["chat_id"]   = TELEGRAM_CHAT_ID
            params["text"]      = message
            return params
        }
    }

    queue.add(stringRequest)
}