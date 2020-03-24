package mu.mcb.mobileacademytodo

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_todo_row.view.*

class TodoRecyclerAdapter(private val todos: ArrayList<Todo>) : RecyclerView.Adapter<TodoRecyclerAdapter.TodoHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoRecyclerAdapter.TodoHolder {
        val inflatedView = parent.inflate(R.layout.rv_todo_row, false)
        return TodoHolder(inflatedView)
    }

    override fun getItemCount() = todos.size

    override fun onBindViewHolder(holder: TodoRecyclerAdapter.TodoHolder, position: Int) {
        holder.bindTodo(todos[position])
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