package mu.mcb.mobileacademytodo

import mu.mcb.mobileacademytodo.Interfaces.INotificationService
import mu.mcb.mobileacademytodo.Interfaces.ITodoRepository
import javax.xml.transform.SourceLocator

object ServiceLocator{
    lateinit var notificationService: INotificationService
    lateinit var todoRepository: TodoRepository
    var initilised: Boolean = false
}