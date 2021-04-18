package tasks.services;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import tasks.TasksRepository;
import tasks.model.ArrayTaskList;
import tasks.model.Task;
import tasks.model.TasksOperations;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.getInstance;

public class TasksService {
    private TasksRepository repo;
    private File file;

    public TasksService(TasksRepository repo, File file) {
        this.repo=repo;
        this.file = file;
    }



    public String getIntervalInHours(Task task){
        int seconds = task.getRepeatInterval();
        int minutes = seconds / DateService.SECONDS_IN_MINUTE;
        int hours = minutes / DateService.MINUTES_IN_HOUR;
        minutes = minutes % DateService.MINUTES_IN_HOUR;
        return formTimeUnit(hours) + ":" + formTimeUnit(minutes);//hh:MM
    }
    public String formTimeUnit(int timeUnit){
        StringBuilder sb = new StringBuilder();
        if (timeUnit < 10) sb.append("0");
        if (timeUnit == 0) sb.append("0");
        else {
            sb.append(timeUnit);
        }
        return sb.toString();
    }


    public int parseFromStringToSeconds(String stringTime){//hh:MM
        String[] units = stringTime.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        int result = (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
        return result;
    }

    public Iterable<Task> filterTasks(Date start, Date end) throws IOException, MyException {
        ArrayTaskList tasks=new ArrayTaskList();
        TaskIO.readBinary(tasks, file);
        TasksOperations tasksOps = new TasksOperations(tasks);
        Iterable<Task> filtered = tasksOps.incoming(start,end);
       // Iterable<Task> filtered = tasks.incoming(start, end);

        return filtered;
    }



    public Task createTask(String title,String timeStart,String timeEnd,String interval,Date dateStart,Date dateEnd,boolean checkBoxRepeatedIsSelected,boolean checkBoxActiveIsSelected) throws MyException {
        Task result;
        validateTask(title,timeStart,timeEnd,interval,dateStart,dateEnd,checkBoxRepeatedIsSelected,checkBoxActiveIsSelected);

        Date startDateWithNoTime = dateStart;//ONLY date!!without time
        Date newStartDate = getDateMergedWithTime(timeStart, startDateWithNoTime);
        if (checkBoxRepeatedIsSelected){
            Date endDateWithNoTime = dateEnd;
            Date newEndDate = getDateMergedWithTime(timeEnd, endDateWithNoTime);
            int newInterval = parseFromStringToSeconds(interval);
            if (newStartDate.after(newEndDate)) throw new IllegalArgumentException("Start date should be before end");
            result = new Task(title, newStartDate,newEndDate, newInterval);
        }
        else {
            result = new Task(title, newStartDate);
        }
        boolean isActive = checkBoxActiveIsSelected;
        result.setActive(isActive);
        System.out.println(result);
        return result;
    }

    public void validateTask(String title,String timeStart,String timeEnd,String interval,Date dateStart,Date dateEnd,boolean checkBoxRepeatedIsSelected,boolean checkBoxActiveIsSelected) throws MyException {
        if(title.equals("")) throw new MyException(Exceptions.noTitle.label);
        if(title.length()==1) throw new MyException(Exceptions.titleMin2.label);
        if(title.length()>60) throw new MyException(Exceptions.titleMax60.label);
        if(timeStart.equals("")) throw new MyException(Exceptions.noStartTime.label);
        if(timeEnd.equals("")&&checkBoxRepeatedIsSelected) throw new MyException(Exceptions.noEndTime.label);

        if(!timeStart.matches( "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) throw new MyException(Exceptions.startTimeFormatBad.label);
        if(!timeEnd.matches( "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")&&checkBoxRepeatedIsSelected) throw new MyException(Exceptions.endTimeFormatBad.label);

        if(checkBoxRepeatedIsSelected&&interval.equals("")) throw new MyException(Exceptions.noInterval.label);
        if(checkBoxRepeatedIsSelected&&!interval.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) throw new MyException(Exceptions.intervalFormatBad.label);


    }

    public Date getDateMergedWithTime(String time, Date noTimeDate) {//to retrieve Date object from both DatePicker and time field
        String[] units = time.split(":");
        int hour = Integer.parseInt(units[0]);
        int minute = Integer.parseInt(units[1]);
        if (hour > 24 || minute > 60) throw new IllegalArgumentException("time unit exceeds bounds");
        Calendar calendar = getInstance();
        calendar.setTime(noTimeDate);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    public void addTask(Task t) throws MyException {
        repo.addTask(t);
    }

    public ObservableList<Task> getObservableList(){
        return repo.getObservableList();
    }

    public ArrayTaskList getArrayTaskList(){
        return repo.getArrayTaskList();
    }

}
