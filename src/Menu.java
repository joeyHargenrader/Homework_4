import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    JButton play, instructions, settings, highScores;
//    int[][] snakeLogoCords = {
//            {1, 2, 3, 4, 6, 10, 13, 14, 15, 18, 22, 24, 25, 26, 27, 28}, //Row 0
//            {0, 6, 10, 12, 16, 18, 21, 24}, //Row 1
//            {0, 6, 7, 10, 12, 16, 18, 20, 24}, //Row 2
//            {0, 1, 2, 3, 6, 8, 10, 12, 13, 14, 15, 16, 18, 19, 24, 25, 26, 27}, //Row 3
//            {4, 6, 9, 10, 12, 16, 18, 20, 24}, //Row 4
//            {4, 6, 10, 12, 16, 18, 21, 24}, //Row 5
//            {0, 1, 2, 3, 6, 10, 12, 16, 18, 22, 24, 25, 26, 27, 28} //Row 6
//    };
    int [][] snakeLogoCords = {
            {1, 2, 3, 6}, {0, 3, 6}, {0, 3, 6}, {0, 3, 6}, {0, 4, 5}, {}, //S
            {0, 1, 2, 3, 4, 5, 6}, {2}, {3}, {4}, {0, 1, 2, 3, 4, 5, 6}, {}, //N
            {1, 2, 3, 4, 5, 6}, {0, 3}, {0, 3}, {0, 3}, {1, 2, 3, 4, 5, 6}, {}, //A
            {0, 1, 2, 3, 4, 5, 6}, {3}, {2, 4}, {1, 5}, {0, 6}, {}, //K
            {0, 1, 2, 3, 4, 5, 6}, {0, 3, 6,}, {0, 3, 6}, {0, 3, 6}, {0, 6} //E
            };
    shapeItem[][] snakeLogo;
    int offsetY, offsetX, size, ani = 0, ind = 0, col = 0;
    // constructor
    public Menu(int w, int h) {
        this.size = w / 35;
        this.offsetY = ((((h / 2) / this.size) - 7) / 2) * this.size;
        this.offsetX = (((w / this.size) - 28) / 2) * this.size;
        int buttonWidth = 280, buttonHeight = 45;
        snakeLogo = new shapeItem[snakeLogoCords.length][];
        for (int r = 0; r < snakeLogoCords.length; r++) {
            snakeLogo[r] = new shapeItem[snakeLogoCords[r].length];
            for (int sn = 0; sn < snakeLogoCords[r].length; sn++) {
                int y = snakeLogoCords[r][sn] * this.size;
                snakeLogo[r][sn] = new shapeItem(new Rectangle(
                        (r * this.size) + this.offsetX,
                        y + this.offsetY,
                        this.size - 2,
                        this.size - 2));
            }
        }
        //creating components & setting positions
        int xPos = w / 2 - (buttonWidth / 2);
        int yPos = h / 2 - (buttonHeight / 2);
        this.setLayout(null);

        //JLabel bg=new JLabel("SNAKE"); // maybe changing later to icon
        //bg.setBounds(w/2 - 401/2, 60, 401, 100);

        play = new JButton("Play");
        play.setBounds(xPos, yPos, buttonWidth, buttonHeight);

        instructions = new JButton("Instructions");
        instructions.setBounds(xPos, yPos + 50, buttonWidth, buttonHeight);

        settings = new JButton("Settings");
        settings.setBounds(xPos, yPos + 100, buttonWidth, buttonHeight);

        highScores = new JButton("High Scores");
        highScores.setBounds(xPos, yPos + 150, buttonWidth, buttonHeight);

        // adding to JFrame
        //this.add(bg);
        this.add(play);
        this.add(instructions);
        this.add(settings);
        this.add(highScores);

        // editing colors and fonts
        this.setBackground(Color.BLACK);
//        bg.setFont(new Font("Serif", Font.PLAIN, 120));
//        bg.setForeground(Color.WHITE);
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

    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        for(shapeItem[] rows : snakeLogo) {
            for (shapeItem snake : rows) {
                g2.setColor(snake.getColor());
                g2.fill(snake.getShape());
            }
        }

    }

    public void wiggle() {
        int max = 10;
        int inc = 1;
        int curr = col < 29 ? col : 29;
        boolean test;
        for(shapeItem[] rows : snakeLogo) {
            for (shapeItem snake : rows) {
                snake.vel = snake.getDiff() == 0 ?
                        -inc : snake.getDiff() == max ? inc : snake.vel;
                snake.setRec(snake.getX(), snake.getY() + snake.vel, snake.getH());
            }
            if (curr == 0) {break;}
            curr--;
        }
        col++;
    }
}