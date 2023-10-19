# Test Definition File

The test definition files contained in the `tests` directory of the validation environmane define the tests that should
be executed. The test definition files are JSON files and must be named like `test-<test-name>.json`. You can create
such files by installing the Product Validation Environment in SNAP and using the `Create EOM Validation Expectation`
action from the context menu of the Product you want to test. Alternatively you can create the files manually, by using
the provided template [test-def-template.json](test-def-template.json).

## Test Definition Structure

A test definition can have the following elements:

* `testName` - The name of the test. This is used to identify the test and to name the result files. This element is
  mandatory and must be unique.
* `description` - A description of the test. This is used in the report files.
* `gptCall` - The GPT call to execute. This is the same as you would use in the command line. This element is mandatory.
  The GPT call should not specify a target product, this is up to the testing environment. The output format can be
  specified, otherwise the ZNAP format is used.
* `tags` - A list of tags that can be used to filter the tests to be executed.
* `expectation` - The expected content of the product. This is a list of elements that are expected in the product.
  This element is mandatory.

## Expectation Structure

The definition of the expected content of the product can have the following elements, all are optional:

* `name` - The name of the product.
* `description` - The description of the product.
* `productType` - The product type.
* `sceneSize` - The size of the scene. This is a list of two elements, the width and the height. `[WIDTH, HEIGHT]`
* `startTime` - The start time of the scene. This is a string in the format `dd-MMM-yyyy HH:mm:ss.SSSSSS`.
* `endTime` - The end time of the scene. This is a string in the format `dd-MMM-yyyy HH:mm:ss.SSSSSS`.
* `geoLocations` - The geolocations of the scene. The geolocation defines the latitude and longitude for a pixel
  position. The geolocation has the following elements:
    * `pixel` - The pixel position. This is a list of two elements, the x and the y position. `[X, Y]`
    * `geo` - The geolocation. This is a list of two elements, the latitude and the longitude. `[LAT, LON]`
    * `eps` - The allowed deviation of the geolocation.
* `rasters` - The expected rasters of the product. It defines several expected attributes:
    * `name` - The name of the raster.
    * `description` - The description of the raster.
    * `size` - The size of the raster. This is a list of two elements, the width and the height. `[WIDTH, HEIGHT]`
    * `dataType` - The data type of the raster. This is one
      of `[INT8, INT16, INT32, INT64, UINT8, UINT16, UINT32, UINT64, FLOAT32, FLOAT64, ASCII, UTC]`.
    * `rasterType` - The raster type. This is one of `[BAND, TIE_POINT, MASK, VIRTUAL, FILTER]`.
    * `noDataValue` - The no data value of the raster.
    * `noDataValueUsed` - If the no data value is used.
    * `pixels` - The expected pixels of the raster. This is a list of elements, each element defines one pixel. The
      pixel has the following elements:
        * `position` - The position of the pixel. This is a list of two elements, the x and the y position. `[X, Y]`
        * `value` - The value of the pixel.
        * `eps` - The allowed deviation of the pixel value.
    * `geoLocations` - The geolocations of the raster. The geolocation defines the latitude and longitude for a pixel
      position. The geolocation has the following elements:
    * `pixel` - The pixel position. This is a list of two elements, the x and the y position. `[X, Y]`
    * `geo` - The geolocation. This is a list of two elements, the latitude and the longitude. `[LAT, LON]`
    * `eps` - The allowed deviation of the geolocation.
* `vectorData` - The vector data expected to be contained in the product.
    * `name` - The name of the vector data.
    * `description` - The description of the vector data.
    * `numFeatures` - The number of features expected in the vector data.
* `metadata` -
    * `path` - The path to a metadata attribute. If in the Metadata, elements exists with the same name on the same
      level then the name must be appended with a number, like `path: "metadata/path/element[2]/attribute[16]"`.
    * `value` - The expected value of the attribute as text.
* `sampleCoding` - The expected sample codings (FlagCoding and IndexCoding)
    * `name` - The name of the sample coding.
    * `samples` - The expected samples of the sample coding. This is a list of elements, each element defines one
      sample. The sample has the following elements:
        * `name` - The name of the sample.
        * `description` - The description of the sample.
        * `value` - The expected value of the sample.

