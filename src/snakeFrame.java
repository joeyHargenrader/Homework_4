import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

class snakeFrame extends JFrame {

    private Snake game;
    private Menu main;
    private instructions ins;

    private int score, xVel, newX, yVel, newY, f;
    private boolean p = true;


    snakeFrame(int rows, int cols, int size, int numSnakes) {
        super("Snake");
        this.setSize(cols * size, (rows * size) + 20);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.main = new Menu(this.getWidth(), this.getHeight());
        this.ins = new instructions(this.getWidth(), this.getHeight());
        this.game = new Snake(rows, cols , size, numSnakes);
        this.add(this.main);

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
            if(game.checkFood()) {
                score++;
            }
            game.update(this.xVel, this.yVel);
            if(game.collision) {
                this.xVel = this.yVel = 0;
                vel.stop();
                if (f < 12) {
                    this.game.dead();
                    f++;
                } else if (f < 24) {
                    this.game.gameOver(score);
                    f++;
                }
            }
        });

        this.game.cont.addActionListener(e -> {
            this.game.cont();
            reset(numSnakes);

            update.stop();
            vel.stop();
            rainbow.stop();
            game.reset(numSnakes);

            swap(this.game, this.main);
        });

        this.main.play.addActionListener(e -> {
            swap(this.main, this.game);

            vel.start();
            update.start();
            rainbow.start();
            p = false;
        });

        this.main.settings.addActionListener(e -> {
        });

        this.main.instructions.addActionListener(e -> {
            swap(this.main, this.ins);
        });

        this.ins.back.addActionListener(e -> {
            swap(this.ins, this.main);
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

        reset(numSnakes);
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

    public void reset(int num) {
        this.newY = -1;
        this.newX = 0;
        this.f = 0;
        score = 0;
        this.game.reset(num);
    }

    public void toggleTimers() {
    }

    public void swap(Component a, Component b) {
        this.remove(a); this.add(b);
        this.revalidate(); this.repaint();
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
