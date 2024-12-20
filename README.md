# Random Spanning Tree Generation

## Useful Information
- Project Members: FANANI Amina, MARCUCCINI Fabien
- Teaching Assistant: ROLLAND Marius
- Course: "Algorithmics 2" - 3rd year Bachelor's in Computer Science at Aix-Marseille University
- SDK Version: Azul Zulu 13.0.14 - aarch64

## Project Objective
- The project description is provided in the file **/pdf/tp2.pdf** (in French). <br>

## Commands to Use the Project
- Each *make* command should be executed from the root directory of the project.
- To run unit tests: *make run_tests*
- To compile and run the project without specifications: *make*
- To compile and run the project with specifications: *make ARGS="x y"*, where x = algorithm index and y = graph generator index.
- To execute the jar without recompiling: *make exec* or *make exec ARGS="x y"*
  
### Algorithm Indexes for Random Spanning Tree Generation (x):
  - 1 = MinimumWeightSpanningTree 
  - 2 = RandomWalkTree
  - 3 = RandomEdgeInsertion 
  - 4 = AldousBroder

### Graph Generator Indexes (y):
  - 1 = Grid Mode
  - 2 = Complete Graph Mode
  - 3 = ErdosRenyi Mode
  - 4 = Lollipop Mode

## Project Progress
- The classes in the Graph package have been implemented and tested (see the Tests package).
- The project code has been restructured and documented for better readability.
- Random Spanning Tree Generation Algorithms implemented:
  - Random Minimum Weight Spanning Tree (tested with Grid mode)
  - Random Walk Tree (tested with Grid mode)
  - Random Edge Insertion (tested with Grid mode)
  - Aldous-Broder Algorithm (tested with Grid mode)

## Algorithm Comparison
...
