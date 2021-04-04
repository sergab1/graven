package tasks.services;

public enum Exceptions {
    noTitle("introduceti titlul!"),
    titleMin2("titlul trebuie sa aiba minim doua caractere!"),
    titleMax60("titlul trebuie sa aiba maxim 60 de caractere!"),
    noStartTime("introduceti timpul de start!"),
    noEndTime("introduceti timpul de final!"),
    startTimeFormatBad("formatul timpului de start nu este corect!"),
    endTimeFormatBad("formatul timpului de final nu este corect!"),
    noInterval("introduceti intervalul!"),
    intervalFormatBad("formatul intervalului nu este corect!"),
    noTasks("nu exista task-uri in acest interval!"),
    startAfterEnd("timpul de start trebuie sa fie inaintea timpului de final!");


    public final String label;
    Exceptions(String label) {
        this.label=label;
    }
}
