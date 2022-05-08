package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar

class FragmentView: Fragment() {

    private val viewModel: MyViewModel by activityViewModels()
    private var note:Note? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_view, container, false)

        // get widgets
        val noteView = root.findViewById<TextView>(R.id.noteView)
        val titleView = root.findViewById<TextView>(R.id.titleView)
        val bodyView = root.findViewById<TextView>(R.id.bodyView)
        val imptChip = root.findViewById<TextView>(R.id.imptChip)
        val edit = root.findViewById<Button>(R.id.edit)

        // display selected note
        viewModel.selectedNote.observe(this){
            note = viewModel.selectedNote.value
            if (note != null) {
                noteView.text = "Note #${note!!.id}"
                titleView.text = note!!.title
                bodyView.text = note!!.body
                imptChip.isVisible = note!!.important
            }
        }

        // update pop up message on snackbar
        viewModel.popupMessage.observe(this){
            if (viewModel.popupMessage.value != null) {
                val snackbar =
                    Snackbar.make(edit, viewModel.popupMessage.value.toString(), Snackbar.LENGTH_SHORT)
                snackbar.show()
                viewModel.messageShowed()
            }
        }

        edit.setOnClickListener {
            findNavController().navigate(R.id.action_view_edit)
            viewModel.editNote()
        }

        return root
    }

}

