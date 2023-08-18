package apex.play.time.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.MimeTypeMap
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

private var mCameraPhotoPath = ""
private var mCapturedImageURI: Uri = Uri.parse("")
private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
private var mUploadMessage: ValueCallback<Uri?>? = null

@Composable
fun WebScreen(
    modifier: Modifier = Modifier,
    url: String,
) {
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.data.let { uri ->
            if (uri != null) {
                mFilePathCallback?.onReceiveValue(arrayOf(uri))
            }
        }
    }
    val context = LocalContext.current
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(4.dp),
    ) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                webChromeClient = object : WebChromeClient() {

                    override fun onShowFileChooser(
                        webView: WebView?,
                        filePathCallback: ValueCallback<Array<Uri>>?,
                        fileChooserParams: FileChooserParams?
                    ): Boolean {
                        val acceptTypes = fileChooserParams!!.acceptTypes
                        val allowMultiple =
                            fileChooserParams!!.mode === FileChooserParams.MODE_OPEN_MULTIPLE
                        val captureEnabled = fileChooserParams.isCaptureEnabled
                        return startPickerIntent(
                            callback = filePathCallback,
                            acceptTypes = acceptTypes,
                            allowMultiple = allowMultiple,
                            captureEnabled = captureEnabled,
                            activityResultLauncher = activityResultLauncher,
                            context = context
                        )

                    }


                }
                settings.domStorageEnabled = true
                settings.javaScriptCanOpenWindowsAutomatically = true
                val cookieManager = CookieManager.getInstance()
                cookieManager.setAcceptCookie(true)
                settings.javaScriptEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.domStorageEnabled = true
                settings.databaseEnabled = true
                settings.setSupportZoom(false)
                settings.allowFileAccess = true
                settings.allowContentAccess = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true

                onBackPressedDispatcher?.addCallback {
                    if (this@apply.canGoBack()) {
                        this@apply.goBack()
                    }
                }
                loadUrl(url)
            }
        }, update = {
            it.loadUrl(url)
        })

    }
}

private fun startPickerIntent(
    callback: ValueCallback<Array<Uri>>?,
    acceptTypes: Array<String>,
    allowMultiple: Boolean?,
    captureEnabled: Boolean?,
    activityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
    context: Context
): Boolean {
    mFilePathCallback = callback;
    /* val extraIntents = ArrayList<Parcelable>()
     extraIntents.add(getPhotoIntent(
         activityResultLauncher = activityResultLauncher,
         context = context))*/
    val fileSelectionIntent = getFileChooserIntent(acceptTypes, allowMultiple)
    val pickerIntent = Intent(Intent.ACTION_CHOOSER)
    pickerIntent.putExtra(Intent.EXTRA_INTENT, fileSelectionIntent);
    //pickerIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents.toTypedArray());
    activityResultLauncher.launch(pickerIntent)
    return true;
}

private fun getFileChooserIntent(
    acceptTypes: Array<String>,
    allowMultiple: Boolean?
): Intent {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "*/*"
    intent.putExtra(Intent.EXTRA_MIME_TYPES, getAcceptedMimeType(acceptTypes))
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
    return intent
}

private fun getAcceptedMimeType(types: Array<String>): Array<String?>? {
    if (types.isEmpty()) {
        val DEFAULT_MIME_TYPES = "*/*";
        return arrayOf<String?>(DEFAULT_MIME_TYPES)
    }
    val mimeTypes = arrayOfNulls<String>(types.size)
    for (i in types.indices) {
        val t = types[i]
        val regex = Regex("\\.\\w+")
        if (t.matches(regex)) {
            val oldValue = ".";
            val newValue = "";
            val mimeType = getMimeTypeFromExtension(t.replace(oldValue, newValue))
            mimeTypes[i] = mimeType
        } else {
            mimeTypes[i] = t
        }
    }
    return mimeTypes
}

private fun getMimeTypeFromExtension(extension: String?): String? {
    var type: String? = null
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    return type
}

