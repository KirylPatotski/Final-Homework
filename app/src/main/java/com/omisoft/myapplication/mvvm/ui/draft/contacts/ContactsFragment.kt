package com.omisoft.myapplication.mvvm.ui.draft.contacts

import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.omisoft.myapplication.R

class ContactsFragment : Fragment() {
    companion object {
        private const val TAG = "ContactsFragment"
    }

    private val contactDataList = mutableListOf<ContactData>()
    private lateinit var contactsRecycler: RecyclerView
    private lateinit var permissionResolver: ActivityResultLauncher<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_contacts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactsRecycler = view.findViewById(R.id.contacts_recycler)

        permissionResolver = requireActivity().registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                readContacts()
            }
        }

        readContacts()
    }

    private fun readContacts() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionResolver.launch(android.Manifest.permission.READ_CONTACTS)
            return
        }

        try {
            val cursor = requireActivity().contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
            cursor?.let { nonNullCursor ->
                while (nonNullCursor.moveToNext()) {
                    val contactId = nonNullCursor.getString(nonNullCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                    val contactName = nonNullCursor.getString(nonNullCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                    val hasNumbers = nonNullCursor.getInt(nonNullCursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    Log.d(TAG, "Contact Id: $contactId, contact name: $contactName")

                    if (hasNumbers > 0) {
                        val numberCursor = requireActivity().contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                            "contact_id = $contactId",
                            null,
                            null
                        )

                        numberCursor?.let { nonNullNumberCursor ->
                            val numbers = mutableListOf<String>()
                            while (nonNullNumberCursor.moveToNext()) {
                                val number =
                                    nonNullNumberCursor.getString(nonNullNumberCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                numbers.add(number)
                            }
                            if (numbers.isNotEmpty()) {
                                contactDataList.add(ContactData(contactName, numbers))
                            }
                        }
                    }
                }
                Log.d(TAG, "Contacts size = ${contactDataList.size}")
                showContactsList()
            }
        } catch (exception: Exception) {
            Log.e(TAG, exception.message ?: "")
        }
    }

    private fun showContactsList() {
        if (contactDataList.isNotEmpty()) {
            contactsRecycler.adapter = ContactsAdapter(contactDataList)
        }
    }
}

data class ContactData(val name: String, val numbers: List<String>)