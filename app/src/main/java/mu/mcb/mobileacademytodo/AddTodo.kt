package mu.mcb.mobileacademytodo

import android.app.Activity
import android.os.Bundle
import android.util.Log
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

        binding.lifecycleOwner = this;

        bindControls(vm)

        vm.onComplete =
            {
                setResult(Activity.RESULT_OK)
                finish()
            }
    }

    private fun bindControls(vm: AddTodoViewModel) {
        vm.titleError.observe(this, Observer<String> { i -> txtTitle.error = i })
    }
}
