import javax.swing.*;
import java.awt.*;
import javax.swing.border.TitledBorder;
public class Instructions extends JFrame {
    // constructor
    public Instructions(int w, int h) {
        this.setSize(w,h);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        int xPos=w/30;
        int yPos=h/10;
        // panel
        JPanel inst=new JPanel();
        this.add(inst);
        inst.setLayout(null);
        inst.setBackground(Color.BLACK);
        inst.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), "INSTRUCTIONS", TitledBorder.CENTER, TitledBorder.TOP, new Font("Times New Roman", Font.PLAIN, 45), Color.WHITE));
        
        // back button to main menu
        JButton back=new JButton("←");
        inst.add(back);
        back.setBounds(xPos, yPos+545, 75, 25);
        back.setFont(new Font("Sans Serif", Font.BOLD, 25));
        back.setBackground(Color.WHITE);    
        
        // labels
        JLabel moveKeys=new JLabel();
        moveKeys.setIcon(new ImageIcon(getClass().getResource("wasd.png")));
        moveKeys.setBounds(w/25, yPos, 700, 170);
        inst.add(moveKeys);
        JLabel movement=new JLabel("Use the arrow keys or WASD to move your snake towards the food.");
        movement.setFont(new Font("Serif", Font.BOLD, 20));
        movement.setForeground(Color.WHITE);
        movement.setBounds(w/17, yPos+80, 700, 200);
        inst.add(movement);
        
        JLabel pause=new JLabel("Press Esc to pause.");
        pause.setIcon(new ImageIcon(getClass().getResource("esc.png")));
        pause.setFont(new Font("Serif", Font.BOLD, 20));
        pause.setForeground(Color.WHITE);
        pause.setBounds(w/3-20, yPos+160, 400, 170);
        inst.add(pause);
        
        JLabel dieGif1=new JLabel();
        dieGif1.setIcon(new ImageIcon(getClass().getResource("hitSnake.gif")));
        dieGif1.setBounds(w/20, yPos+290, 300, 225);
        inst.add(dieGif1);
        JLabel dieGif2=new JLabel();
        dieGif2.setIcon(new ImageIcon(getClass().getResource("hitWall.gif")));
        dieGif2.setBounds(w/2, yPos+290, 300, 225);
        inst.add(dieGif2);
        JLabel avoid=new JLabel("Avoid hitting yourself and the walls.");
        avoid.setFont(new Font("Serif", Font.BOLD, 20));
        avoid.setForeground(Color.WHITE);
        avoid.setBounds(w/4, yPos+510, 700, 40);
        inst.add(avoid);
        
        this.setVisible(true);
    }
    
    // testing
    public static void main(String[] args)
    {
        Instructions i=new Instructions(660,680);
    }
}
