package TankGame.menus;

import TankGame.GameConstants;
import TankGame.Launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EndGamePanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;
    private JLabel winnerLabel;

    public EndGamePanel(Launcher lf) {
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

        winnerLabel = new JLabel("");
        winnerLabel.setFont(new Font("Courier New", Font.BOLD, 24));
        winnerLabel.setForeground(Color.WHITE);
        winnerLabel.setBounds(150, 273, 250, 50);

        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(123, 348, 250, 50);
        start.addActionListener((actionEvent -> this.lf.setFrame("game")));

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(123, 448, 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(winnerLabel);
        this.add(start);
        this.add(exit);
    }

    public void setWinner(String winner)
    {
        winnerLabel.setText(winner);
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
    }
}
