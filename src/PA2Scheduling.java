public class PA2Scheduling {
  int nBuildings = 0;
  int nRooms = 0;
  int nCourses = 0;
  int TIME_LIMIT_SECONDS = 0;
  int algorithm = 0;
  long seed = 0;

  public PA2Scheduling(String[] args) {
    parseArgs(args);
  }

  public void parseArgs(String[] args) {
    if (args.length == 0 || (args.length == 1 && args[0].equals("--help"))){
      System.out.println("Usage: java PA2Scheduling <number of buildings> <number of rooms> <number of courses>\n\t\t"+
                          "<time limit in seconds> <algorithm> <seed>\n");
      System.exit(0);
    }

    if (args.length == 6) {
      try {
        this.nBuildings = Integer.parseInt(args[0]);
        this.nRooms = Integer.parseInt(args[1]);
        this.nCourses = Integer.parseInt(args[2]);
        this.TIME_LIMIT_SECONDS = Integer.parseInt(args[3]);
        this.algorithm = Integer.parseInt(args[4]);
        this.seed = Long.parseLong(args[5]);
      } catch (NumberFormatException e) {
        System.out.println("Number format exception reading arguments");
        System.exit(1);
      }
    } else {
      System.out.println("ERROR: Incorrect number of arguments (should have six).");
      System.exit(1);
    }
  }

  public Schedule computeSchedule(){
    SchedulingProblem schedulingProblem = new SchedulingProblem(seed);
    schedulingProblem.createRandomInstance(nBuildings, nRooms, nCourses);

    SearchAlgorithm search = new SearchAlgorithm();

    long deadline = System.currentTimeMillis() + (1000 * TIME_LIMIT_SECONDS);

    // Add your seach algorithms here, each with a unique number
    Schedule solution = null;

    if (algorithm == 0) {
      solution = search.naiveBaseline(schedulingProblem, deadline);
    } else if (algorithm == 1) {
      solution = search.solve1(schedulingProblem, deadline);
    } else if (algorithm == 2) {
      solution = search.solve2(schedulingProblem, deadline);
    }else {
      System.out.println("ERROR: Given algorithm number does not exist!");
      System.exit(1);
    }

    return solution;
  }

  @Override
  public String toString() {
    String str = "Number of Buildings: " + nBuildings
                + "Number of Rooms: " + nRooms
                + "Number of Courses: " + nCourses
                + "Time limit (s): " + TIME_LIMIT_SECONDS
                + "Algorithm number: " + algorithm
                + "Random seed: " + seed;
    return str;
  }

  public static void main(String[] args) {
    PA2Scheduling pa2Scheduling = new PA2Scheduling(args);
    Schedule answer = pa2Scheduling.computeSchedule();
    System.out.println(answer.toString());
  }
}