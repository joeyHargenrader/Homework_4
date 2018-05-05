import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class snakeFrame extends JFrame {

    private Snake game;
    private Menu main;
    private Instructions ins;

    private int score = 0, xVel = 0, newX = 0, yVel = 0, newY = -1;
    private boolean p = true;


    snakeFrame(int rows, int cols, int size, int numSnakes) {
        super("Snake");
        this.setSize(cols * size, (rows * size) + 20);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //JMenuBar settings = new JMenuBar();
        this.main = new Menu(this.getWidth(), this.getHeight());
        this.ins = new Instructions(this.getWidth(), this.getHeight());
        this.game = new Snake(rows, cols , size, numSnakes);
        this.add(this.main, BorderLayout.CENTER);

        int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
        Keybinds[] keybinds = new Keybinds[]{
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
        for (Keybinds keybind : keybinds) {
            this.game.getInputMap(IFW).put(KeyStroke.getKeyStroke(keybind.c, 0), keybind.name);
            if (keybind.n) {
                this.game.getActionMap().put(keybind.name, keybind.action);
            }
        }
        Timer animation = new Timer(60, null);
        animation.addActionListener(e -> {
            this.main.wiggle();
            this.main.repaint();
        });
        animation.start();
        Timer rainbow = new Timer(100, null);
        rainbow.addActionListener(e -> {
            game.ind++;
            if(game.ind >= game.rainbow.length){game.ind = 0;}
            game.repaint();
        });

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
                //valScore.setText("<html><body><center>SCORE<br>" + score + "</center></body></html>");
            }
            game.update(this.xVel, this.yVel);
            if(game.collision){
                vel.stop();
                update.stop();
                this.main.ind = 0;
                animation.start();
                //valScore.setText("<html><body><center>SCORE<br>" + score + "<br>GAME OVER</center></body></html>");
                System.out.println("Game Over");


                update.stop(); vel.stop();
                this.newY = -1; this.newX = 0;
                score = 0; //valScore.setText("<html><body><center>SCORE<br>" + score + "</center></body></html>");
                game.reset(numSnakes); game.repaint();


                this.remove(this.game);
                this.add(this.main);
                this.revalidate(); this.repaint();
            }
        });

        this.main.play.addActionListener(e -> {
            this.remove(this.main);
            this.add(this.game);
            this.revalidate(); this.repaint();


            vel.start();
            update.start();
            rainbow.start();
            p = false;
        });

        this.main.settings.addActionListener(e -> {
        });

        this.main.instructions.addActionListener(e -> {
            this.remove(this.main);
            this.add(this.ins);
            this.revalidate(); this.repaint();
        });

        this.ins.back.addActionListener(e -> {
            this.remove(this.ins);
            this.add(this.main);
            this.revalidate(); this.repaint();
        });



        this.game.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                main.play.doClick();
            }
        });

        this.game.getActionMap().put("ESC", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(p) {vel.start();update.start(); p = false;}
                else {vel.stop();update.stop(); p = true;}
            }
        });

//        addSnake.addActionListener(e -> {
//            score++;
//            game.addToSnake();
//            game.repaint();
//        });
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
            if (!p) {
                if (Math.abs(xVel - this.tempX) != 2 && Math.abs(yVel - this.tempY) != 2) {
                    newX = this.tempX;
                    newY = this.tempY;
                }
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
        snakeFrame main = new snakeFrame(55, 55, 12, 3);
        main.setVisible(true);
    }
}
