/* importing  java libraries to use arraylists, random selection, java swing GUI framework, and file operations */
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.swing.table.DefaultTableModel;
/* class to handle creating the window and the variables used for the game logic */
public class RockPaperScissors{
    JFrame frame = new JFrame(); // create a JFrame to show the GUI elements
    static JLabel label = new JLabel(""); // create a JLabel to display the results of the match
    static int wins = 0; // int to store the actual amount of wins in the streak
    static JLabel streak = new JLabel("Win Streak: " + wins); // create a JLabel to display the win streak of the user
    /* create the JButtons for user to click which choose an option */
    JButton rock = new JButton("Rock");
    JButton paper = new JButton("Paper");
    JButton scissors = new JButton("Scissors");
    static String choice = ""; // initialize a string to hold the user choice
    ArrayList<StreakRecord> topStreaks = new ArrayList<>(); // initialize an arraylist to hold the top 5 win streaks
    JTable leaderboardTable; // initialize a JTable to show to top 5 win streaks
    // Inner class to store username and streak
    private class StreakRecord {
        String username;
        int streak;
        public StreakRecord(String username, int streak) {
            this.username = username;
            this.streak = streak;
        }
    }
    RockPaperScissors(){
        /* set the location of the label and buttons */
        label.setBounds(30,80,400,20);
        streak.setBounds(155,20,110,25);
        rock.setBounds(30,50,100,20);
        paper.setBounds(140,50,100,20);
        scissors.setBounds(250,50,100,20);
        // Initialize JTable for leaderboard
        String[] columnNames = {"Username", "Streak"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        leaderboardTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBounds(30, 120, 300, 100);
        // Load top streaks from file
        loadTopStreaks();
        // add the listener to the jbuttons to handle the "pressed" event
        rock.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String CompChoice = initializeComputer(); // get and store the computers choice
                choice = "Rock"; // set the user choice to what they selected
                PlayGame(choice,CompChoice); // play the game with the user and computer choices
            }
        });
        paper.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String CompChoice = initializeComputer();
                choice = "Paper";
                PlayGame(choice,CompChoice);
            }
        });
        scissors.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String CompChoice = initializeComputer();
                choice = "Scissors";
                PlayGame(choice,CompChoice);
            }
        });
        leaderboardTable.setDefaultEditor(Object.class, null); // make the JTable not editable
        /* add the elements to the frame and set some settings of the frame */
        frame.add(label);frame.add(streak);
        frame.add(scrollPane);
        frame.add(rock);frame.add(paper);frame.add(scissors);
        frame.setSize(450,300);
        frame.setLayout(null);
        frame.setVisible(true);
}
    public static void main(String[] args){
        new RockPaperScissors(); // create a new instance of the RockPaperScissors class
    }
    /* function that initializes the computer and returns the computer choice */
    public static String initializeComputer(){
        ArrayList<String> choices = new ArrayList<String>(); // initialize an arraylist to store the available choices
        /* add the choices to the list */
        choices.add("Rock");
        choices.add("Paper");
        choices.add("Scissors");
        return choices.get(new Random().nextInt(choices.size())); // return the computers random choice
    }
    // Main game logic
    public void PlayGame(String choice, String compChoice) {
        String outcome; // string to hold the outcome of the match
        /* compare the users choice to the computers choice and set the outcome accordingly */
        if (choice == compChoice) {
            outcome = "tie";
        } 
        else if ((choice == "Rock" && compChoice == "Scissors") || (choice == "Paper" && compChoice == "Rock") || (choice == "Scissors" && compChoice == "Paper")) {
            outcome = "win";
        } 
        else {
            outcome = "lose";
        }
        if (outcome == "win") {
            wins++; // increment wins on a win
            streak.setText("Win Streak: " + wins); // update the streak label text to show the new win streak
            label.setText("You chose " + choice + " | Computer chose " + compChoice + " | You Win"); // show what the user and computer chose and the outcome of the match
        } 
        else {
            int streakEnded = wins; // save the streak number that the streak was ended on
            wins = 0;
            streak.setText("Win Streak: " + wins); // reset the win streak label to 0
            if (streakEnded > 0) {
                /* Check if the ended streak qualifies for top 5, or the topStreaks list is less than 5 elements big */
                if (topStreaks.size() < 5 || streakEnded > topStreaks.get(topStreaks.size() - 1).streak) {
                    String username = JOptionPane.showInputDialog(frame, "New top 5 win streak! Enter your username:"); // get  the username from the user using a new window
                    /* add new username and streak to the topStreaks array if the username is not NULL or an empty string */
                    if (username != null && !username.trim().isEmpty()) {
                        topStreaks.add(new StreakRecord(username, streakEnded)); // add new record to the arraylist
                        topStreaks.sort((a, b) -> Integer.compare(b.streak, a.streak)); // sort the arraylist in descending order
                        /* if the arraylist is bigger than 5 items create a new list with only the top 5 */
                        if (topStreaks.size() > 5) {
                            topStreaks = new ArrayList<>(topStreaks.subList(0, 5));
                        }
                        saveTopStreaks(); // call the function to save the new score to file
                        updateLeaderboardTable(); // call the function to update the JTable
                    }
                }
            }
            if (outcome == "lose") {
                label.setText("You chose " + choice + " | Computer chose " + compChoice + " | You Lose");
            } 
            else {
                label.setText("You chose " + choice + " | Computer chose " + compChoice + " | It's a Tie");
            }
        }
    }
    // Save top streaks to file
    private void saveTopStreaks() {
        /* try to add the streaks to the file */
        try {
            List<String> lines = new ArrayList<>(); // initialize a new list of strings to hold the streaks
            /* take every element in the topStreaks arraylist and add it to the list created above */
            for (StreakRecord record : topStreaks) {
                lines.add(record.username + ":" + record.streak);
            }
            Files.write(Paths.get("top_streaks.txt"), lines); // write the new list to the file to save the streaks
        } 
        catch (Exception e) {
            // Handle IO errors silently for simplicity
        }
    }
    // Load top streaks from file
    private void loadTopStreaks() {
        topStreaks.clear(); // clear the JTable
        /* try to read the file */
        try {
            List<String> lines = Files.readAllLines(Paths.get("top_streaks.txt")); // get the streaks from the file and store them in a list of lines
            /* go through each line and separate the name and streak by a : */
            for (String line : lines) {
                String[] parts = line.split(":");
                /* if the string is in the correct form "Name:streak" set the user name to the first part of the string and the string to the second part then add to the arraylist of win streaks */
                if (parts.length == 2) {
                    String username = parts[0];
                    int streak = Integer.parseInt(parts[1]);
                    topStreaks.add(new StreakRecord(username, streak));
                }
            }
        } 
        catch (Exception e) {
            // File not found or error, start with empty list
        }
        topStreaks.sort((a, b) -> Integer.compare(b.streak, a.streak)); // sort the arraylist in descending order
        /* if there are more than 5 scores, only display the top 5 by creating a new list */
        if (topStreaks.size() > 5) {
            topStreaks = new ArrayList<>(topStreaks.subList(0, 5));
        }
        updateLeaderboardTable(); // update the leaderboard table
    }
    // Update JTable with current top streaks
    private void updateLeaderboardTable() {
        DefaultTableModel model = (DefaultTableModel) leaderboardTable.getModel();
        model.setRowCount(0); // initialize row count to 0
        /* add a new row with the username and streak for every element in the topStreaks arraylist */
        for (StreakRecord record : topStreaks) {
            model.addRow(new Object[]{record.username, record.streak});
        }
    }
}