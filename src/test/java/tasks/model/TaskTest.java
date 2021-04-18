package tasks.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import tasks.TasksRepository;
import tasks.services.Exceptions;
import tasks.services.MyException;
import tasks.services.TasksService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Date dateStart;
    private Date dateNew;

    @BeforeEach
    void initialize() {
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
    void createTaskTitleTime()  {
        Task t= new Task("abc",dateStart);
        assert(t.getTitle().equals("abc"));
        assert(t.getTime()==dateStart);
    }

    @Tag("invalid")
    @Test
    void createTaskTitleTimeNegative()  {
        try {
            Task t = new Task("abc", dateNew);
            assert(false);
        }catch (IllegalArgumentException il){
            assert(il.getMessage().equals("Time cannot be negative"));
        }

    }
}