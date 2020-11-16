package life;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;


public class GameOfLife extends JFrame {
    GameEngine gameEngine;
    JLabel labelGeneration;
    JLabel labelAlive;
    int speed;
    Timer timer;


    public static void main(String[] args) {
        new GameOfLife();
    }

    public GameOfLife() {
        int seed = 1;
        int size = 55;
        this.gameEngine = new GameEngine(size, seed);
        Cells cells = new Cells();
        speed = 100;

        setSize(gameEngine.getSize() * 14 + 170, gameEngine.getSize() * 14 - 20);
        setTitle("Game of Life");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel labelContainer = new JPanel();
        labelGeneration = new JLabel("Generation #" + gameEngine.getNumberOfGenerations());
        labelAlive = new JLabel("Alive: " + gameEngine.getAliveCells());
        labelGeneration.setName("GenerationLabel");
        labelAlive.setName("AliveLabel");
        labelContainer.add(labelGeneration);
        labelContainer.add(Box.createRigidArea(new Dimension(1, 5)));
        labelContainer.add(labelAlive);
        labelContainer.setLayout(new BoxLayout(labelContainer, BoxLayout.Y_AXIS));
        labelContainer.setVisible(true);

        timer = new Timer(speed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameEngine.mutate();
                repaint();
                updateLabels();
            }
        });
        timer.setRepeats(true);


        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));

        JButton colorChooserButton = new JButton("Choose cell colour");
        colorChooserButton.setBackground(Color.BLACK);
        colorChooserButton.setForeground(Color.WHITE);

        JColorChooser colorChooser = new JColorChooser();
        colorChooser.setPreviewPanel(new JPanel());
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Color background = colorChooser.showDialog(null, "Choose colour", null);
                if (background != null) {
                    colorChooserButton.setBackground(background);
                    if (Color.RGBtoHSB(background.getRed(), background.getGreen(), background.getBlue(), null)[2] < 0.5) {
                        colorChooserButton.setForeground(Color.WHITE);
                    } else {
                        colorChooserButton.setForeground(Color.BLACK);
                    }
                }
                cells.setColour(background);
                cells.repaint();
            }
        };
        colorChooserButton.addActionListener(actionListener);

        JToggleButton jToggleButton = new JToggleButton("Pause/ Resume");
        jToggleButton.setMaximumSize(new Dimension(139, 22));

        jToggleButton.addActionListener(e -> {
            if (!jToggleButton.isSelected()) {
                timer.restart();
            } else {
                timer.stop();
            }
        });

        jToggleButton.setSelected(false);
        jToggleButton.setName("PlayToggleButton");

        JSlider speedSlider = new JSlider();
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(1000);
        speedSlider.setInverted(true);
        speedSlider.setName("Choose speed");
        speedSlider.createStandardLabels(100);
        speedSlider.setSnapToTicks(true);
        speedSlider.setMajorTickSpacing(50);
        speedSlider.setPaintTicks(true);

        speedSlider.addChangeListener(e -> {
            setSpeed(speedSlider.getValue());
        });


        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> cells.reset());
        resetButton.setMaximumSize(new Dimension(139, resetButton.getHeight()));
        resetButton.setName("ResetButton");

        buttonContainer.add(jToggleButton);
        buttonContainer.add(resetButton);
        buttonContainer.add(colorChooserButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(1, 10)));
        buttonContainer.add(new JLabel("Adjust speed"));
        buttonContainer.add(speedSlider);
        buttonContainer.add(Box.createRigidArea(new Dimension(1, 100)));


        JPanel labelsAndButtonsContainer = new JPanel();
        labelsAndButtonsContainer.setLayout(new BoxLayout(labelsAndButtonsContainer, BoxLayout.Y_AXIS));
        labelsAndButtonsContainer.add(labelContainer);
        labelsAndButtonsContainer.add(Box.createRigidArea(new Dimension(1, 20)));
        labelsAndButtonsContainer.add(buttonContainer);


        add(Box.createRigidArea(new Dimension(40, 25)), BorderLayout.NORTH);
        add(cells, BorderLayout.CENTER);
        add(labelsAndButtonsContainer, BorderLayout.WEST);

        setVisible(true);

        timer.start();
    }

    protected void setSpeed(int speed) {
        this.timer.setDelay(speed);
    }


//    @Override
//    public void actionPerformed(ActionEvent e) {
//
//    }

    private class Cells extends JComponent {
        Color color = Color.BLACK;

        @Override
        public void paint(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;
            int y = 0;
            int cellSize = 12;
            boolean[][] currentGeneration = gameEngine.getCurrentGeneration();
            for (boolean[] cellRow : currentGeneration) {
                int x = 5;
                for (boolean cell : cellRow) {
                    g2.setPaint(Color.BLACK);
                    g2.drawRect(x, y, cellSize, cellSize);
                    if (cell) {
                        g2.setPaint(color);
                        g2.fillRect(x, y, cellSize, cellSize);
                    }
                    x += 12;
                }
                y += 12;
            }
        }

        protected void setColour(Color color) {
            this.color = color;
        }

        protected void reset() {
            boolean wasOn = timer.isRunning();
            timer.stop();
            gameEngine = new GameEngine(gameEngine.getSize(), new Random().nextInt());
            updateLabels();
            repaint();
            if (wasOn) {
                timer.start();
            }
        }
    }


    protected void updateLabels() {
        labelAlive.setText("Alive: " + gameEngine.getAliveCells());
        labelGeneration.setText("Generation #" + gameEngine.getNumberOfGenerations());
    }
}
