package mu.mcb.mobileacademytodo

import mu.mcb.mobileacademytodo.Interfaces.ITodoRepository

class TodoRepository : ITodoRepository{

    var todoList = ArrayList<Todo>()

    init {
        todoList.add(Todo("Todo 1", "10 April 2020"))
        todoList.add(Todo("Todo 2", "11 April 2020"))
        todoList.add(Todo("Todo 3", "12 April 2020"))
    }

    override fun Save(todo: Todo) {
        todoList.add(todo)
    }

    override fun GetTodo(): ArrayList<Todo> {
        return  todoList
    }

}