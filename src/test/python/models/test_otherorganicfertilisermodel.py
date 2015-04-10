import unittest
from models.otherorganicfertilisermodel import OtherOrganicFertiliserType, OtherOrganicFertModel
from models.modelEnums import HeavyMetalType

class Test(unittest.TestCase):
    inputs = {"other_organic_fertiliser_quantities":
                  { OtherOrganicFertiliserType.compost: 98.0,
                    OtherOrganicFertiliserType.meat_and_bone_meal: 78.2,
                    OtherOrganicFertiliserType.castor_oil_shell_coarse: 12.0,#No value for now
                    OtherOrganicFertiliserType.vinasse: 13.0,#No value for now
                    OtherOrganicFertiliserType.dried_poultry_manure: 44.1,
                    OtherOrganicFertiliserType.stone_meal: 63.1,#No value for now
                    OtherOrganicFertiliserType.feather_meal: 88.2,
                    OtherOrganicFertiliserType.horn_meal: 96.1,
                    OtherOrganicFertiliserType.horn_shavings_fine: 63.2,
                    OtherOrganicFertiliserType.sewagesludge_liquid: 54.2,
                    OtherOrganicFertiliserType.sewagesludge_dehydrated: 83.5,
                    OtherOrganicFertiliserType.sewagesludge_dried: 46.1
                  }
             }
    
    def testP2O5(self):
        expectedResults = 189.7
        results = OtherOrganicFertModel(self.inputs).computeP2O5()
        self.assertAlmostEqual(results, expectedResults)
        
    def testN(self):
        expectedResults = 34230.815
        results = OtherOrganicFertModel(self.inputs).computeN()
        self.assertAlmostEqual(results, expectedResults)
        
    def testNH3(self):
        expectedResults = 0.0
        results = OtherOrganicFertModel(self.inputs).computeN()
        self.assertAlmostEqual(results, expectedResults)
            
    def testHeavyMetal(self):
        expectedResults = {
                            HeavyMetalType.cd: 235307.08,
                            HeavyMetalType.cu: 44818680.9,
                            HeavyMetalType.zn: 133614262.1,
                            HeavyMetalType.pb: 11094829.1,
                            HeavyMetalType.ni: 6496816.8,
                            HeavyMetalType.cr: 22064443.6,
                            HeavyMetalType.hg: 197829.58}
                        
        results = OtherOrganicFertModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
                self.assertAlmostEqual(results[key], value)
            
    
if __name__ == "__main__":
    unittest.main()
