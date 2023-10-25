<img alt="env-validation-logo_100.png" style="float: left;" src="resources/logo/davalien-logo_100.png"/>

EOMasters DAVALIEN
==================
------------------

DAVALIEN - the **DA**ta **VALI**dation **EN**vironment projects provides an environment for validating EO data. It is
intended for quality assurance of EO data and data processing software. It is based on
 [SNAP](https://step.esa.int/main/toolboxes/snap/) software and uses the [GPF](https://step.esa.int/main/wp-content/help/?version=9.0.0&helpid=gpf.overview).

You can define tests by specifying the SNAP GPT command line call and the expected product content. The test environment will
then execute the command and compare the result with the result that was expected. In the end you will get 2 report
files, one JSON and one HTML file. They are explained on the [Report Files](docs/ReportFiles.md) page.

The environment can be executed from your IDE where you develop your GPF operator or from an installed SNAP. Even the
processing results of 3d party software can be tested, and it can be ensured that the result does not change
accidentally while improving the processor. Only requirement is that SNAP can read the data.

For details on how to use the environment see the [Getting Started](docs/GettingStarted.md) guide.

## Feedback

If you find issues or have suggestions for improvements and extensions, please use the issue tracker on GitHub or
post on the [EOMasters forum](https://www.eomasters.org/forum).

## Release Notes

The release notes can be found [here](https://github.com/eomasters-repos/eom-validation-env/releases).

