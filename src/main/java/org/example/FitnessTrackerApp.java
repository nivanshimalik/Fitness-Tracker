import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FitnessTrackerApp extends JFrame {
    private JTextField stepsField;
    private JTextField distanceField;
    private JButton trackButton;
    private JLabel resultLabel;
    private JTextField ageField;
    private JTextField weightField;
    private JTextField heightField;
    private JComboBox<String> activityLevelCombo;
    private JTextField goalCaloriesField;
    private JButton bmiCalculatorButton;

    public FitnessTrackerApp() {
        setTitle("Health and Fitness Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 600);


        // Create and configure components
        stepsField = new JTextField(10);
        distanceField = new JTextField(10);
        trackButton = new JButton("Track Activity");
        resultLabel = new JLabel();
        ageField = new JTextField(10);
        weightField = new JTextField(10);
        heightField = new JTextField(10);
        activityLevelCombo = new JComboBox<>(new String[]{"Sedentary", "Lightly Active", "Moderately Active", "Very Active"});
        goalCaloriesField = new JTextField(10);
        bmiCalculatorButton = new JButton("BMI Calculator");

        // Add action listener to BMI calculator button
        bmiCalculatorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openBMICalculator();
            }
        });

        // Add action listener to trackButton
        trackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Parse input values
                    int steps = Integer.parseInt(stepsField.getText());
                    double distance = Double.parseDouble(distanceField.getText());
                    int age = Integer.parseInt(ageField.getText());
                    double weight = Double.parseDouble(weightField.getText());
                    double height = Double.parseDouble(heightField.getText());
                    int activityLevelIndex = activityLevelCombo.getSelectedIndex();

                    // Calculate estimated calories burned
                    double caloriesBurned = calculateCaloriesBurned(steps, distance, age, weight, height);
                    resultLabel.setText("Calories Burned: " + caloriesBurned);

                    // Calculate recommended daily calorie intake
                    double recommendedCalories = calculateRecommendedCalories(age, weight, height, activityLevelIndex);
                    JOptionPane.showMessageDialog(FitnessTrackerApp.this, "Recommended Daily Calories: " + recommendedCalories, "Recommendation", JOptionPane.INFORMATION_MESSAGE);

                    // Calculate goal achievement details
                    if (!goalCaloriesField.getText().isEmpty()) {
                        int goalCalories = Integer.parseInt(goalCaloriesField.getText());
                        double calorieDeficitPerDay = recommendedCalories - caloriesBurned;
                        int daysToAchieveGoal = (int) Math.ceil((goalCalories - calculateBMR(age, weight, height)) / calorieDeficitPerDay);
                        JOptionPane.showMessageDialog(FitnessTrackerApp.this, "Days to achieve goal: " + daysToAchieveGoal, "Goal Achievement", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(FitnessTrackerApp.this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        addLabelField(inputPanel, "Age:", ageField);
        addLabelField(inputPanel, "Weight (kg):", weightField);
        addLabelField(inputPanel, "Height (cm):", heightField);
        addLabelField(inputPanel, "Activity Level:", activityLevelCombo);
        addLabelField(inputPanel, "Steps taken:", stepsField);
        addLabelField(inputPanel, "Distance (km):", distanceField);
        addLabelField(inputPanel, "Goal Calories:", goalCaloriesField);

        // Add trackButton and BMI calculator button to input panel
        inputPanel.add(trackButton);
        inputPanel.add(bmiCalculatorButton);

        // Create result panel for displaying output
        JPanel resultPanel = new JPanel();
        resultPanel.add(resultLabel);

        // Add panels to frame
        add(inputPanel, BorderLayout.CENTER);
        add(resultPanel, BorderLayout.SOUTH);
    }

    // Helper method to add label-field pair to input panel
    private void addLabelField(JPanel panel, String labelText, JComponent field) {
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        wrapperPanel.add(label);
        wrapperPanel.add(field);
        panel.add(wrapperPanel);
    }

    // Method to calculate estimated calories burned
    private double calculateCaloriesBurned(int steps, double distance, int age, double weight, double height) {
        double caloriesPerStep = 0.04;
        double caloriesPerDistance = 0.1;
        double basalMetabolicRate = calculateBMR(age, weight, height);
        double activityFactor = getActivityFactor(); // Use selected index to get activity factor
        return ((steps * caloriesPerStep) + (distance * caloriesPerDistance)) + (basalMetabolicRate * activityFactor);
    }

    private double calculateBMR(int age, double weight, double height) {
        if (age > 0 && weight > 0 && height > 0) {
            if (age >= 18) {
                return (10 * weight) + (6.25 * height) - (5 * age) + 5; // For adults
            } else {
                return (10 * weight) + (6.25 * height) - (5 * age) - 161; // For adolescents
            }
        }
        return 0;
    }

    // Method to calculate recommended daily calorie intake
    private double calculateRecommendedCalories(int age, double weight, double height, int activityLevelIndex) {
        double basalMetabolicRate = calculateBMR(age, weight, height);
        double activityFactor = getActivityFactor(activityLevelIndex); // Use activity level index to get activity factor
        return basalMetabolicRate * activityFactor;
    }

    // Method to get the activity factor based on selected activity level
    private double getActivityFactor() {
        switch (activityLevelCombo.getSelectedIndex()) {
            case 0: return 1.2; // Sedentary
            case 1: return 1.375; // Lightly Active
            case 2: return 1.55; // Moderately Active
            case 3: return 1.725; // Very Active
            default: return 1.2;
        }
    }

    // Method to get the activity factor based on specified activity level index
    private double getActivityFactor(int activityLevelIndex) {
        switch (activityLevelIndex) {
            case 0: return 1.2; // Sedentary
            case 1: return 1.375; // Lightly Active
            case 2: return 1.55; // Moderately Active
            case 3: return 1.725; // Very Active
            default: return 1.2;
        }
    }

    // Method to open the BMI calculator dialog
    private void openBMICalculator() {
        JFrame bmiFrame = new BMICalculatorDialog();
        bmiFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        bmiFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new FitnessTrackerApp().setVisible(true);
            }
        });
    }
}

