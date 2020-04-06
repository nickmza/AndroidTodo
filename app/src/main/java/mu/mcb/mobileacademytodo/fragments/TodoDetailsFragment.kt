package mu.mcb.mobileacademytodo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.todo_details.*
import mu.mcb.mobileacademytodo.Interfaces.IModalDialog
import mu.mcb.mobileacademytodo.R
import mu.mcb.mobileacademytodo.databinding.TodoDetailsBinding
import mu.mcb.mobileacademytodo.viewmodels.AddTodoViewModel


class TodoDetailsFragment : Fragment(){

    private lateinit var binding: TodoDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.todo_details, container, false)

        val vm: AddTodoViewModel by lazy { ViewModelProviders.of(this).get(AddTodoViewModel::class.java) }
        binding.vm = vm;

        binding.lifecycleOwner = this;

        vm.titleError.observe(this, Observer<String> { i -> txtTitle.error = i })

        vm.onComplete =
            {
                if(activity is IModalDialog){
                   var dialog = activity as IModalDialog
                    dialog.close()
                }
            }

        return binding.root
    }

}