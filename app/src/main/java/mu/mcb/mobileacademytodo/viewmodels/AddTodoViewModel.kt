package mu.mcb.mobileacademytodo.viewmodels

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
    var isValid:Boolean = true
    var titleError:MutableLiveData<String> = MutableLiveData<String>().default("")
    var isBusy : MutableLiveData<Boolean> = MutableLiveData<Boolean>().default(false)


    fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

    fun createTodo(){
        isBusy.value = true

        if(title.isEmpty())
        {
            titleError.value = "Required";
            isValid = false;
            isBusy.value = false;
        }
        else{
            GlobalScope.async(Dispatchers.Main) {
                    saveTodo(title, notes, reminderDate)
                    isBusy.value = false;
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