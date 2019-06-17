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

##### Sample output for one location

```csv
Melbourne|-37.814,144.96332,7.0|2019-06-15 20:54:33|Snow|+12|990.3|96
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:01:13|Sunny|+11.5|970.3|95
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:07:53|Rain|+11|950.3|94
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:14:33|Snow|+10.5|930.3|93
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:21:13|Sunny|+10|910.3|92
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:27:53|Rain|+9.5|890.3|91
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:34:33|Snow|+9|870.3|90
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:41:13|Sunny|+8.5|850.3|89
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:47:53|Rain|+8|830.3|88
Melbourne|-37.814,144.96332,7.0|2019-06-15 21:54:33|Snow|+7.5|810.3|87
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:01:13|Sunny|+7|830.3|86
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:07:53|Rain|+6.5|850.3|85
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:14:33|Snow|+6|870.3|84
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:21:13|Sunny|+5.5|890.3|83
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:27:53|Rain|+5|910.3|82
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:34:33|Snow|+4.5|930.3|81
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:41:13|Sunny|+4|950.3|80
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:47:53|Rain|+3.5|970.3|79
Melbourne|-37.814,144.96332,7.0|2019-06-15 22:54:33|Snow|+3|990.3|78
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:01:13|Sunny|+2.5|1010.3|77
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:07:53|Rain|+2|1030.3|76
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:14:33|Snow|+1.5|1050.3|75
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:21:13|Sunny|+1|1070.3|74
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:27:53|Rain|+0.5|1090.3|73
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:34:33|Snow|0|1110.3|72
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:41:13|Sunny|-0.5|1130.3|71
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:47:53|Rain|-1|1150.3|70
Melbourne|-37.814,144.96332,7.0|2019-06-15 23:54:33|Snow|-1.5|1170.3|69
```

###### Libraries:
[Cats](https://typelevel.org/cats/) is used to take care of the side-effects.

Configuration is parsed with [pureconfig](https://pureconfig.github.io).

Unit tests done with [scalatest](http://www.scalatest.org).


### Configuration.

Default configuration is set in [application.conf](https://github.com/DanyMariaLee/nwp_/blob/master/src/main/resources/application.conf).
```csv
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
Results are: `Class 77%, Method 89%, Line 95%`

With default settings we will expect around 96MB of data in the output.csv, that will be written alongside with `build.sbt` in the project folder.

#### WARNING
Written on mac, application contains tests that are actually writing into files. As I don't work on Windows machine I don't know if those tests will run. Better, use mac to run this app.

M.
