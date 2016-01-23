# Name Recognition Neural Net
Aim of this project is to create program that learn to recognite name provided in configuration using Neural Network 
with Back Propagation. There is a util to create teaching data - it just takes name provided in config and creates all combinations.

User is able to configure settings (in main method) listed below:
- Name to recognite
- Learning Coeficient
- Momentum parameter
- Number of hidden neurons on each hidden layer (array of layers
- Argument for acceptance function
- Amount of samples to train in percentage (30 % of them will be incorrect for learning purpose)
- Chosen activation function (there ae three implemented in program)

There is also static configuration (in controller):
- Minimum acceptable error(current setting 0.001)
- Number of iteration allowed - tey are called EPOCH's (current setting 50000)

After neural net finish its learning process user can provide name that will test recognition of neural net.



