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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.Math;
import java.awt.Font;



class MapPanel extends JPanel {
    private Image mapImage;
    private Image antImage;
    private boolean ant_diceVisible = true;
    private Ant ant;
    private Spider[] spider = new Spider[2];
    private LinkedList<int[]> possibleantPosition = new LinkedList<>();
    private LinkedList<int[]> possiblespiderPosition = new LinkedList<>();
    private Image[] diceImages = new Image[6];
    private Image current_dice;
    private int dice = -1;
    private int spiderMove = 5;

    private int[][] antPosition = {
            {1099, 96},
            {844, 292},
            {679, 435},
            {517, 576},
            {680, 715},
            {843, 574},
            {1013, 716},
            {850, 862},
            {523, 867},
            {356, 719},
            {353, 438},
            {521, 296},
            {352, 151},
            {184, 295},
            {185, 579},
            {190, 868},
            

            // Add more possible antPosition as needed
    };
    private int[][] spiderPosition = {
        {1007,149},
        {843,155},
        {674,151},
        {513,149},
        {181,151},
        {1009,288},
        {676,293},
        {346,295},
        {1012,432},
        {841,434},
        {517,435},
        {183,438},
        {1010,573},
        {679,573},
        {355,576},
        {848,716},
        {518,718},
        {190,720},
        {1015,856},
        {686,859},
        {359,863},
        {190,868},
        {352,151},
        {184,295},
        {521,296},
        {844,292},
        {353,438},
        {679,435},
        {185,579},
        {517,576},
        {843,574},
        {356,719},
        {680,715},
        {1013,716},
        {850,862},
        {523,867},
    };

    private int antWidth = 80; // Desired width for the piece image
    private int antHeight = 80; // Desired height for the piece image
    private int currentPositionIndex = 0; 

    public void swapMap(String updatedImagePath, boolean ant_diceVisible){
        try{
            this.mapImage = ImageIO.read(new File(updatedImagePath));
            this.ant_diceVisible = ant_diceVisible;
            repaint();   
        } catch (IOException e){
            e.printStackTrace();
        }
    }

        public double drawStringBetweenSpiders(Graphics g) {
        double distance = 0;
        if (!ant_diceVisible) {
            int spider1X = spider[0].getX();
            int spider1Y = spider[0].getY();
            int spider2X = spider[1].getX();
            int spider2Y = spider[1].getY();

            g.setColor(Color.BLACK); // Set the color for the string
            g.drawLine(spider1X, spider1Y, spider2X, spider2Y); // Draw a line between the spiders
            distance = pythagore(spider1X, spider1Y, spider2X, spider2Y);
        }
        return distance;
    }

