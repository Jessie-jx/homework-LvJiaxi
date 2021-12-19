package jessie.cs175.hw4_translator.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import jessie.cs175.hw4_translator.BuildConfig
import jessie.cs175.hw4_translator.R
import jessie.cs175.hw4_translator.db.AppPreferences
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

private const val USER_NAME_PARA = "user name"
private const val USER_EMAIL_PARA = "user email"
private const val USER_IMAGE_PARA = "user image"

class AccountFragment : Fragment() {


    lateinit var headPortrait: ImageView
    lateinit var userName: TextView
    lateinit var userEmail: TextView
    lateinit var logOutBtn: Button

    lateinit var popupWindow: PopupWindow
    lateinit var currentPhotoPath: String

    private var userNameText: String? = null
    private var userEmailText: String? = null
    private var userImageText: String? = null

    val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userNameText = it.getString(USER_NAME_PARA)
            userEmailText = it.getString(USER_EMAIL_PARA)
            userImageText = it.getString(USER_IMAGE_PARA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.layout_profile, container, false)
        headPortrait = view.findViewById(R.id.headPortrait)
        userName = view.findViewById(R.id.user_name)
        userEmail = view.findViewById(R.id.user_email)
        logOutBtn = view.findViewById(R.id.log_out_btn)

        userName.text = userNameText
        userEmail.text = userEmailText




        headPortrait.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(v: View?): Boolean {
                uploadHeadImage()
                return true
            }

        })

        logOutBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                AppPreferences.isLogin = false
                AppPreferences.username = ""
                AppPreferences.email = ""
                AppPreferences.password = ""
                val trans = fragmentManager?.beginTransaction()

                // We use the "root frame" defined in "root_fragment.xml" as the reference to replace fragment
                if (trans != null) {
                    trans.addToBackStack(null)
                    trans.replace(
                        R.id.root_frame,
                        MyFragment()
                    )
                    trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    trans.commit()
                }
            }

        })

        return view
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
                openCamera()
                popupWindow.dismiss()
            }
        })

        btnPhoto.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                openGallery()
                popupWindow.dismiss()
            }
        })

        btnCancel.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                popupWindow.dismiss()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        checkCameraPermission()
        if (AppPreferences.headImages!=""){
            Glide.with(activity as Context).load(AppPreferences.headImages).into(headPortrait)
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity as Context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(activity!!.packageManager)?.also {
                val photoFile: File? = try {
                    createCapturedPhoto()
                } catch (ex: IOException) {
                    // If there is error while creating the File, it will be null
                    null
                }
                photoFile?.also {
                    val photoURI = FileProvider.getUriForFile(
                        activity as Context,
                        "${BuildConfig.APPLICATION_ID}.fileprovider",
                        it
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(intent, REQUEST_CAPTURE)
                }

            }
        }
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(activity!!.packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK)
            }
        }
    }

    @Throws(IOException::class)
    private fun createCapturedPhoto(): File {
        val timestamp: String = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US).format(Date())
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("PHOTO_${timestamp}", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAPTURE) {
                val uri = Uri.parse(currentPhotoPath)
                headPortrait.setImageURI(uri)
                AppPreferences.headImages = currentPhotoPath
//                val bitmap = data?.extras?.get("data") as Bitmap
//                headPortrait.setImageBitmap(bitmap)
            } else if (requestCode == REQUEST_PICK) {
                val uri = data?.getData()
                headPortrait.setImageURI(uri)
            }
        }
    }

    companion object {
        const val REQUEST_CAMERA_PERMISSION = 102
        const val REQUEST_PICK = 101
        const val REQUEST_CAPTURE = 100

        @JvmStatic
        fun newInstance(name: String?, email: String?, image_path: String?) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_NAME_PARA, name)
                    putString(USER_EMAIL_PARA, email)
                    putString(USER_IMAGE_PARA, image_path)
                }
            }

    }

}