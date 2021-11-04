package com.curry.composetest.data

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import com.curry.composetest.R
import kotlin.math.max
import kotlin.math.min

/**
 * 此实体表示棋子的信息
 *
 * @Author: curry
 * @CreateDate: 2021/11/1
 * @Description: 棋子的实体
 */
data class ChessBean(
    val name: String,       //名称
    val w: Int,             //宽
    val h: Int,             //高
    val drawable: Int,      //图片
    val color: Color,       //颜色
    val offset: IntOffset = IntOffset(0, 0)//偏移量
)


val cao: ChessBean = ChessBean("曹操", 2, 2, R.drawable.ic_launcher_foreground, Color.Red)
val zhang: ChessBean = ChessBean("张飞", 1, 2, R.drawable.ic_launcher_foreground, Color.Blue)
val zhao: ChessBean = ChessBean("赵云", 1, 2, R.drawable.ic_launcher_foreground, Color.Yellow)
val huang: ChessBean = ChessBean("黄忠", 1, 2, R.drawable.ic_launcher_foreground, Color.Cyan)
val ma: ChessBean = ChessBean("马超", 1, 2, R.drawable.ic_launcher_foreground, Color.Green)
val guan: ChessBean = ChessBean("关羽", 2, 1, R.drawable.ic_launcher_foreground, Color.Magenta)

@ExperimentalStdlibApi
val bing = buildList {
    repeat(4) {
        add(ChessBean("兵$it", 1, 1, R.drawable.ic_launcher_foreground, Color.Gray))
    }
}

typealias ChessOpening = List<Triple<ChessBean, Int, Int>>

/**
 * 表示开局时棋子的位置
 */
@ExperimentalStdlibApi
val gameOpening: ChessOpening = buildList {
    add(Triple(zhang, 0, 0))
    add(Triple(cao, 1, 0))
    add(Triple(zhao, 3, 0))
    add(Triple(huang, 0, 2))
    add(Triple(ma, 3, 2))
    add(Triple(guan, 1, 2))
    add(Triple(bing[0], 0, 4))
    add(Triple(bing[1], 1, 3))
    add(Triple(bing[2], 2, 3))
    add(Triple(bing[3], 3, 4))
}

/**
 * 棋盘大小
 */
const val boardGridPx = 200
const val boardWidth = boardGridPx * 4
const val boardHeight = boardGridPx * 5

fun ChessOpening.toList() =
    map { (chess, x, y) ->
        chess.moveBy(IntOffset(x * boardGridPx, y * boardGridPx))
    }

fun ChessBean.moveBy(offset: IntOffset) = copy(offset = this.offset + offset)
fun ChessBean.moveByX(x: Int) = moveBy(IntOffset(x, 0))
fun ChessBean.moveByY(y: Int) = moveBy(IntOffset(0, y))


val ChessBean.width get() = w * boardGridPx
val ChessBean.height get() = h * boardGridPx
val ChessBean.left get() = offset.x
val ChessBean.right get() = left + width
val ChessBean.top get() = offset.y
val ChessBean.bottom get() = top + height

infix fun ChessBean.isAboveOf(other: ChessBean) =
    (bottom <= other.top) && ((left until right) intersect (other.left until other.right)).isNotEmpty()

infix fun ChessBean.isToRightOf(other: ChessBean) =
    (left >= other.right) && ((top until bottom) intersect (other.top until other.bottom)).isNotEmpty()

infix fun ChessBean.isToLeftOf(other: ChessBean) =
    (right <= other.left) && ((top until bottom) intersect (other.top until other.bottom)).isNotEmpty()

infix fun ChessBean.isBelowOf(other: ChessBean) =
    (top >= other.bottom) && ((left until right) intersect (other.left until other.right)).isNotEmpty()


fun ChessBean.checkAndMoveX(x: Int, others: List<ChessBean>): ChessBean {
    others.filter { it.name != name }.forEach { other ->
        if (x > 0 && this isToLeftOf other && right + x >= other.left)
            return moveByX(other.left - right)
        else if (x < 0 && this isToRightOf other && left + x <= other.right)
            return moveByX(other.right - left)
    }
    return if (x > 0) moveByX(min(x, boardWidth - right)) else moveByX(max(x, 0 - left))

}

fun ChessBean.checkAndMoveY(y: Int, others: List<ChessBean>): ChessBean {
    //成功后，曹操棋子可以移出！
    if (name == "曹操") {
        Log.e("checkAndMoveY", "曹操: ${boardHeight}, $bottom")
        if (y > 0 && boardHeight - bottom <= 0 && left == boardGridPx) {
            return moveByY(y)
        }
    }

    others.filter {
        Log.d("checkAndMoveY", "checkAndMoveY: ${it.name}, $name")
        it.name != name
    }.forEach { other ->
        Log.d("checkAndMoveY", "y:$y, other.name: ${other.name} ,other.top: ${other.top}, $bottom")
        if (y > 0 && this isAboveOf other && bottom + y >= other.top) {
            Log.d("checkAndMoveY", "isAboveOf")
            //棋子向下拖动，y>0
            //移动的棋子要在其他的上方（底边距小于等于其他的高且在左右范围内）
            //移动棋子底边距+偏移量大于等于其他的高
            return moveByY(other.top - bottom)
        } else if (y < 0 && this isBelowOf other && top + y <= other.bottom)
            return moveByY(other.bottom - top)
    }
    Log.d("checkAndMoveY", "last")

    return if (y > 0) moveByY(min(y, boardHeight - bottom)) else moveByY(max(y, 0 - top))
}

var count = 0

fun count() {
    count++
}
