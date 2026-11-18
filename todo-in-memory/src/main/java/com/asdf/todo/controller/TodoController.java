package com.asdf.todo.controller;

import com.asdf.todo.model.Todo;
import com.asdf.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// REST 컨트롤러 빈임을 선언하고, 모든 HTTP 메소드의 공통된 URL로 지정
@RestController
@RequestMapping("/api/todos/v1")
public class TodoController {
    // TodoService 빈을 컨트롤러 클래스에 주입
    // 컨트롤러는 서비스 레이어의 메서드를 호출 가능
    @Autowired private TodoService todoService;

    // @GetMapping 애노테이션을 사용해 HTTP GET 요청을 처리
    // 클래스 수준에서 설정한 @RequestMapping 경로를 공통 URL 엔드포인트로 활용
    @GetMapping
    @Operation(summary = "전체 작업 조회", description = "전체 작업 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "204", description = "내용 없음")
    })
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = todoService.findAll();

        if (todos == null || todos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(todos);
    }

    // 경로 변수(Path Variable)를 통해 `/{id}` 값을 전달받음
    @GetMapping("/{id]")
    @Operation(summary = "작업 조회", description = "ID로 작업 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.findById(id);

        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(todo);
    }

    // HTTP POST 요청을 보내면 해당 요청이 createTodo() 메서드로 라우팅됨
    // 요청 본문에 전달받은 todo 객체를 서비스 레이어의 save() 메서드에 넘김
    @PostMapping()
    @Operation(summary = "작업 생성", description = "새로운 작업 생성")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "생성됨"),
    })
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        return ResponseEntity.status(201).body(todoService.save(todo));
    }

    // 클라이언트가 `/api/todos/v1/{id}` 엔드포인트에 HTTP PUT 요청을 보내면
    // 해당 요청이 updateTodo() 메서드로 라우팅됨
    @PutMapping("/{id}")
    @Operation(summary = "작업 수정", description = "ID로 작업 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "수정됨"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        Todo existingTodo = todoService.findById(id);
        if (existingTodo == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(todoService.update(id, todo));
    }

    // HTTP 204 (No Content) 응답 코드는 요청이 성공적으로 처리되었지만
    // 반환할 데이터가 없음을 의미, 대체로 DELETE 요청에서 일반적으로 사용
    @DeleteMapping("/{id}")
    @Operation(summary = "작업 삭제", description = "ID로 작업 삭제")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "내용 없음"),
        @ApiResponse(responseCode = "404", description = "작업 없음")
    })
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        Todo todo = todoService.findById(id);
        if (todo == null) {
            return ResponseEntity.notFound().build();
        }

        todoService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
