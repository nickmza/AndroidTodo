package mu.mcb.mobileacademytodo

import mu.mcb.mobileacademytodo.Interfaces.ITodoRepository

object ServiceLocator{

    private var todoRepo = TodoRepository()

    fun getTodoRepo() : ITodoRepository{
        return todoRepo;
    }

}