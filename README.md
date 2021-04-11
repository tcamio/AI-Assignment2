# AI-Assignment2
Programming Assignment 2 for AI class


## Genetic Algorithm
```
function Genetic-Algorithm(population, fitness-function) returns an individual
  inputs: population, a set of individuals
          Fitness-function, a function that measures the fitness of an individual

  repeat
    new_population <-- empty set
    for i = 1 to Size(population) do
      x <-- Random-Selection(population, Fitness-Function)
      y <-- Random-Selection(population, Fitness-Function)
      child <-- Reproduce(x, y)
      if (small random probability) then child <-- Mutate(child)
      add child to new_population
    population <-- new_population
  until some individual is fit enough, or enough time has elapsed
  return the best individual in population, according to Fitness-Function

function Reproduce(x, y) returns and individual
  inputs: x, y, parent individuals

  n <-- length(x); c <-- random number from 1 to n
  return Append(substring(x, 1, c), substring(y, c + 1, n))
```
