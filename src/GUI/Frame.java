package GUI;

import org.w3c.dom.css.RGBColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Frame extends JFrame {
    public void createFrame(String title) {
        JFrame frame = new JFrame(title);
        Container container = frame.getContentPane();
        container.setBackground(Color.DARK_GRAY);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLocation(380,130);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT,20,24));
        JButton btn1 = new JButton("test btn1");
        JButton btn2 = new JButton("test btn2");
        JButton btn3 = new JButton("test btn3");
        container.add(btn1);
        container.add(btn2);
        container.add(btn3);
        frame.setVisible(true);
        TextField tf = new TextField(30);
        container.add(tf);

        btn1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Clicked on btn1");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Pressed on btn1");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("Released on btn1");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("Entered btn1");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("Exited btn1");
            }
        });
    }
}
