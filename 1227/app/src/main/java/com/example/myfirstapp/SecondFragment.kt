package com.example.myfirstapp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapter.GalleryImageAdapter
import com.example.myfirstapp.adapter.GalleryImageClickListener
import com.example.myfirstapp.fragment.GalleryFullscreenFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception
import android.graphics.BitmapFactory
import androidx.core.net.toUri
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL


class SecondFragment : Fragment(), GalleryImageClickListener {
    private val SPAN_COUNT = 3
    private val imageList = ArrayList<Bitmap>()
    lateinit var galleryAdapter: GalleryImageAdapter
    private var recyclerView: RecyclerView? = null
    private val GALLERY_REQUEST_CODE = 3001
    val imageEncodedList: ArrayList<String>? = null

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        try {
            if (resultCode == RESULT_OK && requestCode == 3001) {
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                if (data.data != null) {
                    val mImageUri = data.data
                    val cursor =
                        context?.contentResolver?.query(
                            mImageUri,
                            filePathColumn,
                            null,
                            null,
                            null
                        )
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    val imageEncoded = columnIndex?.let { cursor?.getString(it) }
                    cursor?.close()
                } else {
                    if (data.clipData != null) {
                        val mClipData = data.clipData
                        val mArrayUri = ArrayList<Uri>()
                        for (i in 0..mClipData.itemCount) {
                            val item = mClipData.getItemAt(i)
                            val uri = item.uri
                            mArrayUri.add(uri)
                            val cursor = context?.contentResolver?.query(
                                uri,
                                filePathColumn,
                                null,
                                null,
                                null
                            )
                            cursor?.moveToFirst()
                            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                            val imageEncoded = columnIndex?.let { cursor?.getString(it) }
                            if (imageEncoded != null) {
                                imageEncodedList?.add(imageEncoded)
                            }
                            cursor?.close()
                        }
                    }
                }
            }
            else {
                Toast.makeText(context, "No Image Picked", Toast.LENGTH_LONG).show()
            }
        }
        catch (e: Exception) {
            Log.d("Unexpected error>>", "on image selection")
        }
        if (imageEncodedList != null) {
            loadImages(imageEncodedList)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun loadImages(imageEncodedList: ArrayList<String>) {
        for (url in imageEncodedList) {
            imageList.add(MediaStore.Images.Media.getBitmap(context?.contentResolver, url.toUri()))
        }
        galleryAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val cameraIntent = Intent(Intent.ACTION_GET_CONTENT)
            cameraIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            cameraIntent.type = "image/*"
            activity?.startActivityForResult(Intent.createChooser(cameraIntent, "Select Picture"), GALLERY_REQUEST_CODE)
        }

        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this

        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        recyclerView!!.adapter = galleryAdapter

        return view
    }



    override fun onClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("images", imageList)
        bundle.putInt("position", position)
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        val galleryFragment = GalleryFullscreenFragment()
        galleryFragment.setArguments(bundle)
        galleryFragment.show(fragmentTransaction, "gallery")
    }
}