import java.awt.*;
import javax.swing.*;

public class settings {
  	  public settings() {
		    super("Settings");
		    this.setLayout(new GridLayout(4, 2));
		    this.setBackground(Color.WHITE);
		    this.add(new JLabel("Background Color"));
	  	  String[] colorList = {"Black", "Blue", "Red", "White", "Yellow"};
		    JComboBox backgrColor = new JComboBox(colorList);
		    this.add(backgrColor);
		    this.add(new JLabel("Snake Body Color"));
		    JComboBox snakeBodyColor = new JComboBox(colorList);
		    this.add(snakeBodyColor);
		    this.add(new JLabel("Snake Head Color"));
		    JComboBox snakeHeadColor = new JComboBox(colorList);
		    this.add(snakeHeadColor);
		    this.add(new JLabel("Speed"));
		    JSlider speed = new JSlider(1, 8, 1);
		    speed.setPaintTicks(true);
		    speed.setMajorTickSpacing(1);
		    this.add(speed);
		    this.setVisible(true);
      }
}
