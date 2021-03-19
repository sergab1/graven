package tasks.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import tasks.model.Task;
import tasks.services.DateService;
import tasks.services.TaskIO;
import tasks.services.TasksService;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.getInstance;


public class NewEditController {

    public boolean checkBoxRepeatedIsSelected=false;
    public boolean checkBoxActiveIsSelected=false;
    private static Button clickedButton;

    private static final Logger log = Logger.getLogger(NewEditController.class.getName());

    public static void setClickedButton(Button clickedButton) {
        NewEditController.clickedButton = clickedButton;
    }

    public static void setCurrentStage(Stage currentStage) {
        NewEditController.currentStage = currentStage;
    }

    private static Stage currentStage;

    private Task currentTask;
    private ObservableList<Task> tasksList;
    private TasksService service;
    private DateService dateService;


    private boolean incorrectInputMade;
    @FXML
    private TextField fieldTitle;
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private TextField txtFieldTimeStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private TextField txtFieldTimeEnd;
    @FXML
    private TextField fieldInterval;
    @FXML
    private CheckBox checkBoxActive;
    @FXML
    public CheckBox checkBoxRepeated;

    private static final String DEFAULT_START_TIME = "08:00";
    private static final String DEFAULT_END_TIME = "10:00";
    private static final String DEFAULT_INTERVAL_TIME = "00:30";

    public void setTasksList(ObservableList<Task> tasksList){
        this.tasksList =tasksList;
    }

    public void setService(TasksService service){
        this.service =service;
        this.dateService =new DateService(service);
    }

    public void setCurrentTask(Task task){
        this.currentTask=task;
        switch (clickedButton.getId()){
            case  "btnNew" : initNewWindow("New Task");
                break;
            case "btnEdit" : initEditWindow("Edit Task");
                break;
            default:break;
        }
    }

    @FXML
    public void initialize(){
        log.info("new/edit window initializing");
//        switch (clickedButton.getId()){
//            case  "btnNew" : initNewWindow("New Task");
//                break;
//            case "btnEdit" : initEditWindow("Edit Task");
//                break;
//        }

    }
    public void initNewWindow(String title){
        currentStage.setTitle(title);
        datePickerStart.setValue(LocalDate.now());
        txtFieldTimeStart.setText(DEFAULT_START_TIME);
    }

    private void initEditWindow(String title){
        try{
        currentStage.setTitle(title);
        fieldTitle.setText(currentTask.getTitle());
        datePickerStart.setValue(dateService.getLocalDateValueFromDate(currentTask.getStartTime()));
        txtFieldTimeStart.setText(dateService.getTimeOfTheDayFromDate(currentTask.getStartTime()));

        if (currentTask.isRepeated()){
            checkBoxRepeated.setSelected(true);
            hideRepeatedTaskModule(false);
            datePickerEnd.setValue(dateService.getLocalDateValueFromDate(currentTask.getEndTime()));
            fieldInterval.setText(service.getIntervalInHours(currentTask));
            txtFieldTimeEnd.setText(dateService.getTimeOfTheDayFromDate(currentTask.getEndTime()));
        }
        if (currentTask.isActive()){
            checkBoxActive.setSelected(true);

        }}
        catch (Exception e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Nu ati selectat niciun task");
            errorAlert.showAndWait();
        }
    }
    @FXML
    public void switchRepeatedCheckbox(ActionEvent actionEvent){
        CheckBox source = (CheckBox)actionEvent.getSource();
        if (source.isSelected()){
            hideRepeatedTaskModule(false);
        }
        else if (!source.isSelected()){
            hideRepeatedTaskModule(true);
        }
    }
    private void hideRepeatedTaskModule(boolean toShow){
        datePickerEnd.setDisable(toShow);
        fieldInterval.setDisable(toShow);
        txtFieldTimeEnd.setDisable(toShow);

        datePickerEnd.setValue(LocalDate.now());
        txtFieldTimeEnd.setText(DEFAULT_END_TIME);
        fieldInterval.setText(DEFAULT_INTERVAL_TIME);
    }

    @FXML
    public void saveChanges(){
        Task collectedFieldsTask = collectFieldsData();

        if (incorrectInputMade) return;

        if (currentTask == null){//no task was chosen -> add button was pressed
            tasksList.add(collectedFieldsTask);
        }
        else {
            for (int i = 0; i < tasksList.size(); i++){
                if (currentTask.equals(tasksList.get(i))){
                    tasksList.set(i,collectedFieldsTask);
                }
            }
            currentTask = null;
        }
        TaskIO.rewriteFile(tasksList);
        Controller.editNewStage.close();
    }
    @FXML
    public void closeDialogWindow(){
        Controller.editNewStage.close();
    }

    private Task collectFieldsData()  {
        incorrectInputMade = false;
        Task result = null;
        try {
            String title = fieldTitle.getText();
            String timeStart=txtFieldTimeStart.getText();
            String timeEnd=txtFieldTimeEnd.getText();
            String interval=fieldInterval.getText();
            LocalDate dateStart=datePickerStart.getValue();
            LocalDate dateEnd=datePickerStart.getValue();

            result = makeTask(title,timeStart,timeEnd,interval,dateStart,dateEnd);
        }
        catch (Exception e){
            incorrectInputMade = true;
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText(e.getMessage());
            errorAlert.showAndWait();
            /*
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/field-validator.fxml"));
                stage.setScene(new Scene(root, 350, 150));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            }
            catch (IOException ioe){
                log.error("error loading field-validator.fxml");
            }*/
        }
        return result;
    }


    public Task makeTask(String title,String timeStart,String timeEnd,String interval,LocalDate dateStart,LocalDate dateEnd) throws Exception {
        if (checkBoxRepeated.isSelected())
            checkBoxRepeatedIsSelected=true;
        if(checkBoxActive.isSelected())
            checkBoxActiveIsSelected=true;
        Task result=createTask(title,timeStart,timeEnd,interval,dateStart,dateEnd);
        return result;
    }

    public Task createTask(String title,String timeStart,String timeEnd,String interval,LocalDate dateStart,LocalDate dateEnd) throws Exception {
        Task result;
        if(title.equals("")) throw new Exception("introduceti titlul!");
        if(title.length()==1) throw new Exception("titlul trebuie sa aiba minim doua caractere!");
        if(timeStart.equals("")) throw new Exception("introduceti timpul de start!");
        if(timeEnd.equals("")&&checkBoxRepeatedIsSelected) throw new Exception("introduceti timpul de final!");

        if(!timeStart.matches( "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) throw new Exception("formatul timpului de start nu este corect!");
        if(!timeEnd.matches( "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")&&checkBoxRepeatedIsSelected) throw new Exception("formatul timpului de final nu este corect!");

        if(checkBoxRepeatedIsSelected&&interval.equals("")) throw new Exception("introduceti intervalul!");
        if(checkBoxRepeatedIsSelected&&!interval.matches("^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$")) throw new Exception("formatul intervalului nu este corect!");


        Date startDateWithNoTime = getDateValueFromLocalDate(dateStart);//ONLY date!!without time
        Date newStartDate = getDateMergedWithTime(timeStart, startDateWithNoTime);
        if (checkBoxRepeatedIsSelected){
            Date endDateWithNoTime = getDateValueFromLocalDate(dateEnd);
            Date newEndDate = getDateMergedWithTime(timeEnd, endDateWithNoTime);
            int newInterval = service.parseFromStringToSeconds(interval);
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


    public Date getDateValueFromLocalDate(LocalDate localDate)  {//for getting from DatePicker

        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);

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

}