class BMICalculatorDialog extends JFrame {
    private JTextField weightField;
    private JTextField heightField;
    private JLabel bmiLabel;

    public BMICalculatorDialog() {
        setTitle("BMI Calculator");
        setSize(450, 400);

        // Create components for BMI calculator dialog
        weightField = new JTextField(10);
        heightField = new JTextField(10);
        JButton calculateButton = new JButton("Calculate BMI");
        bmiLabel = new JLabel();

        // Add action listener to calculateButton
        calculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double weight = Double.parseDouble(weightField.getText());
                    double height = Double.parseDouble(heightField.getText()) / 100.0; // Convert height to meters
                    double bmi = calculateBMI(weight, height);
                    bmiLabel.setText("BMI: " + String.format("%.2f", bmi) + " (" + interpretBMI(bmi) + ")");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BMICalculatorDialog.this, "Invalid input. Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Create panel for BMI calculator dialog
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        addLabelField(panel, "Weight (kg):", weightField);
        addLabelField(panel, "Height (cm):", heightField);
        panel.add(calculateButton);
        panel.add(bmiLabel);

        // Add panel to BMI calculator dialog
        add(panel);
    }

    // Helper method to calculate BMI
    private double calculateBMI(double weight, double height) {
        if (weight > 0 && height > 0) {
            return weight / (height * height);
        }
        return 0;
    }
    

    // Helper method to interpret BMI
    private String interpretBMI(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 24.9) {
            return "Normal Weight";
        } else if (bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    // Helper method to add label-field pair to panel
    private void addLabelField(JPanel panel, String labelText, JComponent field) {
        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        wrapperPanel.add(label);
        wrapperPanel.add(field);
        panel.add(wrapperPanel);
    }

}






