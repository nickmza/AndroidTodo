package mu.mcb.mobileacademytodo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mu.mcb.mobileacademytodo.R

import kotlinx.android.synthetic.main.todo_list.*;


class TodoListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todo_list, container, false)
    }
}