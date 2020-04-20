package mu.mcb.mobileacademytodo.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.logon.*
import mu.mcb.mobileacademytodo.Interfaces.IModalDialog
import mu.mcb.mobileacademytodo.R
import mu.mcb.mobileacademytodo.ServiceLocator

class LogonFragment : Fragment(){

    private lateinit var auth: FirebaseAuth

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

            val imm: InputMethodManager =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view!!.windowToken, 0)

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
                auth = FirebaseAuth.getInstance()

                auth.signInWithEmailAndPassword(user, password)
                    .addOnCompleteListener{ logonResult ->
                        if(logonResult.isSuccessful){
                            val action = LogonFragmentDirections.actionLogonFragmentToTodoListFragment()
                            findNavController(it).navigate(action)
                        }else{
                            ServiceLocator.notificationService.notify("Invalid email address or password.")
                        }
                    }
            }
        }
    }
}