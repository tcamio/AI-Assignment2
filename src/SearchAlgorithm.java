import java.util.*;

public class SearchAlgorithm {
  Random random = new Random();

  // Your search algorithm should return a solution in the form of a valid
  // schedule before the deadline given (deadline is given by system time in ms)
  public Schedule solve1(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    // YOUR CODE HERE
    // Simulated annealing
    // Set initial temp
    double temp = 1000000;

    // Cooling rate
    double coolingRate = 0.0001;

    int step = 0;

    // for t = 1 to INFINITY do
    while (temp > 1) {
      // If T = 0 then return current
      if (temp == 0.0) {
        return solution;
      }

      // next <- a randomly selected successor of current solution
      Schedule next = newSolution(problem);

      // deltaE <- VALUE(next) - VALUE(current)
      double deltaE = problem.evaluateSchedule(next) - problem.evaluateSchedule(solution);

      // System.out.println(deltaE);

      if (deltaE > 0) {
        solution = next;
      } else {
        if (Math.random() <= Math.exp(deltaE / temp)) {
          solution = next;
        }
      }

      // Cool System
      temp *= (1 - coolingRate);

      if (deadline < System.currentTimeMillis() + 100) {
        break;
      }

      step++;
      // System.out.println(step);
    }

    /*
     * // Print out solution for (int i = 0; i < solution.schedule.length; i++) {
     * for (int j = 0; j <solution.schedule[0].length; j++) { if
     * (solution.schedule[i][j] > -1) { System.out.print(" " +
     * solution.schedule[i][j] + " "); } else {
     * System.out.print(solution.schedule[i][j] + " "); } } System.out.println(); }
     */

    return solution;
  }

  public Schedule newSolution(SchedulingProblem problem) {
    Random random = new Random();

    Schedule newSolution = problem.getEmptySchedule();

    int NUM_TIME_SLOTS = newSolution.schedule[0].length;
    int nRooms = newSolution.schedule.length;
    int nCourses = problem.courses.size();

    for (int i = 0; i < nCourses; i++) {
      int randCol = random.nextInt(NUM_TIME_SLOTS);
      int randRow = random.nextInt(nRooms);
      newSolution.schedule[randRow][randCol] = i;
    }

    return newSolution;
  }

  // This solution uses the genetic algorithm
  public Schedule solve2(SchedulingProblem problem, long deadline) {
    Schedule solution = problem.getEmptySchedule();
    backtrackingSearch(problem, solution, problem.courses.size() - 1, 0, deadline);
    return solution;
  }

  public void printSchedule(Schedule solution) {
    for (int i = 0; i < solution.schedule.length; i++) {
      for (int j = 0; j < solution.schedule[0].length; j++) {
        if (solution.schedule[i][j] > -1) {
          System.out.print(" " + solution.schedule[i][j] + " ");
        } else {
          System.out.print(solution.schedule[i][j] + " ");
        }
      }
      System.out.println();
    }
  }

  public void backtrackingSearch(SchedulingProblem problem, Schedule solution, int nCourses, int currentCourse,
      long deadline) {
    int originalRow = (currentCourse / nCourses);
    if (currentCourse != nCourses) {
      setFirstFree(solution.schedule[originalRow], currentCourse);
      backtrackingSearch(problem, solution, nCourses, currentCourse + 1, deadline);
    } else {
      setFirstFree(solution.schedule[originalRow], currentCourse);
    }

    double max = problem.evaluateSchedule(solution);
    double score;
    int prevRow = originalRow;

    // checking the next rows
    for (int i = prevRow + 1; i < solution.schedule.length; i++) {
      if (deadline < System.currentTimeMillis() + 100) {
        return;
      }
      if (checkAvailable(solution.schedule[i])) {
        removeLastSet(solution.schedule[prevRow], currentCourse);
        setFirstFree(solution.schedule[i], currentCourse);
        score = problem.evaluateSchedule(solution);
        // if the score is less than the max, then it means we are farther from the
        // preferred building
        if (score < max) {
          removeLastSet(solution.schedule[i], currentCourse);
          setFirstFree(solution.schedule[prevRow], currentCourse);
          break;
        } else {
          max = score;
          prevRow = i;
        }
      }
    }

    /*
     * Checking rows before original row. First checking if the score was better
     * before, this will help with performance since we won't double check
     */
    if (prevRow != originalRow) {
      for (int i = prevRow - 1; i >= 0; i--) {
        if (deadline < System.currentTimeMillis() + 100) {
          return;
        }
        if (checkAvailable(solution.schedule[i])) {
          removeLastSet(solution.schedule[prevRow], currentCourse);
          setFirstFree(solution.schedule[i], currentCourse);
          score = problem.evaluateSchedule(solution);
          if (score < max) {
            removeLastSet(solution.schedule[i], currentCourse);
            setFirstFree(solution.schedule[prevRow], currentCourse);
            break;
          } else {
            max = score;
            prevRow = i;
          }
        }
      }
    }
  }

  public void removeLastSet(int[] arr, int val) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == val) {
        arr[i] = -1;
        return;
      }
    }
  }

  public void setFirstFree(int[] arr, int val) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == -1) {
        arr[i] = val;
        return;
      }
    }
  }

  public boolean checkAvailable(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == -1) {
        return true;
      }
    }
    return false;
  }

  // This is a very naive baseline scheduling strategy
  // It should be easily beaten by any reasonable strategy
  public Schedule naiveBaseline(SchedulingProblem problem, long deadline) {

    // get an empty solution to start from
    Schedule solution = problem.getEmptySchedule();

    for (int i = 0; i < problem.courses.size(); i++) {
      Course c = problem.courses.get(i);
      boolean scheduled = false;
      for (int j = 0; j < c.timeSlotValues.length; j++) {
        if (scheduled)
          break;
        if (c.timeSlotValues[j] > 0) {
          for (int k = 0; k < problem.rooms.size(); k++) {
            if (solution.schedule[k][j] < 0) {
              solution.schedule[k][j] = i;
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
