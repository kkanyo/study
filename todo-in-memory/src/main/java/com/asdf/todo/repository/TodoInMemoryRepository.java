package com.asdf.todo.repository;

import com.asdf.todo.model.Todo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

// Todo 항목을 저장, 조회, 수정, 삭제하는 CRUD(Create, Read, Update, Delete)
@Repository
public class TodoInMemoryRepository {
    private final Map<Long, Todo> todoMap = new HashMap<>();
    // Todo 항목의 고유 ID를 자동 생성
    private final AtomicLong counter = new AtomicLong();

    public List<Todo> findAll() {
        return new ArrayList<>(todoMap.values());
    }

    public Todo findById(Long id) {
        return todoMap.get(id);
    }

    public Todo save(Todo todo) {
        if (todo.getId() == null) {
            todo.setId(counter.incrementAndGet());
        }
        todoMap.put(todo.getId(), todo);

        return todo;
    }

    public void deleteById(Long id) {
        todoMap.remove(id);
    }
}
