package mu.mcb.mobileacademytodo

import android.app.Activity
import android.content.ClipDescription
import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.lang.Exception
import java.nio.charset.Charset
import java.security.KeyStore
import java.util.*
import java.util.concurrent.Executor
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

open class BiometricHelper {

    private val KEY_NAME: String = "MOBILE_ACADEMY_SECRET_KEY"

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    fun showBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    class BiometricSupportResult(val enabled: Boolean, val description: String ){}

    fun checkBiometricSupport(context: Context): BiometricSupportResult{
        val biometricManager = BiometricManager.from(context)
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                return BiometricSupportResult(true, "App can authenticate using biometrics.")
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                return BiometricSupportResult(false, "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                return BiometricSupportResult(false, "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                return BiometricSupportResult(false, "The user hasn't associated any biometric credentials with their account.")
        }
        return BiometricSupportResult(false,"Unsupported.");
    }

    fun configureBiometricPrompt(activity: FragmentActivity, context: Context, title: String, subtitle: String) {

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText("Use account password")
            .build()

        executor = ContextCompat.getMainExecutor(activity)
        biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        context,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        context,
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()
                    encryptPayload(result);
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        context, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })


    }

    var payload: ByteArray = ByteArray(0);
    var iv: ByteArray = ByteArray(0);

    open fun encryptPayload( result: BiometricPrompt.AuthenticationResult){


        if(payload.count() ==0){
            var data = "SECRETSSS".toByteArray(Charset.defaultCharset())
            payload = result.cryptoObject?.cipher?.doFinal(data)!!

        }
        else{

            var result = result.cryptoObject?.cipher?.doFinal(payload)!!
            var s = String(result, Charset.forName("UTF-8"))
        }

    }

    fun promptForDecrypt(){
        val cipher = getCipher()
        var secret = getSecretKey()
        cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    fun promptForEncrypt(){
        val cipher = getCipher()
        var secret = getSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        iv = cipher.iv;
        biometricPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
    }

    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        return keyStore.getKey(KEY_NAME, null) as SecretKey
    }

    private fun getCipher(): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7)
    }

    fun initialise() {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        var key =  keyStore.getKey(KEY_NAME, null)

        if(key == null) {
           generateSecretKey(
                KeyGenParameterSpec.Builder(
                    KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    .setInvalidatedByBiometricEnrollment(true)
                    .build()
            )
        }
    }
}