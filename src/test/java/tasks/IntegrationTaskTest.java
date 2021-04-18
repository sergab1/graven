package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.tsv.TsvFormat;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tasks.TasksRepository;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.Exceptions;
import tasks.services.MyException;
import tasks.services.TasksService;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class IntegrationTaskTest {
    private Date dateStart;
    private Date dateNew;
    private TasksRepository tasksRepository;
    private TasksService tasksService;
    public static File savedTasksFile = new File("data/tasks2.txt");

    @BeforeEach
    void initialize() {
        ArrayTaskList arrayTaskList=new ArrayTaskList();
        tasksRepository=new TasksRepository(arrayTaskList);
        tasksService=new TasksService(tasksRepository,savedTasksFile);


        Date date = new GregorianCalendar(2021, 11, 20).getTime();
        Date date2 = new GregorianCalendar(-2021, -11, -20).getTime();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 02);
        calendar.set(Calendar.MINUTE, 00);
        dateStart=calendar.getTime();
        calendar.setTime(date2);
        dateNew=calendar.getTime();

    }


    @Tag("valid")
    @Test
    void integrationTaskAdd() throws MyException {
        Task t=new Task("abc",dateStart);

        tasksService.addTask(t);

        assert(tasksService.getArrayTaskList().size()==1);
    }

    @Tag("invalid")
    @Test
    void integrationTaskAddExisting()  {
        try {
            Task t=new Task("abc",dateStart);

            tasksService.addTask(t);
            tasksService.addTask(t);

            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.existingTask.label);
        }

    }

}