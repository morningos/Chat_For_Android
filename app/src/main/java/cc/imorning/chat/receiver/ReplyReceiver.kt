package cc.imorning.chat.receiver

import android.app.NotificationManager
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import cc.imorning.chat.utils.ChatNotificationManager
import cc.imorning.common.action.message.MessageManager
import cc.imorning.common.constant.Config

class ReplyReceiver : BroadcastReceiver() {

    private lateinit var notificationManager: NotificationManager

    override fun onReceive(context: Context?, intent: Intent?) {

        if (context == null || intent == null) {
            return
        }
        val toJidString = intent.getStringExtra(Config.Intent.Action.QUICK_REPLY_TO)
        val replyMessage = getMessageText(intent)
        if (null != replyMessage && toJidString != null) {
            MessageManager.sendMessage(jidString = toJidString, message = replyMessage.toString())
        }
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(ChatNotificationManager.CHANNEL_NEW_MESSAGES_ID)
    }

    private fun getMessageText(intent: Intent): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence(Config.Intent.Action.QUICK_REPLY)
    }

    companion object {
        private const val TAG = "ReplyReceiver"
    }
}