package com.example.myfirstapp

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapter.GalleryImageAdapter
import com.example.myfirstapp.adapter.GalleryImageClickListener
import com.example.myfirstapp.adapter.Image
import com.example.myfirstapp.fragment.GalleryFullscreenFragment
import android.widget.Toast
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.content.Intent
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.FileNotFoundException


class SecondFragment : Fragment(), GalleryImageClickListener {
    private val SPAN_COUNT = 3
    private val imageList = ArrayList<Bitmap>()
    lateinit var galleryAdapter: GalleryImageAdapter
    private var recyclerView: RecyclerView? = null
    private val GALLERY_REQUEST_CODE = 3001

    override fun onActivityResult(reqCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(reqCode, resultCode, data)
        Log.d("onActivityResult>>", "I got result : [reqCode = " + reqCode.toString() + ", resultCode = " + resultCode.toString() + ", data = " + data.toString() + "]")
        if (resultCode == RESULT_OK) {
            try {
                val imageUri = data!!.data
                val imageStream = context?.contentResolver?.openInputStream(imageUri)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                imageList.add(selectedImage)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        } else {
            Toast.makeText(context, "You haven't picked Image", Toast.LENGTH_LONG).show()
        }
    }


    @SuppressLint("LongLogTag")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton).setOnClickListener {
            val cameraIntent = Intent(Intent.ACTION_GET_CONTENT)
            cameraIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            cameraIntent.type = "image/*"
            Log.d("startActivityTriggered>>", "on second fragment")
            super.getActivity()?.startActivityForResult(Intent.createChooser(cameraIntent, "Select Picture"), GALLERY_REQUEST_CODE)
        }

        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this

        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        recyclerView!!.adapter = galleryAdapter
        loadImages()
        return view
    }

    private fun loadImages() {
        galleryAdapter.notifyDataSetChanged()
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