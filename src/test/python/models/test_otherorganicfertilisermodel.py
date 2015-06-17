import unittest
from models.otherorganicfertilisermodel import OtherOrganicFertModel, CompostType, SludgeType
from models.modelEnums import HeavyMetalType

class Test(unittest.TestCase):
    inputs = {"compost_quantities":
                  { CompostType.compost: 98.0 * 1000.0,
                    CompostType.meat_and_bone_meal: 78.2 * 1000.0,
                    CompostType.castor_oil_shell_coarse: 12.0 * 1000.0,
                    CompostType.vinasse: 13.0 * 1000.0,
                    CompostType.dried_poultry_manure: 44.1 * 1000.0,
                    CompostType.stone_meal: 63.1 * 1000.0,
                    CompostType.feather_meal: 88.2 * 1000.0,
                    CompostType.horn_meal: 96.1 * 1000.0,
                    CompostType.horn_shavings_fine: 63.2 * 1000.0
                  },
              "sludge_quantities":
                  {
                    SludgeType.sewagesludge_liquid: 54.2 * 1000.0,
                    SludgeType.sewagesludge_dehydrated: 83.5 * 1000.0,
                    SludgeType.sewagesludge_dried: 46.1 * 1000.0
                  }
             }
    
    def testP2O5(self):
        expectedResults = 189.7
        results = OtherOrganicFertModel(self.inputs).computeP2O5()
        self.assertAlmostEqual(results, expectedResults)
        
    def testN(self):
        expectedResults = 43066.915
        results = OtherOrganicFertModel(self.inputs).computeN()
        self.assertAlmostEqual(results, expectedResults)
        
    def testNH3(self):
        expectedResults = 2080.75027
        results = OtherOrganicFertModel(self.inputs).computeNH3()
        self.assertAlmostEqual(results, expectedResults)
            
    def testHeavyMetal(self):
        expectedResults = {
                            HeavyMetalType.cd: 237467.08,
                            HeavyMetalType.cu: 45260190.9,
                            HeavyMetalType.zn: 135075977.1,
                            HeavyMetalType.pb: 11335739.1,
                            HeavyMetalType.ni: 6625926.8,
                            HeavyMetalType.cr: 22177843.6,
                            HeavyMetalType.hg: 198369.58}
                        
        results = OtherOrganicFertModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
                self.assertAlmostEqual(results[key], value)
            
    
if __name__ == "__main__":
    unittest.main()
