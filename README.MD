EOMasters Product Validation Environment
========================================
----------------------------------------

This projects provides a validation environment for EO data products. You can define a test by specifying the GPT
commandline call and the expected product content. The test environment will then execute the command and compare the
result with the result that was expected. In the end you will get 2 report files, one JSON and one HTML file.

The environment can be executed from your IDE where you develop your GPF operator or from an installed SNAP. Even the
processing results of 3d party software can be tested, and it can be ensured that the result does not change
accidentally while improving the processor. Only requirement is that SNAP can read the data.

For details on how to use the environment see the [Getting Started](docs/GettingStarted.md) guide.

## Feedback

If you find issues or have suggestions for improvements and extensions, please use the issue tracker on GitHub or
post on the [EOMasters forum](https://www.eomasters.org/forum).

## Release Notes

The release notes can be found [here](https://github.com/eomasters-repos/eom-validation-env/releases).

