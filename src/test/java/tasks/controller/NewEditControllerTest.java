package tasks.controller;


import org.junit.jupiter.api.*;
import tasks.model.Task;
import tasks.services.Exceptions;
import tasks.services.MyException;


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

    //ECP
    //title: string de lungime cel putin 2 si maxim 60
    @Tag("invalid")
    @Test
    void createTaskECP1()  {
        //EC1: title = ""   (invalid)
        try{
        Task newTask1=newEditController.createTask("","08:00","02:00","1",dateStart,dateEnd);

        assert(false);

        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()== Exceptions.noTitle.toString());
        }

    }

    @Tag("invalid")
    @Test
    void createTaskECP2()  {
        //EC2: title = "a"   (invalid)
        try{
        Task newTask2=newEditController.createTask("a","08:00","02:00","1",dateStart,dateEnd);

        assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.titleMin2.toString());
        }

    }
    @Tag("valid")
    @Test
    void createTaskECP3() throws Exception {
        //EC3: title.length > 0, title="abm"  (valid)
        Task newTask=newEditController.createTask("abm","08:00","02:00","1",dateStart,dateEnd);

        assert(newTask!=null);
        assert(newTask.getTitle().equals("abm"));

    }

    @Tag("valid")
    @Test
    void createTaskECP4() throws Exception {
        //EC4: title.length > 0, title="xyz tuv"  (valid)
        Task newTask4=newEditController.createTask("xyz tuv","08:00","02:00","1",dateStart,dateEnd);

        assert(newTask4!=null);
        assert(newTask4.getTitle().equals("xyz tuv"));

    }

    @Tag("invalid")
    @Test
    void createTaskECP5() {
        //EC5: title.length=80, title="aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbb"  (invalid)
        try{
        Task newTask4=newEditController.createTask("aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbb","08:00","02:00","1",dateStart,dateEnd);

        assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.titleMax60.toString());
        }

    }

    //ECP
    //timeStart: string in formatul hh:ss, 00<=hh<=23, 00<=ss<59

    @Tag("invalid")
    @Test
    void createTaskECP6() {
        //EC6: timeStart = ""   (invalid)
        try{
        Task newTask10=newEditController.createTask("alda","","02:00","1",dateStart,dateEnd);

        assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.noStartTime.toString());
        }

    }

    @Tag("invalid")
    @Test
    void createTaskECP7() {
        //EC7: timeStart = "15"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","15","02:00","1",dateStart,dateEnd);

            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.startTimeFormatBad.toString());
        }
    }

    @Tag("invalid")
    @Test
    void createTaskECP8()  {
        //EC8: timeStart = "1a:k2"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","1a:k2","02:00","1",dateStart,dateEnd);

            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.startTimeFormatBad.toString());
        }
    }

    @Tag("valid")
    @Test
    void createTaskECP9() throws Exception {
        //EC9: timeStart = "15:23"  (valid)
        Task newTask3=newEditController.createTask("alda","15:23","02:00","1",dateStart,dateEnd);

        assert(newTask3!=null);
        assert(newTask3.getStartTime().toString().equals("Sat Nov 20 15:23:00 EET 2021"));
    }

    @Tag("valid")
    @Test
    void createTaskECP10() throws Exception {
        //EC10: timeStart = "08:43"  (valid)
        Task newTask5=newEditController.createTask("alda","08:43","02:00","1",dateStart,dateEnd);

        assert(newTask5!=null);
        assert(newTask5.getStartTime().toString().equals("Sat Nov 20 08:43:00 EET 2021"));
    }


    //BVA
    //timeStart: string in formatul hh:ss, 00<=hh<=24, 00<=ss<60

    @Tag("invalid")
    @Test
    void createTaskBVA1()  {
        //EC1: timeStart = "00:-00"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","00:-00","02:00","1",dateStart,dateEnd);

            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.startTimeFormatBad.toString());
        }
    }

    @Tag("valid")
    @Test
    void createTaskBVA2() throws Exception {
        //EC2: timeStart = "00:00"   (valid)
        Task newTask6=newEditController.createTask("alda","00:00","02:00","1",dateStart,dateEnd);

        assert(newTask6!=null);
        assert(newTask6.getStartTime().toString().equals("Sat Nov 20 00:00:00 EET 2021"));

    }

    @Tag("valid")
    @Test
    void createTaskBVA3() throws Exception {
        //EC3: timeStart = "00:01"   (valid)
        Task newTask7=newEditController.createTask("alda","00:01","02:00","1",dateStart,dateEnd);

        assert(newTask7!=null);
        assert(newTask7.getStartTime().toString().equals("Sat Nov 20 00:01:00 EET 2021"));

    }

    @Tag("valid")
    @Test
    void createTaskBVA4() throws Exception {
        // EC4: timeStart = "23:59"   (valid)
        Task newTask8=newEditController.createTask("alda","23:59","02:00","1",dateStart,dateEnd);

        assert(newTask8!=null);
        assert(newTask8.getStartTime().toString().equals("Sat Nov 20 23:59:00 EET 2021"));

    }

    @Tag("invalid")
    @Test
    void createTaskBVA5() {
        //EC5: timeStart = "24:00"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","24:00","02:00","1",dateStart,dateEnd);

            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.startTimeFormatBad.toString());
        }
    }

    @Tag("invalid")
    @Test
    void createTaskBVA6() {
        //EC6: timeStart = "24:01"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","24:01","02:00","1",dateStart,dateEnd);

            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.startTimeFormatBad.toString());
        }
    }

    @Tag("invalid")
    @Test
    void createTaskBVA7()  {
        //EC7: timeStart = "24:02"   (invalid)
        try{
            Task newTask2=newEditController.createTask("alda","24:02","02:00","1",dateStart,dateEnd);

            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.startTimeFormatBad.toString());
        }
    }

    //BVA
    //title: string cu lungime minim 2 si maxim 60
    @Tag("invalid")
    @Test
    void createTaskBVA8() {
        //EC8: title = "k"   (invalid)
        try{
        Task newTask2=newEditController.createTask("k","23:00","02:00","1",dateStart,dateEnd);

        assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.titleMin2.toString());
        }
    }

    @Tag("valid")
    @Test
    void createTaskBVA9() throws Exception {
        //EC9: title = "al"   (valid)
        Task newTask2=newEditController.createTask("al","23:00","02:00","1",dateStart,dateEnd);

        assert(newTask2!=null);
        assert(newTask2.getTitle().equals("al"));
    }

    @Tag("valid")
    @Test
    void createTaskBVA10() throws Exception {
        //EC10: title = "alb"   (valid)

        Task newTask2=newEditController.createTask("alb","23:00","02:00","1",dateStart,dateEnd);

        assert(newTask2!=null);
        assert(newTask2.getTitle().equals("alb"));
    }

    @RepeatedTest(3)
    @Tag("valid")
    void createTaskBVA11() throws Exception {
        //EC11: title size=60, title = "aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbb"   (valid)
        Task newTask4=newEditController.createTask("aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbb","08:00","02:00","1",dateStart,dateEnd);

        assert(newTask4!=null);
        assert(newTask4.getTitle().equals("aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbb"));
    }


    @DisplayName("createTaskBVA12_NEW")
    @Tag("invalid")
    @Test
    void createTaskBVA12()  {
        //EC12: title size=61, title = "aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbc"   (invalid)
        try{
        Task newTask4=newEditController.createTask("aaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbaaaaaaaaaabbbbbbbbbbc","08:00","02:00","1",dateStart,dateEnd);

        assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()==Exceptions.titleMax60.toString());
        }
    }




    @Disabled
    @Test
    void testCreateTaskWithEndTime()  {
        try{ newEditController.checkBoxRepeatedIsSelected=true;
            Task newTask2=newEditController.createTask("la","08:00","02:00","1",dateStart,dateEnd);

            assert(false);
        }catch (Exception e){
            assert e.getClass()==MyException.class;
            assert(e.getMessage()=="formatul intervalului nu este corect!");
        }
    }
}