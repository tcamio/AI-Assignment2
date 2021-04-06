public class SearchAlgorithm {
  int k = 20;
  double lam = 0.045;
  int limit = 100;

  // Your search algorithm should return a solution in the form of a valid
  // schedule before the deadline given (deadline is given by system time in ms)
  public Schedule solve1(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    // YOUR CODE HERE
    // Simulated annealing
    // for t = 1 to INFINITY do
    int timeStep = 1;
    while (true) {
        // temperature <- schedule(t)
        double temperature = getTemp(timeStep);
        timeStep++;

        // If temperature = 0 then return current
        if (temperature == 0.0) {
            return solution;
        }

        // Schedule next = a randomly selected successor of current
        Schedule next = problem.getEmptySchedule();
        double deltaE = problem.evaluateSchedule(solution) - problem.evaluateSchedule(next);

        if (deltaE > 0) {
            solution = next;
        } else {
            if (Math.random() <= Math.exp(deltaE/temperature)) {
                solution = next;
            }
        }
    }

    // return solution; This is unreachable, should have a break somewhere since the while is infinite or just remove
  }

  public double getTemp(int t) {
		if (t < limit)
			return k * Math.exp((-1) * lam * t);
		else
			return 0.0;
   }

  public Schedule solve2(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    // YOUR CODE HERE

    return solution;
  }

  // This is a very naive baseline scheduling strategy
  // It should be easily beaten by any reasonable strategy
  public Schedule naiveBaseline(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    for (int i = 0; i < problem.getCourses().size(); i++) {
      Course c = problem.getCourses().get(i);
      boolean scheduled = false;
      for (int j = 0; j < c.getTimeSlotValues().length; j++) {
        if (scheduled) break;
        if (c.getTimeSlotValues()[j] > 0) {
          for (int k = 0; k < problem.getRooms().size(); k++) {
            if (solution.getSchedule()[k][j] < 0) {
              solution.getSchedule()[k][j] = i;
              scheduled = true;
              break;
            }
          }
        }
      }
    }

    return solution;
  }
}
