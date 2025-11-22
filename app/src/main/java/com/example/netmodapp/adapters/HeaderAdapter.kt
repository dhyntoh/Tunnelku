package com.example.netmodapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.netmodapp.models.Header
import com.example.netmodapp.R

class HeaderAdapter(
    private val headers: MutableList<Header>,
    private val onHeaderRemoved: (Int) -> Unit
) : RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    fun addHeader(header: Header = Header()) {
        headers.add(header)
        notifyItemInserted(headers.size - 1)
    }

    fun removeHeader(position: Int) {
        if (position >= 0 && position < headers.size) {
            headers.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_header, parent, false)
        return HeaderViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(headers[position], position)
    }

    override fun getItemCount(): Int = headers.size

    inner class HeaderViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        fun bind(header: Header, position: Int) {
            val keyEditText = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_header_key)
            val valueEditText = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.et_header_value)
            val removeButton = view.findViewById<android.widget.ImageButton>(R.id.btn_remove_header)

            keyEditText.setText(header.key)
            valueEditText.setText(header.value)

            keyEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    header.key = keyEditText.text.toString()
                }
            }

            valueEditText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    header.value = valueEditText.text.toString()
                }
            }

            removeButton.setOnClickListener {
                val adapterPosition = adapterPosition
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    removeHeader(adapterPosition)
                    onHeaderRemoved(adapterPosition)
                }
            }
        }
    }
}