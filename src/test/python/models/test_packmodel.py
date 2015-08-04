import unittest
import models.packmodel as packmodel
from models.fertilisermodel import NFertiliserType

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"nitrogen_from_mineral_fert": 1.0,
                  "p2O5_from_mineral_fert": 2.0,
                  "k2O_from_mineral_fert": 3.0,
                  "magnesium_from_fertilizer": 4.0,
                  "ca_from_mineral_fert": 5.0,
                  "n_fertiliser_quantities": {NFertiliserType.ammonia_liquid: 0.5},
                  "pest_total": 70.0,
                  "pest_horticultural_oil":1.0
                 }
        expectedResults = {"m_Pack_wfldb_packaging_liquid": 0.0214,
                           "m_Pack_wfldb_packaging_solid": 0.29,
                           "m_Pack_ecoinvent_packaging_liquid": 2.21,
                           "m_Pack_ecoinvent_packaging_solid": 29.0
                           }
        
        results = packmodel.PackModel(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
