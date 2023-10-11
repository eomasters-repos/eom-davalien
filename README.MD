EOMasters GPT Test Environment
==============================
------------------------------

This projects provides a test environment for operators you have developed. You can define a test by specifying the GPT
commandline call and the expected result. The test environment will then execute the command and compare the result with
the expected result.
The environment can be executed from your IDE where you develop your operator or from an installed SNAP to test all the
installed operators. The expected result can be defined in 2 ways. Either by providing a path to a file containing the
expected result or by providing a list of expected results.

There are only 2 mandatory resource files and 3 optional which need to be provided for the test environment to work.
Mandatory are the list of tests and the list of test products which are defined in simple JSON files named
`test-definitions.json` and `test-products.json`.
The optional files define the relative paths to optional data. Like auxiliary data used by the GPT tests, the paths to
expected target products, and paths to graph XML files if they are used instead of a single operator. Those files are
named `test-auxiliary-data.json`, `test-target-products.json` and `test-graphs.json`.

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

THe `test-definitions.json`

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
