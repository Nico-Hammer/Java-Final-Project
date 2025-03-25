import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GROK_RPS {
    JFrame frame = new JFrame();
    JLabel label = new JLabel("");
    int wins = 0;
    JLabel streak = new JLabel("Win Streak: " + wins);
    JButton rock = new JButton("Rock");
    JButton paper = new JButton("Paper");
    JButton scissors = new JButton("Scissors");
    ArrayList<StreakRecord> topStreaks = new ArrayList<>();
    JTable leaderboardTable;

    // Inner class to store username and streak
    private class StreakRecord {
        String username;
        int streak;
        public StreakRecord(String username, int streak) {
            this.username = username;
            this.streak = streak;
        }
    }

    public GROK_RPS() {
        // Set positions of GUI components
        label.setBounds(30, 80, 400, 20);
        streak.setBounds(155, 20, 110, 25);
        rock.setBounds(30, 50, 100, 20);
        paper.setBounds(140, 50, 100, 20);
        scissors.setBounds(250, 50, 100, 20);

        // Initialize JTable for leaderboard
        String[] columnNames = {"Username", "Streak"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        leaderboardTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBounds(30, 120, 300, 100);

        // Add components to frame
        frame.add(label);
        frame.add(streak);
        frame.add(rock);
        frame.add(paper);
        frame.add(scissors);
        frame.add(scrollPane);
        frame.setSize(450, 350); // Increased height to fit JTable
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Load top streaks from file
        loadTopStreaks();

        // Add action listeners for buttons
        rock.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String compChoice = initializeComputer();
                String userChoice = "Rock";
                GROK_RPS.this.PlayGame(userChoice, compChoice);
            }
        });
        paper.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String compChoice = initializeComputer();
                String userChoice = "Paper";
                GROK_RPS.this.PlayGame(userChoice, compChoice);
            }
        });
        scissors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String compChoice = initializeComputer();
                String userChoice = "Scissors";
                GROK_RPS.this.PlayGame(userChoice, compChoice);
            }
        });
    }

    public static void main(String[] args) {
        new GROK_RPS();
    }

    // Generate computer's random choice
    public static String initializeComputer() {
        ArrayList<String> choices = new ArrayList<>();
        choices.add("Rock");
        choices.add("Paper");
        choices.add("Scissors");
        return choices.get(new Random().nextInt(choices.size()));
    }

    // Main game logic
    public void PlayGame(String choice, String compChoice) {
        String outcome;
        if (choice.equals(compChoice)) {
            outcome = "tie";
        } else if ((choice.equals("Rock") && compChoice.equals("Scissors")) ||
                   (choice.equals("Paper") && compChoice.equals("Rock")) ||
                   (choice.equals("Scissors") && compChoice.equals("Paper"))) {
            outcome = "win";
        } else {
            outcome = "lose";
        }

        if (outcome.equals("win")) {
            wins++;
            streak.setText("Win Streak: " + wins);
            label.setText("You chose " + choice + " | Computer chose " + compChoice + " | You Win");
            
        } else {
            int streakEnded = wins;
            wins = 0;
            streak.setText("Win Streak: " + wins);
            if (streakEnded > 0) {
                // Check if the ended streak qualifies for top 5
                if (topStreaks.size() < 5 || streakEnded > topStreaks.get(topStreaks.size() - 1).streak) {
                    String username = JOptionPane.showInputDialog(frame, "New top 5 win streak! Enter your username:");
                    if (username != null && !username.trim().isEmpty()) {
                        topStreaks.add(new StreakRecord(username, streakEnded));
                        topStreaks.sort((a, b) -> Integer.compare(b.streak, a.streak));
                        if (topStreaks.size() > 5) {
                            topStreaks = new ArrayList<>(topStreaks.subList(0, 5));
                        }
                        saveTopStreaks();
                        updateLeaderboardTable();
                    }
                }
            }
            if (outcome.equals("lose")) {
                label.setText("You chose " + choice + " | Computer chose " + compChoice + " | You Lose");
            } else {
                label.setText("You chose " + choice + " | Computer chose " + compChoice + " | It's a Tie");
            }
        }
    }

    // Load top streaks from file
    private void loadTopStreaks() {
        topStreaks.clear();
        try {
            List<String> lines = Files.readAllLines(Paths.get("top_streaks.txt"));
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String username = parts[0];
                    int streak = Integer.parseInt(parts[1]);
                    topStreaks.add(new StreakRecord(username, streak));
                }
            }
        } catch (Exception e) {
            // File not found or error, start with empty list
        }
        topStreaks.sort((a, b) -> Integer.compare(b.streak, a.streak));
        if (topStreaks.size() > 5) {
            topStreaks = new ArrayList<>(topStreaks.subList(0, 5));
        }
        updateLeaderboardTable();
    }

    // Update JTable with current top streaks
    private void updateLeaderboardTable() {
        DefaultTableModel model = (DefaultTableModel) leaderboardTable.getModel();
        model.setRowCount(0);
        for (StreakRecord record : topStreaks) {
            model.addRow(new Object[]{record.username, record.streak});
        }
    }

    // Save top streaks to file
    private void saveTopStreaks() {
        try {
            List<String> lines = new ArrayList<>();
            for (StreakRecord record : topStreaks) {
                lines.add(record.username + ":" + record.streak);
            }
            Files.write(Paths.get("top_streaks.txt"), lines);
        } catch (Exception e) {
            // Handle IO errors silently for simplicity
        }
    }
}
