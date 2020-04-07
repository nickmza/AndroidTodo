package mu.mcb.mobileacademytodo.viewmodels

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import mu.mcb.mobileacademytodo.ServiceLocator
import mu.mcb.mobileacademytodo.Todo


class AddTodoViewModel : ViewModel() {
    var title:String = "New Todo"
    var notes:String = ""
    var reminderDate:String = ""
    var titleError:MutableLiveData<String> = MutableLiveData<String>().default("")
    var isProgressVisible : MutableLiveData<Int> = MutableLiveData<Int>().default(View.INVISIBLE)
    var isSaveEnabled :  MutableLiveData<Boolean> = MutableLiveData<Boolean>().default(true)

    var onComplete : ()->Unit = {}

    fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

    fun isBusy(busy:Boolean){
        isProgressVisible.value = if(busy) View.VISIBLE else View.INVISIBLE;
        isSaveEnabled.value = !busy;
    }

    fun onCreateTodo(view: View){
        createTodo()
    }

    fun createTodo(){

        isBusy(true)

        if(title.isEmpty())
        {
            titleError.value = "Required";
            isBusy(false)
        }
        else{
            GlobalScope.async(Dispatchers.Main) {
                var saveTask = GlobalScope.async(Dispatchers.IO)
                {
                    saveTodo(title, notes, reminderDate)
                }
                saveTask.await()
                isBusy(false);
                onComplete();
            }
        }
    }

    private suspend fun saveTodo(title: String, date: String, notes: String) {
        var todo = Todo(title, date);
        todo.notes = notes
        ServiceLocator.getTodoRepo().Save(todo)

        //Simulate a slow network call.
        Thread.sleep(2500)
    }
}