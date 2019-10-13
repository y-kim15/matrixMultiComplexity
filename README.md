# CS3052 Practical 1 - Analysis of Matrix Multiplication
This is a repo for first practical of CS3052 Computational Complexity Module.

### Overview
This practical covered the theory of asymptotic complexity and the assessment of theasymptotic behaviour of an algorithm derived from practical implementation. The givenalgorithm is analysed and improvements have been made by implementation of matricesand matrix multiplication algorithms. 

The test suite is designed as a Maven project written in Java where source code and JUnit tests are locatedin relevant directories. 
There are 4 exercises to be explored.

* Analyse the Basic Algorithm for Matrix Multiplication for the worst-case time complexity.You may use the provided code in BasicMultiplier.java (or any alternative implementationyou write or find on the web). This task is to give a theoretical worst-case guarantee, andwill be assessed by the report.
* Analyse and estimate the average-case performance of the Basic Algorithm by generating random test inputs of various sizes and doing data analysis. You will have to generate harness code to generate random matrices, to find the time taken for the multiplication procedure, and then do some data analysis to find the behaviour as a function of inputsize. 
* Research data structures and algorithms that can save time taken, or memory used, orboth, for sparse matrix multiplication. Note that we care about the sparse case, so youshould think carefully about how you can save time or memory in the case most elements are zero. In your report, describe the results of your research and your sources. This mustbe backed up by an implementation of at least one idea, in any programming language of your choice. 
* nvestigate in what situations your implementation from Task 3 does better than the BasicAlgorithm, and in what situations (if any) the Basic Algorithm wins. This will involve similar data analysis as Task 2, but you will have to decide the characteristics of the input you want to modify. 

Additional analysis carried out is based on the following:

Give a theoretical justification for your results from Task 4, doing a complexity analysis based on input sizes and the characteristics of sparse matrices. 
