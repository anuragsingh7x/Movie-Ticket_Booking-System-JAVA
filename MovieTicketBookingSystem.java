import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MovieTicketBookingSystem extends JFrame implements ActionListener {
    private JComboBox<String> movieList;
    private JButton[][] seats = new JButton[5][8];
    private ArrayList<String> selectedSeats = new ArrayList<>();
    private JLabel totalLabel;
    private int ticketPrice = 150;

    public MovieTicketBookingSystem() {
        setTitle("🎬 Movie Ticket Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Header
        JLabel header = new JLabel("🎟️ Welcome to Movie Ticket Booking System", JLabel.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(0x1565C0));
        header.setForeground(Color.WHITE);
        add(header, BorderLayout.NORTH);

        // Movie selection
        JPanel moviePanel = new JPanel();
        moviePanel.add(new JLabel("Select Movie: "));
        String[] movies = {"Avengers", "Inception", "Dangal", "3 Idiots", "KGF 2"};
        movieList = new JComboBox<>(movies);
        moviePanel.add(movieList);
        add(moviePanel, BorderLayout.SOUTH);

        // Seat layout
        JPanel seatPanel = new JPanel(new GridLayout(5, 8, 10, 10));
        seatPanel.setBorder(BorderFactory.createTitledBorder("Select Your Seats"));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                String seatNo = (char) ('A' + i) + String.valueOf(j + 1);
                seats[i][j] = new JButton(seatNo);
                seats[i][j].setBackground(Color.LIGHT_GRAY);
                seats[i][j].setFocusPainted(false);
                seats[i][j].addActionListener(this);
                seatPanel.add(seats[i][j]);
            }
        }
        add(seatPanel, BorderLayout.CENTER);

        // Bill area
        JPanel billPanel = new JPanel();
        billPanel.setLayout(new FlowLayout());
        JButton bookButton = new JButton("Book Tickets");
        bookButton.setBackground(new Color(0x2E7D32));
        bookButton.setForeground(Color.WHITE);
        bookButton.addActionListener(e -> generateBill());
        totalLabel = new JLabel("Total: ₹0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        billPanel.add(totalLabel);
        billPanel.add(bookButton);
        add(billPanel, BorderLayout.NORTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton seat = (JButton) e.getSource();
        String seatNo = seat.getText();

        if (selectedSeats.contains(seatNo)) {
            selectedSeats.remove(seatNo);
            seat.setBackground(Color.LIGHT_GRAY);
        } else {
            selectedSeats.add(seatNo);
            seat.setBackground(new Color(0xFFB300)); // highlight selected seat
        }
        totalLabel.setText("Total: ₹" + (selectedSeats.size() * ticketPrice));
    }

    private void generateBill() {
        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one seat!");
            return;
        }
        String movie = (String) movieList.getSelectedItem();
        int totalAmount = selectedSeats.size() * ticketPrice;

        StringBuilder bill = new StringBuilder();
        bill.append("🎬 Movie: ").append(movie).append("\n");
        bill.append("💺 Seats: ").append(selectedSeats).append("\n");
        bill.append("💰 Total Amount: ₹").append(totalAmount).append("\n\n");
        bill.append("✅ Booking Successful! Enjoy your show!");

        JOptionPane.showMessageDialog(this, bill.toString(), "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

        // Reset seats
        for (String seatNo : selectedSeats) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 8; j++) {
                    if (seats[i][j].getText().equals(seatNo)) {
                        seats[i][j].setEnabled(false);
                        seats[i][j].setBackground(Color.GRAY);
                    }
                }
            }
        }
        selectedSeats.clear();
        totalLabel.setText("Total: ₹0");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MovieTicketBookingSystem::new);
    }
}
