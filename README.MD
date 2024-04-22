![davalien-logo_100.png](resources%2Flogo%2Fdavalien-logo_100.png)

EOMasters - DAVALIEN
====================
--------------------
[![Maven Build](https://github.com/eomasters-repos/eom-davalien/actions/workflows/maven.yml/badge.svg?branch=master&event=push)](https://github.com/eomasters-repos/eom-davalien/actions/workflows/maven.yml)

DAVALIEN - the **DA**ta **VALI**dation **EN**vironment projects provides an environment for validating EO data. It is
intended for quality assurance of EO data and data processing software. It is based on
[SNAP](https://step.esa.int/main/toolboxes/snap/) software and uses
the [GPF](https://step.esa.int/main/wp-content/help/?version=9.0.0&helpid=gpf.overview).
![report_success.jpeg](https://github.com/eomasters-repos/eom-davalien/wiki/images/report_success.jpeg)
You can define tests by specifying the SNAP GPT command line call and the expected product content. The test environment
will
then execute the command and compare the result with the result that was expected. In the end you will get 2 report
files, one JSON and one HTML file. They are explained on
the [Report File](https://github.com/eomasters-repos/eom-davalien/wiki/Report-File) page.

The environment can be executed from your IDE where you develop your GPF operator or from an installed SNAP. Even the
processing results of 3d party software can be tested, and it can be ensured that the result does not change
accidentally while improving the processor. Only requirement is that SNAP can read the data.

For details on how to use the environment see
the [Getting Started](https://github.com/eomasters-repos/eom-davalien/wiki/Getting-Started) guide.

--------------------

| Release Notes                                                                                          | Feedback                                                                                                                    |
|--------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| The release notes can be found [here](https://github.com/eomasters-repos/eom-validation-env/releases). | Issues and suggestions can be posted in the [issue tracker](https://github.com/eomasters-repos/eom-davalien/issues) or post in the [EOMasters forum](https://www.eomasters.org/forum). |

