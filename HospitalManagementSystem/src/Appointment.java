public class Appointment {
    public int id;
    public int patientId;
    public int doctorId;
    public String date; // yyyy-MM-dd
    public String time; // HH:mm:ss
    public String notes;

    public Appointment(int id, int patientId, int doctorId, String date, String time, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }
}
