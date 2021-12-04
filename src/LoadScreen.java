import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadScreen {

    public static void main(String[] args){

        JFrame frame = new JFrame("Directed Weighted Graph");
        frame.setSize(500,250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(220, 190, 153));

        JLabel Header = new JLabel("Directed Weighted Graph -- Made by Eldad and Ilan");
        Header.setBounds(0,0, frame.getWidth(),30);
        Header.setHorizontalAlignment(JTextField.CENTER);
        Header.setForeground(new Color(10, 59, 73));
        Header.setFont(new Font("Serif", Font.BOLD, 18));
        contentPane.add(Header);

        JTextArea tx = new JTextArea("Enter json path here");
        tx.setLocation(10,100);
        tx.setSize(250,50);
        contentPane.add(tx);

        JButton button = new JButton("Create graph");
        button.setLocation(260,100);
        button.setSize(160,50);
        contentPane.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String str = tx.getText();
                System.out.println(str);
                // load json -- str is the path.
            }
        });

        frame.setVisible(true);

    }
}
