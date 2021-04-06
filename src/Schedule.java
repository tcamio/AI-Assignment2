public class Schedule {
  private int[][] schedule;

  Schedule(int nRooms, int nTimeSlots) {
    schedule = new int[nRooms][nTimeSlots];
  }

  public int[][] getSchedule() {
      return schedule;
  }

  public void setSchedule(int[][] schedule) {
      this.schedule = schedule;
  }

  @Override
  public String toString() {
    String s = "";
    for(int room = 0; room < schedule.length; room++){
      s += "Room " + room + "\n";
      for(int timeSlot = 0; timeSlot < schedule[room].length; timeSlot++){
        s+= "Times slot " + timeSlot + " " + schedule[room][timeSlot] + "\n";
      }
      s+="\n\n";
    }
    return s;
  }
}
