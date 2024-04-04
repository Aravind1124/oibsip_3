import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class User {
    private String userId;
    private String pin;
    private double balance;
    private List<Transaction> transactions;

    public User(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

public class ATM extends JFrame {
    private JTextField userIdField;
    private JPasswordField pinField;
    private JLabel messageLabel;
    private User currentUser;

    public ATM() {
        setTitle("ATM");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel userIdLabel = new JLabel("User ID:");
        userIdField = new JTextField(10); // Set preferred width
        JLabel pinLabel = new JLabel("PIN:");
        pinField = new JPasswordField(10); // Set preferred width

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());
        loginButton.setPreferredSize(new Dimension(100, 30)); // Set preferred size
        loginButton.setBackground(Color.decode("#87CEEB")); // Set sky blue color

        messageLabel = new JLabel();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(pinLabel, gbc);
        gbc.gridx = 1;
        panel.add(pinField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        gbc.gridy = 3;
        panel.add(messageLabel, gbc);

        add(panel);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userId = userIdField.getText();
            String pin = new String(pinField.getPassword());

            User user = getUserById(userId);

            if (user != null && user.getPin().equals(pin)) {
                currentUser = user;
                openMainMenu();
                clearFields();
                messageLabel.setText("");
            } else {
                messageLabel.setText("Invalid user ID or PIN. Please try again.");
            }
        }
    }

    private User getUserById(String userId) {
        // Sample implementation, replace with actual user retrieval logic
        if ("user1".equals(userId)) {
            return new User("user1", "1234", 1000);
        } else if ("user2".equals(userId)) {
            return new User("user2", "5678", 2000);
        }
        return null;
    }

    private void openMainMenu() {
        JFrame mainMenuFrame = new JFrame("Main Menu");
        mainMenuFrame.setSize(400, 300);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainMenuPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JButton transactionHistoryButton = new JButton("Transaction History");
        transactionHistoryButton.addActionListener(e -> displayTransactionHistory());
        transactionHistoryButton.setPreferredSize(new Dimension(150, 30)); // Set preferred size
        transactionHistoryButton.setBackground(Color.decode("#87CEEB")); // Set sky blue color

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(e -> withdraw());
        withdrawButton.setPreferredSize(new Dimension(150, 30)); // Set preferred size
        withdrawButton.setBackground(Color.decode("#87CEEB")); // Set sky blue color

        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(e -> deposit());
        depositButton.setPreferredSize(new Dimension(150, 30)); // Set preferred size
        depositButton.setBackground(Color.decode("#87CEEB")); // Set sky blue color

        JButton transferButton = new JButton("Transfer");
        transferButton.addActionListener(e -> transfer());
        transferButton.setPreferredSize(new Dimension(150, 30)); // Set preferred size
        transferButton.setBackground(Color.decode("#87CEEB")); // Set sky blue color

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> mainMenuFrame.dispose());
        quitButton.setPreferredSize(new Dimension(150, 30)); // Set preferred size
        quitButton.setBackground(Color.decode("#87CEEB")); // Set sky blue color

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainMenuPanel.add(transactionHistoryButton, gbc);
        gbc.gridy = 1;
        mainMenuPanel.add(withdrawButton, gbc);
        gbc.gridy = 2;
        mainMenuPanel.add(depositButton, gbc);
        gbc.gridy = 3;
        mainMenuPanel.add(transferButton, gbc);
        gbc.gridy = 4;
        mainMenuPanel.add(quitButton, gbc);

        mainMenuFrame.add(mainMenuPanel);
        mainMenuFrame.setVisible(true);
    }

    private void displayTransactionHistory() {
        StringBuilder transactionHistory = new StringBuilder("<html><body><b>Transaction History:</b><br>");
        for (Transaction transaction : currentUser.getTransactions()) {
            transactionHistory.append(transaction.getType()).append(": $").append(transaction.getAmount()).append("<br>");
        }
        transactionHistory.append("</body></html>");
        JOptionPane.showMessageDialog(this, transactionHistory.toString(), "Transaction History", JOptionPane.INFORMATION_MESSAGE);
    }

    private void withdraw() {
        String amountString = JOptionPane.showInputDialog(this, "Enter amount to withdraw:", "Withdraw", JOptionPane.QUESTION_MESSAGE);
        try {
            double amount = Double.parseDouble(amountString);
            if (amount > 0 && amount <= currentUser.getBalance()) {
                currentUser.setBalance(currentUser.getBalance() - amount);
                currentUser.addTransaction(new Transaction("Withdraw", amount));
                JOptionPane.showMessageDialog(this, "$" + amount + " withdrawn successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid amount or insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deposit() {
        String amountString = JOptionPane.showInputDialog(this, "Enter amount to deposit:", "Deposit", JOptionPane.QUESTION_MESSAGE);
        try {
            double amount = Double.parseDouble(amountString);
            if (amount > 0) {
                currentUser.setBalance(currentUser.getBalance() + amount);
                currentUser.addTransaction(new Transaction("Deposit", amount));
                JOptionPane.showMessageDialog(this, "$" + amount + " deposited successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void transfer() {
        String recipientId = JOptionPane.showInputDialog(this, "Enter recipient's user ID:", "Transfer", JOptionPane.QUESTION_MESSAGE);
        if (recipientId == null) {
            return; // User canceled
        }

        User recipient = getUserById(recipientId);

        if (recipient == null) {
            JOptionPane.showMessageDialog(this, "Recipient user not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String amountString = JOptionPane.showInputDialog(this, "Enter amount to transfer:", "Transfer", JOptionPane.QUESTION_MESSAGE);
        try {
            double amount = Double.parseDouble(amountString);
            if (amount > 0 && amount <= currentUser.getBalance()) {
                currentUser.setBalance(currentUser.getBalance() - amount);
                recipient.setBalance(recipient.getBalance() + amount);
                currentUser.addTransaction(new Transaction("Transfer to " + recipientId, amount));
                recipient.addTransaction(new Transaction("Transfer from " + currentUser.getUserId(), amount));
                JOptionPane.showMessageDialog(this, "$" + amount + " transferred to " + recipientId + " successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid amount or insufficient funds.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        userIdField.setText("");
        pinField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ATM::new);
    }
}
