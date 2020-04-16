package mu.mcb.mobileacademytodo

class Todo(var title:String, var reminderDate:String) {
    var notes:String = ""

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "title" to title,
            "notes" to notes,
            "reminderDate" to reminderDate
        )
    }
}
