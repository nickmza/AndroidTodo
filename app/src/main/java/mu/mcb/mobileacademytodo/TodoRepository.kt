package mu.mcb.mobileacademytodo

import android.util.Log
import mu.mcb.mobileacademytodo.Interfaces.ITodoRepository
import com.google.firebase.firestore.FirebaseFirestore
import mu.mcb.mobileacademytodo.Interfaces.INotificationService

class TodoRepository(var notificationService: INotificationService) : ITodoRepository{

    private val collectionName = "todo"

    private var todoItems = ArrayList<Todo>()

    override fun Save(todo: Todo) {

        val db = FirebaseFirestore.getInstance()

        var data = todo.toHashMap()

        db.collection(collectionName)
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("ToDo", "DocumentSnapshot added with ID: ${documentReference.id}")
                notificationService.notify("Todo Saved.")
            }
            .addOnFailureListener { e ->
                Log.w("ToDo", "Error adding document", e)
                notificationService.notify("Error: $e")
            }
        notificationService.notify("Todo Saved.")
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
        val db = FirebaseFirestore.getInstance()
        var document = db.collection(collectionName).document(item.id)
        document.delete()
        notificationService.notify("Todo Deleted...")
    }

    override var onRefresh: () -> Unit = {}

}