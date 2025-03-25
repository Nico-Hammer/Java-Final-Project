/* importing  java libraries to use arraylists, random selection, and the java swing GUI framework */
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
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
    RockPaperScissors(){
        /* set the location of the label and buttons */
        label.setBounds(30,80,350,20);
        streak.setBounds(155,20,110,25);
        rock.setBounds(30,50,100,20);
        paper.setBounds(140,50,100,20);
        scissors.setBounds(250,50,100,20);
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
        /* add the elements to the frame and set some settings of the frame */
        frame.add(label);frame.add(streak);
        frame.add(rock);frame.add(paper);frame.add(scissors);
        frame.setSize(400,300);
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
    /* function to handle the main game logic */
    public static void PlayGame(String choice,String CompChoice){
        /* switch case that compares the user choice to the computer choice and displays whether you won,lost,or tied */
        switch(choice){
            case "Rock":
                if(CompChoice == "Paper"){
                    wins = 0;
                    streak.setText("Win Streak: " + wins);
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | You Lose");
                }
                else if(CompChoice == "Scissors"){
                    wins += 1;
                    streak.setText("Win Streak: " + wins);
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | You Win");
                }
                else{
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | Its a Tie");
                }
                break;
            case "Paper":
                if(CompChoice == "Paper"){
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | Its a Tie");
                }
                else if(CompChoice == "Scissors"){
                    wins = 0;
                    streak.setText("Win Streak: " + wins);
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | You Lose");
                }
                else{
                    wins += 1;
                    streak.setText("Win Streak: " + wins);
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | You Win");
                }
                break;
            case "Scissors":
                if(CompChoice == "Paper"){
                    wins += 1;
                    streak.setText("Win Streak: " + wins);
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | You Win");
                }
                else if(CompChoice == "Scissors"){
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | Its a Tie");
                }
                else{
                    wins = 0;
                    streak.setText("Win Streak: " + wins);
                    label.setText("You chose " + choice + " | Computer chose " + CompChoice + " | You Lose");
                }
                break;
        }
    }
}