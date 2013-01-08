ShapeShifterSolver
==================

This program solves the Shapeshifter puzzle game on Neopets.com.

Instructions:
- Run the Parser class
- Copy and paste the page source of the current shapeshifter game into console
- After a solution is found, you will be given a list of coordinates. This is the order in which you have to place
   the pieces.

There are a number of optimizations I added to make solving puzzles faster. After around level 40,
the performance of the program stalls. The search space of higher levels increases exponentially, so 
solutions become increasingly difficult to find.

Optimizations:
- Tree pruner: Branches of the tree can be pruned by counting how many tiles go from solved to unsolved.
- The order in which moves are placed are irrelevant, so pieces are sorted in decreasing size. This is to
  ensure that pruning occurs early as possible.
- Uses a backtracking method to traverse tree. This can easily be modified depending on which data structure
  you use to store the nodes of the tree. A best-first search would simply be if you used a priority queue.
  However, I realized that using a priority queue would be very taxing on the performance since, as the size 
  of the tree grows exponentially, adding nodes to the priority queue becomes increasingly slow.

I also played around with multi-threading, but it is unclear that this helps very much. Due to the unpredictable nature
of thread management in Java, sometimes this may cause longer solve times. A thread may be on the right track towards
a solution, but it will get interrupted with multiple threads.

Unfortunately, I've found that the most effective way to improve performance is to just get a new puzzle. 
This may get tedious, but the speed at which a puzzle is solved often boils down to luck. Getting an easier puzzle
is sometimes the difference between a puzzle that takes years to solve and one that takes seconds. 
