import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MovieTicketBookingSystem extends JFrame implements ActionListener {
    CardLayout layout;
    JPanel mainPanel, userPanel, seatPanel;
    JTextField nameField, ageField, dateField;
    JComboBox<String> movieBox, slotBox, locationBox, cinemaBox, hallBox;
    JRadioButton male, female, other;
    JButton[][] seats = new JButton[5][8];
    JLabel totalLabel, posterLabel;
    ArrayList<String> selectedSeats = new ArrayList<>();
    String userName, gender, movie, slot, location, date, cinemaType, hallName;
    int age, ticketPrice = 150;

    public MovieTicketBookingSystem() {
        setTitle("🎬 Movie Ticket Booking System");
        setSize(950, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        createUserPanel();
        createSeatPanel();

        mainPanel.add(userPanel, "User");
        mainPanel.add(seatPanel, "Seat");
        add(mainPanel);
        setVisible(true);
    }

    // ======= USER DETAILS PAGE =======
    void createUserPanel() {
        // gradient background
        userPanel = new GradientPanel();
        userPanel.setLayout(new BorderLayout(15, 15));

        JLabel title = new JLabel("🎟️ Movie Ticket Booking", JLabel.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 26));
        title.setOpaque(true);
        title.setBackground(new Color(0x1976D2));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0, 70));
        userPanel.add(title, BorderLayout.NORTH);

        // white glass-like card
        JPanel formCard = new JPanel(new BorderLayout());
        formCard.setBackground(new Color(255, 255, 255, 230));
        formCard.setBorder(new CompoundBorder(
            new EmptyBorder(30, 30, 30, 30),
            new LineBorder(new Color(230, 230, 230), 2, true)
        ));

        JPanel form = new JPanel(new GridLayout(10, 2, 12, 12));
        form.setOpaque(false);

        nameField = new JTextField();
        ageField = new JTextField();
        dateField = new JTextField("dd-mm-yyyy");

        male = new JRadioButton("Male");
        female = new JRadioButton("Female");
        other = new JRadioButton("Other");
        for (JRadioButton rb : new JRadioButton[]{male, female, other}) {
            rb.setOpaque(false);
            rb.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }
        ButtonGroup g = new ButtonGroup();
        g.add(male); g.add(female); g.add(other);

        movieBox = new JComboBox<>(new String[]{"Avengers","Inception","Dangal","3 Idiots","KGF 2"});
        slotBox = new JComboBox<>(new String[]{"10:00 AM","2:00 PM","6:00 PM","9:00 PM"});
        locationBox = new JComboBox<>(new String[]{"Noida","Delhi","Lucknow","Mumbai","Pune"});
        cinemaBox = new JComboBox<>(new String[]{"Normal (₹150)","Gold (₹250)","Premium (₹400)"});
        hallBox = new JComboBox<>(new String[]{"PVR Cinemas","INOX","Cinepolis","Carnival Cinemas"});

        styleComboBox(movieBox); styleComboBox(slotBox);
        styleComboBox(locationBox); styleComboBox(cinemaBox); styleComboBox(hallBox);

        posterLabel = new JLabel();
        posterLabel.setHorizontalAlignment(JLabel.CENTER);
        posterLabel.setPreferredSize(new Dimension(350, 500));
        posterLabel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 2, true),
            new EmptyBorder(5, 5, 5, 5)
        ));
        updatePoster((String) movieBox.getSelectedItem());
        movieBox.addActionListener(e -> updatePoster((String) movieBox.getSelectedItem()));

        cinemaBox.addActionListener(e -> updateTicketPrice());

        JButton next = createButton("Next ➡️", new Color(0x2E7D32));
        next.addActionListener(e -> goNext());

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Age:")); form.add(ageField);
        form.add(new JLabel("Gender:"));
        JPanel gp = new JPanel(); gp.setOpaque(false);
        gp.add(male); gp.add(female); gp.add(other);
        form.add(gp);
        form.add(new JLabel("Location:")); form.add(locationBox);
        form.add(new JLabel("Movie Hall:")); form.add(hallBox);
        form.add(new JLabel("Movie:")); form.add(movieBox);
        form.add(new JLabel("Cinema Type:")); form.add(cinemaBox);
        form.add(new JLabel("Slot:")); form.add(slotBox);
        form.add(new JLabel("Date:")); form.add(dateField);
        form.add(new JLabel("")); form.add(next);

        formCard.add(form, BorderLayout.CENTER);

        JPanel container = new JPanel(new BorderLayout(20, 0));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(20, 60, 20, 60));
        container.add(formCard, BorderLayout.CENTER);
        container.add(posterLabel, BorderLayout.EAST);

        userPanel.add(container, BorderLayout.CENTER);
    }

    // ======= SEAT SELECTION PAGE =======
    void createSeatPanel() {
        seatPanel = new GradientPanel();
        seatPanel.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("🎬 Choose Your Seats", JLabel.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(new Color(0x1976D2));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(0, 70));
        seatPanel.add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(5,8,8,8));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(40, 100, 20, 100));

        for (int i=0;i<5;i++)
            for (int j=0;j<8;j++){
                JButton b=new JButton((char)('A'+i)+""+(j+1));
                b.addActionListener(this);
                b.setBackground(new Color(240,240,240));
                b.setFocusPainted(false);
                b.setFont(new Font("Segoe UI",Font.BOLD,12));
                b.setBorder(new LineBorder(Color.LIGHT_GRAY,1,true));
                seats[i][j]=b;
                grid.add(b);
            }

        JPanel legend=new JPanel(); legend.setOpaque(false);
        legend.add(createLegendBox(new Color(240,240,240),"Available"));
        legend.add(createLegendBox(Color.ORANGE,"Selected"));
        legend.add(createLegendBox(Color.GRAY,"Booked"));

        JPanel bottom=new JPanel(); bottom.setOpaque(false);
        totalLabel=new JLabel("Total: ₹0");
        totalLabel.setFont(new Font("Segoe UI",Font.BOLD,16));
        JButton back=createButton("⬅️ Back",new Color(0xC62828));
        back.addActionListener(e->layout.show(mainPanel,"User"));
        JButton book=createButton("Book ✅",new Color(0x2E7D32));
        book.addActionListener(e->bookTickets());
        bottom.add(back); bottom.add(totalLabel); bottom.add(book);

        seatPanel.add(legend,BorderLayout.NORTH);
        seatPanel.add(grid,BorderLayout.CENTER);
        seatPanel.add(bottom,BorderLayout.SOUTH);
    }

    // ======= HELPERS =======
    void updatePoster(String movieName){
        try{
            ImageIcon icon=new ImageIcon("images/"+movieName+".jpg");
            Image img=icon.getImage().getScaledInstance(350,500,Image.SCALE_SMOOTH);
            posterLabel.setIcon(new ImageIcon(img));
        }catch(Exception e){ posterLabel.setIcon(null); }
    }

    JButton createButton(String text,Color bg){
        JButton b=new JButton(text);
        b.setBackground(bg); b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI",Font.BOLD,15));
        b.setBorder(new EmptyBorder(10,20,10,20));
        return b;
    }

    JPanel createLegendBox(Color c,String txt){
        JPanel p=new JPanel(new FlowLayout(FlowLayout.LEFT,5,2)); p.setOpaque(false);
        JLabel box=new JLabel("■"); box.setForeground(c);
        JLabel l=new JLabel(txt); l.setFont(new Font("Segoe UI",Font.PLAIN,13));
        p.add(box); p.add(l); return p;
    }

    void styleComboBox(JComboBox<String> b){
        b.setFont(new Font("Segoe UI",Font.PLAIN,13));
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
    }

    void updateTicketPrice(){
        String t=(String)cinemaBox.getSelectedItem();
        if(t.contains("Normal"))ticketPrice=150;
        else if(t.contains("Gold"))ticketPrice=250;
        else ticketPrice=400;
        totalLabel.setText("Total: ₹"+selectedSeats.size()*ticketPrice);
    }

    void goNext(){
        String n=nameField.getText().trim(), a=ageField.getText().trim(), d=dateField.getText().trim();
        gender=male.isSelected()?"Male":female.isSelected()?"Female":other.isSelected()?"Other":"";
        if(n.isEmpty()||a.isEmpty()||gender.isEmpty()||d.isEmpty()){JOptionPane.showMessageDialog(this,"Please fill all details!");return;}
        try{age=Integer.parseInt(a);}catch(Exception e){JOptionPane.showMessageDialog(this,"Invalid age!");return;}
        userName=n; movie=(String)movieBox.getSelectedItem(); slot=(String)slotBox.getSelectedItem();
        location=(String)locationBox.getSelectedItem(); date=d;
        cinemaType=(String)cinemaBox.getSelectedItem(); hallName=(String)hallBox.getSelectedItem();
        layout.show(mainPanel,"Seat");
    }

    public void actionPerformed(ActionEvent e){
        JButton b=(JButton)e.getSource(); String s=b.getText();
        if(selectedSeats.contains(s)){selectedSeats.remove(s);b.setBackground(new Color(240,240,240));}
        else{selectedSeats.add(s);b.setBackground(Color.ORANGE);}
        totalLabel.setText("Total: ₹"+selectedSeats.size()*ticketPrice);
    }

    void bookTickets(){
        if(selectedSeats.isEmpty()){JOptionPane.showMessageDialog(this,"Please select at least one seat!");return;}
        int total=selectedSeats.size()*ticketPrice;
        String msg=String.format(
            "🎟️ Booking Confirmed!\n\nName: %s\nAge: %d\nGender: %s\nLocation: %s\nMovie Hall: %s\nMovie: %s\nCinema Type: %s\nSlot: %s\nDate: %s\nSeats: %s\nTotal: ₹%d\n\n✅ Enjoy your show!",
            userName,age,gender,location,hallName,movie,cinemaType,slot,date,selectedSeats,total);
        JOptionPane.showMessageDialog(this,msg,"Booking Successful",JOptionPane.INFORMATION_MESSAGE);
        for(String s:selectedSeats)
            for(JButton[] row:seats)
                for(JButton btn:row)
                    if(btn.getText().equals(s)){btn.setEnabled(false);btn.setBackground(Color.GRAY);}
        selectedSeats.clear(); totalLabel.setText("Total: ₹0");
    }

    public static void main(String[] args){SwingUtilities.invokeLater(MovieTicketBookingSystem::new);}
}

// ======= GRADIENT BACKGROUND PANEL =======
class GradientPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        GradientPaint gp=new GradientPaint(0,0,new Color(233,236,252),getWidth(),getHeight(),new Color(207,216,220));
        g2.setPaint(gp);
        g2.fillRect(0,0,getWidth(),getHeight());
    }
}
