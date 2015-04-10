import unittest
from models.hmmodel import HeavyMetalType
from models.seedmodel import SeedModel, SeedType

class Test(unittest.TestCase):
    inputs = {"seed_quantities":
                  { SeedType.field_bean_seed_ip: 1.1,
                    SeedType.field_bean_seed_org: 2.2,
                    SeedType.flower_ip: 3.3,
                    SeedType.flower_org: 4.4,
                    SeedType.pea_ip: 5.5,
                    SeedType.pea_org: 6.6,
                    SeedType.vegetable_ip: 7.7,
                    SeedType.vegetable_org:8.8,
                    SeedType.barley_ip:  9.9,
                    SeedType.barley_org: 10.1,
                    SeedType.grass: 11.11,
                    SeedType.clover: 12.12,
                    SeedType.potato_ip:  13.13,
                    SeedType.potato_org: 14.14,
                    SeedType.maize_ip: 15.15,
                    SeedType.maize_org:16.16,
                    SeedType.rape: 17.17,
                    SeedType.rye_ip:  18.18,
                    SeedType.rye_org: 19.19,
                    SeedType.soya_bean_ip:  20.2,
                    SeedType.soya_bean_org: 21.21,
                    SeedType.sunflower_ip: 22.22,
                    SeedType.sunflower_org: 23.23,
                    SeedType.wheat_ip:  24.24,
                    SeedType.wheat_org: 25.25,
                    SeedType.sugar_fodder_beet: 26.26,
                    SeedType.tree_seedlings_ip: 27.27,
                    SeedType.tree_seedlings_org: 28.28,
                    SeedType.salix: 29.29,
                    SeedType.miscanthus: 30.3,
                    SeedType.other: 31.31
                  }
             }
            
    def testHeavyMetal(self):
        expectedResults = {
                            HeavyMetalType.cd: 119.662601,
                            HeavyMetalType.cu: 6490.90505,
                            HeavyMetalType.zn: 33038.76163,
                            HeavyMetalType.pb: 563.827147,
                            HeavyMetalType.ni: 1125.987457,
                            HeavyMetalType.cr: 517.883863,
                            HeavyMetalType.hg: 38.175756}
                        
        results = SeedModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
                self.assertAlmostEqual(results[key], value)

    
if __name__ == "__main__":
    unittest.main()
