import unittest
from models.manuremodel import ManureModel, LiquidManureType, SolidManureType
from models.modelEnums import HeavyMetalType

class Test(unittest.TestCase):
    inputs = {"liquid_manure_part_before_dilution":0.5,
              "liquid_manure_quantities":
                  {LiquidManureType.cattle: 1234.5 * 0.5,
                   LiquidManureType.fattening_pigs: 1234.5 * 0.2,
                   LiquidManureType.laying_hens: 1234.5 * 0.0,
                   LiquidManureType.sows_and_piglets: 1234.5 * 0.1,
                   LiquidManureType.other: 1234.5 * 0.0
                  },
              "solid_manure_quantities":
                  {SolidManureType.broiler_litter: 9876.5 * 0.0 * 1000.0,
                   SolidManureType.cattle: 9876.5 * 0.05 * 1000.0,
                   SolidManureType.horses: 9876.5 * 0.0 * 1000.0,
                   SolidManureType.laying_hen_litter: 9876.5 * 0.25 * 1000.0,
                   SolidManureType.pigs: 9876.5 * 0.4 * 1000.0,
                   SolidManureType.sheep_goats: 9876.5 * 0.2 * 1000.0,
                   SolidManureType.other: 9876.5 * 0.1 * 1000.0
                  }
             }
    
    def testP2O5(self):
        expectedResults = (1129.5675, 116893.355256)
        results = ManureModel(self.inputs).computeP2O5()
        self._assertTuplesEqual(results, expectedResults)
            
    def testN(self):
        expectedResults = 2450.4825 + 126081.3940705
        results = ManureModel(self.inputs).computeN()
        self.assertAlmostEqual(results, expectedResults)
            
    def testNH3(self):
        expectedResults = 891.56112189 + 31325.641473536725
        result = ManureModel(self.inputs).computeNH3()
        self.assertAlmostEqual(result, expectedResults)
        
    def _assertTuplesEqual(self, resultTuple, expectedTuple):
        for result, expected in zip(resultTuple, expectedTuple):
            self.assertAlmostEqual(result, expected)
            
    def testHeavyMetal(self):
        expectedResults = {
                            HeavyMetalType.cd: 643.7351365,
                            HeavyMetalType.cu: 193483.06650425,
                            HeavyMetalType.zn: 1466829.67538725,
                            HeavyMetalType.pb: 6712.52877915,
                            HeavyMetalType.ni: 24156.51939425,
                            HeavyMetalType.cr: 17067.08426725,
                            HeavyMetalType.hg: 1413.7372515}
                        
        results = ManureModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
                self.assertAlmostEqual(results[key], value)

    
if __name__ == "__main__":
    unittest.main()
