import java.io.File;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;

public class Timer {
    private int seconds;
    private JLabel label = new JLabel();
    private int minutesPart;
    private int secondsPart;
    private String time;
    private int initialSeconds;
    private boolean running = true;
    private Thread thread;

    public Timer(int minutes) {
        this.initialSeconds = minutes * 60;
        this.seconds = initialSeconds;
        Font poppinsFont = null;
        Font poppinsFontBold = null;
        try {
            poppinsFont = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src\\font\\Poppins-Regular.ttf"));
            poppinsFontBold = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src\\font\\Poppins-Bold.ttf"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        this.label.setFont(poppinsFontBold.deriveFont(18f));
        this.label.setForeground(Color.decode("#4467df"));
        this.minutesPart = seconds / 60;
        this.secondsPart = seconds % 60;
        this.time = String.format("%02d:%02d", minutesPart, secondsPart);
        this.label.setText(time);
    }

    public String getTime() {
        return this.time;
    }

    public void start() {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(this::run);
            thread.start();
        }
    }

    private void run() {
        while (running && seconds >= 0) {
            minutesPart = seconds / 60;
            secondsPart = seconds % 60;
            time = String.format("%02d:%02d", minutesPart, secondsPart);
            SwingUtilities.invokeLater(() -> label.setText(time));
            seconds--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        SwingUtilities.invokeLater(() -> label.setText("Time's up!"));
    }

    public JLabel getLabel() {
        return label;
    }

    public boolean hasExpired() {
        return seconds <= 0;
    }

    public void stopTimer() {
        this.running = false;
        thread.interrupt();
    }

    public void restart() {
        if (thread != null && thread.isAlive()) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.running = true;
        this.seconds = initialSeconds;
        this.minutesPart = seconds / 60;
        this.secondsPart = seconds % 60;
        this.time = String.format("%02d:%02d", minutesPart, secondsPart);
        SwingUtilities.invokeLater(() -> label.setText(time));
    }

    public int getSeconds() {
        return this.seconds;
    }

}