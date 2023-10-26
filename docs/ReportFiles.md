# Report Files

The validation environment creates two report files, one JSON and one HTML file. The JSON file is intended to be used by
automated processes, while the HTML file is intended for human consumption.

## HTML Report File

![report.gif](..%2FlocalResources%2Fscreenshots%2Freport.gif)

The report starts with a summary of the executed validation environment.  
It starts with the execution date and time, followed by the test names and tags used to filter the tests contained in
the environment and the file path to the environment directory.
If the test has a description text it is shown below the name as collapsible details section.
Also shown is how many tests have been executed of all available tests (depending on the filtering) and a summary of the
results. The different statuses of the tests have the following meaning:

* **SUCCESS** = The test was executed successful and no error occurred.
* **ERROR** = The test was executed but the data didn't match the expectation.
* **FAILURE** = The test couldn't be executed. Mostly due to a misconfiguration.

The HTML report contains one row for each executed test. The row contains a number for the test, the test name, the
execution status and the duration of the test. If the failed or an validation error occurred, the row contains a link to
the product that was created by the test. If the test was successful it depends on the configuration. If the resulting
product is kept the link is added too.<br>

If a test ends with an error or a failure details of this problem are added as collapsible section. The main error is
shown and the detailed stacktrace can be made visible on demand.

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

An example looks like:

```
{
  "envPath": "D:\\EOData\\validEnv4Snap",
  "creationTime": "2023-10-26T11:03:30",
  "testsExecuted": 5,
  "numSuccessTests": 2,
  "numErrorTests": 2,
  "numFailureTests": 1,
  "testResults": [
    {
      "testName": "S1_Calibration",
      "status": "SUCCESS",
      "duration": 9.99,
      "targetPath": "D:\\EOData\\validEnv4Snap\\results\\20231026_110330\\products\\S1_Calibration.znap.zip"
    },
    {
      "testName": "S2_Subsample_Resample",
      "status": "SUCCESS",
      "duration": 7.197,
      "targetPath": "D:\\EOData\\validEnv4Snap\\results\\20231026_110330\\products\\S2_Subsample_Resample.znap.zip"
    },
    {
      "testName": "OLCI_Reprojection",
      "status": "ERROR",
      "duration": 11.398,
      "targetPath": "D:\\EOData\\validEnv4Snap\\results\\20231026_110330\\products\\OLCI_Reprojection.nc",
      "errors": [
        {
          "message": "Raster[Oa19_radiance] - Pixel[0]: For pixel position [112.50000000,372.50000000] expected value [68.02348328] but was [67.02348328], with eps 1.000000e-08",
          "stacktrace": "java.lang.AssertionError: Raster[Oa19_radiance] - Pixel[0]: For pixel position [112.50000000,372.50000000] expected value [68.02348328] but was [67.02348328], with eps 1.000000e-08\r\n\tat org.eomasters.davalien.asserts.ProductAssert.hasRasters(ProductAssert.java:227)\r\n\tat org.eomasters.davalien.ProductValidator.lambda$testProduct$8(ProductValidator.java:44)\r\n\tat org.eomasters.davalien.ProductValidator.runValidation(ProductValidator.java:51)\r\n\tat org.eomasters.davalien.ProductValidator.testProduct(ProductValidator.java:44)\r\n\tat org.eomasters.davalien.ValidationEnv.compareResults(ValidationEnv.java:193)\r\n\tat org.eomasters.davalien.ValidationEnv.execute(ValidationEnv.java:119)\r\n\tat org.eomasters.davalien.ValidationOptionProcessor.process(ValidationOptionProcessor.java:105)\r\n\tat org.netbeans.spi.sendopts.Option$1.process(Option.java:362)\r\n\tat org.netbeans.api.sendopts.CommandLine.process(CommandLine.java:336)\r\n\tat org.netbeans.modules.sendopts.HandlerImpl.execute(HandlerImpl.java:37)\r\n\tat org.netbeans.modules.sendopts.Handler.cli(Handler.java:44)\r\n\tat org.netbeans.CLIHandler.notifyHandlers(CLIHandler.java:209)\r\n\tat org.netbeans.core.startup.CLICoreBridge.cli(CLICoreBridge.java:57)\r\n\tat org.netbeans.CLIHandler.notifyHandlers(CLIHandler.java:209)\r\n\tat org.netbeans.CLIHandler$1.exec(CLIHandler.java:243)\r\n\tat org.netbeans.CLIHandler.finishInitialization(CLIHandler.java:422)\r\n\tat org.netbeans.MainImpl.finishInitialization(MainImpl.java:231)\r\n\tat org.netbeans.Main.finishInitialization(Main.java:67)\r\n\tat org.netbeans.core.startup.Main.start(Main.java:291)\r\n\tat org.netbeans.core.startup.TopThreadGroup.run(TopThreadGroup.java:98)\r\n\tat java.base/java.lang.Thread.run(Thread.java:829)\r\n"
        },
        {
          "message": "Metadata[1]: Value of metadata attribute [Variable_Attributes/Oa21_radiance_unc/radiation_wavelength_unit] should be [mm] but was [nm]]",
          "stacktrace": "java.lang.AssertionError: Metadata[1]: Value of metadata attribute [Variable_Attributes/Oa21_radiance_unc/radiation_wavelength_unit] should be [mm] but was [nm]]\r\n\tat org.eomasters.davalien.asserts.ProductAssert.hasMetadata(ProductAssert.java:238)\r\n\tat org.eomasters.davalien.ProductValidator.lambda$testProduct$10(ProductValidator.java:46)\r\n\tat org.eomasters.davalien.ProductValidator.runValidation(ProductValidator.java:51)\r\n\tat org.eomasters.davalien.ProductValidator.testProduct(ProductValidator.java:46)\r\n\tat org.eomasters.davalien.ValidationEnv.compareResults(ValidationEnv.java:193)\r\n\tat org.eomasters.davalien.ValidationEnv.execute(ValidationEnv.java:119)\r\n\tat org.eomasters.davalien.ValidationOptionProcessor.process(ValidationOptionProcessor.java:105)\r\n\tat org.netbeans.spi.sendopts.Option$1.process(Option.java:362)\r\n\tat org.netbeans.api.sendopts.CommandLine.process(CommandLine.java:336)\r\n\tat org.netbeans.modules.sendopts.HandlerImpl.execute(HandlerImpl.java:37)\r\n\tat org.netbeans.modules.sendopts.Handler.cli(Handler.java:44)\r\n\tat org.netbeans.CLIHandler.notifyHandlers(CLIHandler.java:209)\r\n\tat org.netbeans.core.startup.CLICoreBridge.cli(CLICoreBridge.java:57)\r\n\tat org.netbeans.CLIHandler.notifyHandlers(CLIHandler.java:209)\r\n\tat org.netbeans.CLIHandler$1.exec(CLIHandler.java:243)\r\n\tat org.netbeans.CLIHandler.finishInitialization(CLIHandler.java:422)\r\n\tat org.netbeans.MainImpl.finishInitialization(MainImpl.java:231)\r\n\tat org.netbeans.Main.finishInitialization(Main.java:67)\r\n\tat org.netbeans.core.startup.Main.start(Main.java:291)\r\n\tat org.netbeans.core.startup.TopThreadGroup.run(TopThreadGroup.java:98)\r\n\tat java.base/java.lang.Thread.run(Thread.java:829)\r\n"
        }
      ]
    }
  ]
}
```
