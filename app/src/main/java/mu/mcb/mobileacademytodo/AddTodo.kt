package mu.mcb.mobileacademytodo

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.todo_details.*
import android.view.View
import android.os.AsyncTask

class AddTodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_details)
        saveTodo.setOnClickListener {
            validateAndSave()
        }
    }

    private fun validateAndSave(){

        var isValid = true;

        setBusyState(true)

        var title = txtTitle.text.toString();
        var notes = txtNotes.text.toString();
        var date = txtDate.text.toString();

        if(title.isEmpty())
        {
            txtTitle.error = "Required";
            isValid = false;
        }

        if(isValid){
            saveTodo(title, date, notes)
        }
        else{
           setBusyState(false)
        }
    }

    private fun saveTodo(title: String, date: String, notes: String) {
        var todo = Todo(title, date);
        todo.notes = notes

        ServiceLocator.getTodoRepo().Save(todo)
        setResult(Activity.RESULT_OK)

        //Simulate network call.
        AsyncTask.execute {
            Thread.sleep(2500)
            finish()
        }
    }

    private fun setBusyState(state:Boolean) {
        progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
        saveTodo.isEnabled = !state;
    }
}
