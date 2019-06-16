package au.com.ms.app

case class NWPConfig(endDate: String,
                     step: Long,
                     printOut: Boolean,
                     inputFile: String,
                     outputFile: String
                    )
