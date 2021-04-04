package tasks.model;

import javafx.collections.ObservableList;
import tasks.services.Exceptions;
import tasks.services.MyException;

import java.util.*;

public class TasksOperations {


    public ArrayTaskList tasks;

    public TasksOperations(ArrayTaskList tasksList){
        this.tasks=tasksList;
    }

    public Iterable<Task> incoming(Date start, Date end) throws MyException {
     /*1*/   if(start.after(end)){
         /*2*/ throw new MyException(Exceptions.startAfterEnd.label); }
        else {
            /*3*/ ArrayList<Task> incomingTasks = new ArrayList<>();
            /*4*/for (Task t : tasks) {
                /*5*/ Date nextTime = new Date();
                /*6*/if (start.after(t.getEndTime()) || start.equals(t.getEndTime())) {
                    /*7*/nextTime = null;
                } else {
                    /*8*/if (t.isRepeated() && t.isActive()) {
                        Date timeBefore = t.getStartTime();
                        /*9*/Date timeAfter = t.getStartTime();
                        /*10*/if (start.before(t.getStartTime())) {
                            /*11*/nextTime = t.getStartTime();
                        } else {
                            /*12*/if ((start.after(t.getStartTime()) || start.equals(t.getStartTime())) && (start.before(t.getEndTime()) || start.equals(t.getEndTime()))) {
                                /*13*/for (long i = t.getStartTime().getTime(); i <= t.getEndTime().getTime(); i += t.getRepeatInterval() * 1000) {
                                    /*14*/if (start.equals(timeAfter)) {
                                        /*15*/nextTime = new Date(timeAfter.getTime() + t.getRepeatInterval() * 1000);
                                        break;
                                    }
                                    /*16*/if (start.after(timeBefore) && start.before(timeAfter)) {
                                        /*17*/nextTime = timeBefore;
                                        break;
                                    }
                                    timeBefore = timeAfter;
                                    /*18*/timeAfter = new Date(timeAfter.getTime() + t.getRepeatInterval() * 1000);
                                }
                            }
                        }
                    } else {
                        /*19*/if (!t.isRepeated() && start.before(t.getTime()) && t.isActive()) {
                            /*20*/nextTime = t.getTime();
                        }
                    }
                }

                /*21*/if (nextTime != null && (nextTime.before(end) || nextTime.equals(end))) {
                    /*22*/incomingTasks.add(t);
                    System.out.println(t.getTitle());
                }
            }
            /*23*/ if (incomingTasks.size() == 0) {
                /*24*/ throw new MyException(Exceptions.noTasks.label);
            }
            /*25*/return incomingTasks;
        }
        /*26*/ }


    /*
    public SortedMap<Date, Set<Task>> calendar( Date start, Date end){
        Iterable<Task> incomingTasks = incoming(start, end);
        TreeMap<Date, Set<Task>> calendar = new TreeMap<>();

        for (Task t : incomingTasks){
            Date nextTimeAfter = t.nextTimeAfter(start);
            while (nextTimeAfter!= null && (nextTimeAfter.before(end) || nextTimeAfter.equals(end))){
                if (calendar.containsKey(nextTimeAfter)){
                    calendar.get(nextTimeAfter).add(t);
                }
                else {
                    HashSet<Task> oneDateTasks = new HashSet<>();
                    oneDateTasks.add(t);
                    calendar.put(nextTimeAfter,oneDateTasks);
                }
                nextTimeAfter = t.nextTimeAfter(nextTimeAfter);
            }
        }
        return calendar;
    }*/
}
