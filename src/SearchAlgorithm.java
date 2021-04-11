import java.util.ArrayList;
import java.util.Random;

public class SearchAlgorithm {

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
        if (Math.random() <= Math.exp(deltaE / temperature)) {
          solution = next;
        }
      }
    }

    // return solution;
  }

  int k = 20;
  double lam = 0.045;
  int limit = 100;

  public double getTemp(int t) {
    if (t < limit)
      return k * Math.exp((-1) * lam * t);
    else
      return 0.0;
  }

  // This solution uses the genetic algorithm from above
  public Schedule solve2(SchedulingProblem problem, long deadline) {
    ArrayList<Schedule> population = new ArrayList<Schedule>();
    population = generateRandomPopulation();
    return geneticAlgorithm(problem, population, deadline);
  }

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
    Schedule best = getBestIndividual(population);
    System.out.println("BEST INDIV: " + fitnessFunction(best));
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
  private ArrayList<Schedule> generateRandomPopulation() {
    return new ArrayList<Schedule>();
  }

  public Schedule getBestIndividual(ArrayList<Schedule> population) {
    Schedule best = population.get(0);
    int bestFitness = fitnessFunction(best);
    for (int i = 1; i < population.size(); i++) {
      int t = fitnessFunction(population.get(i));
      if (t > bestFitness) {
        bestFitness = t;
        best = population.get(i);
      }
    }
    return best;
  }

  // TODO: implement
  private int fitnessFunction(Schedule best) {
    return 0;
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

    return solution;
  }
}
