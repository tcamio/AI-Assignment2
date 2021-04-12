import java.util.*;

public class SearchAlgorithm {

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

      //System.out.println(deltaE);

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
      //System.out.println(step);
    }

    /*
    // Print out solution
    for (int i = 0; i < solution.schedule.length; i++) {
        for (int j = 0; j <solution.schedule[0].length; j++) {
            if (solution.schedule[i][j] > -1) {
                System.out.print(" " + solution.schedule[i][j] + " ");
            } else {
                System.out.print(solution.schedule[i][j] + " ");
            }
        }
        System.out.println();
    }
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


  // This solution uses the genetic algorithm from above
  public Schedule solve2(SchedulingProblem problem, long deadline) {
    ArrayList<Schedule> population = new ArrayList<Schedule>();
    population = generateRandomPopulation(problem, 50); // Making a population of size 50
    return geneticAlgorithm(problem, population, deadline);
  }

  // TODO: find a good threshold for the fitness function
  public Schedule geneticAlgorithm(SchedulingProblem problem, ArrayList<Schedule> population, long deadline) {
    Schedule x;
    Schedule y;
    Schedule child;
    ArrayList<Schedule> new_population;
    Random random = new Random();
    long time = System.currentTimeMillis() / 1000;

    while ((System.currentTimeMillis() / 1000) - time < deadline) {
      new_population = new ArrayList<Schedule>();

      for (int i = 0; i < population.size(); i++) {
        x = randomSelection(population);
        y = randomSelection(population);
        child = reproduce(problem, x, y);
        if (random.nextInt(100) < 5) { // 5 % probability of choosing this
          child = mutate(child);
        }
        new_population.add(child);
      }
      population = new_population;
    }
    Schedule best = getBestIndividual(problem, population);
    System.out.println("BEST INDIV: " + problem.evaluateSchedule(best));
    return best;
  }

  public Schedule reproduce(SchedulingProblem problem, Schedule x, Schedule y) {
    Random rand = new Random();
    int n = x.schedule.length;
    int c = rand.nextInt(n);

    // This is the same as append(substring(x, 1, c), substring(y, c + 1, n))
    Schedule child = problem.getEmptySchedule();

    for (int i = 0; i < x.schedule.length; i++) {
      if (i < c) {
        child.schedule[i] = x.schedule[i];
      } else {
        child.schedule[i] = y.schedule[i];
      }
    }
    return child;
  }

  // TODO: implement
  private Schedule mutate(Schedule s) {
    return s;
  }

  // TODO: implement
  private ArrayList<Schedule> generateRandomPopulation(SchedulingProblem problem, int populationSize) {
    ArrayList<Schedule> population = new ArrayList<Schedule>();

    for (int i = 0; i < populationSize; i++) {
      Schedule tmp = problem.getEmptySchedule();

      for (int r = 0; r < tmp.schedule.length; r++) {
        for (int j = 0; j < tmp.schedule.length; j++) {
          tmp.schedule[r][j] = -1;
        }
      }
    }

    return population;
  }

  public Schedule getBestIndividual(SchedulingProblem problem, ArrayList<Schedule> population) {
    Schedule best = population.get(0);
    double bestFitness = problem.evaluateSchedule(best);
    for (int i = 1; i < population.size(); i++) {
      double t = problem.evaluateSchedule(population.get(i));
      if (t > bestFitness) {
        bestFitness = t;
        best = population.get(i);
      }
    }
    return best;
  }

  private Schedule randomSelection(ArrayList<Schedule> population) {
    Random random = new Random();
    return population.get(random.nextInt(population.size()));
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
    System.out.println("\nSOLUTION\n");
    for (int i = 0; i < solution.schedule.length; i++) {
      for (int j = 0; j < solution.schedule[i].length; j++) {
        System.out.print(solution.schedule[i][j] + " ");
      }
      System.out.println();
    }
    return solution;
  }
}
