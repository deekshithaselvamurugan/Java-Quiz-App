import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class QuizAppWithTopics extends JFrame implements ActionListener {
    ArrayList<String[]> questions = new ArrayList<>();
    JLabel questionLabel;
    JRadioButton[] options = new JRadioButton[4];
    ButtonGroup group;
    JButton nextButton;
    int index = 0, score = 0;

    public QuizAppWithTopics(String topicFile) {
        setTitle("Quiz App - " + topicFile.replace(".txt", "").toUpperCase());
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadQuestions(topicFile);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel optionPanel = new JPanel(new GridLayout(4, 1));
        group = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            group.add(options[i]);
            optionPanel.add(options[i]);
        }

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);

        add(questionLabel, BorderLayout.NORTH);
        add(optionPanel, BorderLayout.CENTER);
        add(nextButton, BorderLayout.SOUTH);

        loadQuestion(index);
        setVisible(true);
    }

    private void loadQuestions(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] qData = line.split("\\|");
                if (qData.length == 6)
                    questions.add(qData);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading questions file.");
            System.exit(0);
        }
    }

    private void loadQuestion(int i) {
        String[] q = questions.get(i);
        questionLabel.setText("Q" + (i + 1) + ": " + q[0]);
        for (int j = 0; j < 4; j++) {
            options[j].setText(q[j + 1]);
            options[j].setSelected(false);
        }
    }

    private String getSelectedAnswer() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                return String.valueOf((char) ('A' + i));
            }
        }
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selected = getSelectedAnswer();
        if (!selected.isEmpty()) {
            if (selected.equals(questions.get(index)[5])) {
                score++;
            }
            index++;
            if (index < questions.size()) {
                loadQuestion(index);
            } else {
                JOptionPane.showMessageDialog(this, "Quiz Finished!\nYour Score: " + score + "/" + questions.size());
                System.exit(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an answer.");
        }
    }

    public static void main(String[] args) {
        String[] topics = {"general.txt", "technical.txt", "science.txt"};
        String topic = (String) JOptionPane.showInputDialog(
            null,
            "Select Topic:",
            "Quiz Topic",
            JOptionPane.QUESTION_MESSAGE,
            null,
            new String[]{"General Knowledge", "Technical", "Science"},
            "General Knowledge"
        );

        if (topic != null) {
            switch (topic) {
                case "General Knowledge" -> new QuizAppWithTopics("general.txt");
                case "Technical" -> new QuizAppWithTopics("technical.txt");
                case "Science" -> new QuizAppWithTopics("science.txt");
            }
        } else {
            System.exit(0);
        }
    }
}
