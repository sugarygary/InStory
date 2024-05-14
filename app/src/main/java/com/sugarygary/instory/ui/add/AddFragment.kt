package com.sugarygary.instory.ui.add

import android.Manifest
import android.app.Dialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.sugarygary.instory.BuildConfig
import com.sugarygary.instory.R
import com.sugarygary.instory.data.repository.State
import com.sugarygary.instory.databinding.DialogImageChooserBinding
import com.sugarygary.instory.databinding.FragmentAddBinding
import com.sugarygary.instory.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@AndroidEntryPoint
class AddFragment : BaseFragment<FragmentAddBinding>(FragmentAddBinding::inflate) {
    private val viewModel: AddViewModel by viewModels()
    private var currentLatLng: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri == null || uri == Uri.EMPTY) {
            return@registerForActivityResult
        } else {
            viewModel.setImageUri(uri)
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getLastLocation()
                }

                else -> {
                    binding.switch1.isChecked = false
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.unable_to_access_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    private fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile("temp_uploaded", ".jpg", filesDir)
    }

    private fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(
            buffer,
            0,
            length
        )
        outputStream.close()
        inputStream.close()
        return myFile
    }

    private fun showImage() {
        with(binding) {
            if (viewModel.imageUri.value != null && viewModel.imageUri.value != Uri.EMPTY) {
                ivPreview.setImageURI(null)
                ivPreview.setImageURI(viewModel.imageUri.value)
            } else {
                ivPreview.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_image_24
                    )
                )
            }
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        } else {
            viewModel.setImageUri(Uri.EMPTY)
            showImage()
        }
    }

    private fun getImageUri(context: Context): Uri {
        var uri: Uri? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "temp_preview.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/InstoryCamera/")
            }
            val contentResolver = context.contentResolver
            val projection = arrayOf(MediaStore.Images.Media._ID)
            val selection =
                "${MediaStore.MediaColumns.RELATIVE_PATH} = ? AND ${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
            val selectionArgs = arrayOf(
                "Pictures/InstoryCamera/", "temp_preview.jpg"
            )
            val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )

            cursor?.use {
                if (cursor.moveToFirst()) {
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    val deleteUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    contentResolver.delete(deleteUri, null, null)
                }
            }

            uri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        }
        return uri ?: getImageUriForPreQ(context)
    }

    private fun getImageUriForPreQ(context: Context): Uri {
        val filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(filesDir, "/InstoryCamera/temp_preview.jpg")
        if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdir()
        return FileProvider.getUriForFile(
            context, "${BuildConfig.APPLICATION_ID}.fileprovider", imageFile
        )
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLatLng = LatLng(location.latitude, location.longitude)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.fail_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private fun showDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogBinding: DialogImageChooserBinding =
            DialogImageChooserBinding.inflate(layoutInflater)
        with(dialog) {
            setContentView(dialogBinding.root)
            show()
            window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.BOTTOM)
        }
        with(dialogBinding) {
            btnGallery.setOnClickListener {
                launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                dialog.dismiss()
            }
            btnPhoto.setOnClickListener {
                val imageUri = getImageUri(requireContext())
                viewModel.setImageUri(imageUri)
                launcherCamera.launch(imageUri)
                dialog.dismiss()
            }
        }
    }

    override fun handleTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            startContainerColor = requireContext().getColor(R.color.md_theme_primary)
            endContainerColor = requireContext().getColor(R.color.md_theme_background)
            duration = 500
            pathMotion = MaterialArcMotion()
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun initData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun setupListeners() {
        with(binding) {
            materialToolbar3.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            ivPreview.setOnClickListener {
                showDialog()
            }
            edAddDescription.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    edAddDescription.error = getString(R.string.required_field)
                }
            }
            buttonAdd.setOnClickListener {
                val tempUri = viewModel.imageUri.value
                if (tempUri == null || tempUri == Uri.EMPTY) {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.please_choose_an_image), Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (edAddDescription.text.toString().isEmpty()) {
                    edAddDescription.error = getString(R.string.required_field)
                    edAddDescription.requestFocus()
                    return@setOnClickListener
                }
                val imageFile = uriToFile(tempUri, requireContext())
                val description = edAddDescription.text.toString()
                if (switch1.isChecked) {
                    viewModel.postStory(
                        imageFile,
                        description,
                        currentLatLng?.latitude?.toFloat(),
                        currentLatLng?.longitude?.toFloat()
                    )
                } else {
                    viewModel.postStory(
                        imageFile,
                        description,
                        null,
                        null
                    )
                }

            }
            switch1.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        !checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    ) {
                        requestPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    } else {
                        getLastLocation()
                    }
                }
            }
        }
    }

    override fun setupObservers() {
        with(binding) {
            viewModel.imageUri.observe(viewLifecycleOwner) {
                if (it != null && it != Uri.EMPTY) {
                    ivPreview.setImageURI(null)
                    ivPreview.setImageURI(it)
                } else {
                    ivPreview.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.baseline_image_24
                        )
                    )
                }
            }
            viewModel.postResponse.observe(viewLifecycleOwner) {
                with(binding) {
                    when (it) {
                        State.Empty -> {}
                        is State.Error -> {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.error_uploading_story),
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBar3.isInvisible = true
                            buttonAdd.isVisible = true
                        }

                        State.Loading -> {
                            buttonAdd.isInvisible = true
                            progressBar3.isVisible = true
                        }

                        is State.Success -> {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.story_successfully_posted),
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(AddFragmentDirections.actionAddFragmentToHomeFragment())
                        }
                    }
                }
            }
        }
    }
}