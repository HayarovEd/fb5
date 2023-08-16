package com.edurda77.fb5.presentation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

private var mCameraPhotoPath = ""
private var mCapturedImageURI: Uri = Uri.parse("")
private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
private var mUploadMessage: ValueCallback<Uri?>? = null

@Composable
fun WebScreen(
    modifier: Modifier = Modifier,
    url: String = "https://ya.ru/",
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
                        return startPickerIntent(
                            filePath = filePathCallback,
                            activityResultLauncher = activityResultLauncher,
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
    filePath: ValueCallback<Array<Uri>>?,
    activityResultLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>,
): Boolean {
    if (mFilePathCallback != null) {
        mFilePathCallback!!.onReceiveValue(null)
    }
    mFilePathCallback = filePath
    var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    var photoFile: File? = null
    try {
        photoFile = createImageFile()
        takePictureIntent?.putExtra("PhotoPath", mCameraPhotoPath)
    } catch (ex: IOException) {
        // Error occurred while creating the File
        Log.e("ErrorCreatingFile", "Unable to create Image File", ex)
    }

    // Continue only if the File was successfully created
    if (photoFile != null) {
        mCameraPhotoPath = "file:" + photoFile.absolutePath
        takePictureIntent?.putExtra(
            MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(photoFile)
        )
    } else {
        takePictureIntent = null
    }
    val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
    contentSelectionIntent.type = "image/*"
    val intentArray: Array<Intent?> =
        takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
    val chooserIntent = Intent(Intent.ACTION_CHOOSER)
    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
    activityResultLauncher.launch(chooserIntent)
    return true
}

@SuppressLint("SimpleDateFormat")
@Throws(IOException::class)
private fun createImageFile(): File {
    // Create an image file name
    val timeStamp =
        SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES
    )
    return File.createTempFile(
        imageFileName,  /* prefix */
        ".jpg",  /* suffix */
        storageDir /* directory */
    )
}

