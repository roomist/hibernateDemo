import entity.TodoItemsEntity;
import jakarta.persistence.*;
import java.util.List;
import java.util.Scanner;

public class TodoApp {
    private static EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public TodoApp() {
        entityManagerFactory = Persistence.createEntityManagerFactory("TodoListPersistenceManager");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public TodoApp(EntityManager entityManager) {
        TodoApp.entityManagerFactory = Persistence.createEntityManagerFactory("TodoListPersistenceManager");
        this.entityManager = entityManager;
    }
    public void addTodoItem(String task) {
        TodoItemsEntity todoItem = new TodoItemsEntity();
        todoItem.setTask(task);

        entityManager.getTransaction().begin();
        entityManager.persist(todoItem);
        entityManager.getTransaction().commit();
    }

    public void deleteTodoItem(int number) {
        entityManager.getTransaction().begin();
        TodoItemsEntity todoItem = entityManager.find(TodoItemsEntity.class, number);
        if (todoItem != null) {
            entityManager.remove(todoItem);
            entityManager.getTransaction().commit();
        } else {
            System.out.println("Invalid number. Item not found.");
            entityManager.getTransaction().rollback();
        }
    }

    public void viewTodoItems() {
        entityManager.getTransaction().begin();
        TypedQuery<TodoItemsEntity> query = entityManager.createQuery("SELECT t FROM TodoItemsEntity t", TodoItemsEntity.class);
        List<TodoItemsEntity> items = query.getResultList();
        entityManager.getTransaction().commit();

        if (items.isEmpty()) {
            System.out.println("No to-do items.");
        } else {
            System.out.println("To-do items:");
            for (TodoItemsEntity item : items) {
                System.out.println(item.getId() + ". " + item.getTask());
            }
        }
    }

    public void close() {
        entityManager.close();
        entityManagerFactory.close();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TodoApp todoApp = new TodoApp();

        System.out.println("         ,..........   ..........,\n" +
                        "     ,..,'          '.'          ',..,\n" +
                        "    ,' ,'            :            ', ',\n" +
                        "   ,' ,'             :             ', ',\n" +
                        "  ,' ,'              :              ', ',\n" +
                        " ,' ,'............., : ,.............', ',\n" +
                        ",'  '............   '.'   ............'  ',\n" +
                        " '''''''''''''''''';''';''''''''''''''''''\n" +
                        "                    '''");
        System.out.println("Todo List Application");
        System.out.println("1. Add a to-do item");
        System.out.println("2. Delete a to-do item");
        System.out.println("3. View to-do items");
        System.out.println("4. Quit");
        while (true) {

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter the task: ");
                    String task = scanner.nextLine();
                    todoApp.addTodoItem(task);
                    System.out.println("Task added successfully!");
                    break;

                case 2:
                    System.out.print("Enter the number of the task to delete: ");
                    int number = scanner.nextInt();
                    scanner.nextLine();
                    todoApp.deleteTodoItem(number);
                    break;

                case 3:
                    todoApp.viewTodoItems();
                    break;

                case 4:
                    todoApp.close();
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}