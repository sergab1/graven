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
import tasks.services.*;

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
        datePickerEnd.setValue(LocalDate.now());
        txtFieldTimeStart.setText(DEFAULT_START_TIME);
    }

    private void initEditWindow(String title){
        try{
        currentStage.setTitle(title);
        fieldTitle.setText(currentTask.getTitle());
        datePickerStart.setValue(dateService.getLocalDateValueFromDate(currentTask.getStartTime()));
        datePickerEnd.setValue(dateService.getLocalDateValueFromDate(currentTask.getStartTime()));
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
    public void saveChanges() throws MyException {

        Task collectedFieldsTask = collectFieldsData();

        if (incorrectInputMade) return;

        if (currentTask == null){//no task was chosen -> add button was pressed
            try{
                service.addTask(collectedFieldsTask);
                tasksList.add(collectedFieldsTask);
            }
            catch (Exception e) {

                incorrectInputMade = true;
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setHeaderText("Input not valid");
                errorAlert.setContentText(e.getMessage());
                errorAlert.showAndWait();
            }
            }
        else {
            for (int i = 0; i < tasksList.size(); i++){
                if (currentTask.equals(tasksList.get(i))){
                    tasksList.set(i,collectedFieldsTask);
                }
            }
            currentTask = null;
        }
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


            int dayStart=datePickerStart.getValue().getDayOfMonth();
            int monthStart=datePickerStart.getValue().getMonthValue();
            int yearStart=datePickerStart.getValue().getYear();
            Calendar calendar=Calendar.getInstance();
            calendar.set(yearStart,monthStart,dayStart);
            Date dateStart=calendar.getTime();

            int dayEnd=datePickerEnd.getValue().getDayOfMonth();
            int monthEnd=datePickerEnd.getValue().getMonthValue();
            int yearEnd=datePickerEnd.getValue().getYear();
            calendar.set(yearEnd,monthEnd,dayEnd);
            Date dateEnd=calendar.getTime();

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


    public Task makeTask(String title,String timeStart,String timeEnd,String interval,Date dateStart,Date dateEnd) throws Exception {
        if (checkBoxRepeated.isSelected())
            checkBoxRepeatedIsSelected=true;
        if(checkBoxActive.isSelected())
            checkBoxActiveIsSelected=true;
        Task result=service.createTask(title,timeStart,timeEnd,interval,dateStart,dateEnd,checkBoxRepeatedIsSelected,checkBoxActiveIsSelected);
        return result;
    }



    public Date getDateValueFromLocalDate(LocalDate localDate)  {//for getting from DatePicker

        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);

    }


    public int parseFromStringToSeconds(String stringTime){//hh:MM
        String[] units = stringTime.split(":");
        int hours = Integer.parseInt(units[0]);
        int minutes = Integer.parseInt(units[1]);
        int result = (hours * DateService.MINUTES_IN_HOUR + minutes) * DateService.SECONDS_IN_MINUTE;
        return result;
    }

}
