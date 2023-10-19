# Report Files

The validation environment creates two report files, one JSON and one HTML file. The JSON file is intended to be used by
automated processes, while the HTML file is intended for human consumption.

## JSON Report File

The JSON report file contains the following elements:

* `envPath` - The absolute path to the validation environment.
* `creationTime` - The creation time of the report file. This is a string in the ISO-8601 format `dd-MMM-yyyyTHH:mm:ss`.
* `testNames` - The names used to filter the tests.
* `tags` - The tags that were used to filter the tests.
* `testsExecuted` - The number of tests executed.
* `numSuccessTests` - The number of tests that passed.
* `numErrorTests` - The number of tests with an error.
* `numFailureTests` - The number of tests that failed.
* `testResults` - The results of the tests. This is a list of elements, each element contains the result of one test.
  The test result has the following elements:
    * `testName` - The name of the test.
    * `description` - The description of the test. Taken from the test definition file.
    * `status` - The status of the test. This is one of `[SUCCESS, ERROR, FAILURE]`.
        * `SUCCESS` - The test passed.
        * `ERROR` - An error occurred while executing the test.
        * `FAILURE` - The test could not be executed.
    * `duration` - The duration of the test in seconds.
    * `targetPath` - The absolute path to the product that was created by the test. This is not available if the test
      could not be executed or if the test passed but the option `deleteResultAfterSuccess` was set in the configuration
      file.
    * `errors` - The list of errors which occurred during the product validation. This is not available if the test
      passed. Each error has the following elements:
        * `message` - The error message.
        * `stackTrace` - The stack trace of the error.
        * `cause` - The cause of the error.
    * `exception` - The exception that occurred during the test. Usually this happens during the initialisation, e.g., a
      resource could not be loaded due to a wrong file path. The exception has the following elements:
        * `message` - The exception message.
        * `stackTrace` - The stack trace of the exception.
        * `cause` - The cause of the exception.

## HTML Report File
