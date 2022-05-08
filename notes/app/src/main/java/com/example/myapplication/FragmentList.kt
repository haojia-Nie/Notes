package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


class FragmentList: Fragment() {

    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_list, container, false)

        // get container
        val noteContainer = root.findViewById<LinearLayout>(R.id.noteContainer)

        // get buttons
        val clear = root.findViewById<Button>(R.id.clear)
        val random = root.findViewById<Button>(R.id.random)
        val add = root.findViewById<Button>(R.id.add)
        val filter = root.findViewById<Switch>(R.id.filter)
        val search = root.findViewById<EditText>(R.id.search)


        // update snackbar messagae
        viewModel.popupMessage.observe(this){
            if (viewModel.popupMessage.value != null) {
                val snackbar =
                    Snackbar.make(noteContainer, viewModel.popupMessage.value.toString(), Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }

        random.setOnClickListener {
            viewModel.randomNote()
        }

        // set clear button
        clear.isEnabled = !(viewModel.notes.value == null ||  (viewModel.notes.value != null && viewModel.notes.value!!.size == 0))

        clear.setOnClickListener {
            viewModel.clearNotes()
            clear.isEnabled = false
        }

        // set add button
        add.setOnClickListener {
            debug("add new note")
            findNavController().navigate(R.id.action_list_edit)
            viewModel.addNote()
            viewModel.statusMessage("Add New Note")
        }

        // set filter
        filter.setOnClickListener {
            viewModel.setFilter(filter.isChecked)
        }

        // search bar action
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                viewModel.setSearch(search.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })

        if (viewModel.searchBar.value != null) {
            val editable: Editable = SpannableStringBuilder(viewModel.searchBar.value)
            search.text = editable
        }

        viewModel.notes.observe(this){
            // display the current notes being created
            noteContainer.removeAllViews()

            // update views by adding the display notes
            for (i in viewModel.notes.value!!){
                val noteView = inflater.inflate(R.layout.note_layout, null)
                val note = noteView.findViewById<ConstraintLayout>(R.id.note)

                // set up delete button action
                val delete = noteView.findViewById<Button>(R.id.delete)
                delete.setOnClickListener {
                    viewModel.deleteNote(i)
                }

                val noteTitle = note.findViewById<TextView>(R.id.noteTitle)
                noteTitle.text = i.title

                val noteContent = note.findViewById<TextView>(R.id.noteContent)
                noteContent.text = i.body
                noteContainer.addView(noteView)

                // if note section get clicked, go to the View fragment
                noteContent.setOnClickListener{
                    viewModel.viewNote(i)
                    findNavController().navigate(R.id.action_list_view)
                }

                noteTitle.setOnClickListener{
                    viewModel.viewNote(i)
                    findNavController().navigate(R.id.action_list_view)
                }

                // set important color
                if (i.important){
                    noteView.setBackgroundColor(resources.getColor(R.color.light_pink))
                }
            }

            // update clear button
            if (viewModel.notes.value != null && viewModel.notes.value!!.size > 0){
                clear.isEnabled = true
            }
            else if (viewModel.notes.value != null && viewModel.notes.value!!.size == 0){
                clear.isEnabled = false
            }
        }

        return root
    }
}