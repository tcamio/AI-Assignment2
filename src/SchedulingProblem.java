import java.util.*;

public class SchedulingProblem {
  private static final int NUM_TIME_SLOTS = 10;
  private static final double MAX_X_COORD = 10;
  private static final double MAX_Y_COORD = 10;
  private static final double DISTANCE_PENALTY = 2.5d;

  private ArrayList<Building> buildings;
  private ArrayList<Room> rooms;
  private ArrayList<Course> courses;

  private Random random;

  SchedulingProblem(long seed) {
    if (seed > 0) {
      random = new Random(seed);
    } else {
      random = new Random();
    }

    buildings = new ArrayList<Building>();
    rooms = new ArrayList<Room>();
    courses = new ArrayList<Course>();
  }

  public void createRandomInstance(int nBuildings, int nRooms, int nCourses) {
    // create random buildings
    for (int i = 0; i < nBuildings; i++) {
      Building tmp = new Building();
      tmp.setxCoord(random.nextDouble() * MAX_X_COORD);
      tmp.setyCoord(random.nextDouble() * MAX_Y_COORD);;
      buildings.add(tmp);
    }

    // create random rooms
    for (int i = 0; i < nRooms; i++) {
      Room tmp = new Room();
      tmp.setBuilding(buildings.get((int) (random.nextDouble() * nBuildings)));;
      tmp.setCapacity(((int)(random.nextDouble() * 70)) + 30);
      rooms.add(tmp);
    }

    // create random courses
    for (int i = 0; i < nCourses; i++) {
      Course tmp = new Course();
      tmp.setEnrolledStudents(((int) (random.nextDouble() * 70)) + 30);
      tmp.setPreferredLocation(buildings.get((int) (random.nextDouble() * nBuildings)));
      tmp.setValue(random.nextDouble() * 100);
      tmp.setTimeSlotValues(new int[NUM_TIME_SLOTS]);
      for (int j = 0; j < NUM_TIME_SLOTS; j++) {
        if (random.nextDouble() < 0.3d) {
          tmp.getTimeSlotValues()[j] = 0;
        } else {
          tmp.getTimeSlotValues()[j] = (int)(random.nextDouble() * 10);
        }
      }
      courses.add(tmp);
    }
  }

  public Schedule getEmptySchedule() {
    Schedule tmp = new Schedule(rooms.size(), NUM_TIME_SLOTS);

    for (int i = 0; i < rooms.size(); i++) {
      for (int j = 0; j < NUM_TIME_SLOTS; j++) {
        tmp.getSchedule()[i][j] = -1;
      }
    }
    return tmp;
  }

  public double evaluateSchedule(Schedule solutionSchedule) {
    int[][] s = solutionSchedule.getSchedule();

    if (s.length != rooms.size() || s[0].length != NUM_TIME_SLOTS) {
      System.out.println("ERROR: invalid schedule dimensions");
      return Double.NEGATIVE_INFINITY;
    }

    // check that all classes are assigned only once
    int[] assigned = new int[courses.size()];
    for (int i = 0; i < s.length; i++) {
      for (int j = 0; j < s[0].length; j++) {

        // indicates an unassigned time slot
        if (s[i][j] < 0 || s[i][j] > courses.size()) continue;

        // class that hase been scheduled more than once
        if (assigned[s[i][j]] > 0) {
          System.out.println("ERROR: Invalid schedule");
          return Double.NEGATIVE_INFINITY;
        }

        assigned[s[i][j]]++;
      }
    }

    double value = 0d;

    for (int i = 0; i < s.length; i++) {
      for (int j = 0; j < s[0].length; j++) {

        // indicates an unassigned time slot
        if (s[i][j] < 0 || s[i][j] > courses.size()) continue;

        Course c = courses.get(s[i][j]);
        Room r = rooms.get(i);

        // course was not assigned to a feasible time slot
        if (c.getTimeSlotValues()[j] <= 0) {
          continue;
        }

        // course was assigned to a room that is too small
        if (c.getEnrolledStudents() > r.getCapacity()) {
          continue;
        }

        // add in the value for the class
        value += c.getValue();
        value += c.getTimeSlotValues()[j];

        // calculate the distance penalty
        Building b1 = r.getBuilding();
        Building b2 = c.getPreferredLocation();
        double xDist = (b1.getxCoord() - b2.getxCoord()) * (b1.getxCoord() - b2.getxCoord());
        double yDist = (b1.getyCoord() - b2.getyCoord()) * (b1.getyCoord() - b2.getyCoord());
        double dist = Math.sqrt(xDist + yDist);

        value -= DISTANCE_PENALTY * dist;
      }
    }

    return value;
  }

  public void setBuildings(ArrayList<Building> buildings) {
      this.buildings = buildings;
  }

  public void setCourses(ArrayList<Course> courses) {
      this.courses = courses;
  }

  public void setRooms(ArrayList<Room> rooms) {
      this.rooms = rooms;
  }

  public ArrayList<Building> getBuildings() {
      return buildings;
  }

  public ArrayList<Course> getCourses() {
      return courses;
  }

  public static double getDistancePenalty() {
      return DISTANCE_PENALTY;
  }

  public static double getMaxXCoord() {
      return MAX_X_COORD;
  }

  public static double getMaxYCoord() {
      return MAX_Y_COORD;
  }

  public static int getNumTimeSlots() {
      return NUM_TIME_SLOTS;
  }

  public ArrayList<Room> getRooms() {
      return rooms;
  }
}
