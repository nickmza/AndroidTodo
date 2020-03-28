package mu.mcb.mobileacademytodo

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.todo_details.*
import android.view.View
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import mu.mcb.mobileacademytodo.ViewModels.AddTodoViewModel

class AddTodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todo_details)

        val vm: AddTodoViewModel by lazy { ViewModelProviders.of(this).get(AddTodoViewModel::class.java) }

        bindControls(vm)

        saveTodo.setOnClickListener {
            validateAndSave(vm)
        }
    }

    private fun bindControls(vm: AddTodoViewModel) {
        vm.isBusy.observe(this, Observer<Boolean> { i -> setBusyState(i) });
        vm.titleError.observe(this, Observer<String> { i -> txtTitle.error = i })
    }

    private fun validateAndSave(vm:AddTodoViewModel){
        vm.title = txtTitle.text.toString();
        vm.notes = txtNotes.text.toString();
        vm.reminderDate = txtDate.text.toString();
        vm.createTodo()
        if(vm.isValid){
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun setBusyState(state:Boolean) {
        progressBar.visibility = if(state) View.VISIBLE else View.INVISIBLE
        saveTodo.isEnabled = !state;
    }
}
