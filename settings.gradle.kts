rootProject.name = "MARSSIII"

include("BankCore", "Gateway", "Storage")

include("Gateway:Application", "Gateway:DataAccess", "Gateway:Presentation")
include("BankCore:Application", "BankCore:DataAccess", "BankCore:Presentation")
include("Storage:Application", "Storage:Infrastructure", "Storage:Presentation", "Storage:Business")
include("BankCore:Infrastraction")