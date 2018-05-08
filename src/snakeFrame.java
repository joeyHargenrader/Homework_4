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
        //Initialize the frame, and screens
        super("Snake");
        this.setSize(cols * size, (rows * size) + 20);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.main = new Menu(this.getWidth(), this.getHeight());
        this.ins = new instructions(this.getWidth(), this.getHeight());
        this.game = new Snake(rows, cols , size, numSnakes);

        //Add main screen
        this.add(this.main);

        //Define keybinds
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
                new Keybinds(KeyEvent.VK_ENTER, "ESC"),
                new Keybinds(KeyEvent.VK_ESCAPE, "ESC")
        };

        //Set up keybinds
        for (Keybinds keybind : keybinds) {
            this.game.getInputMap(IFW).put(KeyStroke.getKeyStroke(keybind.c, 0), keybind.name);
            if (keybind.n) {
                this.game.getActionMap().put(keybind.name, keybind.action);
            }
        }

        //Timer that controls logo animation
        Timer animation = new Timer(60, null);
        animation.addActionListener(e -> {
            this.main.wiggle();
            this.main.repaint();
        });
        animation.start();

        //Timer that controls the snake being a rainbow, if set to true
        Timer rainbow = new Timer(100, null);
        rainbow.addActionListener(e -> {
            game.ind++;
            if(game.ind >= game.rainbow.length){game.ind = 0;}
            game.repaint();
        });

        //Timer that serves as a buffer, so that you aren't able to input commands too fast which caused an issue of flipping
        //velocities
        Timer vel = new Timer(1, null);
        vel.addActionListener(e -> {
            if(this.xVel != this.newX || this.yVel != this.newY) {
                if (Math.abs(this.newX - game.xVel) != 2 && Math.abs(this.newY - game.yVel) != 2) {
                    this.xVel = this.newX;
                    this.yVel = this.newY;
                }
            }
        });

        //Timer that updates the snake
        Timer update = new Timer(80, null);
        update.addActionListener(e -> {

            //Checks for food each update, if found increases score
            if(game.checkFood()) {
                score++;
            }

            //Calls update function in snake to move to next section of grid
            game.update(this.xVel, this.yVel);

            //Checks for snake collision
            if(game.collision) {

                //If true sets velocity to zero and sets vel timer to stop so no more updates to the variables can be made
                this.xVel = this.yVel = 0;
                vel.stop();

                //Runs a function to make snake flash red a certain amount of times
                if (f < 12) {
                    this.game.dead();
                    f++;

                //Runs a function to fade snake out, and fade Game Over screen in
                } else if (f < 24) {
                    this.game.gameOver(score);
                    f++;
                }
            }
        });

        //Refers to the button in the Game Over screen
        this.game.cont.addActionListener(e -> {

            //Resets Game Over screen to be invisible again
            this.game.cont();

            //Resets the snake to middle of screen, and with starting length and all variables in snakeFrame
            reset(numSnakes);

            //Stops all timers
            update.stop();
            vel.stop();
            rainbow.stop();

            //Swaps the game component out with the menu component
            swap(this.game, this.main);
        });

        //Refers to the play button in menu
        this.main.play.addActionListener(e -> {

            //Swaps the menu component out with the game component
            swap(this.main, this.game);

            //Starts all relative timers
            vel.start();
            update.start();
            if(this.game.bRainbow) {
                rainbow.start();
            }

            //Reset pause variable
            p = false;
        });

        //Refers to the instructions button in menu
        this.main.instructions.addActionListener(e -> {
            swap(this.main, this.ins);
        });

        //Refers to the settings button in menu
        this.main.settings.addActionListener(e -> {
        });

        //Refers to the back button in instructions
        this.ins.back.addActionListener(e -> {

            //Swaps out instructions component for menu component
            swap(this.ins, this.main);
        });

        //Action that unpauses game if paused
        this.game.getActionMap().put("ENTER", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vel.start(); update.start(); p = false;
            }
        });

        //Action that toggles paused on and off
        this.game.getActionMap().put("ESC", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(p) {vel.start();update.start(); p = false;}
                else {vel.stop();update.stop(); p = true;}
            }
        });

        //Initial reset to assign all variables
        reset(numSnakes);
    }

    //Class that handles switching the snakes velocity
    private class dirAction extends AbstractAction {
        int tempX;
        int tempY;
        dirAction(int x, int y) {
            this.tempX = x;
            this.tempY = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            //Stops the changing of the velocity when paused
            if (!p) {

                //Checks if player is trying to flip snake around
                if (Math.abs(xVel - this.tempX) != 2 && Math.abs(yVel - this.tempY) != 2) {

                    //Assigns new velocities
                    newX = this.tempX;
                    newY = this.tempY;
                }
            }
        }
    }

    //Class that handles keybinds
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

    //Function that resets all variables in snakeFrame
    public void reset(int num) {
        this.newY = -1;
        this.newX = 0;
        this.f = 0;
        score = 0;
        this.game.reset(num);
    }

    public void toggleTimers() {
    }

    //Function that swaps input components
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
