package cc.imorning.chat.activity.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cc.imorning.chat.App
import cc.imorning.chat.R
import cc.imorning.chat.ui.theme.ChatTheme
import cc.imorning.chat.view.ui.ComposeDialogUtils
import coil.compose.rememberAsyncImagePainter

private const val TAG = "ProfileFragment"

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]
        return ComposeView(requireContext()).apply {
            setContent {
                ChatTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ProfileScreen(profileViewModel)
                    }
                }
            }
        }
    }

}

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .offset(y = 12.dp)
            .fillMaxSize()
    ) {
        val avatarPath = "${profileViewModel.avatarPath.observeAsState().value}"
        val nickName = profileViewModel.nickname.observeAsState()
        val phoneNumber = profileViewModel.phoneNumber.observeAsState()
        val userName = profileViewModel.userName.observeAsState()

        var showBuildingDialog by remember { mutableStateOf(false) }
        if (showBuildingDialog) {
            ComposeDialogUtils.FunctionalityNotAvailablePopup { showBuildingDialog = false }
        }

        var showAboutDialog by remember { mutableStateOf(false) }
        if (showAboutDialog) {
            ComposeDialogUtils.ShowAbout { showAboutDialog = false }
        }
        Text(
            text = context.getString(R.string.title_profile),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
        )
        Divider(
            modifier = Modifier
                .padding(top = 12.dp)
                .background(color = colorResource(R.color.diver_color))
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = avatarPath),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(
                        height = 72.dp,
                        width = 72.dp
                    )
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(72.dp)
                    )
                    .offset(x = 8.dp),
                contentScale = ContentScale.FillBounds,
                alignment = Alignment.Center,
            )
            Column(
                modifier = Modifier.offset(x = 20.dp)
            ) {
                Text(
                    text = "${nickName.value}",
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.titleMedium
                )
                ClickableText(
                    text = AnnotatedString(
                        text = "${phoneNumber.value}",
                        spanStyle = SpanStyle(
                            color = Color.Blue,
                        )
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    onClick = {
                        Toast.makeText(context, phoneNumber.value, Toast.LENGTH_SHORT).show()
                    }
                )
                ClickableText(
                    text = AnnotatedString(
                        text = "@${userName.value}",
                        spanStyle = SpanStyle(
                            color = Color.Blue,
                        )
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    onClick = {
                        Toast.makeText(context, "${userName.value}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
        Text(
            text = "温带的风 温柔栖息 这样来信的才读得认真 随风摇曳的单薄纸张 不小心就多了泪痕",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 28.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            softWrap = true
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(2.dp)
                .background(color = Color.Cyan)
        )
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showAboutDialog = true },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_code),
                    contentDescription = "关于",
                    modifier = Modifier
                        .size(36.dp, 36.dp)
                        .background(
                            color = Color.Blue.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(36.dp)
                        )
                )
                Text(
                    text = "关于",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { showBuildingDialog = true },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_bug_report),
                    contentDescription = "报告问题",
                    modifier = Modifier
                        .size(36.dp, 36.dp)
                        .background(
                            color = Color.Blue.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(36.dp)
                        )
                )
                Text(
                    text = "报告问题",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { App.exitApp() },
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_exit_to_app),
                    contentDescription = "退出",
                    modifier = Modifier
                        .size(36.dp, 36.dp)
                        .background(
                            color = Color.Blue.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(36.dp)
                        )
                )
                Text(
                    text = "退出",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}