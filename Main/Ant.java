package Main;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Ant {
    private Image antImage;
    private int x;
    private int y;
    private int initX;
    private int initY;
    private boolean visibility = true;

    public Ant(int initialX, int initialY, String imagePath) {
        try {
            antImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        x = initialX;
        y = initialY;
        initX = initialX;
        initY = initialY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getAntImage() {
        return antImage;
    }

    public void moveTo(int newX, int newY) {
        x = newX;
        y = newY;
    }

    public void killed(){
        x = initX;
        y = initY;
    }

    public void setVisible(boolean visible){
        visibility = visible;
    }
}
