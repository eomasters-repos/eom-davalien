EOMasters Product Validation Environment
========================================
----------------------------------------

## Introduction

This projects provides a validation environment for EO data products. You can define a test by specifying the GPT
commandline call and the expected product content. The test environment will then execute the command and compare the
result with the result that was expected. In the end you will get a 2 report files one JSON and one HTML file.

The environment can be executed from your IDE where you develop your GPF operator or from an installed SNAP. Even the
processing results of 3d party software can be tested, and it can be ensured that the result does not change
accidentally while improving the processor. Only requirement is that SNAP can read the data.

For details on how to use the environment see the [Getting Started](GettingStarted.md) guide.


  
