package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

class FragmentEdit: Fragment() {

    private val viewModel: MyViewModel by activityViewModels()
    private var note:Note? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_edit, container, false)

        val saveButton = root.findViewById<Button>(R.id.saveButton)
        val title = root.findViewById<EditText>(R.id.editTitle)
        val body = root.findViewById<EditText>(R.id.editBody)
        val impt = root.findViewById<Switch>(R.id.importantSwitch)

        viewModel.selectedNote.observe(this){
            note = viewModel.selectedNote.value

            if (note != null) {
                title.setText(note!!.title)
                body.setText(note!!.body)
                impt.isChecked = note!!.important
            }
        }

        viewModel.statusMessage.observe(this){
            val textView = root.findViewById<TextView>(R.id.editStatus)
            textView.text = viewModel.statusMessage.value
        }


        saveButton.setOnClickListener {

            val add:Boolean = (note == null)

            if (note != null){
                // changing the exisitng note
                debug("${impt.isChecked}")
                viewModel.saveNote(
                    note!!.copy(
                        title=title.text.toString(),
                        body=body.text.toString(),
                        important = impt.isChecked
                    )
                )
            }
            else {
                debug("${impt.isChecked}")
                // new note being added
                viewModel.saveNote(
                    Note(
                        null,
                        title.text.toString(),
                        body.text.toString(),
                        impt.isChecked
                    )
                )
            }
            if (!add) {
                findNavController().navigate(R.id.action_edit_view)
            }
            else{
                findNavController().navigate(R.id.action_edit_list)
            }
        }

        return root

    }



}