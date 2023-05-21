package com.delitx.furnituremaster.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.delitx.furnituremaster.R

class SaveDialogFragment(private val mName: String, private val mPhone: String, private val mComment: String, val onSuccess: (String, String, String) -> Unit) : DialogFragment() {
    lateinit var name: EditText
    lateinit var comment: EditText
    lateinit var phoneNumber: EditText
    lateinit var mView: View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            mView = requireActivity().layoutInflater.inflate(R.layout.fragment_save_dialog, null)
            name = mView.findViewById(R.id.name)
            phoneNumber = mView.findViewById(R.id.phone_number)
            comment = mView.findViewById(R.id.comment)
            name.setText(mName)
            phoneNumber.setText(mPhone)
            comment.setText(mComment)
            val ok: Button = mView.findViewById(R.id.ok)
            val cancel: Button = mView.findViewById(R.id.cancel)
            val dialog = AlertDialog.Builder(it)
                .setView(mView)
                .create()
            ok.setOnClickListener {
                if (name.text.toString().trim() == "") {
                    name.requestFocus()
                    val imm = getSystemService(name.context, InputMethodManager::class.java)
                    imm?.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT)
                } else if (phoneNumber.text.toString().trim() == "") {
                    phoneNumber.requestFocus()
                    val imm = getSystemService(name.context, InputMethodManager::class.java)
                    imm?.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT)
                } else {
                    onSuccess(
                        name.text.toString(),
                        phoneNumber.text.toString(),
                        comment.text.toString()
                    )
                    dialog.dismiss()
                }
            }
            cancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_save_dialog, container, false)
    }
}
