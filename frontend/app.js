// change to env
const API_URL = "http://localhost:8080/api/tasks"

let form = document.getElementById("taskForm");
let taskInput = document.getElementById("taskInput")
let errorMessageField = document.getElementById("errorMessage");
let taskListField = document.getElementById("taskList");


// Check if status code is 200-299
async function checkIfOk(response) {
    if (response.ok) { return response }
    const error = await response.json().catch(()=>({}));
    throw new Error(error.message || "Unexpected error server");
}

// Load task list
async function loadTasks() {
    try {
        const taskList = await fetchTasks();
        renderTasks(taskList);
    } catch (err) {
        errorMessageField.textContent = "Could not load tasks: " + err.message;
    }
}

// REQUEST CALLS

// GET ""
async function fetchTasks() {
    const response = await fetch(API_URL);

    await checkIfOk(response);

    return await response.json();
}

// POST ""
async function createTask(title) {
    const response = await fetch(API_URL,{
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({title})
    })

    await checkIfOk(response);

    return await response.json();
}

// PUT "/{id}"
async function toggleTask(task) {
    const response = await fetch(`${API_URL}/${task.id}`,{
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({title:task.title,completed:!task.completed})
    });
    
    await checkIfOk(response);

    return await response.json();
}

// DELETE "/{id}"
async function deleteTask(id) {
    const response = await fetch(`${API_URL}/${id}`,{
        method: "DELETE",
    });
    
    await checkIfOk(response);
}


// TO EDIT HTML

// Render task list
function renderTasks(tasks) {
    taskListField.textContent = "";
    
    tasks.forEach((task) => {
        const item = document.createElement("li");

        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.checked  =task.completed;
        checkbox.addEventListener("change",() => handleAction(() => toggleTask(task)));
        
        const titleSpan = document.createElement("span");
        titleSpan.textContent = task.title;
        titleSpan.classList.toggle("completed", task.completed);

        const deleteButton = document.createElement("button");
        deleteButton.textContent = "Delete";
        deleteButton.addEventListener("click",() => handleAction(() => deleteTask(task.id)));

        item.append(checkbox,titleSpan,deleteButton);
        taskListField.append(item);

    });
}

// Handle errors while rendering task list
async function handleAction(action) {
    try {
        errorMessageField.textContent = ""
        await action();
        await loadTasks();
    } catch (err) {
        errorMessageField.textContent = "Ups, something went wrong: " + err.message;
    }
}

// Submit form
form.addEventListener("submit",(event) => {
    event.preventDefault();
    handleAction(async () => {
        await createTask(taskInput.value);
        taskInput.value = "";
    });
});

/*
  __
<(. )__   Eugenia Pavía Ruz
 (____/   Octubre 2026

 */