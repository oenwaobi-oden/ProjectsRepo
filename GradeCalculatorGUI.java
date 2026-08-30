import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GradeCalculatorGUI extends JFrame {

    // Course fields
    private JTextField courseNameField;
    private JTextField targetGradeField;

    // Assessment fields
    private JTextField assessmentNameField;
    private JTextField assessmentWeightField;
    private JTextField assessmentScoreField;
    private JTextField assessmentTotalField;
    private JTextField assessmentGradeField;

    // Assessment table
    private DefaultTableModel tableModel;
    private JTable assessmentTable;

    // Result labels
    private JLabel currentGradeLabel;
    private JLabel securedGradeLabel;
    private JLabel completedWeightLabel;
    private JLabel remainingWeightLabel;
    private JLabel requiredGradeLabel;
    private JLabel projectedGradeLabel;
    private JLabel goalMessageLabel;

    public GradeCalculatorGUI() {
        setTitle("Semester Grade Calculator");
        setSize(1250, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        createGUI();
    }

    private void createGUI() {
        setLayout(new BorderLayout(10, 10));

        add(createHeaderPanel(), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 15, 15)
        );

        mainPanel.add(createCourseInformationPanel(), BorderLayout.NORTH);
        mainPanel.add(createAssessmentSection(), BorderLayout.CENTER);
        mainPanel.add(createResultsPanel(), BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();

        headerPanel.setBackground(new Color(35, 66, 120));
        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        JLabel titleLabel = new JLabel("Semester Grade Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel);

        return headerPanel;
    }

    private JPanel createCourseInformationPanel() {
        JPanel coursePanel = new JPanel(new GridLayout(2, 2, 10, 10));

        coursePanel.setBorder(
                BorderFactory.createTitledBorder("Course Information")
        );

        JLabel courseNameLabel = new JLabel("Course Name:");
        courseNameField = new JTextField();

        JLabel targetGradeLabel = new JLabel("Target Final Grade (%):");
        targetGradeField = new JTextField();

        coursePanel.add(courseNameLabel);
        coursePanel.add(courseNameField);

        coursePanel.add(targetGradeLabel);
        coursePanel.add(targetGradeField);

        return coursePanel;
    }

    private JPanel createAssessmentSection() {
        JPanel assessmentSection = new JPanel(new BorderLayout(10, 10));
        assessmentSection.setBorder(
                BorderFactory.createTitledBorder("Assessments")
        );

        assessmentSection.add(createAssessmentInputPanel(), BorderLayout.NORTH);
        assessmentSection.add(createAssessmentTablePanel(), BorderLayout.CENTER);

        return assessmentSection;
    }

    private JPanel createAssessmentInputPanel() {
        JPanel assessmentPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 8, 8)
        );

        JLabel assessmentNameLabel = new JLabel("Name:");
        assessmentNameField = new JTextField(12);

        JLabel assessmentWeightLabel = new JLabel("Weight (%):");
        assessmentWeightField = new JTextField(5);

        JLabel assessmentScoreLabel = new JLabel("Score:");
        assessmentScoreField = new JTextField(5);

        JLabel assessmentTotalLabel = new JLabel("Out Of:");
        assessmentTotalField = new JTextField(5);

        JLabel assessmentGradeLabel = new JLabel("Grade (%):");
        assessmentGradeField = new JTextField(5);
        assessmentGradeField.setEditable(false);

        JButton addButton = new JButton("Add Assessment");
        addButton.addActionListener(e -> addAssessment());

        JButton updateButton = new JButton("Update Selected");
        updateButton.addActionListener(e -> updateSelectedAssessment());

        JButton clearInputButton = new JButton("Clear Input");
        clearInputButton.addActionListener(e -> clearAssessmentInput());

        assessmentPanel.add(assessmentNameLabel);
        assessmentPanel.add(assessmentNameField);

        assessmentPanel.add(assessmentWeightLabel);
        assessmentPanel.add(assessmentWeightField);

        assessmentPanel.add(assessmentScoreLabel);
        assessmentPanel.add(assessmentScoreField);

        assessmentPanel.add(assessmentTotalLabel);
        assessmentPanel.add(assessmentTotalField);

        assessmentPanel.add(assessmentGradeLabel);
        assessmentPanel.add(assessmentGradeField);

        assessmentPanel.add(addButton);
        assessmentPanel.add(updateButton);
        assessmentPanel.add(clearInputButton);

        return assessmentPanel;
    }

    private JPanel createAssessmentTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));

        String[] columnNames = {
                "Assessment",
                "Weight (%)",
                "Score",
                "Out Of",
                "Grade (%)",
                "Contribution (%)"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        assessmentTable = new JTable(tableModel);
        assessmentTable.setRowHeight(24);

        assessmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedAssessmentIntoFields();
            }
        });

        JScrollPane scrollPane = new JScrollPane(assessmentTable);
        scrollPane.setPreferredSize(new Dimension(900, 220));

        JButton removeButton = new JButton("Remove Selected Assessment");
        removeButton.addActionListener(e -> removeSelectedAssessment());

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(removeButton, BorderLayout.SOUTH);

        return tablePanel;
    }

    private JPanel createResultsPanel() {
        JPanel resultsPanel = new JPanel(new GridLayout(4, 2, 10, 8));

        resultsPanel.setBorder(
                BorderFactory.createTitledBorder("Semester Grade Results")
        );

        completedWeightLabel = new JLabel("0.00%");
        remainingWeightLabel = new JLabel("100.00%");
        currentGradeLabel = new JLabel("0.00%");
        securedGradeLabel = new JLabel("0.00%");
        requiredGradeLabel = new JLabel("N/A");
        projectedGradeLabel = new JLabel("0.00%");
        goalMessageLabel = new JLabel("Enter course and assessment information.");

        JButton calculateButton = new JButton("Calculate Semester Grade");
        calculateButton.addActionListener(e -> calculateSemesterGrade());

        JButton resetButton = new JButton("Reset Calculator");
        resetButton.addActionListener(e -> resetCalculator());

        resultsPanel.add(new JLabel("Completed Course Weight:"));
        resultsPanel.add(completedWeightLabel);

        resultsPanel.add(new JLabel("Remaining Course Weight:"));
        resultsPanel.add(remainingWeightLabel);

        resultsPanel.add(new JLabel("Current Overall Average:"));
        resultsPanel.add(currentGradeLabel);

        resultsPanel.add(new JLabel("Grade Secured So Far:"));
        resultsPanel.add(securedGradeLabel);

        resultsPanel.add(new JLabel("Average Needed for Target:"));
        resultsPanel.add(requiredGradeLabel);

        resultsPanel.add(new JLabel("Projected Final Grade:"));
        resultsPanel.add(projectedGradeLabel);

        resultsPanel.add(calculateButton);
        resultsPanel.add(resetButton);

        resultsPanel.add(new JLabel("Goal Status:"));
        resultsPanel.add(goalMessageLabel);

        return resultsPanel;
    }

    private void addAssessment() {
        AssessmentData assessment = getAssessmentDataFromFields();

        if (assessment == null) {
            return;
        }

        double currentTotalWeight = getTotalWeight();

        if (currentTotalWeight + assessment.weight > 100.0) {
            showError(
                    "The total assessment weight cannot be greater than 100%."
            );
            return;
        }

        tableModel.addRow(new Object[] {
                assessment.name,
                assessment.weight,
                assessment.score,
                assessment.total,
                assessment.grade,
                assessment.contribution
        });

        clearAssessmentInput();
        calculateSemesterGrade();
    }

    private void updateSelectedAssessment() {
        int selectedRow = assessmentTable.getSelectedRow();

        if (selectedRow == -1) {
            showError("Select an assessment from the table to update.");
            return;
        }

        AssessmentData assessment = getAssessmentDataFromFields();

        if (assessment == null) {
            return;
        }

        double currentTotalWithoutSelected = getTotalWeight()
                - Double.parseDouble(
                        tableModel.getValueAt(selectedRow, 1).toString()
                );

        if (currentTotalWithoutSelected + assessment.weight > 100.0) {
            showError(
                    "The total assessment weight cannot be greater than 100%."
            );
            return;
        }

        tableModel.setValueAt(assessment.name, selectedRow, 0);
        tableModel.setValueAt(assessment.weight, selectedRow, 1);
        tableModel.setValueAt(assessment.score, selectedRow, 2);
        tableModel.setValueAt(assessment.total, selectedRow, 3);
        tableModel.setValueAt(assessment.grade, selectedRow, 4);
        tableModel.setValueAt(assessment.contribution, selectedRow, 5);

        clearAssessmentInput();
        calculateSemesterGrade();
    }

    private void removeSelectedAssessment() {
        int selectedRow = assessmentTable.getSelectedRow();

        if (selectedRow == -1) {
            showError("Select an assessment from the table to remove.");
            return;
        }

        tableModel.removeRow(selectedRow);
        clearAssessmentInput();
        calculateSemesterGrade();
    }

    private void loadSelectedAssessmentIntoFields() {
        int selectedRow = assessmentTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        assessmentNameField.setText(
                tableModel.getValueAt(selectedRow, 0).toString()
        );

        assessmentWeightField.setText(
                tableModel.getValueAt(selectedRow, 1).toString()
        );

        assessmentScoreField.setText(
                tableModel.getValueAt(selectedRow, 2).toString()
        );

        assessmentTotalField.setText(
                tableModel.getValueAt(selectedRow, 3).toString()
        );

        assessmentGradeField.setText(
                String.format(
                        "%.2f",
                        Double.parseDouble(
                                tableModel.getValueAt(selectedRow, 4).toString()
                        )
                )
        );
    }

    private AssessmentData getAssessmentDataFromFields() {
        String assessmentName = assessmentNameField.getText().trim();

        if (assessmentName.isEmpty()) {
            showError("Enter an assessment name.");
            return null;
        }

        try {
            double weight = Double.parseDouble(
                    assessmentWeightField.getText().trim()
            );

            double score = Double.parseDouble(
                    assessmentScoreField.getText().trim()
            );

            double total = Double.parseDouble(
                    assessmentTotalField.getText().trim()
            );

            if (weight <= 0 || weight > 100) {
                showError("Assessment weight must be greater than 0 and no more than 100.");
                return null;
            }

            if (score < 0 || total <= 0) {
                showError("Score must be 0 or greater, and total must be greater than 0.");
                return null;
            }

            if (score > total) {
                showError("Assessment score cannot be greater than the total.");
                return null;
            }

            double grade = (score / total) * 100;
            double contribution = (grade / 100) * weight;

            assessmentGradeField.setText(String.format("%.2f", grade));

            return new AssessmentData(
                    assessmentName,
                    weight,
                    score,
                    total,
                    grade,
                    contribution
            );

        } catch (NumberFormatException e) {
            showError("Enter valid numbers for assessment weight, score, and total.");
            return null;
        }
    }

    private void calculateSemesterGrade() {
        if (tableModel.getRowCount() == 0) {
            completedWeightLabel.setText("0.00%");
            remainingWeightLabel.setText("100.00%");
            currentGradeLabel.setText("0.00%");
            securedGradeLabel.setText("0.00%");
            requiredGradeLabel.setText("N/A");
            projectedGradeLabel.setText("0.00%");
            goalMessageLabel.setText("Add assessments to calculate your grade.");
            return;
        }

        double targetGrade;

        try {
            targetGrade = Double.parseDouble(
                    targetGradeField.getText().trim()
            );

            if (targetGrade < 0 || targetGrade > 100) {
                showError("Target final grade must be between 0 and 100.");
                return;
            }

        } catch (NumberFormatException e) {
            showError("Enter a valid target final grade before calculating.");
            return;
        }

        double completedWeight = getTotalWeight();
        double securedGrade = getTotalContribution();
        double remainingWeight = 100.0 - completedWeight;

        double currentOverallAverage = 0.0;

        if (completedWeight > 0) {
            currentOverallAverage =
                    (securedGrade / completedWeight) * 100;
        }

        double requiredAverage = 0.0;

        if (remainingWeight > 0) {
            requiredAverage =
                    ((targetGrade - securedGrade) / remainingWeight) * 100;
        }

        completedWeightLabel.setText(
                String.format("%.2f%%", completedWeight)
        );

        remainingWeightLabel.setText(
                String.format("%.2f%%", remainingWeight)
        );

        currentGradeLabel.setText(
                String.format("%.2f%%", currentOverallAverage)
        );

        securedGradeLabel.setText(
                String.format("%.2f%%", securedGrade)
        );

        if (remainingWeight <= 0) {
            requiredGradeLabel.setText("Course is complete");
            projectedGradeLabel.setText(
                    String.format("%.2f%%", securedGrade)
            );

            if (securedGrade >= targetGrade) {
                goalMessageLabel.setText("Congratulations — you achieved your target.");
            } else {
                goalMessageLabel.setText("Your final grade is below the target.");
            }

            return;
        }

        projectedGradeLabel.setText(
                String.format("%.2f%%", currentOverallAverage)
        );

        if (requiredAverage <= 0) {
            requiredGradeLabel.setText("Target already secured");
            goalMessageLabel.setText(
                    "You have already earned enough to reach your target."
            );
        } else if (requiredAverage > 100) {
            requiredGradeLabel.setText(
                    String.format("%.2f%%", requiredAverage)
            );

            goalMessageLabel.setText(
                    "Your target is not possible with the remaining course weight."
            );
        } else {
            requiredGradeLabel.setText(
                    String.format("%.2f%%", requiredAverage)
            );

            goalMessageLabel.setText(
                    "You need this average on all remaining assessments."
            );
        }
    }

    private double getTotalWeight() {
        double totalWeight = 0.0;

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            totalWeight += Double.parseDouble(
                    tableModel.getValueAt(row, 1).toString()
            );
        }

        return totalWeight;
    }

    private double getTotalContribution() {
        double totalContribution = 0.0;

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            totalContribution += Double.parseDouble(
                    tableModel.getValueAt(row, 5).toString()
            );
        }

        return totalContribution;
    }

    private void clearAssessmentInput() {
        assessmentNameField.setText("");
        assessmentWeightField.setText("");
        assessmentScoreField.setText("");
        assessmentTotalField.setText("");
        assessmentGradeField.setText("");
        assessmentTable.clearSelection();

        assessmentNameField.requestFocus();
    }

    private void resetCalculator() {
        courseNameField.setText("");
        targetGradeField.setText("");

        tableModel.setRowCount(0);
        clearAssessmentInput();

        completedWeightLabel.setText("0.00%");
        remainingWeightLabel.setText("100.00%");
        currentGradeLabel.setText("0.00%");
        securedGradeLabel.setText("0.00%");
        requiredGradeLabel.setText("N/A");
        projectedGradeLabel.setText("0.00%");
        goalMessageLabel.setText("Enter course and assessment information.");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private static class AssessmentData {
        private String name;
        private double weight;
        private double score;
        private double total;
        private double grade;
        private double contribution;

        public AssessmentData(
                String name,
                double weight,
                double score,
                double total,
                double grade,
                double contribution
        ) {
            this.name = name;
            this.weight = weight;
            this.score = score;
            this.total = total;
            this.grade = grade;
            this.contribution = contribution;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GradeCalculatorGUI app = new GradeCalculatorGUI();
            app.setVisible(true);
        });
    }
}
