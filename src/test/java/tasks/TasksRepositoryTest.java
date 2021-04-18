package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.services.Exceptions;
import tasks.services.MyException;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class TasksRepositoryTest {
    private Date dateStart;
    private Date dateNew;
    private TasksRepository tasksRepository;

    @Mock
    private Task tMock;

    @BeforeEach
    public void prepare() {
        MockitoAnnotations.initMocks(this);
    }


    @BeforeEach
    void initialize() {
        ArrayTaskList arrayTasksList=new ArrayTaskList();
        tasksRepository=new TasksRepository(arrayTasksList);

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
    void taskRepositoryAdd() throws MyException {
        tMock=mock(Task.class);
        Mockito.when(tMock.getTitle()).thenReturn("abc");
        Mockito.when(tMock.getTime()).thenReturn(dateStart);

        tasksRepository.addTask(tMock);

        assert(tasksRepository.getArrayTaskList().size()==1);
    }

    @Tag("invalid")
    @Test
    void taskRepositoryAddExisting()  {
        try {
            tMock=mock(Task.class);
            Mockito.when(tMock.getTitle()).thenReturn("abc");
            Mockito.when(tMock.getTime()).thenReturn(dateStart);
            tasksRepository.addTask(tMock);
            tasksRepository.addTask(tMock);
            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.existingTask.label);
        }

    }

}