import unittest
from models.otherfertilisermodel import OtherFertiliserType, OtherFertModel
from models.modelEnums import HeavyMetalType

class Test(unittest.TestCase):
    inputs = {"other_fertiliser_quantities":
                  { OtherFertiliserType.compost: 98.0,
                    OtherFertiliserType.meat_and_bone_meal: 78.2,
                    OtherFertiliserType.castor_oil_shell_coarse: 12.0,#No value for now
                    OtherFertiliserType.vinasse: 13.0,#No value for now
                    OtherFertiliserType.dried_poultry_manure: 44.1,
                    OtherFertiliserType.stone_meal: 63.1,#No value for now
                    OtherFertiliserType.feather_meal: 88.2,
                    OtherFertiliserType.horn_meal: 96.1,
                    OtherFertiliserType.horn_shavings_fine: 63.2,
                    OtherFertiliserType.sewagesludge_liquid: 54.2,
                    OtherFertiliserType.sewagesludge_dehydrated: 83.5,
                    OtherFertiliserType.sewagesludge_dried: 46.1
                  }
             }
    
    def testP2O5(self):
        expectedResults = 0.0
        results = OtherFertModel(self.inputs).computeP2O5()
        self.assertAlmostEqual(results, expectedResults)
        
    def testN(self):
        expectedResults = 0.0
        results = OtherFertModel(self.inputs).computeN()
        self.assertAlmostEqual(results, expectedResults)
            
    def testHeavyMetal(self):
        expectedResults = {'hm_total_otherfert':{
                            HeavyMetalType.cd: 235307.08,
                            HeavyMetalType.cu: 44818680.9,
                            HeavyMetalType.zn: 133614262.1,
                            HeavyMetalType.pb: 11094829.1,
                            HeavyMetalType.ni: 6496816.8,
                            HeavyMetalType.cr: 22064443.6,
                            HeavyMetalType.hg: 197829.58}
                        }
        results = OtherFertModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
            for k,v in value.items():
                self.assertAlmostEqual(results[key][k], v)
            
    
if __name__ == "__main__":
    unittest.main()
