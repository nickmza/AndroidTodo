package mu.mcb.mobileacademytodo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import mu.mcb.mobileacademytodo.Interfaces.ITodoRepository
import com.google.firebase.firestore.FirebaseFirestore
import mu.mcb.mobileacademytodo.Interfaces.INotificationService

class TodoRepository(var notificationService: INotificationService) : ITodoRepository{

    private val collectionName = "todo"

    private var todoItems = ArrayList<Todo>()

    override fun Save(todo: Todo) {
        getCollection()
            .add(todo.toHashMap())
            .addOnSuccessListener {
                notificationService.notify("Todo Saved.")
            }
            .addOnFailureListener { e ->
                Log.w("ToDo", "Error adding document", e)
                notificationService.notify("Error: $e")
            }
    }

    override fun GetTodo(): ArrayList<Todo> {
        getCollection()
            .get()
            .addOnSuccessListener { result ->
                todoItems.clear()
                for (document in result) {
                    var todo = Todo(document.data["title"].toString(), document.data["reminderDate"].toString())
                    todo.notes = document.data["notes"].toString()
                    todo.id = document.id
                    todoItems.add(todo)
                }
                onRefresh();
            }
            .addOnFailureListener { exception ->
                Log.w("ToDo", "Error getting documents.", exception)
                notificationService.notify("Error: $exception")
            }
        return todoItems
    }

    override fun delete(item: Todo) {
        getCollection().document(item.id).delete()
            .addOnSuccessListener() {
            notificationService.notify("Todo Deleted...")
        }
    }

    private fun getCollection() : CollectionReference {
        val db = FirebaseFirestore.getInstance()
        return db.collection(collectionName)
    }

    override var onRefresh: () -> Unit = {}

}