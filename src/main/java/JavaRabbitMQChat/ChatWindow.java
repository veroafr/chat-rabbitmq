package JavaRabbitMQChat;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChatWindow {
    private JFrame frame;
    private JPanel messagePanel;
    private JScrollPane scrollPane;
    private JTextField inputField;
    private String myQueue;
    private String targetQueue;
private String userName;

    public ChatWindow(String title, String myQueue, String targetQueue, String userName) {
    this.myQueue = myQueue;
    this.targetQueue = targetQueue;
    this.userName = userName;
    init(title);
    startReceiver();
}


    private void init(String title) {
        frame = new JFrame(title);
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(new Color(240, 242, 245));

        scrollPane = new JScrollPane(messagePanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        inputField = new JTextField();
        JButton sendButton = new JButton("➤");

        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setPreferredSize(new Dimension(300, 40));
        sendButton.setPreferredSize(new Dimension(60, 40));

        sendButton.addActionListener((ActionEvent e) -> sendMessage());

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            try {
                Sender.send(targetQueue, message);
                addMessageBubble("Yo: " + message, true);
                inputField.setText("");
            } catch (Exception e) {
addMessageBubble(userName + ": " + message, true);
            }
        }
    }

    private void addMessageBubble(String text, boolean isSender) {
        JPanel bubble = new JPanel();
        bubble.setLayout(new BorderLayout());
        JLabel label = new JLabel("<html><p style='width: 200px'>" + text + "</p></html>");
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setFont(new Font("Arial", Font.PLAIN, 14));

        if (isSender) {
            label.setBackground(new Color(220, 248, 198)); // verde claro
            bubble.add(label, BorderLayout.EAST);
        } else {
            label.setBackground(Color.WHITE);
            bubble.add(label, BorderLayout.WEST);
        }

        bubble.setBorder(new EmptyBorder(5, 10, 5, 10));
        messagePanel.add(bubble);
        messagePanel.revalidate();

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    private void startReceiver() {
    new Thread(() -> {
        try {
            Receiver.receive(myQueue, message ->
                SwingUtilities.invokeLater(() -> {
                    addMessageBubble("Usuario B: " + message, false);
                })
            );
        } catch (Exception e) {
            addMessageBubble("Error al recibir: " + e.getMessage(), false);
        }
    }).start();
}

}
