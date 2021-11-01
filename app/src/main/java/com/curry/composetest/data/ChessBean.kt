package com.curry.composetest.data

import androidx.compose.ui.unit.IntOffset
import com.curry.composetest.R

/**
 * @Author: curry
 * @CreateDate: 2021/11/1
 * @Description: 棋子的实体
 */
data class ChessBean(
    val name: String,       //名称
    val width: Int,      //宽
    val height: Int,     //高
    val drawable: Int,      //图片
    val offset: IntOffset = IntOffset(0, 0)//偏移量
)


val cao: ChessBean = ChessBean("曹操", 2, 2, R.drawable.ic_launcher_background)
val zhang: ChessBean = ChessBean("张飞", 1, 2, R.drawable.ic_launcher_background)
val zhao: ChessBean = ChessBean("赵云", 1, 2, R.drawable.ic_launcher_background)
val huang: ChessBean = ChessBean("黄忠", 1, 2, R.drawable.ic_launcher_background)
val ma: ChessBean = ChessBean("马超", 1, 2, R.drawable.ic_launcher_background)
val guan: ChessBean = ChessBean("关羽", 2, 1, R.drawable.ic_launcher_background)
@ExperimentalStdlibApi
val bing = buildList { repeat(4){
    add(ChessBean("兵$it", 1, 1, R.drawable.ic_launcher_background))
} }
