package com.eugepavia.todolist.controller;

import com.eugepavia.todolist.dto.CreateTaskRequest;
import com.eugepavia.todolist.dto.TaskResponse;
import com.eugepavia.todolist.dto.UpdateTaskRequest;
import com.eugepavia.todolist.model.Task;
import com.eugepavia.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.findAll().stream()
                .map(TaskResponse::from)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable("id") Long id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok(TaskResponse.from(task));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest task) {
        Task newTask = taskService.create(task.title());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTask.getId())
                .toUri();
        return ResponseEntity.created(uri).body(TaskResponse.from(newTask));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<TaskResponse> updateTask(@PathVariable("id") Long id, @Valid @RequestBody UpdateTaskRequest task) {
        Task updatedTask = taskService.updateById(id, task.title(), task.completed());
        return ResponseEntity.ok(TaskResponse.from(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}