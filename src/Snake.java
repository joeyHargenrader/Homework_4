import javafx.scene.shape.Circle;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;

public class Snake extends JPanel {
    private shapeItem[] snake;
    private shapeItem food;
    private int x, y, gSize, size, rows, cols, maxX, maxY;
    int xVel, yVel;
    boolean collision, bRainbow = false;
    private final Color VIOLET = new Color( 128, 0, 128 );
    private final Color INDIGO = new Color( 75, 0, 130 );
    Color[] c = {Color.green, Color.white};
    private Color[] rainbow =  {Color.red, Color.orange, Color.yellow, Color.green, Color.blue, INDIGO, VIOLET};
    int ind = rainbow.length;


    Snake(int rows, int cols, int gSize, int num){
        this.maxX = cols * gSize; this.maxY = rows * gSize;
        this.gSize = gSize;
        this.size = gSize - 2;
        this.rows = rows;
        this.cols = cols;
        reset(num);
        newFood();
    }

    //Draws all the squares
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2 = (Graphics2D) g;
        //Loop through all squares]
        int sNum = 0;
        for(shapeItem shapes: snake) {
            int test = (ind + sNum) % (rainbow.length);
            //System.out.println(test);
            if(!bRainbow) {
                g2.setColor(shapes.getColor());
            } else {
                g2.setColor(rainbow[test]);
            }
            g2.fill(shapes.getShape());
            sNum++;
        }
//        ind--;
//        if(ind < 0){ind = rainbow.length;}
        //Draw food
        g2.setColor(food.getColor());
        g2.fill(food.getShape());
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
                int tempX = snake[x - 1].shape.getBounds().x;
                int tempY = snake[x - 1].shape.getBounds().y;
                snake[x] = new shapeItem(new Rectangle(tempX, tempY, this.size, this.size), Color.white);
            }
            snake[0] = new shapeItem(new Rectangle((this.x), (this.y), this.size, this.size), Color.green);
        }
    }

    //Adds a square to the snake
    public void addToSnake(){
        int tempX1 = snake[snake.length - 1].shape.getBounds().x;
        int tempY1 = snake[snake.length - 1].shape.getBounds().y;
        int tempX2 = snake[snake.length - 2].shape.getBounds().x;
        int tempY2 = snake[snake.length - 2].shape.getBounds().y;

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
            if(snake[s].shape.getBounds().x == x && snake[s].shape.getBounds().y == y){
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
        if(checkForSnake(food.shape.getBounds().x, food.shape.getBounds().y, "foodEat")){
            addToSnake();
            newFood();
            return true;
        }
        return false;
    }

    public class shapeItem {
        Shape shape;
        Color color;

        shapeItem(Shape shape, Color color) {
            this.shape = shape;
            this.color = color;
        }

        public Shape getShape() {
            return this.shape;
        }

        public Color getColor() {
            return this.color;
        }

    }
}
