package mu.mcb.mobileacademytodo.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.logon.*
import mu.mcb.mobileacademytodo.BiometricHelper
import mu.mcb.mobileacademytodo.R
import mu.mcb.mobileacademytodo.ServiceLocator
import java.nio.charset.Charset
import java.util.*

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

        val uiHandler = Handler()
        uiHandler.post(Runnable {  useBiometricAuth()})

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
                authenticate(user, password, it, true)
            }
        }
    }

    private fun authenticate(user: String, password: String, it: View, saveCredentials: Boolean) {
        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(user, password)
            .addOnCompleteListener { logonResult ->
                if (logonResult.isSuccessful) {

                    if(saveCredentials){
                        configureBiometricAuth(user, password);
                    }

                    val action = LogonFragmentDirections.actionLogonFragmentToTodoListFragment()
                    findNavController(it).navigate(action)
                } else {
                    ServiceLocator.notificationService.notify("Invalid email address or password.")
                }
            }
    }

    private fun useBiometricAuth() {
        var password = getSetting("password");
        if(password != null){
            var iv = getSetting("iv");

            var bio = object : BiometricHelper()
            {
                override fun authenticationComplete(result: BiometricPrompt.AuthenticationResult) {
                    var passwordData = Base64.getDecoder().decode(password);
                    var decryptedData =  result.cryptoObject?.cipher?.doFinal(passwordData)!!

                    var decoded = String(decryptedData, Charset.forName("UTF-8"))
                        .split(" ")

                    authenticate(decoded[0], decoded[1], view!!, false)
                }
            };

            bio.iv = Base64.getDecoder().decode(iv);

            bio.configureBiometricPrompt(this.activity!!, context!!, "Logon", "Securely log on with your fingerprint.")
            if(bio.checkBiometricSupport(this.context!!).enabled){
                bio.initialise()
                bio.promptForDecrypt();
            }
        }
    }

    private fun configureBiometricAuth(user: String, password: String) {
        var bio = object : BiometricHelper()
        {
            override fun authenticationComplete(result: BiometricPrompt.AuthenticationResult) {
                var passwordData = "$user $password".toByteArray(Charset.defaultCharset())
                var encryptedBytes = result.cryptoObject?.cipher?.doFinal(passwordData)!!
                saveBiometricInfo(encryptedBytes, this.iv);
            }
        };
        bio.configureBiometricPrompt(this.activity!!, context!!, "Biometric", "In future you can use your fingerprint to log on.")
        if(bio.checkBiometricSupport(this.context!!).enabled){
            bio.initialise()
            bio.promptForEncrypt();
        }
    }

    private fun saveBiometricInfo(payload: ByteArray, iv: ByteArray) {
        writeSetting("password", Base64.getEncoder().encodeToString(payload))
        writeSetting("iv", Base64.getEncoder().encodeToString(iv))
    }

    private fun getSetting(setting: String) : String?{
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPref?.getString(setting, null);
    }

    private fun writeSetting(key: String, value: String){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(key, value);
            commit()
        }
    }
}