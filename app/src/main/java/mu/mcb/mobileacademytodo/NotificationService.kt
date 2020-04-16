package mu.mcb.mobileacademytodo

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import mu.mcb.mobileacademytodo.Interfaces.INotificationService

class NotificationService(private val context: Context) : INotificationService{
    override fun notify(message: String) {
        GlobalScope.async(Dispatchers.Main) {
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
        }
    }
}