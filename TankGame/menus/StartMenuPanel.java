package TankGame.menus;


import TankGame.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StartMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;

    public StartMenuPanel(Launcher lf) {
        this.lf = lf;
        try {

            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("TankGame/resources/title.png"));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JLabel creator = new JLabel("Created By Ulices Gonzalez");
        creator.setFont(new Font("Courier New", Font.BOLD, 24));
        creator.setForeground(Color.WHITE);
        creator.setBounds(60, 273, 364, 50);

        JButton start = new JButton("Start");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(123, 348, 250, 50);
        start.addActionListener(actionEvent -> this.lf.setFrame("game"));

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(123, 448, 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(creator);
        this.add(start);
        this.add(exit);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
    }
}
