![davalien-logo_100.png](resources%2Flogo%2Fdavalien-logo_100.png)

EOMasters - DAVALIEN
====================
--------------------
[![Maven Build](https://github.com/eomasters-repos/eom-davalien/actions/workflows/mvn-build.yml/badge.svg)](https://github.com/eomasters-repos/eom-davalien/actions/workflows/mvn-build.yml)
[![Static Badge](https://img.shields.io/badge/FOR%20ESA%20SNAP-Version%2010-00AA89?labelColor=5A5A5A)](https://step.esa.int/main/)

[![ko-fi](https://img.shields.io/badge/Ko--fi-F16061?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/eomasters) 
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/company/eomasters)
[![Bluesky](https://img.shields.io/badge/Bluesky-0285FF?style=for-the-badge&logo=bluesky&logoColor=fff&labelColor=0285FF)](https://bsky.app/profile/eomasters.org)
[![Mastodon Follow](https://img.shields.io/badge/Mastodon-6364FF?style=for-the-badge&logo=Mastodon&logoColor=white)](https://mastodon.green/@EOMasters)

DAVALIEN - the **DA**ta **VALI**dation **EN**vironment projects provides an environment for validating EO data. It is
intended for quality assurance of EO data and data processing software. It is based on
[SNAP](https://step.esa.int/main/toolboxes/snap/) software and uses
the [GPF](https://step.esa.int/main/wp-content/help/?version=9.0.0&helpid=gpf.overview).
![report_success.jpeg](https://github.com/eomasters-repos/eom-davalien/wiki/images/report_success.jpeg)
You can define tests by specifying the SNAP GPT command line call and the expected product content. The test environment
will then execute the command and compare the result with the result that was expected. In the end you will get 2 report
files, one JSON and one HTML file. They are explained on the
[Report File](https://github.com/eomasters-repos/eom-davalien/wiki/Report-File) page.

The environment can be executed from your IDE where you develop your GPF operator or from an installed SNAP. Even the
processing results of 3rd party software can be tested, and it can be ensured that the result does not change
accidentally while improving the processor. Only requirement is that SNAP can read the data.

For details on how to use the environment see
the [Getting Started](https://github.com/eomasters-repos/eom-davalien/wiki/Getting-Started) guide.

--------------------

| Release Notes                                                                                          | Feedback                                                                                                                                                                          |
|--------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| The release notes can be found [here](https://github.com/eomasters-repos/eom-validation-env/releases). | Issues and suggestions can be posted in the [issue tracker](https://github.com/eomasters-repos/eom-davalien/issues) or in the [EOMasters forum](https://www.eomasters.org/forum). |

