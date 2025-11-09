package Banking;

import java.io.*;
import java.sql.*;

public class BankManagement {

    static Connection con = DBConnection.getConnection();

   
    private static final int NULL = 0;

    // CREATE ACCOUNT 
    public static boolean createAccount(String name, int passCode) {
        if (name.isEmpty() || passCode == NULL) {
            System.out.println("All fields are required!");
            return false;
        }

        try {
            String sql = "INSERT INTO customer(cname, balance, pass_code) VALUES (?, 1000, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, passCode);

            int rows = ps.executeUpdate();
            if (rows == 1) {
                System.out.println("Account created successfully! You can now login.");
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username is already in Use Try another one.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // LOGIN ACCOUNT
    public static boolean loginAccount(String name, int passCode) {
        if (name.isEmpty() || passCode == NULL) {
            System.out.println("All fields are required!");
            return false;
        }

        try {
            String sql = "SELECT * FROM customer WHERE cname = ? AND pass_code = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, passCode);
            ResultSet rs = ps.executeQuery();

            BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

            if (rs.next()) {
                int senderAc = rs.getInt("ac_no");
                int ch;

                while (true) {
                    System.out.println("\nHello, " + rs.getString("cname") + "! What would you like to do?");
                    System.out.println("1) Transfer Money");
                    System.out.println("2) View Balance");
                    System.out.println("3) Logout");
                    System.out.print("Enter Choice: ");
                    ch = Integer.parseInt(sc.readLine());

                    if (ch == 1) {
                        System.out.print("Enter Receiver A/c No: ");
                        int receiverAc = Integer.parseInt(sc.readLine());
                        System.out.print("Enter Amount: ");
                        int amt = Integer.parseInt(sc.readLine());

                        if (transferMoney(senderAc, receiverAc, amt)) {
                            System.out.println("Transaction successful!");
                        } else {
                            System.out.println("Transaction failed! Please try again.");
                        }

                    } else if (ch == 2) {
                        getBalance(senderAc);

                    } else if (ch == 3) {
                        System.out.println("Logged out successfully. Returning to main menu.");
                        break;

                    } else {
                        System.out.println("Invalid choice! Try again.");
                    }
                }                                                                           
                return true;

            } else {
                System.out.println("Invalid username or password!");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //TRANSFER MONEY 
    public static boolean transferMoney(int senderAc, int receiverAc, int amount) {
        if (amount <= 0) {
            System.out.println("Amount must be greater than zero!");
            return false;
        }

        try {
            con.setAutoCommit(false); // Start transaction

            // Check sender balance
            PreparedStatement ps1 = con.prepareStatement("SELECT balance FROM customer WHERE ac_no = ?");
            ps1.setInt(1, senderAc);
            ResultSet rs1 = ps1.executeQuery();

            if (!rs1.next()) {
                System.out.println("Sender account not found!");
                con.rollback();
                return false;
            }

            int senderBalance = rs1.getInt("balance");
            if (senderBalance < amount) {
                System.out.println("Insufficient balance!");
                con.rollback();
                return false;
            }

            // Deduct from sender
            PreparedStatement ps2 = con.prepareStatement("UPDATE customer SET balance = balance - ? WHERE ac_no = ?");
            ps2.setInt(1, amount);
            ps2.setInt(2, senderAc);
            ps2.executeUpdate();

            // Add to receiver
            PreparedStatement ps3 = con.prepareStatement("UPDATE customer SET balance = balance + ? WHERE ac_no = ?");
            ps3.setInt(1, amount);
            ps3.setInt(2, receiverAc);
            int updated = ps3.executeUpdate();

            if (updated == 0) {
                System.out.println("Receiver account not found!");
                con.rollback();
                return false;
            }

            con.commit(); // Finalize transaction
            return true;

        } catch (Exception e) {
            try {
                con.rollback(); // Undo if any step fails
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    // GET BALANCE 
    public static void getBalance(int ac_no) {
        try {
            String sql = "SELECT cname, balance FROM customer WHERE ac_no = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, ac_no);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Account Holder: " + rs.getString("cname"));
                System.out.println("Current Balance: £" + rs.getInt("balance"));
            } else {
                System.out.println("Account not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
