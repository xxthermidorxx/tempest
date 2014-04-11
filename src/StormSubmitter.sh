#javac tempest/topology/PerceptronTopology.java
javac tempest/topology/LocalIOTopology.java
jar cvf perceptron.jar tempest/*
#storm jar perceptron.jar tempest/topology/PerceptronTopology perceptron
storm jar perceptron.jar tempest/topology/LocalIOTopology io
