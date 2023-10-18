How to get started
==================

## Set up the Validation Environment

Create a directory for the test environment and create a subdirectory named `tests` in it.
Within this directory add the test definition files. Those files are JSON files which define the tests to be executed.
They must be named like `test-<test-name>.json`.
You can create such files by installing the Product Validation Environment in SNAP and using
the `Create EOM Validation Expectation` action from the context menu of the Product you want to test. Alternatively you
can create the files manually, by using the provided template [test-def-template.json](test-def-template.json).
Details on the test definition files are described in the [Test Definition](TestDefinition.md) guide.

Now you are done. This is all what is needed to set up the validation environment. You can add more tests for more
files. But you can add additional configuration and resource files.

### Additional Files

#### Config File

To configure the validation environment you can add a file named `config.json` to the base directory of the environment.
The following configuration options are available:<br>
`rollingResults`: Number of validation results to keep. If not specified the last two results are kept.<br>
`deleteResultAfterSuccess`: If true the product result files are deleted after a successful validation. In case of an
error the result is kept. The default is `true`.<br>

Example:

```json
{
  "rollingResults": 4,
  "deleteResultAfterSuccess": false
}
```

#### Resource File

You can add additional resource files to the base directory of the environment which define paths to resource files.
Three different resource files are possible. The `source-products.json`, the `auxiliary-data.json` and
the `test-graphs.json`.
The defined resources can be referenced in the test definition files by their type and id. This allows to reuse the same
resource in multiple tests and shortens the definition text.
The type of resource is either SRC, AUX or GPH. Each element in the resource file must have a unique id and a absolute
path to the resource file.
In the GPT call of the test definition the resource is then referenced `{TYPE:ID}`. For example `{SRC:SourceProduct}`,
or `{AUX:ShapeFile}`.

Example:

```json
[
  {
    "id": "insitu",
    "path": "path/to/source/insitu.csv"
  },
  {
    "id": "ShapeFile",
    "path": "path/to/shape/file.shp"
  }
]
```

## Run the Validation Environment

### SNAP
After installing the Product Validation Environment in SNAP you can run the validation environment by using the
command line and the following command:
On Windows:
```bash
snap64 --validate <envPath> [-N=<TestNameList>] [-T=<TagList>]
```
On Unix:
```bash
snap --validate <envPath> [-N=<TestNameList>] [-T=<TagList>]
```

The envPath is the path to the test environment directory where the test resources are located and the test results will be
written to. The optional parameters are used to filter the tests to be executed. If not provided all tests will be:
* `-N=<TestNameList>`: Optional comma separated list of test names to execute.
* `-T=<TagList>`: Optional comma separated list of tags associated with Tests to be executed.

* A test is executed if it matches at least one of the provided names AND at least one of the provided tags.



