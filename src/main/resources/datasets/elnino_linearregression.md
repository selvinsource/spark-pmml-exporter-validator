##Elnino Dataset

Downloaded from http://www.dmg.org/pmml_examples/index.html#Elnino

Attribute Information:
* 1. latitude
* 2. longitude
* 3. zonal winds (west<0, east>0)
* 4. meridional winds (south<0, north>0)
* 5. relative humidity
* 6. sea surface temperature
* 7. air temperature

Note:
* The date attribute has been removed as not relevant for linear, ridge or lasso regression
* The instances with missing values have been deleted
* The target attribute air temperature has been swapped with sea surface temperature to be the last column of the dataset for easier parsing

###References
* [UCI Machine Learning Repository] University of California, School of Information and Computer Science

[UCI Machine Learning Repository]:http://archive.ics.uci.edu/ml
