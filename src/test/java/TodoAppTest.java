import entity.TodoItemsEntity;
import jakarta.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import static org.mockito.Mockito.*;

class TodoAppTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction transaction;

    private TodoApp todoApp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.getTransaction()).thenReturn(transaction);
        todoApp = new TodoApp(entityManager);
    }

    @Test
    void addTodoItem() {
        String task = "Test task";
        todoApp.addTodoItem(task);
        verify(entityManager).persist(any(TodoItemsEntity.class));
        verify(transaction).begin();
        verify(transaction).commit();
    }

    @Test
    void deleteTodoItem_existingItem() {
        int id = 1;
        TodoItemsEntity entity = new TodoItemsEntity();
        entity.setId(id);
        entity.setTask("Sample Task");

        when(entityManager.find(TodoItemsEntity.class, id)).thenReturn(entity);

        todoApp.deleteTodoItem(id);

        verify(transaction).begin();
        verify(entityManager).remove(entity);
        verify(transaction).commit();
    }

    @Test
    void deleteTodoItem_nonExistingItem() {
        int id = 99;
        when(entityManager.find(TodoItemsEntity.class, id)).thenReturn(null);

        todoApp.deleteTodoItem(id);

        verify(transaction).begin();
        verify(transaction).rollback();
    }

    @Test
    void viewTodoItems() {
        TypedQuery<TodoItemsEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(TodoItemsEntity.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        todoApp.viewTodoItems();

        verify(transaction).begin();
        verify(query).getResultList();
        verify(transaction).commit();
    }


}
