package com.eugepavia.todolist.service;

import com.eugepavia.todolist.exception.TaskNotFoundException;
import com.eugepavia.todolist.model.Task;
import com.eugepavia.todolist.repository.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task create(Task task) {
        if (task.getTitle() != null && !task.getTitle().isBlank()) {
            task.setCompleted(false);
            taskRepository.save(task);
            return task;
        } else {
            throw new IllegalArgumentException("Invalid arguments");
        }
    }

    @Transactional
    public Task updateById(Long id, Task task) {
        Task updatedTask = findById(id);
        if (task.getTitle() != null) {
            updatedTask.setTitle(task.getTitle());
        }
        updatedTask.setCompleted(task.isCompleted());
        taskRepository.save(updatedTask);
        return updatedTask;
    }

    @Transactional
    public void deleteById(Long id) {
        Task task = findById(id);
        taskRepository.deleteById(id);
    }



}