    public double pythagore(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow(x1 - x2,2) + Math.pow(y1-y2,2));
    }

    public int findNearestPosition(int x, int y){
        double minDistance = 9999;
        int i = 0;
        int res = 0;
        for(int[] position : spiderPosition){
            double distance = pythagore(x, y, position[0], position[1]);
            if(distance < minDistance){
                minDistance = distance;
                res = i;
            }
            i++;
        }
        return res;
    }

    public MapPanel(String mapImagePath, String antImagePath, String spider1ImagePath, String spider2ImagePath, String dicesPath) {
        try {
            mapImage = ImageIO.read(new File(mapImagePath));
            antImage = ImageIO.read(new File(antImagePath)); // Load the piece image
            antImage = antImage.getScaledInstance(antWidth, antHeight, Image.SCALE_SMOOTH); // Resize the piece image
            for(int[] position : antPosition){
                possibleantPosition.add(position);
            }
            for(int[] position : spiderPosition){
                possiblespiderPosition.add(position);
            }
            for(int i = 1; i < 7; i++){
                diceImages[i-1] = ImageIO.read(new File(dicesPath + i + ".png"));
                diceImages[i-1] = diceImages[i-1].getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            }
            current_dice = diceImages[0];
            spider[0] = new Spider(possiblespiderPosition.get(0)[0], possiblespiderPosition.get(0)[1], spider1ImagePath);
            spider[1] = new Spider(possiblespiderPosition.get(5)[0], possiblespiderPosition.get(5)[1], spider2ImagePath);
            ant = new Ant(possibleantPosition.get(0)[0], possibleantPosition.get(0)[1], antImagePath);
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
                if(ant_diceVisible){
                    // Check if the mouse click is within the bounds of the piece
                    int antX = ant.getX() - antWidth / 2;
                    int antY = ant.getY() - antHeight / 2;
                    if (e.getX() >= antX && e.getX() <= antX + antWidth &&
                        e.getY() >= antY && e.getY() <= antY + antHeight) {
                        // Move the ant to the next possible position
                        currentPositionIndex = (currentPositionIndex + dice + 1) % possibleantPosition.size();
                        int[] newPosition = possibleantPosition.get(currentPositionIndex);
                        ant.moveTo(newPosition[0], newPosition[1]);
                        dice = -1;
                        repaint(); // Redraw the panel to show the updated position
                    }
                }
            }
             @Override
            public void mousePressed(MouseEvent e) {
                if (!ant_diceVisible) {
                    for(Spider sp : spider){
                        int spiderX = sp.getX() - 150 / 2;
                        int spiderY = sp.getY() - 150 / 2;
                        // Check if the mouse click is within the bounds of the spider
                        if (e.getX() >= spiderX && e.getX() <= spiderX + 150 &&
                        e.getY() >= spiderY && e.getY() <= spiderY + 150) {
                            sp.dragging(true);
                    }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                for(Spider sp: spider){
                if (sp.isDragged()) {
                    sp.dragging(false);;
                    int index = findNearestPosition(e.getX(), e.getY());
                    
                    // Update spider's position when released
                    double moveDistance = pythagore(sp.getCurrentX(), sp.getCurrentY(), possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
                    if(moveDistance > 210 && moveDistance < 230 && spiderMove > 0){
                        sp.moveTo(possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
                        sp.setCurrentPosition(sp.getX(), sp.getY());
                        spiderMove--;
                    }
                    else{
                        sp.failedmove();
                    }
                    // sp.moveTo(possiblespiderPosition.get(index)[0], possiblespiderPosition.get(index)[1]);
                    // sp.setCurrentPosition(sp.getX(), sp.getY());

                    double distanceBetween2spider = (drawStringBetweenSpiders(getGraphics()));
                    if(spider[0].getX() == ant.getX() && spider[0].getY() == ant.getY() && distanceBetween2spider > 135 && distanceBetween2spider < 225){
                        ant.killed();
                    }

                    repaint();
                }
            }
        }
    }
        );

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                for(Spider sp: spider){
                    if(sp.isDragged()){
                        sp.moveTo(e.getX(), e.getY());
                        repaint();
                    }
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

        g.setColor(Color.RED); // Set the color for possible antPosition
        // int radius = 10; // Adjust the radius as needed for position indicators
        // for (int[] position : possiblespiderPosition) {
        //     int x = position[0];
        //     int y = position[1];
        //     g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius); // Draw a circle as a possible position indicator
        // }
        
        if (antImage != null && ant != null && ant_diceVisible) {
            int x = ant.getX() - antWidth / 2; // Adjust position to center the ant image
            int y = ant.getY() - antHeight / 2; // Adjust position to center the ant image
            g.drawImage(antImage, x, y, this); // Draw the resized ant image at the piece's position
        }
        if (current_dice != null && diceImages != null){
            g.drawImage(current_dice, 50, 50, this);
        }
        if (!ant_diceVisible){
            for(Spider sp : spider){
                int spiderX = sp.getX() - 150/2;
                int spiderY = sp.getY() - 150/2;
                g.drawImage(sp.getSpiderImage(), spiderX, spiderY, this);
                String movesText = "Moves: " + spiderMove;
                g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString(movesText, 500, 100);
            }
            drawStringBetweenSpiders(g);
        }
    }
}


public class Spinderella {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(10, 10, 1200, 1200);
        
        MapPanel mapPanel = new MapPanel("Spinderrela/Main/Resource/Map/map.jpg", "Spinderrela/Main/Resource/Ant-8.png", "Spinderrela/Main/Resource/Spider.png", "Spinderrela/Main/Resource/Spider2.png" ,"Spinderrela/Main/Resource/Dices/"); // Replace with your image path
        frame.add(mapPanel);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Not needed for arrow keys
            }
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_RIGHT) {
                    // Call method to update map with new background and ant visibility
                    mapPanel.swapMap("Spinderrela/Main/Resource/Map/map2.jpg", false);
                } else if (keyCode == KeyEvent.VK_LEFT) {
                    // Call method to update map with original background and ant visibility
                    mapPanel.swapMap("Spinderrela/Main/Resource/Map/map.jpg", true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not needed for arrow keys
            }
        });

        // Set focusable to true to receive key events
        frame.setFocusable(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}