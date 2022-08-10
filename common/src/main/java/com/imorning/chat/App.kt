package com.imorning.chat

import android.app.Application
import android.content.Context
import com.imorning.common.BuildConfig
import com.imorning.common.constant.Server
import com.imorning.common.utils.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration


class App : Application() {

    companion object {
        private const val TAG = "App"

        private var application: Application? = null
        private var xmppTcpConnection: XMPPTCPConnection? = null

        fun getContext(): Context {
            return application!!
        }

        @Synchronized
        fun getTCPConnection(): XMPPTCPConnection {
            return xmppTcpConnection!!
        }

    }

    override fun onCreate() {
        super.onCreate()
        application = this

        val configurationBuilder = XMPPTCPConnectionConfiguration.builder()
        try {
            configurationBuilder.setHost(Server.HOST_NAME)
            configurationBuilder.setXmppDomain(Server.DOMAIN)
            configurationBuilder.setPort(Server.LOGIN_PORT)
            configurationBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
            configurationBuilder.setSendPresence(false)
            xmppTcpConnection = XMPPTCPConnection(configurationBuilder.build())
            MainScope().launch(Dispatchers.IO) {
                xmppTcpConnection!!.connect()
                Log.d(TAG, "server connected")
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "server connect failed", e)
            }
        }

    }

}