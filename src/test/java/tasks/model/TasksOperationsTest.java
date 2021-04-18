package tasks.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tasks.TasksRepository;
import tasks.controller.NewEditController;
import tasks.services.Exceptions;
import tasks.services.MyException;
import tasks.services.TasksService;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TasksOperationsTest {
    private NewEditController newEditController;
    private Date dateStart;
    private Date dateEnd;
    private TasksOperations tasksOperations;
    private ArrayTaskList savedTasksList;
    public static File savedTasksFile = new File("data/tasks2.txt");
    private TasksService tasksService;

    @BeforeEach
    void initialize()  {
        savedTasksList = new ArrayTaskList();
        TasksRepository repo=new TasksRepository(savedTasksList);
        tasksService=new TasksService(repo,savedTasksFile);
        Date date = new GregorianCalendar(2021, 11, 20).getTime();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 02);
        calendar.set(Calendar.MINUTE, 00);
        dateStart=calendar.getTime();

        calendar.set(2021,11,22);
        dateEnd=calendar.getTime();

    }

    @Tag("valid")
    @Test
    void incomingTestActiveNotRepeated() throws Exception {
        Task newTask=tasksService.createTask("abm","08:00","02:00","1",dateStart,dateEnd,false,true);
        savedTasksList.add(newTask);
        tasksOperations=new TasksOperations(savedTasksList);
        Iterable<Task> newTasks= tasksOperations.incoming(dateStart,dateEnd);
        Iterator<Task> it=newTasks.iterator();
        Task t=it.next();

        assert(t.getTitle().equals("abm"));
        assert(t.isActive()==true);
        assert(t.getStartTime().toString().equals("Mon Dec 20 08:00:00 EET 2021"));
        assert(t.getEndTime().toString().equals("Mon Dec 20 08:00:00 EET 2021"));
    }

    @Tag("valid")
    @Test
    void incomingTestActiveRepeated_startBeforeTimeStart() throws Exception {
        Task newTask2=tasksService.createTask("abm2","05:00","02:00","00:10",dateStart,dateEnd,true,true);
        savedTasksList.add(newTask2);
        tasksOperations=new TasksOperations(savedTasksList);
        Iterable<Task> newTasks= tasksOperations.incoming(dateStart,dateEnd);
        Iterator<Task> it=newTasks.iterator();
        Task t=it.next();

        assert(t.getTitle().equals("abm2"));
        assert(t.getStartTime().toString().equals("Mon Dec 20 05:00:00 EET 2021"));
        assert(t.getEndTime().toString().equals("Wed Dec 22 02:00:00 EET 2021"));
    }

    @Tag("valid")
    @Test
    void incomingTestActiveRepeated_startAfterTimeStart_startEqualsTimeAfter() throws Exception {
        Task newTask3=tasksService.createTask("abm3","02:00","03:00","00:10",dateStart,dateEnd,true,true);
        savedTasksList.add(newTask3);
        tasksOperations=new TasksOperations(savedTasksList);
        Iterable<Task> newTasks= tasksOperations.incoming(dateStart,dateEnd);
        Iterator<Task> it=newTasks.iterator();
        Task t=it.next();

        assert(t.getTitle().equals("abm3"));
        assert(t.getStartTime().toString().equals("Mon Dec 20 02:00:00 EET 2021"));
        assert(t.getEndTime().toString().equals("Wed Dec 22 03:00:00 EET 2021"));
    }

    @Tag("valid")
    @Test
    void incomingTestActiveRepeated_startAfterTimeStart_startBetweenTimeBeforeTimeAfter() throws Exception {
        Task newTask4=tasksService.createTask("abm4","00:01","02:00","00:10",dateStart,dateEnd,true,true);
        savedTasksList.add(newTask4);
        tasksOperations=new TasksOperations(savedTasksList);
        Iterable<Task> newTasks= tasksOperations.incoming(dateStart,dateEnd);
        Iterator<Task> it=newTasks.iterator();
        Task t=it.next();

        assert(t.getTitle().equals("abm4"));
        assert(t.getStartTime().toString().equals("Mon Dec 20 00:01:00 EET 2021"));
        assert(t.getEndTime().toString().equals("Wed Dec 22 02:00:00 EET 2021"));
    }

    @Tag("invalid")
    @Test
    void incomingTestStartAfterEndTime() throws Exception {
        Task newTask5=tasksService.createTask("abm5","00:00","01:00","1",dateStart,dateEnd,false,false);
        savedTasksList.add(newTask5);
        tasksOperations=new TasksOperations(savedTasksList);

        try{
        tasksOperations.incoming(dateStart,dateEnd);}
        catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()== Exceptions.noTasks.label);
        }
    }

    @Tag("invalid")
    @Test
    void incomingTestNoTasks() throws Exception {

        Date date = new GregorianCalendar(2020, 11, 20).getTime();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 02);
        calendar.set(Calendar.MINUTE, 00);
        Date newDateStart=calendar.getTime();

        calendar.set(2020,11,22);
        Date newDateEnd=calendar.getTime();

        Task newTask6=tasksService.createTask("abm6","00:00","01:00","1",dateStart,dateEnd,false,false);
        savedTasksList.add(newTask6);
        tasksOperations=new TasksOperations(savedTasksList);

        try{
        tasksOperations.incoming(newDateStart,newDateEnd);}
        catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()== Exceptions.noTasks.label);
        }

    }

    @Tag("invalid")
    @Test
    void incomingTestStartAfterEnd() throws Exception {
        Task newTask7=tasksService.createTask("abm7","00:00","01:00","1",dateStart,dateEnd,false,false);
        savedTasksList.add(newTask7);
        tasksOperations=new TasksOperations(savedTasksList);

        try{
            tasksOperations.incoming(dateEnd,dateStart);}
        catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()== Exceptions.startAfterEnd.label);
        }

    }


}