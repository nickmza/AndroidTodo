package mu.mcb.mobileacademytodo.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.todo_list.*
import mu.mcb.mobileacademytodo.R
import mu.mcb.mobileacademytodo.ServiceLocator
import mu.mcb.mobileacademytodo.TodoRecyclerAdapter


class TodoListFragment : Fragment() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: TodoRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todo_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        linearLayoutManager = LinearLayoutManager(this.activity)
        rv_todo.layoutManager = linearLayoutManager

        var repo = ServiceLocator.todoRepository
        repo.onRefresh = {
            adapter.notifyDataSetChanged()
        }
        adapter = TodoRecyclerAdapter(repo.GetTodo())
        rv_todo.adapter = adapter

    }


    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        hideKeyboard()
    }

    private fun hideKeyboard() {
        val imm: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
}