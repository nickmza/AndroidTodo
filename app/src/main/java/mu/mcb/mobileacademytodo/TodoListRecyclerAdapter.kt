package mu.mcb.mobileacademytodo

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.rv_todo_row.view.*
import mu.mcb.mobileacademytodo.fragments.TodoListFragment


class TodoRecyclerAdapter(private val todos: ArrayList<Todo>, var context: TodoListFragment) : RecyclerView.Adapter<TodoRecyclerAdapter.TodoHolder>()  {

    lateinit var itemToDelete: Todo
    var itemToDeletePosition = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRecyclerAdapter.TodoHolder {
        val inflatedView = parent.inflate(R.layout.rv_todo_row, false)
        return TodoHolder(inflatedView)
    }

    override fun getItemCount() = todos.size

    override fun onBindViewHolder(holder: TodoRecyclerAdapter.TodoHolder, position: Int) {
        holder.bindTodo(todos[position])
    }

    fun deleteItem(position: Int) {
        itemToDelete = todos.get(position)
        itemToDeletePosition = position
        todos.removeAt(position)
        notifyItemRemoved(position)
        showUndoSnackbar()
    }

    private fun showUndoSnackbar() {

        val view :View? = context.view?.rootView?.findViewById(R.id.coordinator_layout)
        val snackbar: Snackbar = Snackbar.make(
            view!!, "Todo Options",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") { v -> undoDelete() }
        snackbar.show()
    }

    private fun undoDelete() {
        todos.add(
            itemToDeletePosition,
            itemToDelete
        )
        notifyItemInserted(itemToDeletePosition)
    }

    class TodoHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var todo: Todo? = null

        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
        }

        companion object {
            private val PHOTO_KEY = "PHOTO"
        }

        fun bindTodo(todo: Todo) {
            this.todo = todo
            //Picasso.with(view.context).load(photo.url).into(view.itemImage)
            view.itemDate.text = todo.reminderDate
            view.itemDescription.text = todo.title
        }
    }
}