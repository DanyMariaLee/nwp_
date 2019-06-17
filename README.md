# Weather generator.

[![N|Solid](https://upload.wikimedia.org/wikipedia/commons/d/d5/Weather_App_Icon_iOS.png)](https://nodesource.com/products/nsolid)

The application is generating weather using simple formulas:

`time`:  t2 = t1 + dt, dt = step from application.conf

`temperature`:  T[-30,30], dT = 0.5

`hunidity`:  H[1,100], dH = 1

`pressure`:  P[800,1400], dP = 20

`condition`:  Rain => Snow => Sunny => Rain

For input we have 14 different locations with starting conditions.
The idea is to show how with the same input functions produce the same output.
After calculation is done file with output is saved.

###### Libraries:
[Cats](https://typelevel.org/cats/) is used to take care of the side-effects.

Configuration is parsed with [pureconfig](https://pureconfig.github.io).

Unit tests done with [scalatest](http://www.scalatest.org).


### Configuration.

Default configuration is set in [application.conf](https://github.com/DanyMariaLee/nwp_/blob/master/src/main/resources/application.conf).
```
end-date = "2019-06-17"
step = 1000
print-out = true
input-file = "input.csv"
output-file = "output.csv"
```

Script is using `2019-06-15` as a start date and we will provide `end-date` = `2019-06-16`. After reaching this date simulation stops.

Parameter `step` is a calculation step in milliseconds. Default 1000 means that we will recalculate weather every 1000 milliseconds.

With `print-out` set to `true` application will output all the data in console.

### Quick start.
Prerequisites: `SBT 1.2.8`, `scala 2.12`
Steps to success:
- clone repo
- navigate to the project directory: `$ cd /nwp_`
- compile `sbt compile`
- run tests `sbt test`
- run `sbt run`

To take a look at the test coverage right-click on `nwp` folder in `IntellijIdea`, and then on `Run with coverage`.
Results are: `Class 77%, Method 91%, Line 95%`

With default settings we will expect around 96MB of data in the output.csv, that will be written alongside with `build.sbt` in the project folder.

#### WARNING
Written on mac, application contains tests that are actually writing into files. As I don't work on Windows machine I don't know if those tests will run. Better, use mac to run this app.

M.
