package com.example.ca3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_PERMISSION = 1001
    private val REQUEST_CODE_PICK_IMAGE = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uploadButton = findViewById<Button>(R.id.uploadButton)
        val imageView = findViewById<ImageView>(R.id.imageView)

        uploadButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open image picker
                openImagePicker()
            } else {
                // Request permission
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION
                )
            }
        }
    }

    // Open file picker to choose an image
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    // Handle the permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open image picker
                openImagePicker()
            } else {
                // Permission denied, show explanation dialog
                showExplanationDialog()
            }
        }
    }

    // Show dialog to explain why the permission is required
    private fun showExplanationDialog() {
        AlertDialog.Builder(this)
            .setMessage("This app requires access to your storage to upload photos. Please grant permission to proceed.")
            .setPositiveButton("Yes") { _, _ ->
                // Request permission again when user clicks Yes
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSION
                )
            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }

    // Handle the result from the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                displayImage(uri)
            }
        }
    }

    // Display the selected image in an ImageView
    private fun displayImage(uri: Uri) {
        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageURI(uri)
        Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
    }
}
