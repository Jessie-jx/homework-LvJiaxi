package jessie.cs175.hw4_translator.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import jessie.cs175.hw4_translator.R
import android.widget.TextView
import android.widget.PopupWindow
import android.view.WindowManager
import android.view.Gravity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import jessie.cs175.hw4_translator.MainActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest


class MyFragment : Fragment() {
    companion object {
        const val REQUEST_CAMERA_PERMISSION = 102
        const val REQUEST_PICK = 101
        const val REQUEST_CAPTURE = 100
    }

    lateinit var headPortrait: ImageView
    lateinit var uploadBtn: Button
    lateinit var popupWindow: PopupWindow


    private var imageUri: Uri? = null
    private var mCurrentPhotoPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_my, container, false)
        headPortrait = view.findViewById(R.id.HeadPortrait)
        uploadBtn = view.findViewById(R.id.buttonLoadPicture)

        uploadBtn.setOnClickListener {
            uploadHeadImage()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            // get pic from gallery
            REQUEST_PICK -> {
                try {
                    if (resultCode == RESULT_OK) {
                        Log.d("@=>Translator", "gallery result ok")

                        imageUri = data?.data
                        val bitmap = BitmapFactory.decodeStream(activity?.contentResolver?.openInputStream(
                            imageUri!!
                        ));
                        headPortrait.setImageBitmap(bitmap);

//                        headPortrait.setImageURI(imageUri)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            // take pic from camera
            REQUEST_CAPTURE -> {
                try {
                    if (requestCode == RESULT_OK) {
                        Log.d("@=>Translator", "camera result ok")
                        val targetW = headPortrait.width
                        val targetH = headPortrait.height
                        val bmOptions = BitmapFactory.Options()
                        bmOptions.inJustDecodeBounds = true
                        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)
                        val photoW = bmOptions.outWidth
                        val photoH = bmOptions.outHeight
                        var insampleSize = 1
                        if (photoW > targetW || photoH > targetH) {
                            val halfHeight = photoH / 2
                            val halfWidth = photoW / 2
                            while (halfHeight / insampleSize >= targetH && halfWidth / insampleSize >= targetW) {
                                insampleSize *= 2
                            }
                        }
                        bmOptions.inJustDecodeBounds = false
                        bmOptions.inSampleSize = insampleSize
                        val bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions)

                        headPortrait.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    Log.d("@=>Translator", "camera result not ok")
                    e.printStackTrace()
                }

            }
            else -> {

            }
        }

    }

    private fun uploadHeadImage() {
        val popupView =
            LayoutInflater.from(activity as Context).inflate(R.layout.layout_popup_window, null)
        val btnCarema = popupView.findViewById(R.id.btn_camera) as TextView
        val btnPhoto = popupView.findViewById(R.id.btn_photo) as TextView
        val btnCancel = popupView.findViewById(R.id.btn_cancel) as TextView

        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setBackgroundDrawable(resources.getDrawable(android.R.color.transparent))
        popupWindow.isOutsideTouchable = true
        val parent = LayoutInflater.from(activity as Context).inflate(R.layout.layout_my, null)
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0)
        //popupWindow在弹窗的时候背景半透明
        //popupWindow在弹窗的时候背景半透明
        val params: WindowManager.LayoutParams =
            requireActivity().window.attributes
        params.alpha = 0.5f

        popupWindow.setOnDismissListener {
            params.alpha = 1.0f
        }

        btnCarema.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (ContextCompat.checkSelfPermission(
                        activity as Context,
                        android.Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        activity as Context,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ),
                        REQUEST_CAMERA_PERMISSION
                    )
                } else {
                    gotoCamera()
                    popupWindow.dismiss()
                }

            }
        })

        btnPhoto.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                gotoPhoto()
                popupWindow.dismiss()
            }
        })

        btnCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                popupWindow.dismiss()
            }
        })
    }

    private fun gotoCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = createImageFile()
        } catch (ex: IOException) {
            // error
            Log.d("@=>Translator","create file error")
            ex.printStackTrace()
        }

        if (photoFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val photoURI = FileProvider.getUriForFile(
                    activity as Context,
                    "jessie.cs175.hw4_translator.fileprovider", photoFile
                )

                camera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            } else {
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
            }

        }
        startActivityForResult(camera, REQUEST_CAPTURE)

    }

    private fun gotoPhoto() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, REQUEST_PICK)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // 获取当前时间作为文件名
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"

        // 获取应用文件存储路径 Android/data/com.bytedance.camera.demo/files/Pictures
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpeg", storageDir)

        // 保存文件路径
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            gotoCamera()
            popupWindow.dismiss()
        }
    }


}