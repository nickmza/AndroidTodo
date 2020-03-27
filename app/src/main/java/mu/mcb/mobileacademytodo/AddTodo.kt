package mu.mcb.mobileacademytodo

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.todo_details.*

class AddTodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_details)
        saveTodo.setOnClickListener {
            validateAndSave()
        }
    }

    private fun validateAndSave(){

        var title = txtTitle.text.toString();
        var notes = txtNotes.text.toString();
        var date = txtDate.text.toString();

        var todo = Todo(title, date);
        todo.notes = notes

        ServiceLocator.getTodoRepo().Save(todo)
        setResult(Activity.RESULT_OK)
        finish()
    }
}
