package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


class MapPanel extends JPanel {
    private Image mapImage;
    private Image pieceImage; // Image for the piece
    private Piece piece;
    private LinkedList<int[]> possiblePositions = new LinkedList<>();
    private Image[] diceImages = new Image[6];
    private Image current_dice;
    private Random rand = new Random();
    private int dice = -1;

    private int[][] Positions = {
            {1100, 100}, // Example possible position 1 (x, y)
            {850, 300},
            {680, 440},
            {520, 580},
            {670, 730},
            {850, 590},
            {1010, 730},
            {850, 870},
            {520, 870},
            {350, 730},
            {350, 450},
            {520, 300},
            {350, 150},
            {170, 300},
            {170, 580},
            {120, 950},

            // Add more possible positions as needed
    };
    private int pieceWidth = 80; // Desired width for the piece image
    private int pieceHeight = 80; // Desired height for the piece image
    private int currentPositionIndex = 0; 

    public MapPanel(String mapImagePath, String pieceImagePath, String dicesPath) {
        try {
            mapImage = ImageIO.read(new File(mapImagePath));
            pieceImage = ImageIO.read(new File(pieceImagePath)); // Load the piece image
            pieceImage = pieceImage.getScaledInstance(pieceWidth, pieceHeight, Image.SCALE_SMOOTH); // Resize the piece image
            for(int[] position : Positions){
                possiblePositions.add(position);
            }
            for(int i = 1; i < 7; i++){
                diceImages[i-1] = ImageIO.read(new File(dicesPath + i + ".png"));
                diceImages[i-1] = diceImages[i-1].getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            }
            current_dice = diceImages[0];
            
            piece = new Piece(possiblePositions.get(0)[0], possiblePositions.get(0)[1], this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Random rand = new Random();
        JButton rollButton = new JButton("Click To Roll");
        rollButton.setBounds(50, 150, 100, 50);
        rollButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            rollButton.setEnabled(false);
                // roll for 3 seconds
                long startTime = System.currentTimeMillis();
                Thread rollThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long endTime = System.currentTimeMillis();
                        try {
                            while ((endTime - startTime) / 1000F < 3) {
                                // roll dice
                                dice = rand.nextInt(6);
                                // Assuming you have ImgService class to handle image updates
                                // Update dice images
                                
                                current_dice = diceImages[dice];
                                repaint();
                                revalidate();

                                // sleep thread
                                Thread.sleep(60);

                                endTime = System.currentTimeMillis();
                            }

                            rollButton.setEnabled(true);
                        } catch (InterruptedException ex) {
                            System.out.println("Threading Error: " + ex);
                        }
                    }
                });
                rollThread.start();
            }
        });
        this.add(rollButton);
        setLayout(null);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check if the mouse click is within the bounds of the piece
                int pieceX = piece.getX() - pieceWidth / 2;
                int pieceY = piece.getY() - pieceHeight / 2;
                if (e.getX() >= pieceX && e.getX() <= pieceX + pieceWidth &&
                    e.getY() >= pieceY && e.getY() <= pieceY + pieceHeight) {
                    // Move the piece to the next possible position
                    currentPositionIndex = (currentPositionIndex + dice + 1) % possiblePositions.size();
                    int[] newPosition = possiblePositions.get(currentPositionIndex);
                    piece.setX(newPosition[0]);
                    piece.setY(newPosition[1]);
                    dice = -1;
                    repaint(); // Redraw the panel to show the updated position
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
        }

        // g.setColor(Color.RED); // Set the color for possible positions
        // int radius = 10; // Adjust the radius as needed for position indicators
        // for (int[] position : possiblePositions) {
        //     int x = position[0];
        //     int y = position[1];
        //     g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius); // Draw a circle as a possible position indicator
        // }
        
        if (pieceImage != null && piece != null) {
            int x = piece.getX() - pieceWidth / 2; // Adjust position to center the piece image
            int y = piece.getY() - pieceHeight / 2; // Adjust position to center the piece image
            g.drawImage(pieceImage, x, y, this); // Draw the resized piece image at the piece's position
        }
        if (current_dice != null){
            g.drawImage(current_dice, 50, 50, this);
        }
    }
}


public class Spinderella {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 1200, 1200);
        
        MapPanel mapPanel = new MapPanel("Main/map.jpg", "Main/Ant-8.png", "Main/Dices/"); // Replace with your image path
        frame.add(mapPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}