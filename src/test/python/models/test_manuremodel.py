import unittest
from models.manuremodel import ManureModel, LiquidManureType, SolidManureType
from models.hmmodel import HeavyMetalType

class Test(unittest.TestCase):
    inputs = {"liquid_manure_part_before_dilution":0.5,
              "liquid_manure_proportions":
                  {LiquidManureType.cattle: 0.5,
                   LiquidManureType.fattening_pigs: 0.2,
                   LiquidManureType.laying_hens: 0.0,
                   LiquidManureType.sows_and_piglets: 0.1,
                   LiquidManureType.other: 0.0
                  },
              "solid_manure_proportions":
                  {SolidManureType.broiler_litter: 0.0,
                   SolidManureType.cattle: 0.05,
                   SolidManureType.horses: 0.0,
                   SolidManureType.laying_hen_litter: 0.25,
                   SolidManureType.pigs: 0.4,
                   SolidManureType.sheep_goats: 0.2,
                   SolidManureType.other: 0.1
                  },
              "total_liquid_manure": 1234.5,
              "total_solid_manure": 9876.5
             }
    
    def testP2O5(self):
        expectedResults = (1129.5675, 116893.355256)
        results = ManureModel(self.inputs).computeP2O5()
        self._assertTuplesEqual(results, expectedResults)
            
    def testN(self):
        expectedResults = (2450.4825, 126081.3940705)
        results = ManureModel(self.inputs).computeN()
        self._assertTuplesEqual(results, expectedResults)
            
    def testNH3(self):
        expectedResults = {"nh3_total_liquid_manure": 891.56112189,
                           "nh3_total_solid_manure": 31325.641473536725}
        results = ManureModel(self.inputs).computeNH3()
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
    def _assertTuplesEqual(self, resultTuple, expectedTuple):
        for result, expected in zip(resultTuple, expectedTuple):
            self.assertAlmostEqual(result, expected)
            
    def testHeavyMetal(self):
        expectedResults = {'hm_total_manure':{
                            HeavyMetalType.cd: 643.7351365,
                            HeavyMetalType.cu: 193483.06650425,
                            HeavyMetalType.zn: 1466829.67538725,
                            HeavyMetalType.pb: 6712.52877915,
                            HeavyMetalType.ni: 24156.51939425,
                            HeavyMetalType.cr: 17067.08426725,
                            HeavyMetalType.hg: 1413.7372515}
                        }
        results = ManureModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
            for k,v in value.items():
                self.assertAlmostEqual(results[key][k], v)

    
if __name__ == "__main__":
    unittest.main()
