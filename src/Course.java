public class Course {
  private double value;
  private Building preferredLocation;
  private int[] timeSlotValues;
  private int enrolledStudents;

  public double getValue() {
      return value;
  }

  public int getEnrolledStudents() {
      return enrolledStudents;
  }

  public Building getPreferredLocation() {
      return preferredLocation;
  }

  public int[] getTimeSlotValues() {
      return timeSlotValues;
  }

  public void setEnrolledStudents(int enrolledStudents) {
      this.enrolledStudents = enrolledStudents;
  }

  public void setPreferredLocation(Building preferredLocation) {
      this.preferredLocation = preferredLocation;
  }

  public void setTimeSlotValues(int[] timeSlotValues) {
      this.timeSlotValues = timeSlotValues;
  }

  public void setValue(double value) {
      this.value = value;
  }
}
