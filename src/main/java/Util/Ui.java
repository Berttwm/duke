package Util;

import Tasks.Deadline;
import Tasks.Event;
import Tasks.Task;
import Tasks.Todo;
import Tasks.TaskList;
import java.util.ArrayList;
import java.util.Scanner;

import Exception.DukeException;
import Exception.IncorrectTaskNameException;
import Exception.EmptyDeadlineDescriptionException;
import Exception.EmptyEventDescriptionException;
import Exception.EmptyTodoDescriptionException;

public class Ui {

    private Parser parser;
    private TaskList tasksList;
    private Storage storage;

    public Ui(TaskList taskList, Storage storage) {
        this.tasksList = taskList;
        this.storage = storage;
    }

    public void readInput() throws Exception, DukeException{
        Scanner scanner = new Scanner(System.in);
        showWelcome();
        while(scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")){
                showExit();
                break;
            }
            if (input.equals("list")){
                tasksList.showAllTasks();
                continue;
            }
            if ((input.length() > 5) && input.substring(0, 5).equals("done ")){
                showDone(input);
                continue;
            }
            if((input.length() > 7) && input.substring(0, 7).equals("delete ")) {
                showDelete(input);
                continue;
            }
            else {
                // just to catch wrong input
                try{
                    input.substring(0,4).equals("todo");
                    input.substring(0,8).equals("deadline");
                    input.substring(0,5).equals("event");
                } catch (Exception e){
                    throw new DukeException("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
                //handles the todo, deadline and event tasks
                System.out.println("Got it. I've added this task:");

                if(input.substring(0,4).equals("todo")) {
                    try{
                        input.substring(5,6);
                    } catch (Exception e) {
                        throw new DukeException("☹ OOPS!!! I'm sorry, todo cannot be empty");
                    }
                    showTodo(input);
                    continue;
                }
                if(input.substring(0,8).equals("deadline")){
                    try {
                        input.substring(9,10);
                    } catch (Exception e){
                        throw new DukeException("☹ OOPS!!! The description of a deadline cannot be empty.");
                    }
                    showDeadline(input);
                    continue;
                }
                if(input.substring(0,5).equals("event")){
                    try {
                        input.substring(6,7);
                    } catch (Exception e){
                        throw new DukeException("☹ OOPS!!! The description of a deadline cannot be empty.");
                    }
                    showEvent(input);
                    continue;
                }
            }
        }
    }

    public void showWelcome() {
        System.out.println("Hello! I'm Duke");
        System.out.println("What can I do for you?");
    }

    public void showExit() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void showDone(String input) throws Exception{
        int itemIndex = Integer.parseInt(input.substring(5)) - 1;
        // close if item number don't exist
        if ((itemIndex + 1) > tasksList.getTaskList().size()){
            System.out.println("failed to done item, please try again");
        } else {
            tasksList.markTaskDone(itemIndex);
            storage.rewriteWriter(tasksList);
        }
    }

    public void showDelete(String input) throws Exception{
        int itemIndex = Integer.parseInt(input.substring(7)) - 1;
        if ((itemIndex + 1) > tasksList.getTaskList().size()){
            System.out.println("failed to delete item, please try again.");
        } else {
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + tasksList.getTaskList().get(itemIndex));
            tasksList.getTaskList().remove(itemIndex);
            storage.rewriteWriter(tasksList);
            System.out.println("Now you have " + tasksList.getTaskList().size() + " in the list.");
        }
    }

    public void showTodo(String input){
        String inputAdd = input.substring(5);
        Todo todo = new Todo(inputAdd, "");
        tasksList.addTask(todo);
        storage.writeTodo(todo);
        System.out.println("  " + todo);
    }

    public void showDeadline(String input){
        String inputAddArr[] = input.split(" /by ");
        String inputAdd1 = inputAddArr[0].substring(9);
        String inputAdd2 = inputAddArr[1];
        Deadline deadline = new Deadline(inputAdd1, inputAdd2);
        tasksList.addTask(deadline);
        storage.writeDeadline(deadline);
        System.out.println("  " + deadline);
    }

    public void showEvent(String input){
        String inputAddArr[] = input.split(" /at ");
        String inputAdd1 = inputAddArr[0].substring(6);
        String inputAdd2 = inputAddArr[1];
        Event event = new Event(inputAdd1, inputAdd2);
        tasksList.addTask(event);
        storage.writeEvent(event);
        System.out.println("  " + event);
    }

}