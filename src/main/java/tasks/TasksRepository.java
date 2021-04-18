package tasks;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.Exceptions;
import tasks.services.MyException;
import tasks.services.TaskIO;

import java.io.File;

public class TasksRepository {
    private ArrayTaskList arrayTaskList;

    public TasksRepository(ArrayTaskList arrayTaskList) {
        this.arrayTaskList=arrayTaskList;
    }


    public void addTask(Task t) throws MyException {
        for (Task f:arrayTaskList) {
            if(f.getTitle().equals(t.getTitle())){
                throw new MyException(Exceptions.existingTask.label);
            }
        }
        arrayTaskList.add(t);
        TaskIO.rewriteFile(FXCollections.observableArrayList(arrayTaskList.getAll()));
    }

    public ObservableList<Task> getObservableList(){
        return FXCollections.observableArrayList(arrayTaskList.getAll());
    }

    public ArrayTaskList getArrayTaskList() {
        return arrayTaskList;
    }
}
