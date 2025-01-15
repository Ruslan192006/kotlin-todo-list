import java.io.File

data class Task(
    val id: Int,
    val description: String,
    var isCompleted: Boolean = false
)

class TodoList {
    private val tasks = mutableListOf<Task>()
    private var nextId = 1
    private val fileName = "tasks.txt"

    init {
        loadTasks()
    }

    fun addTask(description: String) {
        val task = Task(nextId++, description)
        tasks.add(task)
        println("✓ Задача добавлена: $description")
        saveTasks()
    }

    fun listTasks() {
        if (tasks.isEmpty()) {
            println("Список задач пуст")
            return
        }
        println("\n=== Список задач ===")
        tasks.forEach { task ->
            val status = if (task.isCompleted) "✓" else "○"
            println("$status ${task.id}. ${task.description}")
        }
        println()
    }

    fun completeTask(id: Int) {
        val task = tasks.find { it.id == id }
        if (task != null) {
            task.isCompleted = true
            println("✓ Задача выполнена: ${task.description}")
            saveTasks()
        } else {
            println("✗ Задача с ID $id не найдена")
        }
    }

    fun deleteTask(id: Int) {
        val task = tasks.find { it.id == id }
        if (task != null) {
            tasks.remove(task)
            println("✓ Задача удалена: ${task.description}")
            saveTasks()
        } else {
            println("✗ Задача с ID $id не найдена")
        }
    }

    private fun saveTasks() {
        val file = File(fileName)
        file.writeText(tasks.joinToString("\n") { "${it.id},${it.description},${it.isCompleted}" })
    }

    private fun loadTasks() {
        val file = File(fileName)
        if (file.exists()) {
            file.readLines().forEach { line ->
                val parts = line.split(",")
                if (parts.size == 3) {
                    val task = Task(parts[0].toInt(), parts[1], parts[2].toBoolean())
                    tasks.add(task)
                    if (task.id >= nextId) nextId = task.id + 1
                }
            }
        }
    }
}

fun main() {
    val todoList = TodoList()
    
    println("=== Kotlin Todo List ===")
    
    while (true) {
        println("\nВыберите действие:")
        println("1. Добавить задачу")
        println("2. Показать все задачи")
        println("3. Отметить задачу выполненной")
        println("4. Удалить задачу")
        println("5. Выход")
        print("\nВаш выбор: ")
        
        when (readLine()?.toIntOrNull()) {
            1 -> {
                print("Введите описание задачи: ")
                val description = readLine() ?: ""
                if (description.isNotEmpty()) {
                    todoList.addTask(description)
                }
            }
            2 -> todoList.listTasks()
            3 -> {
                print("Введите ID задачи: ")
                val id = readLine()?.toIntOrNull()
                if (id != null) {
                    todoList.completeTask(id)
                }
            }
            4 -> {
                print("Введите ID задачи: ")
                val id = readLine()?.toIntOrNull()
                if (id != null) {
                    todoList.deleteTask(id)
                }
            }
            5 -> {
                println("До свидания!")
                break
            }
            else -> println("Неверный выбор")
        }
    }
}
