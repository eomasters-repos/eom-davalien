EOMasters GPT Test Environment
==============================
------------------------------

This projects provides a test environment for operators you have developed. You can define a test by specifying the GPT
commandline call and the expected result. The test environment will then execute the command and compare the result with
the expected result.
The environment can be executed from your IDE where you develop your operator or from an installed SNAP to test all the
installed operators. Even the processing results of external processors can be tested, and it can be ensured that the
result does not change accidentally while improving the processor. For doing this only a read operator needs to be used
with the output of the external processor. The test environment will then compare the result with the expected result.
The expected result can be defined in 2 ways. Either by providing a path to a file containing the
expected result or by providing a list of expected results.

The test environment must be setup following some rules.
Within the test environment directory there must be a directory named `tests` which contains the test definition files.
Those files are JSON files which define the tests to be executed. They must be named like `test-<test-name>.json`.
In the base directory of the test environment there must be a file named `test-products.json` which defines the list of
test products.
Beside this there can be optional files which define auxiliary data used by the tests or paths to graph XML files.
if they are used instead of a single operator. Those files are named `auxiliary-data.json` and `test-graphs.json`.

The test environment will execute the GPT commandline call and compare the result with the expected result and will
generate a report.

## Usage

snap --gpttests <envPath> `[tests=<TestNameList>]` `[tags=<TagList>]`

Parameters:

* `<envPath>`: Path to the test environment directory where the test resources are located and the test results will be
  written to.
* `[tests=<TestNameList>]`: Optional comma separated list of test names to execute. If not provided all tests will be
  executed.
* `[tags=<TagList>]`: Optional comma separated list of tags associated with Tests to be executed. If not provided all
  tests will be executed.

### IDE

### SNAP

snap --gpttests <envPath> `[tests=<TestNameList>]` `[tags=<TagList>]`

## File format

### Test Definitions

The `test-definitions.json`
The test name must be usable a file name.
The GPT call should not specify a target product, this is up to the testing environment. The output format can be
specified,
otherwise the ZNAP format is used.

### Other Resource Files

The other resource files follow a common structure. They are JSON files too and contain a list of resources. Each
resource has an `id` and a `relPath` property. The `id` is used to reference the resource in the test definitions. The
`relPath` is the relative path to the resource file. The path is relative to the environment path `envPath` provided via
the command line call.

```
[
  {
    "id": "AnID",
    "relPath": "path/file1.shp"
  },
  {
    "id": "AnotherID",
    "relPath": "path/file2.txt"
  }
]
```
