package com.example.myfirstapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



/**
 * A simple [Fragment] subclass.
 */
class FirstFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var customAdapter: CustomAdapter? = null
    private var contactModelArrayList: ArrayList<ContactModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        contactModelArrayList = ArrayList()

        val PROJECTION_EMAIL: Array<out String> = arrayOf(
            ContactsContract.CommonDataKinds.Email.DATA
        )
        val PROJECTION_PHONE: Array<out String> = arrayOf(
            ContactsContract.RawContacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor_email: Cursor? = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            PROJECTION_EMAIL,
            null,
            null,
            ContactsContract.CommonDataKinds.Email.DISPLAY_NAME + " ASC"
        )

        val cursor_phone: Cursor? = context?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION_PHONE,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        var fetch_email = true
        var fetch_phone = true
        while ((cursor_email != null) || (cursor_phone != null)) {
            val contactmodel = ContactModel()
            if  (fetch_email) {
                cursor_email?.moveToNext()
                val mail = cursor_email?.getString(0)
                if (mail != null) {
                    contactmodel.setMails(mail)
                }
            }
            if (fetch_phone) {
                cursor_phone?.moveToNext()
                val name = cursor_phone?.getString(1)
                val number = cursor_phone?.getString(2)
                if (name != null) {
                    contactmodel.setNames(name)
                }
                if (number != null) {
                    contactmodel.setNumbers(number)
                }
            }

            if(!fetch_email && !fetch_phone) {
                break
            }

            contactModelArrayList!!.add(contactmodel)
            Log.d("name>>", contactmodel.getNames() + "  " + contactmodel.getNumbers() + "  " + contactmodel.getMails())

            if(cursor_email!!.isLast) {
                fetch_email = false
            }
            if(cursor_phone!!.isLast){
                fetch_phone = false
            }
        }
        cursor_email?.close()
        cursor_phone?.close()

        customAdapter = context?.let { CustomAdapter(it, contactModelArrayList!!) }
        recyclerView!!.adapter = customAdapter

        val lm = LinearLayoutManager(getContext())
        recyclerView!!.layoutManager = lm
        recyclerView!!.setHasFixedSize(true)

        return view
    }
}
