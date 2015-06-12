import unittest
from models.manuremodel import ManureModel, LiquidManureType, SolidManureType
from models.modelEnums import HeavyMetalType

class Test(unittest.TestCase):
    inputs = {"liquid_manure_part_before_dilution":0.5,
              "liquid_manure_quantities":
                  {LiquidManureType.cattle: 1234.5 * 0.5,
                   LiquidManureType.pig: 1234.5 * 0.3,
                   LiquidManureType.laying_hens: 1234.5 * 0.0,
                   LiquidManureType.other: 1234.5 * 0.0
                  },
              "solid_manure_quantities":
                  {#SolidManureType.broiler_litter: 9876.5 * 0.0 * 1000.0,
                   SolidManureType.cattle: 9876.5 * 0.05 * 1000.0,
                   SolidManureType.horses: 9876.5 * 0.0 * 1000.0,
                   SolidManureType.laying_hen_litter: 9876.5 * 0.25 * 1000.0,
                   SolidManureType.pigs: 9876.5 * 0.4 * 1000.0,
                   SolidManureType.sheep_goats: 9876.5 * 0.2 * 1000.0,
                   SolidManureType.other: 9876.5 * 0.1 * 1000.0
                  }
             }
    
    def testP2O5(self):
        expectedResults = (1111.05, 104547.730256)
        results = ManureModel(self.inputs).computeP2O5()
        self._assertTuplesEqual(results, expectedResults)
            
    def testN(self):
        expectedResults = 2410.36125 + 134723.3315705
        results = ManureModel(self.inputs).computeN()
        self.assertAlmostEqual(results, expectedResults)
            
    def testNH3(self):
        expectedResults = 858.8579139382183 + 34050.108599716696
        result = ManureModel(self.inputs).computeNH3()
        self.assertAlmostEqual(result, expectedResults)
        
    def _assertTuplesEqual(self, resultTuple, expectedTuple):
        for result, expected in zip(resultTuple, expectedTuple):
            self.assertAlmostEqual(result, expected)
            
    def testHeavyMetal(self):
        expectedResults = {
                            HeavyMetalType.cd: 731.1619145,
                            HeavyMetalType.cu: 205919.60468675,
                            HeavyMetalType.zn: 1502306.2609172503,
                            HeavyMetalType.pb: 7708.44540965,
                            HeavyMetalType.ni: 47301.3317155,
                            HeavyMetalType.cr: 21383.2382235,
                            HeavyMetalType.hg: 1462.5271615}
                        
        results = ManureModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
                self.assertAlmostEqual(results[key], value)

    
if __name__ == "__main__":
    unittest.main()
