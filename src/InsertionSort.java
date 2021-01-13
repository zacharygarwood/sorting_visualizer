import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class InsertionSort extends JPanel {
    private static final long serialVersionUID = 1L;

    //Dimensions of window
    private final int WIDTH = 1000;
    private final int HEIGHT = WIDTH * 9 / 16;

    //Variables used for the bars
    private final int SIZE = 100;
    private final double BAR_WIDTH = (double)WIDTH / SIZE;
    private double[] bar_height = new double[SIZE];

    //Used for shuffling and sorting
    private SwingWorker<Void, Void> shuffler, sorter;
    private int current_index, traversing_index;

    private InsertionSort() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initBarHeight();
        initSorter();
        initShuffler();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //Needed to use doubles as inputs when drawing the bars
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.WHITE);
        Rectangle2D.Double bar;

        //Draws the bars to be sorted
        for(int i = 0; i < SIZE; i++) {
            bar = new Rectangle2D.Double(i * BAR_WIDTH, bar_height[i], BAR_WIDTH, HEIGHT - bar_height[i]);
            g2d.draw(bar);
        }

        //Draws the traversing index the color red
        g2d.setColor(Color.RED);
        bar = new Rectangle2D.Double(traversing_index * BAR_WIDTH, bar_height[traversing_index], BAR_WIDTH, HEIGHT - bar_height[traversing_index]);
        g2d.draw(bar);

    }

    /*
     * Sorts the bars in the array
     * Uses the Insertion Sort algorithm
     */
    private void initSorter() {
        sorter = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                //Insertion Sort logic
                for(current_index = 1; current_index < SIZE; current_index++) {
                    traversing_index = current_index;
                    while(traversing_index > 0 && bar_height[traversing_index] < bar_height[traversing_index - 1]) {
                        swap(traversing_index, traversing_index - 1);
                        traversing_index--;

                        //Used so you can see the sorting
                        Thread.sleep(1);
                        repaint();
                    }
                }
                current_index = 0;
                traversing_index = 0;

                return null;
            }
        };
    }

    /*
     * Shuffles the bars in a random order
     */
    private void initShuffler() {
        shuffler = new SwingWorker<Void, Void>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                //Logic for shuffling
                for(int i = 0; i < SIZE; i++) {
                    int random_index = new Random().nextInt(SIZE);
                    swap(i , random_index);

                    //Used so you can see the shuffling
                    Thread.sleep(10);
                    repaint();
                }

                return null;
            }

            @Override
            public void done() {
                super.done();
                sorter.execute();
            }
        };
        shuffler.execute();
    }

    /*
     * Initializes the heights of the bars
     */
    private void initBarHeight() {
        double interval = (double)HEIGHT / SIZE;
        for(int i = 0; i < SIZE; i++) {
            bar_height[i] = i * interval;
        }
    }

    /*
     * Swaps two bars with one another
     */
    private void swap(int indexA, int indexB) {
        double temp = bar_height[indexA];
        bar_height[indexA] = bar_height[indexB];
        bar_height[indexB] = temp;
    }

    public static void main(String args[]) {
        //Creates JComponents
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Insertion Sort Visualizer");
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new InsertionSort());
            frame.validate();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
