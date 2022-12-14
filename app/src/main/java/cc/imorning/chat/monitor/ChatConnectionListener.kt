package cc.imorning.chat.monitor

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.*
import cc.imorning.chat.activity.LoginActivity
import cc.imorning.chat.service.MessageMonitorService
import cc.imorning.common.BuildConfig
import cc.imorning.common.CommonApp
import cc.imorning.common.connection.ReconnectionWorker
import org.jivesoftware.smack.ConnectionListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.XMPPException
import org.jivesoftware.smackx.vcardtemp.VCardManager

class ChatConnectionListener : ConnectionListener {

    private val context: Context = CommonApp.getContext()
    private var messageMonitor: Intent? = null

    override fun authenticated(connection: XMPPConnection?, resumed: Boolean) {
        if (messageMonitor == null) {
            messageMonitor = Intent(CommonApp.getContext(), MessageMonitorService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(messageMonitor)
            } else {
                context.startService(messageMonitor)
            }
        }
        CommonApp.vCard = VCardManager.getInstanceFor(connection).loadVCard()
    }

    override fun connectionClosed() {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "connection closed")
        }
        context.stopService(messageMonitor)
        super.connectionClosed()
    }

    /**
     * java.net.SocketException: Software caused connection abort >>> Close cause network error
     * org.jivesoftware.smack.XMPPException$StreamErrorException  >>> Close cause sign in elsewhere
     */
    override fun connectionClosedOnError(e: Exception?) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "connection closed with error: ${e?.localizedMessage}", e)
        }
        context.stopService(messageMonitor)
        messageMonitor = null
        if (e is XMPPException.StreamErrorException) {
            val loginActivity = Intent(context, LoginActivity::class.java)
            loginActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(loginActivity)
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "??????????????????????????????", Toast.LENGTH_LONG).show()
            }
        } else {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "connection closed and reconnecting...")
            }
            reconnect(CommonApp.getContext())
        }
        super.connectionClosedOnError(e)
    }

    private fun reconnect(context: Context) {
        val constraints: Constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val reconnectionWorkerRequest: WorkRequest =
            OneTimeWorkRequestBuilder<ReconnectionWorker>()
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(context).enqueue(reconnectionWorkerRequest)
    }

    companion object {
        private const val TAG = "ChatConnectionListener"
    }
}