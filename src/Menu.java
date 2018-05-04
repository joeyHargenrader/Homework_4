import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    JButton play, instructions, settings, highScores;
    int buttonWidth = 280;
    int buttonHieght = 45;
    // constructor
    public Menu(int w, int h)
    {
//        JFrame menu=new JFrame();
//        menu.setSize(760, 530);
//        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        menu.setVisible(true);
//        menu.setResizable(false);
        
        // creating components & setting positions
        int xPos = w/2 - (buttonWidth/2);
        int yPos = h/2 - (buttonHieght/2);
        this.setLayout(null);
        
        JLabel bg=new JLabel("SNAKE"); // maybe changing later to icon
        bg.setBounds(w/2 - 401/2, 60, 401, 100);
        
        play=new JButton("Play");
        play.setBounds(xPos, yPos, buttonWidth, buttonHieght);
        
        instructions=new JButton("Instructions");
        instructions.setBounds(xPos, yPos + 50, buttonWidth, buttonHieght);
        
        settings=new JButton("Settings");
        settings.setBounds(xPos, yPos + 100, buttonWidth, buttonHieght);
        
        highScores=new JButton("High Scores");
        highScores.setBounds(xPos, yPos + 150, buttonWidth, buttonHieght);
        
        // adding to JFrame
        this.add(bg);
        this.add(play);
        this.add(instructions);
        this.add(settings);
        this.add(highScores);
        
        // editing colors and fonts
        this.setBackground(Color.BLACK);
        bg.setFont(new Font("Serif", Font.PLAIN, 120));
        bg.setForeground(Color.WHITE);
        play.setBackground(Color.WHITE);
        play.setForeground(Color.BLUE);
        play.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        instructions.setBackground(Color.WHITE);
        instructions.setForeground(Color.BLUE);
        instructions.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        settings.setBackground(Color.WHITE);
        settings.setForeground(Color.BLUE);
        settings.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        highScores.setBackground(Color.WHITE);
        highScores.setForeground(Color.BLUE);
        highScores.setFont(new Font("Sans Serif", Font.PLAIN, 30));

        // button actions
//        play.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e) {
//                menu.dispose();
//                Grid game = new Grid(51, 51, 10, 3);
//                game.setVisible(true);
//            }
//        });
//        instructions.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e) {
//                //
//            }
//        });
//        settings.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e) {
//                //
//            }
//        });
//        highScores.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent e) {
//                //
//            }
//        });
    }
//    public static void main(String[] args)
//    {
//        Menu m=new Menu();
//    }
}