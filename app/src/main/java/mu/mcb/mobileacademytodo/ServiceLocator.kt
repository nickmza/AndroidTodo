package mu.mcb.mobileacademytodo

import mu.mcb.mobileacademytodo.Interfaces.INotificationService
import mu.mcb.mobileacademytodo.Interfaces.ITodoRepository
import mu.mcb.mobileacademytodo.Interfaces.IUserInfo
import javax.xml.transform.SourceLocator

object ServiceLocator{
    lateinit var notificationService: INotificationService
    lateinit var todoRepository: TodoRepository
    lateinit var userInfo :IUserInfo
    var initilised: Boolean = false
}