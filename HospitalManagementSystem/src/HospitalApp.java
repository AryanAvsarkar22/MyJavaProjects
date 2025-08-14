import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class HospitalApp extends JFrame {
    private JTabbedPane tabs;

    // Patients components
    private JTextField pName, pAge, pPhone;
    private JComboBox<String> pGender;
    private JTextArea pAddress;
    private JTable pTable;

    // Doctors components
    private JTextField dName, dSpec, dPhone;
    private JTable dTable;

    // Appointments components
    private JComboBox<Item> aPatient, aDoctor;
    private JTextField aDate, aTime, aNotes;
    private JTable aTable;

    public HospitalApp() {
        setTitle("Hospital Management System - Swing (MySQL)");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabs = new JTabbedPane();
        tabs.add("Patients", buildPatientsPanel());
        tabs.add("Doctors", buildDoctorsPanel());
        tabs.add("Appointments", buildAppointmentsPanel());
        setContentPane(tabs);

        refreshPatients();
        refreshDoctors();
        refreshPatientDoctorCombos();
        refreshAppointments();
    }

    // ===== Patients Panel =====
    private JPanel buildPatientsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        pName = new JTextField();
        pAge = new JTextField();
        pGender = new JComboBox<>(new String[]{"Male","Female","Other"});
        pPhone = new JTextField();
        pAddress = new JTextArea(3, 20);
        form.add(new JLabel("Name:")); form.add(pName);
        form.add(new JLabel("Age:")); form.add(pAge);
        form.add(new JLabel("Gender:")); form.add(pGender);
        form.add(new JLabel("Phone:")); form.add(pPhone);
        form.add(new JLabel("Address:")); form.add(new JScrollPane(pAddress));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add");
        JButton update = new JButton("Update Selected");
        JButton delete = new JButton("Delete Selected");
        actions.add(add); actions.add(update); actions.add(delete);

        add.addActionListener(e -> addPatient());
        update.addActionListener(e -> updatePatient());
        delete.addActionListener(e -> deletePatient());

        pTable = new JTable(new DefaultTableModel(new Object[]{"ID","Name","Age","Gender","Phone","Address"}, 0));
        panel.add(form, BorderLayout.NORTH);
        panel.add(actions, BorderLayout.CENTER);
        panel.add(new JScrollPane(pTable), BorderLayout.SOUTH);
        return panel;
    }

    private void addPatient() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO patients(name,age,gender,phone,address) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pName.getText());
            ps.setInt(2, Integer.parseInt(pAge.getText()));
            ps.setString(3, pGender.getSelectedItem().toString());
            ps.setString(4, pPhone.getText());
            ps.setString(5, pAddress.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient added.");
            clearPatientForm();
            refreshPatients();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void updatePatient() {
        int row = pTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to update."); return; }
        int id = Integer.parseInt(pTable.getValueAt(row, 0).toString());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE patients SET name=?, age=?, gender=?, phone=?, address=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pName.getText());
            ps.setInt(2, Integer.parseInt(pAge.getText()));
            ps.setString(3, pGender.getSelectedItem().toString());
            ps.setString(4, pPhone.getText());
            ps.setString(5, pAddress.getText());
            ps.setInt(6, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient updated.");
            clearPatientForm();
            refreshPatients();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void deletePatient() {
        int row = pTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = Integer.parseInt(pTable.getValueAt(row, 0).toString());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM patients WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Patient deleted.");
            clearPatientForm();
            refreshPatients();
            refreshAppointments();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void clearPatientForm() {
        pName.setText("");
        pAge.setText("");
        pGender.setSelectedIndex(0);
        pPhone.setText("");
        pAddress.setText("");
    }

    private void refreshPatients() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM patients ORDER BY id DESC");
            DefaultTableModel model = (DefaultTableModel) pTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"), rs.getInt("age"),
                    rs.getString("gender"), rs.getString("phone"), rs.getString("address")
                });
            }
            pTable.getSelectionModel().addListSelectionListener(e -> {
                int r = pTable.getSelectedRow();
                if (r >= 0) {
                    pName.setText(pTable.getValueAt(r,1).toString());
                    pAge.setText(pTable.getValueAt(r,2).toString());
                    pGender.setSelectedItem(pTable.getValueAt(r,3).toString());
                    pPhone.setText(pTable.getValueAt(r,4).toString());
                    pAddress.setText(pTable.getValueAt(r,5).toString());
                }
            });
            refreshPatientDoctorCombos();
        } catch (Exception ex) {
            System.out.println("refreshPatients error: " + ex.getMessage());
        }
    }

    // ===== Doctors Panel =====
    private JPanel buildDoctorsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        dName = new JTextField();
        dSpec = new JTextField();
        dPhone = new JTextField();
        form.add(new JLabel("Name:")); form.add(dName);
        form.add(new JLabel("Specialization:")); form.add(dSpec);
        form.add(new JLabel("Phone:")); form.add(dPhone);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Add");
        JButton update = new JButton("Update Selected");
        JButton delete = new JButton("Delete Selected");
        actions.add(add); actions.add(update); actions.add(delete);

        add.addActionListener(e -> addDoctor());
        update.addActionListener(e -> updateDoctor());
        delete.addActionListener(e -> deleteDoctor());

        dTable = new JTable(new DefaultTableModel(new Object[]{"ID","Name","Specialization","Phone"}, 0));
        panel.add(form, BorderLayout.NORTH);
        panel.add(actions, BorderLayout.CENTER);
        panel.add(new JScrollPane(dTable), BorderLayout.SOUTH);
        return panel;
    }

    private void addDoctor() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO doctors(name,specialization,phone) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dName.getText());
            ps.setString(2, dSpec.getText());
            ps.setString(3, dPhone.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor added.");
            dName.setText(""); dSpec.setText(""); dPhone.setText("");
            refreshDoctors();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void updateDoctor() {
        int row = dTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to update."); return; }
        int id = Integer.parseInt(dTable.getValueAt(row, 0).toString());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE doctors SET name=?, specialization=?, phone=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dName.getText());
            ps.setString(2, dSpec.getText());
            ps.setString(3, dPhone.getText());
            ps.setInt(4, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor updated.");
            dName.setText(""); dSpec.setText(""); dPhone.setText("");
            refreshDoctors();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void deleteDoctor() {
        int row = dTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = Integer.parseInt(dTable.getValueAt(row, 0).toString());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM doctors WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Doctor deleted.");
            refreshDoctors();
            refreshAppointments();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void refreshDoctors() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM doctors ORDER BY id DESC");
            DefaultTableModel model = (DefaultTableModel) dTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("name"),
                    rs.getString("specialization"), rs.getString("phone")
                });
            }
            dTable.getSelectionModel().addListSelectionListener(e -> {
                int r = dTable.getSelectedRow();
                if (r >= 0) {
                    dName.setText(dTable.getValueAt(r,1).toString());
                    dSpec.setText(dTable.getValueAt(r,2).toString());
                    dPhone.setText(dTable.getValueAt(r,3).toString());
                }
            });
            refreshPatientDoctorCombos();
        } catch (Exception ex) {
            System.out.println("refreshDoctors error: " + ex.getMessage());
        }
    }

    // ===== Appointments Panel =====
    private JPanel buildAppointmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridLayout(0,2,8,8));
        aPatient = new JComboBox<>();
        aDoctor = new JComboBox<>();
        aDate = new JTextField("2025-01-01"); // yyyy-MM-dd
        aTime = new JTextField("10:00:00");   // HH:mm:ss
        aNotes = new JTextField();

        form.add(new JLabel("Patient:")); form.add(aPatient);
        form.add(new JLabel("Doctor:")); form.add(aDoctor);
        form.add(new JLabel("Date (yyyy-MM-dd):")); form.add(aDate);
        form.add(new JLabel("Time (HH:mm:ss):")); form.add(aTime);
        form.add(new JLabel("Notes:")); form.add(aNotes);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton add = new JButton("Create Appointment");
        JButton delete = new JButton("Delete Selected");
        actions.add(add); actions.add(delete);

        add.addActionListener(e -> addAppointment());
        delete.addActionListener(e -> deleteAppointment());

        aTable = new JTable(new DefaultTableModel(new Object[]{"ID","Patient","Doctor","Date","Time","Notes"}, 0));
        panel.add(form, BorderLayout.NORTH);
        panel.add(actions, BorderLayout.CENTER);
        panel.add(new JScrollPane(aTable), BorderLayout.SOUTH);
        return panel;
    }

    private void addAppointment() {
        Item p = (Item) aPatient.getSelectedItem();
        Item d = (Item) aDoctor.getSelectedItem();
        if (p == null || d == null) { JOptionPane.showMessageDialog(this, "Select patient and doctor."); return; }
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO appointments(patient_id, doctor_id, date, time, notes) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, p.id);
            ps.setInt(2, d.id);
            ps.setString(3, aDate.getText());
            ps.setString(4, aTime.getText());
            ps.setString(5, aNotes.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment created.");
            aNotes.setText("");
            refreshAppointments();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void deleteAppointment() {
        int row = aTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a row to delete."); return; }
        int id = Integer.parseInt(aTable.getValueAt(row, 0).toString());
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM appointments WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment deleted.");
            refreshAppointments();
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    private void refreshAppointments() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT a.id, p.name as patient, d.name as doctor, a.date, a.time, a.notes " +
                           "FROM appointments a " +
                           "JOIN patients p ON a.patient_id = p.id " +
                           "JOIN doctors d ON a.doctor_id = d.id " +
                           "ORDER BY a.id DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            DefaultTableModel model = (DefaultTableModel) aTable.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"), rs.getString("patient"), rs.getString("doctor"),
                    rs.getString("date"), rs.getString("time"), rs.getString("notes")
                });
            }
        } catch (Exception ex) {
            System.out.println("refreshAppointments error: " + ex.getMessage());
        }
    }

    private void refreshPatientDoctorCombos() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            aPatient.removeAllItems();
            aDoctor.removeAllItems();

            Statement sp = conn.createStatement();
            ResultSet rp = sp.executeQuery("SELECT id, name FROM patients ORDER BY name");
            while (rp.next()) aPatient.addItem(new Item(rp.getInt("id"), rp.getString("name")));

            Statement sd = conn.createStatement();
            ResultSet rd = sd.executeQuery("SELECT id, name FROM doctors ORDER BY name");
            while (rd.next()) aDoctor.addItem(new Item(rd.getInt("id"), rd.getString("name")));
        } catch (Exception ex) {
            System.out.println("refreshPatientDoctorCombos error: " + ex.getMessage());
        }
    }

    // Helper class to store id+label in JComboBox
    private static class Item {
        int id; String label;
        Item(int id, String label) { this.id = id; this.label = label; }
        @Override public String toString() { return label; }
    }
}
