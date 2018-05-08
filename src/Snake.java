import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Snake extends JPanel {
    private shapeItem[] snake;
    private shapeItem food;
    private shapeItem[] testFood;
    private int x, y, gSize, size, rows, cols, maxX, maxY, test = 0;
    int xVel, yVel;
    boolean collision, dead, bRainbow = false;
    private final Color VIOLET = new Color( 128, 0, 128 );
    private final Color INDIGO = new Color( 75, 0, 130 );
    Color[] c = {Color.green, Color.white};
    Color[] rainbow =  {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, INDIGO, VIOLET};
    int ind = 0;
    JPanel gameOver;
    JLabel game, over, score;
    JButton cont;
    int opac = -20;


    Snake(int rows, int cols, int gSize, int num){
        //Initialize variables
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.BLACK);
        this.maxX = cols * gSize; this.maxY = rows * gSize;
        this.gSize = gSize;
        this.size = gSize - 2;
        this.rows = rows;
        this.cols = cols;

        //Create starting snake and food
        reset(num);

        //Game Over
        int font = (int) ((cols * gSize) / 2.5);

        //Initialize the gui
        gameOver = new JPanel();
        game = new JLabel("GAME"); over = new JLabel("OVER"); score = new JLabel();
        cont = new JButton("Continue");

        //Load in custom font
        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getClassLoader().getResourceAsStream("resources/Game_Over.ttf")));
        } catch (IOException |FontFormatException e) {
            //Handle exception
            System.out.println(e.getMessage());
        }

        //Set the font
        Font customFont = new Font("Game Over", Font.PLAIN, font);
        game.setFont(customFont); over.setFont(customFont);
        score.setFont(new Font("Game Over", Font.PLAIN, font / 2));
        cont.setFont(new Font("Game Over", Font.PLAIN, font / 2));

        //Align the labels to the center
        game.setHorizontalAlignment(SwingConstants.CENTER);
        over.setHorizontalAlignment(SwingConstants.CENTER);
        score.setHorizontalAlignment(SwingConstants.CENTER);

        //Make everything white and transparent
        game.setForeground(new Color(255, 255, 255, 0));
        over.setForeground(new Color(255, 255, 255, 0));
        score.setForeground(new Color(255, 255, 255, 0));
        cont.setOpaque(false); cont.setContentAreaFilled(false); cont.setBorderPainted(false);
        cont.setForeground(new Color(255,255,255,0));

        //Set layout for panel and add
        gameOver.setLayout(new GridLayout(4, 1));
        gameOver.add(game); gameOver.add(over); gameOver.add(score);
        gameOver.setOpaque(false);
        GridBagConstraints g = new GridBagConstraints();
        this.add(gameOver, g);
    }

    //Draws all the squares
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;

        //Draw food
        g2.setColor(food.getColor());
        g2.fill(food.getShape());

