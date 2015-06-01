import unittest
from models.hmmodel import HeavyMetalType
from models.seedmodel import SeedModel, SeedType

class Test(unittest.TestCase):
    inputs = {"seed_quantities":
                  { SeedType.field_bean_seed: 3.3,
                    SeedType.flower: 7.7,
                    SeedType.pea: 11.11,
                    SeedType.vegetable: 15.15,
                    SeedType.barley: 20.0,
                    SeedType.grass: 11.11,
                    SeedType.clover: 12.12,
                    SeedType.potato: 27.27,
                    SeedType.maize: 31.31,
                    SeedType.rape: 17.17,
                    SeedType.rye: 37.37,
                    SeedType.soya_bean:  40.41,
                    SeedType.sunflower: 45.45,
                    SeedType.wheat:  49.49,
                    SeedType.sugar_fodder_beet: 26.26,
                    SeedType.tree_seedlings: 55.55,
                    SeedType.other: 31.31
                  }
             }
            
    def testHeavyMetal(self):
        expectedResults = {
                            HeavyMetalType.cd: 56.144201,
                            HeavyMetalType.cu: 2298.69065,
                            HeavyMetalType.zn: 12712.87363,
                            HeavyMetalType.pb: 220.827787,
                            HeavyMetalType.ni: 465.396097,
                            HeavyMetalType.cr: 168.532663,
                            HeavyMetalType.hg: 12.768396}
                        
        results = SeedModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
                self.assertAlmostEqual(results[key], value)

    
if __name__ == "__main__":
    unittest.main()
