import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;

class Grid extends JFrame {

    private Snake game;
    private int score = 0, xVel = 0, newX = 0, yVel = 0, newY = -1;
    boolean p = false;
    private Keybinds[] keybinds = new Keybinds[] {
            new Keybinds(KeyEvent.VK_UP, "UP", new dirAction(0, -1)),
            new Keybinds(KeyEvent.VK_W, "UP"),
            new Keybinds(KeyEvent.VK_RIGHT, "RIGHT", new dirAction(1, 0)),
            new Keybinds(KeyEvent.VK_D, "RIGHT"),
            new Keybinds(KeyEvent.VK_DOWN, "DOWN", new dirAction(0, 1)),
            new Keybinds(KeyEvent.VK_S, "DOWN"),
            new Keybinds(KeyEvent.VK_LEFT, "LEFT", new dirAction(-1, 0)),
            new Keybinds(KeyEvent.VK_A, "LEFT"),
            new Keybinds(KeyEvent.VK_ENTER, "ENTER"),
            new Keybinds(KeyEvent.VK_ESCAPE, "ESC")
    };


    Grid(int rows, int cols, int size, int numSnakes) {
        super("Snake");
        this.setSize(cols * size + 250, (rows * size) + 20);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //JMenuBar settings = new JMenuBar();
        JPanel panel;
        this.game = new Snake(rows, cols , size, numSnakes);
        this.game.setBackground(Color.BLACK);
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(250, rows * size));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //c.fill = GridBagConstraints.BOTH;

        JButton start = new JButton("Start");
        c.gridx = 0; c.gridy = 0; c.gridwidth = 3;
        panel.add(start, c);

        JButton pause = new JButton("Pause");
        c.gridx = 3; c.gridy = 0; c.gridwidth = 3;
        panel.add(pause, c);

        JButton reset = new JButton("Reset");
        c.gridx = 2; c.gridy = 1; c.gridwidth = 2;
        panel.add(reset, c);

        JButton addSnake = new JButton("Add Snake");
        c.gridx = 2; c.gridy = 5;
        panel.add(addSnake, c);

        c.gridx = 0; c.gridy = 6; c.gridwidth = 6;
        JLabel valScore = new JLabel("<html><body><center>SCORE<br>" + score + "</center></body></html>");
        valScore.setFont(new Font("TimesRoman", Font.BOLD, 30));
        c.gridx = 0; c.gridy = 7; c.gridwidth = 6;
        panel.add(valScore, c);

        int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
        for(int kb = 0; kb < keybinds.length; kb++){
            this.game.getInputMap(IFW).put(KeyStroke.getKeyStroke(keybinds[kb].c,0), keybinds[kb].name);
            if(keybinds[kb].n) {
                this.game.getActionMap().put(keybinds[kb].name, keybinds[kb].action);
            }
        }
        this.game.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start.doClick();
            }
        });
        this.game.getActionMap().put("ESC", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pause.doClick();
            }
        });

        this.add(this.game);
        this.add(panel, BorderLayout.EAST);

//        Timer rainbow = new Timer(80, null);
//        rainbow.addActionListener(e -> {
//            game.ind--;
//            if(game.ind < 0){game.ind = game.rainbow.length;}
//            game.repaint();
//        });

        Timer vel = new Timer(10, null);
        vel.addActionListener(e -> {
            if(this.xVel != this.newX || this.yVel != this.newY) {
                if (Math.abs(this.newX - game.xVel) != 2 && Math.abs(this.newY - game.yVel) != 2) {
                    this.xVel = this.newX;
                    this.yVel = this.newY;
                }
            }
        });

        Timer update = new Timer(80, null);
        update.addActionListener(e -> {
            game.repaint();
            if(game.checkFood()) {
                score++;
                valScore.setText("<html><body><center>SCORE<br>" + score + "</center></body></html>");
            }
            game.update(this.xVel, this.yVel);
            if(game.collision){
                update.stop();
                vel.stop();
                valScore.setText("<html><body><center>SCORE<br>" + score + "<br>GAME OVER</center></body></html>");
                System.out.println("Game Over");
            }
        });

        start.addActionListener(e -> {
            vel.start();
            update.start();
            //rainbow.start();
            p = false;
        });
        reset.addActionListener(e -> {
            update.stop(); vel.stop();
            this.newY = -1; this.newX = 0;
            score = 0; valScore.setText("<html><body><center>SCORE<br>" + score + "</center></body></html>");
            game.reset(numSnakes); game.repaint();
        });
        pause.addActionListener(e -> {
            if(p) {vel.start();update.start(); p = false;}
            else {vel.stop();update.stop(); p = true;}
        });

        addSnake.addActionListener(e -> {
            score++;
            game.addToSnake();
            game.repaint();
        });
    }

    private class dirAction extends AbstractAction {
        int tempX;
        int tempY;
        dirAction(int x, int y) {
            this.tempX = x;
            this.tempY = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
                if (Math.abs(xVel - this.tempX) != 2 &&
                    Math.abs(yVel - this.tempY) != 2) {
                    newX = this.tempX;
                    newY = this.tempY;
                }
        }
    }

    public class Keybinds {
        int c; String name; dirAction action;
        boolean n = false;
        Keybinds(int c, String n, dirAction action) {
            this.c = c;
            this.name = n;
            this.action = action;
            this.n = true;
        }
        Keybinds(int c, String n) {
            this.c = c;
            this.name = n;
        }

    }

    public static void main(String[] args) {
//        try {
//            FileWriter fstream = new FileWriter("HighScore");
//            BufferedWriter out = new BufferedWriter(fstream);
//            out.write("hello");
//            out.close();
//        }catch (Exception e){
//            System.err.println("Error: " + e.getMessage());
//        }
        Grid main = new Grid(51, 51, 10, 3);
        main.setVisible(true);
    }
}
