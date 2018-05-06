import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Snake extends JPanel {
    private shapeItem[] snake;
    private shapeItem food;
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
        //this.setLayout(null);
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.BLACK);
        this.maxX = cols * gSize; this.maxY = rows * gSize;
        this.gSize = gSize;
        this.size = gSize - 2;
        this.rows = rows;
        this.cols = cols;
        reset(num);
        newFood();

        //Game Over
        int font = 120;
        //gameOver.setText("GAME\nOVER");
        gameOver = new JPanel();
        game = new JLabel("GAME"); over = new JLabel("OVER"); score = new JLabel();
        cont = new JButton("Continue");
        Font sansSerif = new Font("Sans Serif", Font.BOLD, font);
        game.setFont(sansSerif); over.setFont(sansSerif);
        score.setFont(new Font("Sans Serif", Font.BOLD, font / 2));
        cont.setFont(new Font("Sans Serif", Font.BOLD, font / 2));
        game.setHorizontalAlignment(SwingConstants.CENTER);
        over.setHorizontalAlignment(SwingConstants.CENTER);
        score.setHorizontalAlignment(SwingConstants.CENTER);
        game.setForeground(new Color(255, 255, 255, 0));
        over.setForeground(new Color(255, 255, 255, 0));
        score.setForeground(new Color(255, 255, 255, 0));
        cont.setOpaque(false); cont.setContentAreaFilled(false); cont.setBorderPainted(false);
        cont.setForeground(new Color(255,255,255,0));
        cont.setBackground(Color.white);
        gameOver.setLayout(new GridLayout(4, 1));
        gameOver.add(game); gameOver.add(over); gameOver.add(score);gameOver.add(cont);
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
        //Loop through all squares]
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
        collision = false;
        this.x = ((((this.cols - 1) / 2) - 1) * this.gSize);
        this.y = ((((this.rows - 1) / 2) - 1) * this.gSize);
        int offset = 0;
        Color tempC = Color.green;
        snake = new shapeItem[num];
        for(int s = 0; s < num; s++){
            snake[s] = new shapeItem(new Rectangle(this.x, this.y + offset, this.size , this.size), tempC);
            offset += this.size + 2;
            tempC = Color.white;
        }
        newFood();
    }
        //Updates all squares
    public void update(int xVel, int yVel) {
        this.xVel = xVel; this.yVel = yVel;
        int xOff = xVel * this.gSize;
        int yOff = yVel * this.gSize;
        this.x += xOff; this.y += yOff;
        collision(this.x, this.y);
        if(!collision) {
            for (int x = snake.length - 1; x > 0; x--) {
                int tempX = snake[x - 1].getShape().getBounds().x;
                int tempY = snake[x - 1].getShape().getBounds().y;
                snake[x].setRec(tempX, tempY, this.size);
            }
            snake[0].setRec(this.x, this.y, this.size);
        }
    }

    //Adds a square to the snake
    public void addToSnake(){
        int tempX1 = snake[snake.length - 1].getShape().getBounds().x;
        int tempY1 = snake[snake.length - 1].getShape().getBounds().y;
        int tempX2 = snake[snake.length - 2].getShape().getBounds().x;
        int tempY2 = snake[snake.length - 2].getShape().getBounds().y;

        if(tempX1 > tempX2 && tempY1 == tempY2){ tempX1 += gSize;
        } else if(tempX1 < tempX2 && tempY1 == tempY2){tempX1 -= gSize; }
        if(tempY1 > tempY2 && tempX1 == tempX2){ tempY1 += gSize;
        } else if(tempY1 < tempY2 && tempX1 == tempX2){ tempY1 -= gSize; }
        snake = Arrays.copyOf(snake, snake.length + 1);
        snake[snake.length - 1] = new shapeItem(new Rectangle(tempX1, tempY1, this.size, this.size), Color.white);
    }

    private void collision(int colX, int colY){
        collision = colX < 0 || colX >= this.maxX ||
                    colY < 0 || colY >= this.maxY ||
                    checkForSnake(colX, colY,"headCollision");
    }

    private boolean checkForSnake(int x, int y, String type) {
        int start = 0;
        int length = snake.length;
        if(type.equals("foodEat")) {
            length = 1;
        } else if(type.equals("headCollision")) {
            start = 1;
        }
        for(int s = start; s < length; s++){
            if(snake[s].getShape().getBounds().x == x && snake[s].getShape().getBounds().y == y){
                return true;
            }
        }
        return false;
    }

    private void newFood(){
        int tempX = (int) (Math.random() * this.cols) * this.gSize;
        int tempY = (int) (Math.random() * this.rows) * this.gSize;
        Shape foodS = new Rectangle(tempX, tempY, this.size, this.size);
        //Shape foodS = new Ellipse2D.Double(tempX, tempY, this.size, this.size);
        Color foodC = Color.red;
        if(!checkForSnake(tempX, tempY, "foodSpawn")){
            food = new shapeItem(foodS, foodC);
        } else {
            System.out.println("Food spawn attempt on snake.");
            newFood();
        }
    }

    public boolean checkFood() {
        if(checkForSnake(food.getShape().getBounds().x, food.getShape().getBounds().y, "foodEat")){
            addToSnake();
            newFood();
            return true;
        }
        return false;
    }

    public void dead() {
        //System.out.println(rec);
        for(shapeItem snakes : snake){
            if(this.dead){
                snakes.setColor();
            } else {
                snakes.setColor(Color.RED);
            }
        }
        test++;
        if(test == 2) {
            this.dead = !this.dead;
            test = 0;
        }
        //this.repaint();
        //while(rec < 12){dead(rec + 1);}
    }

    public void gameOver(int fScore){
        if(fScore > 0) {
            score.setText("Score: " + fScore);
        }
        game.setForeground(new Color(255, 255, 255, game.getForeground().getAlpha() + -opac));
        over.setForeground(new Color(255, 255, 255, over.getForeground().getAlpha() + -opac));
        score.setForeground(new Color(255, 255, 255, score.getForeground().getAlpha() + -opac));
        cont.setForeground(new Color(255, 255, 255, cont.getForeground().getAlpha() + -opac));
    }

    public void cont() {
        score.setText("");
        game.setForeground(new Color(255, 255, 255, 0));
        over.setForeground(new Color(255, 255, 255, 0));
        score.setForeground(new Color(255, 255, 255, 0));
        cont.setForeground(new Color(255, 255, 255, 0));
    }

    public void fadeOut() {
        for(shapeItem snakes : snake){
                snakes.setOpacity(opac);
        }
        food.setOpacity(opac);
    }
}
