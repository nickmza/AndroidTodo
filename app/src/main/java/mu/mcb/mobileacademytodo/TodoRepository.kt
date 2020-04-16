package mu.mcb.mobileacademytodo

import android.util.Log
import mu.mcb.mobileacademytodo.Interfaces.ITodoRepository
import com.google.firebase.firestore.FirebaseFirestore

class TodoRepository : ITodoRepository{

    private val collectionName = "todo"

    private var todoItems = ArrayList<Todo>()

    override fun Save(todo: Todo) {

        val db = FirebaseFirestore.getInstance()

        var data = todo.toHashMap()

        db.collection(collectionName)
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("ToDo", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("ToDo", "Error adding document", e)
            }
    }

    override fun GetTodo(): ArrayList<Todo> {
        val db = FirebaseFirestore.getInstance()
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                todoItems.clear()
                for (document in result) {
                    Log.d("ToDo", "${document.id} => ${document.data}")
                    var todo = Todo(document.data["title"].toString(), document.data["reminderDate"].toString())
                    todo.notes = document.data["notes"].toString()
                    todoItems.add(todo)
                }
                onRefresh();
            }
            .addOnFailureListener { exception ->
                Log.w("ToDo", "Error getting documents.", exception)
            }
        return todoItems
    }

    override var onRefresh: () -> Unit = {}

}