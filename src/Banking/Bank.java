package Banking;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Bank {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println(" MINI BANKING SYSTEM");
            System.out.println("1) Create Account");
            System.out.println("2) Login");
            System.out.println("3) Exit");

            System.out.print("Enter your choice: ");
            int ch = Integer.parseInt(br.readLine());

            if (ch == 1) {
                System.out.print("Enter Name: ");
                String name = br.readLine();

                System.out.print("Enter Passcode: ");
                int pass = Integer.parseInt(br.readLine());

                BankManagement.createAccount(name, pass);
            }
            else if (ch == 2) {
                System.out.print("Enter Name: ");
                String name = br.readLine();

                System.out.print("Enter Passcode: ");
                int pass = Integer.parseInt(br.readLine());

                BankManagement.loginAccount(name, pass);
            }
            else if (ch == 3) {
                System.out.println("Thank you for using the Mini Banking System!");
                break;
            }
            else {
                System.out.println("Invalid choice Try again.");
            }
        }
    }
}
