package com.omisoft.myapplication.mvvm.ui.draft.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.omisoft.myapplication.R

class ContactsAdapter(private val contacts: List<ContactData>) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContact(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val nameText = view.findViewById<AppCompatTextView>(R.id.contact_text)

        fun setContact(contact: ContactData) {
            val text = "${contact.name}  ${contact.numbers.first()}"
            nameText.text = text
        }
    }
}