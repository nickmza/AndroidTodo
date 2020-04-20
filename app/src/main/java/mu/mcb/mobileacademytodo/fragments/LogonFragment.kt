package mu.mcb.mobileacademytodo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.logon.*
import mu.mcb.mobileacademytodo.Interfaces.IModalDialog
import mu.mcb.mobileacademytodo.R

class LogonFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.logon, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loginButton.setOnClickListener {
            var user = emailText.text.toString()
            var password = passwordText.text.toString()

            var valid = true
            if(user.isNullOrEmpty()){
                emailText.error = "Required"
                valid = false
            }

            if(password.isNullOrEmpty()){
                passwordText.error = "Required"
                valid = false
            }

            if(valid)
            {
                val action = LogonFragmentDirections.actionLogonFragmentToTodoListFragment()
                findNavController(it).navigate(action)
            }
        }
    }
}