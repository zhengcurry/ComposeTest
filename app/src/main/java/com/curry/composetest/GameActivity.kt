package com.curry.composetest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.curry.composetest.data.*
import com.curry.composetest.ui.theme.ComposeTestTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.ui.unit.*

/**
 * 参考：https://juejin.cn/post/7000908871292157989#heading-10
 *
 * 1. 界面UI
 * 2. 滑动监听
 * 2. 逻辑处理
 *
 *
 *
 * @Author: curry
 * @CreateDate: 2021/11/1
 * @Description: 声明式UI
 * Jetpack Compose 是一个适用于 Android 的新式声明性界面工具包
 */
class GameActivity : ComponentActivity() {
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                Column {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = "华容道: $count",
                        style = MaterialTheme.typography.h5
                    )
                    var chessState: List<ChessBean> by remember {
                        mutableStateOf(gameOpening.toList())
                    }
                    with(LocalDensity.current) {
                        showChess(
                            Modifier.weight(1f),
                            chessList = chessState
                        ) { cur, x, y -> // onMove回调
                            //更新状态，刷新UI
                            chessState = chessState.map { //it: Chess
                                if (it.name == cur) {
                                    if (x != 0) it.checkAndMoveX(x, chessState)
                                    else it.checkAndMoveY(y, chessState)
                                } else {
                                    it
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}

/**
 * 在非Density的Scope下无法调用px.toDp()(比如：100.toDp())
 * 只能调用px.dp(比如：100.dp)，这2个是有本质区别的。
 */
@Composable
fun Density.showChess(
    modifier: Modifier = Modifier,
    chessList: List<ChessBean>,
    onMove: (chess: String, x: Int, y: Int) -> Unit = { _, _, _ -> }
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier.fillMaxSize()
    ) {
        Box(
            Modifier
                //注意padding前后background的调用, 这里是有先后顺序的
                .background(MaterialTheme.colors.secondary.copy(alpha = 0.2f))
                .padding(10.dp)
                .width(boardWidth.toDp())
                .height(boardHeight.toDp())
                .background(MaterialTheme.colors.secondary)
                .align(Alignment.Center)
        ) {

            chessList.forEach { chess ->
                Box(
                    modifier = Modifier
                        .offset { chess.offset }
                        .width(chess.width.toDp())
                        .height(chess.height.toDp())
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color.Black)
                            .background(chess.color)
                            /**
                             * 两种监听拖拽的写法 .draggable 、 .pointerInput
                             * orientation 用来指定监听什么方向的手势：水平或垂直。
                             * rememberDraggableState保存拖动状态，onDelta 指定手势的回调
                             */
                            .draggable(
                                orientation = Orientation.Horizontal,
                                state = rememberDraggableState(onDelta = {
                                    //水平移动的距离，进行回调
                                    onMove(chess.name, it.roundToInt(), 0)
                                }),
                                onDragStopped = { count() }
                            )
                            /**
                             * 监听任意方向的拖拽呢，可以使用 detectDragGestures
                             * detectDragGestures 也提供了水平、垂直版本供选择:
                             * detectHorizontalDragGestures,detectVerticalDragGestures
                             */
                            .pointerInput(Unit) {
                                scope.launch {//demonstrate detectDragGestures
                                    detectVerticalDragGestures(
                                        onDragEnd = { count() },
                                        onVerticalDrag = { change, dragAmount ->
                                            //垂直移动监听，进行回调
                                            change.consumeAllChanges()
                                            onMove(chess.name, 0, dragAmount.roundToInt())
                                        })
                                }
                            }
                        /*.draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState(onDelta = {
                                onMove(chess.name, 0, it.roundToInt())
                            })
                        )*/
                        /*.pointerInput(Unit) {
                            scope.launch {//监听水平拖拽
                                detectHorizontalDragGestures { change, dragAmount ->
                                    change.consumeAllChanges()
                                    onMove(chess.name, 0, dragAmount.roundToInt())
                                }
                            }
                            scope.launch {//监听垂直拖拽
                                detectVerticalDragGestures { change, dragAmount ->
                                    change.consumeAllChanges()
                                    onMove(chess.name, 0, dragAmount.roundToInt())
                                }
                            }
                        }*/,
                        painter = painterResource(id = chess.drawable),
                        contentDescription = chess.name
                    )
                    Text(
                        chess.name, color = Color.White, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


@ExperimentalStdlibApi
@Preview
@Composable
fun preview() {
    ComposeTestTheme() {
        Density(2.7f, 1f)
            .showChess(chessList = gameOpening.toList())
    }
}
