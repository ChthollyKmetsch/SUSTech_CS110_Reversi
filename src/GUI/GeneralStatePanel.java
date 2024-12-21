package GUI;

import javax.swing.*;
import java.awt.*;

import static Utility.Constants.*;

public class GeneralStatePanel extends JPanel {
    private final Image blackPieceImage = new ImageIcon(getClass().getResource("/GUI/img/blackPieceImage.png")).getImage();
    private final Image whitePieceImage = new ImageIcon(getClass().getResource("/GUI/img/whitePieceImage.png")).getImage();
    private final Image leftBracketImage = new ImageIcon(getClass().getResource("/GUI/img/leftBracketImage.png")).getImage();
    private final Image rightBracketImage = new ImageIcon(getClass().getResource("/GUI/img/rightBracketImage.png")).getImage();

    private int currentOperator = 1;
    private int blackCount = 2, whiteCount = 2, totCount = 4;
    public int totSeconds = 0;
    public int totMinutes = 0;

    public GeneralStatePanel() {
        setSize(200,710);
        setBackground(Color.LIGHT_GRAY);
        paintImmediately(DISPLAY_PANEL_RECTANGLE);
        setVisible(true);
    }

    public void setBlackCount(int blackCount) {
        this.blackCount = blackCount;
    }

    public void setWhiteCount(int whiteCount) {
        this.whiteCount = whiteCount;
    }

    public void setTotCount(int totCount) {
        this.totCount = totCount;
    }

    @Override
    public void paintComponent(Graphics g) {
        int xOffset = 640;
        int yOffset = 0;
        super.paintComponent(g);
        setBackground(Color.LIGHT_GRAY);

        // Draw default 2 visual-aiding chess pieces
        g.drawImage(blackPieceImage,xOffset+65,yOffset+80,TILE_SIZE - 10, TILE_SIZE - 10,this);
        g.drawImage(whitePieceImage,xOffset+65,yOffset+500,TILE_SIZE - 10, TILE_SIZE - 10,this);
        g.setColor(Color.BLACK);

        // Current operator visual aid
        if (currentOperator == 1) {
            g.drawImage(leftBracketImage,xOffset+10,yOffset+68, GREEN_LIGHT_DIAMETER, GREEN_LIGHT_DIAMETER,this);
            g.drawImage(rightBracketImage,xOffset+90,yOffset+68, GREEN_LIGHT_DIAMETER+3, GREEN_LIGHT_DIAMETER,this);
        } else {
            g.drawImage(leftBracketImage,xOffset+10,yOffset+488, GREEN_LIGHT_DIAMETER, GREEN_LIGHT_DIAMETER,this);
            g.drawImage(rightBracketImage,xOffset+90,yOffset+488, GREEN_LIGHT_DIAMETER+3, GREEN_LIGHT_DIAMETER,this);
        }

        // Print numbers of placed chess
        g.setFont(new Font("Consolas",Font.PLAIN,20));
        g.drawString(String.format("Count: %d", blackCount), xOffset+58, yOffset+180);
        g.drawString(String.format("Count: %d", whiteCount), xOffset+58, yOffset+600);
        g.drawString(String.format("Step(s): %d", totCount-4), xOffset+50, yOffset+350);

        // Print the time of one round of game
        String currentTimeString = String.format("%d%d : %d%d",
                totMinutes/10, totMinutes%10, (totSeconds-60*totMinutes)/10, totSeconds%10);
        g.drawString(currentTimeString, xOffset+65, yOffset+400);
    }

    public void setCurrentOperator(int currentOperator) {
        this.currentOperator = currentOperator;
    }
}
