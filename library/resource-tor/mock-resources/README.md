# mock-resources

Resources are mocked in the event the actual ones do not exist in the 
`external/build/package` directory. This is to preserve runtime and test 
references, as well as mitigate blowing up version control (any further).

The resource source directory for the given source set is dynamically adjusted 
at build time to use the **real** resources src directory, or a mock.
