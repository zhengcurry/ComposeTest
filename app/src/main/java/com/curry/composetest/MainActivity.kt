package com.curry.composetest

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.curry.composetest.ui.theme.ComposeTestTheme

/**
 * ### 为什么采用Compose
 * 1. 更少的代码
 * 2. 实时预览，可简单交互
 * 3. 加快应用开发，可构建多个预览
 * 4. 功能增强，体现在动画，主题
 *
 *
 *
 * 可组合函数可以按任何顺序执行。
 * 可组合函数可以并行执行。
 * 重组会跳过尽可能多的可组合函数和 lambda。
 * 重组是乐观的操作，可能会被取消。
 * 可组合函数可能会像动画的每一帧一样非常频繁地运行。
 */
/**
 * @Author: curry
 * @CreateDate: 2021/11/1
 * @Description: 声明式UI
 * Jetpack Compose 是一个适用于 Android 的新式声明性界面工具包
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Conversation(listOf("Kotlin", "Android", "Java", "Flutter"))
                }
            }
        }

    }
}

@Composable
fun Conversation(messages: List<String>) {
    LazyColumn {
        items(messages.size) { message ->
            Greeting(messages[message])
        }
    }
}


/**
 * 可组合函数都必须带有此注释
 * @Composable 此注释可告知 Compose 编译器：此函数旨在将数据转换为界面。
 */
var a = "TEST"

@Composable
private fun NamePickerItem(name: String, onClicked: (String) -> Unit) {
    Text(name, Modifier.clickable(onClick = { onClicked(name) }))
}

@Composable
fun Greeting(name: String) {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(text = "Column1 $name!")
        Text(text = "Column2 $name!")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            a = "你好"
        }) {
            Text("I've been clicked $a times")
            NamePickerItem("    | picker"){ Log.e("curry", "Button: onClick")}
        }
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "ic_launcher_background",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Divider()
        Row() {
            Text(text = "Row1 $name!")
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Row2 $name!", style = MaterialTheme.typography.subtitle2)
        }
        Box() {
            Text(text = "Box1 $name!")
            Text(text = "Box2 $name!", color = MaterialTheme.colors.secondaryVariant)
        }

        /**
         * 当用户与界面交互时，界面会发起 onClick 等事件。这些事件应通知应用逻辑，应用逻辑随后可以改变应用的状态。
         * 当状态发生变化时，系统会使用新数据再次调用可组合函数。这会导致重新绘制界面元素，此过程称为“重组”。
         *
         * 在命令式界面模型中，如需更改某个微件，您可以在该微件上调用 setter 以更改其内部状态。在 Compose 中，
         * 您可以使用新数据再次调用可组合函数。这样做会导致函数进行重组 -- 系统会根据需要使用新数据重新绘制函数发出的微件。
         * Compose 框架可以智能地仅重组已更改的组件
         *
         *
         * 切勿依赖于执行可组合函数所产生的附带效应，因为可能会跳过函数的重组。如果您这样做，
         * 用户可能会在您的应用中遇到奇怪且不可预测的行为。
         * 附带效应是指对应用的其余部分可见的任何更改。
         * 例如，以下操作全部都是危险的附带效应：
         * 1. 写入共享对象的属性
         * 2. 更新 ViewModel 中的可观察项
         * 3. 更新共享偏好设置
         */
        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        )

        Surface(
            shape = MaterialTheme.shapes.medium,
            contentColor = Color.Red,
            border = BorderStroke(2.dp, Color.Green),
            elevation = 2.dp,
            // surfaceColor color will be changing gradually from primary to surface
            color = Color.Blue,
            // animateContentSize will change the Surface size gradually
            modifier = Modifier
//                .animateContentSize()
                .padding(1.dp)
                .clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = "SurfaceSurfaceSurfaceSurfaceSurfaceSurfaceSurfaceSurfaceSurfaceSurfaceSurfaceSurfaceSurfaceSurface",
                modifier = Modifier.padding(all = 4.dp),
                maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                style = MaterialTheme.typography.body2
            )
        }
    }

}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun DefaultPreview() {
    //darkTheme = true
    ComposeTestTheme() {
        Conversation(listOf("Kotlin", "Android"))
    }
}