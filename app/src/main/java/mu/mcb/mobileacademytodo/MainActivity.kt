package mu.mcb.mobileacademytodo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import mu.mcb.mobileacademytodo.Interfaces.IModalDialog
import mu.mcb.mobileacademytodo.fragments.TodoListFragmentDirections


class MainActivity : AppCompatActivity(), IModalDialog {

    lateinit var current : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        configureServiceLocator()

        fab.setOnClickListener {
            val action =
                TodoListFragmentDirections.actionTodoListFragmentToTodoDetailsFragment()
            findNavController(R.id.nav_host_fragment).navigate(action)
        }
    }

    private fun configureServiceLocator() {
        if(!ServiceLocator.initilised){
            ServiceLocator.notificationService = NotificationService(applicationContext);
            ServiceLocator.todoRepository = TodoRepository(ServiceLocator.notificationService);
            ServiceLocator.initilised = true;
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun close() {
        findNavController(R.id.nav_host_fragment).popBackStack();
    }
}
