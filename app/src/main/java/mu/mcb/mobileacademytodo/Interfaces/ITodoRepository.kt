package mu.mcb.mobileacademytodo.Interfaces

import mu.mcb.mobileacademytodo.Todo

public interface ITodoRepository{
    fun Save(todo:Todo)
    fun GetTodo() : ArrayList<Todo>
}