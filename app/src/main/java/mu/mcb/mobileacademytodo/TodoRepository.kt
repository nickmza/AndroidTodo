package mu.mcb.mobileacademytodo

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import mu.mcb.mobileacademytodo.Interfaces.ITodoRepository
import com.google.firebase.firestore.FirebaseFirestore
import mu.mcb.mobileacademytodo.Interfaces.INotificationService
import java.lang.Exception

class TodoRepository(var notificationService: INotificationService) : ITodoRepository{

    private val collectionName = "todo"

    private var todoItems = ArrayList<Todo>()

    override fun Save(todo: Todo) {
        val collection = getCollection()
        var ref = collection.document()
        todo.id = ref.id
        ref.set(todo)
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
                for (document in result) try{
                    var todo = document.toObject(Todo::class.java)
                    todoItems.add(todo)
                }
                catch(e: Exception){
                    Log.d("TODO", e.toString())
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
            .document(ServiceLocator.userInfo.getUserId())
            .collection("Todos")
    }

    override var onRefresh: () -> Unit = {}

}