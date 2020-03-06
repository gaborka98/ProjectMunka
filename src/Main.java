import Forms.List;
import Forms.Login;
import Forms.Register;


public class Main {
    private static Login login = new Login();
    private static Register register = new Register();
    private static List list = new List();

    public static void main(String[] args) {
        register.setVisible(false);
        list.setVisible(false);
        login.setVisible(true);

    }
}
