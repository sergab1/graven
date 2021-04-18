package tasks.services;

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

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TasksServiceTest {
    private Date dateStart;
    private Date dateNew;
    private TasksRepository tasksRepository;
    private TasksService tasksService;
    public static File savedTasksFile = new File("data/tasks2.txt");

    @Mock
    private Task tMock;

    @Mock
    private TasksRepository tasksRepositoryMock;

    @BeforeEach
    public void prepare() {
        MockitoAnnotations.initMocks(this);
    }


    @BeforeEach
    void initialize() {
        tasksRepository=mock(TasksRepository.class);
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
    void taskServiceAdd() throws MyException {
        tMock=mock(Task.class);
        Mockito.when(tMock.getTitle()).thenReturn("abc");
        Mockito.when(tMock.getTime()).thenReturn(dateStart);

        ArrayTaskList arrayTaskList=new ArrayTaskList();
        arrayTaskList.add(tMock);
        Mockito.when(tasksRepository.getArrayTaskList()).thenReturn(arrayTaskList);

        tasksService.addTask(tMock);

        assert(tasksService.getArrayTaskList().size()==1);
    }

    @Tag("invalid")
    @Test
    void taskServiceAddExisting()  {
        try {
            tMock=mock(Task.class);
            Mockito.when(tMock.getTitle()).thenReturn("abc");
            Mockito.when(tMock.getTime()).thenReturn(dateStart);

            ArrayTaskList arrayTaskList=new ArrayTaskList();
            arrayTaskList.add(tMock);

            Mockito.when(tasksRepository.getArrayTaskList()).thenReturn(arrayTaskList);
            Mockito.doNothing().when(tasksRepository).addTask(tMock);

            tasksService.addTask(tMock);
            Mockito.doThrow(new MyException(Exceptions.existingTask.label)).when(tasksRepository).addTask(tMock);
            tasksService.addTask(tMock);


            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.existingTask.label);
        }

    }

}