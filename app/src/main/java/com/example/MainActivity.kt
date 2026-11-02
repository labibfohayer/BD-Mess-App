package com.example

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

  private var webViewInstance: WebView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
        ) {
          MessWebViewScreen(
            onWebViewCreated = { webViewInstance = it }
          )
        }
      }
    }
  }

  inner class WebAppInterface(private val context: Context) {

    @JavascriptInterface
    fun sendSMS(phoneNumber: String, message: String) {
      try {
        val uri = Uri.parse("smsto:$phoneNumber")
        val intent = Intent(Intent.ACTION_SENDTO, uri).apply {
          putExtra("sms_body", message)
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
      } catch (e: Exception) {
        val fallbackIntent = Intent(Intent.ACTION_VIEW).apply {
          data = Uri.parse("sms:$phoneNumber?body=${Uri.encode(message)}")
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
          context.startActivity(fallbackIntent)
        } catch (_: Exception) {
          Toast.makeText(context, "SMS App not found", Toast.LENGTH_SHORT).show()
        }
      }
    }

    @JavascriptInterface
    fun shareWhatsApp(phoneNumber: String, message: String) {
      try {
        val cleanPhone = phoneNumber.replace("[^0-9]".toRegex(), "")
        val targetPhone = if (cleanPhone.startsWith("88")) cleanPhone else "88$cleanPhone"
        val url = "https://api.whatsapp.com/send?phone=$targetPhone&text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
      } catch (e: Exception) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
          type = "text/plain"
          putExtra(Intent.EXTRA_TEXT, message)
          addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share BD Mess Reminder"))
      }
    }

    @JavascriptInterface
    fun copyToClipboard(text: String) {
      val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
      val clip = ClipData.newPlainText("BD Mess", text)
      clipboard.setPrimaryClip(clip)
      Toast.makeText(context, "ক্লিপবোর্ডে কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun showToast(message: String) {
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Composable
  fun MessWebViewScreen(onWebViewCreated: (WebView) -> Unit) {
    val context = this@MainActivity

    BackHandler {
      if (webViewInstance?.canGoBack() == true) {
        webViewInstance?.goBack()
      } else {
        finish()
      }
    }

    AndroidView(
      modifier = Modifier.fillMaxSize(),
      factory = { ctx ->
        WebView(ctx).apply {
          onWebViewCreated(this)
          settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            builtInZoomControls = false
            displayZoomControls = false
            allowFileAccess = true
            cacheMode = WebSettings.LOAD_DEFAULT
          }
          webChromeClient = WebChromeClient()
          webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
              view: WebView?,
              request: WebResourceRequest?
            ): Boolean {
              val url = request?.url?.toString() ?: return false
              if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("mailto:") || url.startsWith("https://wa.me") || url.startsWith("https://api.whatsapp.com")) {
                try {
                  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                  ctx.startActivity(intent)
                  return true
                } catch (e: Exception) {
                  return false
                }
              }
              return false
            }
          }
          addJavascriptInterface(WebAppInterface(context), "AndroidBridge")
          loadUrl("file:///android_asset/index.html")
        }
      }
    )
  }
}
