javac tempest/topology/PerceptronTopology.java
jar cvf perceptron.jar tempest/*
storm jar perceptron.jar tempest/topology/PerceptronTopology