//        for(shapeItem food: testFood) {
//            g2.setColor(food.getColor());
//            g2.fill(food.getShape());
//        }

        //Loop through all sections of the snake
        for(int s = 0; s < snake.length; s++) {
            int index = (ind + s) % (rainbow.length);
            if (!bRainbow) {
                g2.setColor(snake[s].getColor());
            } else {
                g2.setColor(rainbow[index]);
            }
            g2.fill(snake[s].getShape());
        }
    }

    //Set up snake
    public void reset(int num) {
        //If recovering from collision set it to false
        collision = false;

        //Find center of window and set starting cords
        this.x = ((((this.cols - 1) / 2) - 1) * this.gSize);
        this.y = ((((this.rows - 1) / 2) - 1) * this.gSize);

        //Set initial vertical offset to 0
        int offset = 0;

        //Set first square to be green
        Color tempC = Color.green;
        snake = new shapeItem[num];

        //Create the snake
        for(int s = 0; s < num; s++){
            snake[s] = new shapeItem(new Rectangle(this.x, this.y + offset, this.size , this.size), tempC);
            offset += this.gSize;
            //Set the rest of the squares to be white
            tempC = Color.white;
        }

        //Create a food
//        testFood = new shapeItem[3];
//        for(int f = 0; f < testFood.length; f++) {
//            testFood[f] = newFood();
//        }
        food = newFood();
        this.repaint();
    }

    //Updates all squares
    public void update(int xVel, int yVel) {
        //Get supplied velocities
        this.xVel = xVel; this.yVel = yVel;

        //Set number of pixels for first square to move
        int xOff = xVel * this.gSize;
        int yOff = yVel * this.gSize;

        //Set new cords of first square
        this.x += xOff; this.y += yOff;

        //Check for collision at those cords
        collision(this.x, this.y);
        if(!collision) {

            //If no collision update back end of snake first
            for (int x = snake.length - 1; x > 0; x--) {
                int tempX = snake[x - 1].getShape().getBounds().x;
                int tempY = snake[x - 1].getShape().getBounds().y;
                snake[x].setRec(tempX, tempY, this.size);
            }

            //Then update first square to new position
            snake[0].setRec(this.x, this.y, this.size);
        }

        this.repaint();
    }

    //Adds a square to the snake
    public void addToSnake(){
        //Figure out on which side to place the next square
        int tempX1 = snake[snake.length - 1].getShape().getBounds().x;
        int tempY1 = snake[snake.length - 1].getShape().getBounds().y;
        int tempX2 = snake[snake.length - 2].getShape().getBounds().x;
        int tempY2 = snake[snake.length - 2].getShape().getBounds().y;

        if(tempX1 > tempX2 && tempY1 == tempY2){ tempX1 += gSize;
        } else if(tempX1 < tempX2 && tempY1 == tempY2){tempX1 -= gSize; }
        if(tempY1 > tempY2 && tempX1 == tempX2){ tempY1 += gSize;
        } else if(tempY1 < tempY2 && tempX1 == tempX2){ tempY1 -= gSize; }

        //Make a new array of snakes 1 length longer
        snake = Arrays.copyOf(snake, snake.length + 1);

        //Add new section
        snake[snake.length - 1] = new shapeItem(new Rectangle(tempX1, tempY1, this.size, this.size), Color.white);
    }

    //Test for collision
    private void collision(int colX, int colY){
        collision = colX < 0 || colX >= this.maxX ||
                    colY < 0 || colY >= this.maxY ||
                    checkForSnake(colX, colY,"bodyCollision");
    }

    //Checks if the snake collides with wall/food/itself
    private boolean checkForSnake(int x, int y, String type) {
        //Initialize variables
        int start = 0;
        int length = snake.length;

        //If checking for food only check the head
        if(type.equals("foodEat")) {
            length = 1;

        //If checking for body collision skip the first square
        } else if(type.equals("bodyCollision")) {
            start = 1;
        }

        //Check snake against supplied cords
        for(int s = start; s < length; s++){

            //If equal return true
            if(snake[s].getShape().getBounds().x == x && snake[s].getShape().getBounds().y == y){
                return true;
            }
        }

        //If true never gets returned, no collision return false
        return false;
    }

    //Create a new food
    private shapeItem newFood(){

        //Get random cords within the window
        int tempX = (int) (Math.random() * this.cols) * this.gSize;
        int tempY = (int) (Math.random() * this.rows) * this.gSize;

        //Set a new food at new cords
        Shape foodS = new Rectangle(tempX, tempY, this.size, this.size);
        //Shape foodS = new Ellipse2D.Double(tempX, tempY, this.size, this.size);

        //Set Color
        Color foodC = Color.red;

        //Check if food spawns on snake
        if(!checkForSnake(tempX, tempY, "foodSpawn")){

            //If not create new shapeItem with supplied rectangle
            return new shapeItem(foodS, foodC);
        } else {

            //If true run newFood() again to get new cords
            System.out.println("Food spawn attempt on snake.");
            return newFood();
            //newFood();
        }
    }

    //Check if snake ate a food
    public boolean checkFood() {

        //Check collision against food cords
        if(checkForSnake(food.getShape().getBounds().x, food.getShape().getBounds().y, "foodEat")){

            //If true add new square to snake, reset the food
            addToSnake();
            newFood();

            //Return true to update score value in snakeFrame
            return true;
        }

        //If true isn't returned, no food collision return false
        return false;
    }

    //Function that runs when snake collides with self, or wall
    public void dead() {

        //For all sections of snake change color from standard to red, and back
        for(shapeItem snakes : snake){
            if(this.dead){
                snakes.setColor();
            } else {
                snakes.setColor(Color.RED);
            }
        }

        //This is what alternates from turning red to turning back to standard
        //have to run function twice in order to get color change in order to slow down flashing, based off of
        //update timer in snakeFrame
        test++;
        if(test == 2) {
            this.dead = !this.dead;
            test = 0;
        }
    }

    //Function that runs after dead() is run a certain amount of times, determined in snakeFrame
    // takes current score as input
    public void gameOver(int fScore){

        //Increment all sections of the snakes and food opacity by set amount to fade out
        for(shapeItem snakes : snake){
            snakes.setOpacity(opac);
        }
        food.setOpacity(opac);

        //If the score isn't 0 add score, if 0 leave empty
        if(fScore != 0) {
            score.setText("Score: " + fScore);
        }

        //Add continue button and revalidate
        gameOver.add(cont);
        this.revalidate();

        //Fade in game over text and button as snake fades away
        game.setForeground(new Color(255, 255, 255, game.getForeground().getAlpha() + -opac));
        over.setForeground(new Color(255, 255, 255, over.getForeground().getAlpha() + -opac));
        score.setForeground(new Color(255, 255, 255, score.getForeground().getAlpha() + -opac));
        cont.setForeground(new Color(255, 255, 255, cont.getForeground().getAlpha() + -opac));
    }

    //Function that runs when the continue button is pressed
    public void cont() {

        //Reset score
        score.setText("");

        //Remove continue button from panel and revalidate
        gameOver.remove(cont);
        this.revalidate();

        //Reset game over components to be transparent
        game.setForeground(new Color(255, 255, 255, 0));
        over.setForeground(new Color(255, 255, 255, 0));
        score.setForeground(new Color(255, 255, 255, 0));
        cont.setForeground(new Color(255, 255, 255, 0));
    }

}
