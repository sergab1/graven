package tasks.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tasks.controller.NewEditController;
import tasks.services.Exceptions;
import tasks.services.MyException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TasksOperationsTest {
    private NewEditController newEditController;
    private Date dateStart;
    private Date dateEnd;
    private TasksOperations tasksOperations;
    public ArrayTaskList tasks=new ArrayTaskList();

    @BeforeEach
    void initialize()  {
        newEditController = new NewEditController();
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
    void incomingTestActiveNotRepeated() throws MyException {
        Task newTask=newEditController.createTask("abm","08:00","02:00","1",dateStart,dateEnd);
        newTask.setActive(true);
        tasks.add(newTask);
        tasksOperations=new TasksOperations(tasks);
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
    void incomingTestActiveRepeated_startBeforeTimeStart() throws MyException {
        newEditController.checkBoxRepeatedIsSelected=true;
        Task newTask2=newEditController.createTask("abm2","05:00","02:00","00:10",dateStart,dateEnd);
        newTask2.setActive(true);
        tasks.add(newTask2);
        tasksOperations=new TasksOperations(tasks);
        Iterable<Task> newTasks= tasksOperations.incoming(dateStart,dateEnd);
        Iterator<Task> it=newTasks.iterator();
        Task t=it.next();

        assert(t.getTitle().equals("abm2"));
        assert(t.getStartTime().toString().equals("Mon Dec 20 05:00:00 EET 2021"));
        assert(t.getEndTime().toString().equals("Wed Dec 22 02:00:00 EET 2021"));
    }

    @Tag("valid")
    @Test
    void incomingTestActiveRepeated_startAfterTimeStart_startEqualsTimeAfter() throws MyException {
        newEditController.checkBoxRepeatedIsSelected=true;
        Task newTask3=newEditController.createTask("abm3","02:00","03:00","00:10",dateStart,dateEnd);
        newTask3.setActive(true);
        tasks.add(newTask3);
        tasksOperations=new TasksOperations(tasks);
        Iterable<Task> newTasks= tasksOperations.incoming(dateStart,dateEnd);
        Iterator<Task> it=newTasks.iterator();
        Task t=it.next();

        assert(t.getTitle().equals("abm3"));
        assert(t.getStartTime().toString().equals("Mon Dec 20 02:00:00 EET 2021"));
        assert(t.getEndTime().toString().equals("Wed Dec 22 03:00:00 EET 2021"));
    }

    @Tag("valid")
    @Test
    void incomingTestActiveRepeated_startAfterTimeStart_startBetweenTimeBeforeTimeAfter() throws MyException {
        newEditController.checkBoxRepeatedIsSelected=true;
        Task newTask4=newEditController.createTask("abm4","00:01","02:00","00:10",dateStart,dateEnd);
        newTask4.setActive(true);
        tasks.add(newTask4);
        tasksOperations=new TasksOperations(tasks);
        Iterable<Task> newTasks= tasksOperations.incoming(dateStart,dateEnd);
        Iterator<Task> it=newTasks.iterator();
        Task t=it.next();

        assert(t.getTitle().equals("abm4"));
        assert(t.getStartTime().toString().equals("Mon Dec 20 00:01:00 EET 2021"));
        assert(t.getEndTime().toString().equals("Wed Dec 22 02:00:00 EET 2021"));
    }

    @Tag("invalid")
    @Test
    void incomingTestStartAfterEndTime() throws MyException {
        Task newTask5=newEditController.createTask("abm5","00:00","01:00","1",dateStart,dateEnd);
        tasks.add(newTask5);
        tasksOperations=new TasksOperations(tasks);

        try{
        tasksOperations.incoming(dateStart,dateEnd);}
        catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()== Exceptions.noTasks.label);
        }
    }

    @Tag("invalid")
    @Test
    void incomingTestNoTasks() throws MyException {

        Date date = new GregorianCalendar(2020, 11, 20).getTime();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 02);
        calendar.set(Calendar.MINUTE, 00);
        Date newDateStart=calendar.getTime();

        calendar.set(2020,11,22);
        Date newDateEnd=calendar.getTime();

        Task newTask6=newEditController.createTask("abm6","00:00","01:00","1",dateStart,dateEnd);
        tasks.add(newTask6);
        tasksOperations=new TasksOperations(tasks);

        try{
        tasksOperations.incoming(newDateStart,newDateEnd);}
        catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()== Exceptions.noTasks.label);
        }

    }

    @Tag("invalid")
    @Test
    void incomingTestStartAfterEnd() throws MyException {
        Task newTask7=newEditController.createTask("abm7","00:00","01:00","1",dateStart,dateEnd);
        tasks.add(newTask7);
        tasksOperations=new TasksOperations(tasks);

        try{
            tasksOperations.incoming(dateEnd,dateStart);}
        catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()== Exceptions.startAfterEnd.label);
        }

    }


}