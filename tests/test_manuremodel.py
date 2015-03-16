import unittest
from models.manuremodel import ManureModel, LiquidManureType, SolidManureType

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
              "total_solid_maure": 9876.5
             }
    
    def testP2O5(self):
        expectedResults = (1129.5675, 116893.355256)
        results = ManureModel(self.inputs).computeP2O5()
        self._assertTuplesEqual(results, expectedResults)
            
    def testN(self):
        expectedResults = (2450.4825, 126081.3940705)
        results = ManureModel(self.inputs).computeN()
        self._assertTuplesEqual(results, expectedResults)
            
    def testNH4N(self):
        expectedResults = (733.2621375, 25763.6927648)
        results = ManureModel(self.inputs).computeNH4N()
        self._assertTuplesEqual(results, expectedResults)
        
    def _assertTuplesEqual(self, resultTuple, expectedTuple):
        for result, expected in zip(resultTuple, expectedTuple):
            self.assertAlmostEqual(result, expected)

    
if __name__ == "__main__":
    unittest.main()
