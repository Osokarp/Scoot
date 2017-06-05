import java.util.*;
public class Application{
    public static void main (String[]args){
        List<String[]> workdayHires = Workday.workDayController();
        Ticket.ticketController(workdayHires);
    }
}