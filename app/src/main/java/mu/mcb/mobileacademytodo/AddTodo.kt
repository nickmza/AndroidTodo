package mu.mcb.mobileacademytodo

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.todo_details.*
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import mu.mcb.mobileacademytodo.viewmodels.AddTodoViewModel
import mu.mcb.mobileacademytodo.databinding.TodoDetailsBinding

class AddTodoActivity : AppCompatActivity() {

    private lateinit var binding: TodoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.todo_details)

        val vm: AddTodoViewModel by lazy { ViewModelProviders.of(this).get(AddTodoViewModel::class.java) }
        binding.vm = vm;

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
