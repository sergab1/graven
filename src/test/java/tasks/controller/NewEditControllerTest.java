package tasks.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import tasks.model.Task;


import java.time.LocalDate;

class NewEditControllerTest {
    private NewEditController newEditController;
    private LocalDate dateStart;
    private LocalDate dateEnd;

    @BeforeEach
    void initialize() {
        newEditController = new NewEditController();
        dateStart = LocalDate.of(2021, 11, 20);
        dateEnd = LocalDate.of(2021, 11, 22);
    }


    @Test
    void createTaskECP() throws Exception{
        //ECP
        //title: string de lungime cel putin 2

        //EC1: title = ""   (invalid)
        try{
            Task newTask2=newEditController.createTask("","08:00","02:00","1",dateStart,dateEnd);
        }catch (Exception e){
            assert(e.getMessage()=="introduceti titlul!");
        }

        //EC2: title = "a"   (invalid)
        try{
            Task newTask2=newEditController.createTask("a","08:00","02:00","1",dateStart,dateEnd);
        }catch (Exception e){
            assert(e.getMessage()=="titlul trebuie sa aiba minim doua caractere!");
        }

        //EC3: title.length > 0, title="abm"  (valid)
        Task newTask=newEditController.createTask("abm","08:00","02:00","1",dateStart,dateEnd);
        assert(newTask!=null);
        assert(newTask.getTitle().equals("abm"));

        //EC4: title.length > 0, title="xyz tuv"  (valid)
        Task newTask4=newEditController.createTask("xyz tuv","08:00","02:00","1",dateStart,dateEnd);
        assert(newTask4!=null);
        assert(newTask4.getTitle().equals("xyz tuv"));

        //ECP
        //timeStart: string in formatul hh:ss, 00<=hh<=24, 00<=ss<60


        //EC1: timeStart = ""   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","","02:00","1",dateStart,dateEnd);
        }catch (Exception e){
            assert(e.getMessage()=="introduceti timpul de start!");
        }

        //EC2: timeStart = "15"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","15","02:00","1",dateStart,dateEnd);
        }catch (Exception e){
            assert(e.getMessage()=="formatul timpului de start nu este corect!");
        }

        //EC3: timeStart = "1a:k2"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","1a:k2","02:00","1",dateStart,dateEnd);
        }catch (Exception e){
            assert(e.getMessage()=="formatul timpului de start nu este corect!");
        }

        //EC4: timeStart = "15:23"  (valid)
        Task newTask3=newEditController.createTask("alda","15:23","02:00","1",dateStart,dateEnd);
        assert(newTask3!=null);
        assert(newTask3.getStartTime().toString().equals("Sat Nov 20 15:23:00 EET 2021"));

        //EC5: timeStart = "08:43"  (valid)
        Task newTask5=newEditController.createTask("alda","08:43","02:00","1",dateStart,dateEnd);
        assert(newTask5!=null);
        assert(newTask5.getStartTime().toString().equals("Sat Nov 20 08:43:00 EET 2021"));




    }

    @Test
    void createTaskBVA() throws Exception {
        //BVA
        //timeStart: string in formatul hh:ss, 00<=hh<=24, 00<=ss<60

        //EC1: timeStart = "00:-00"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","00:-00","02:00","1",dateStart,dateEnd);
        }catch (Exception e){
            assert(e.getMessage()=="formatul timpului de start nu este corect!");
        }

        //EC2: timeStart = "00:00"   (valid)
        Task newTask6=newEditController.createTask("alda","00:00","02:00","1",dateStart,dateEnd);
        assert(newTask6!=null);
        assert(newTask6.getStartTime().toString().equals("Sat Nov 20 00:00:00 EET 2021"));

        //EC3: timeStart = "00:01"   (valid)
        Task newTask7=newEditController.createTask("alda","00:01","02:00","1",dateStart,dateEnd);
        assert(newTask7!=null);
        assert(newTask7.getStartTime().toString().equals("Sat Nov 20 00:01:00 EET 2021"));

        // EC4: timeStart = "23:59"   (valid)
        Task newTask8=newEditController.createTask("alda","23:59","02:00","1",dateStart,dateEnd);
        assert(newTask8!=null);
        assert(newTask8.getStartTime().toString().equals("Sat Nov 20 23:59:00 EET 2021"));

        //EC5: timeStart = "24:00"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","24:00","02:00","1",dateStart,dateEnd);

        }catch (Exception e){
            assert(e.getMessage()=="formatul timpului de start nu este corect!");
        }

        //EC6: timeStart = "24:01"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","24:01","02:00","1",dateStart,dateEnd);

        }catch (Exception e){
            assert(e.getMessage()=="formatul timpului de start nu este corect!");
        }

        //EC7: timeStart = "24:02"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","24:02","02:00","1",dateStart,dateEnd);

        }catch (Exception e){
            assert(e.getMessage()=="formatul timpului de start nu este corect!");
        }
    }
    @RepeatedTest(5)

    @Disabled
    void testCreateTaskWithEndTime() throws Exception {
        try{ newEditController.checkBoxRepeatedIsSelected=true;
            Task newTask2=newEditController.createTask("la","08:00","02:00","1",dateStart,dateEnd);
        }catch (Exception e){
            assert(e.getMessage()=="formatul intervalului nu este corect!");
        }
    }
}