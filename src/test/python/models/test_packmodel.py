import unittest
import models.packmodel as packmodel

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"nitrogen_from_mineral_fert": 1.0,
                  "p2O5_from_mineral_fert": 2.0,
                  "k2O_from_mineral_fert": 3.0,
                  "magnesium_from_fertilizer": 4.0,
                  "ca_from_mineral_fert": 5.0,
                  "fert_n_ammonia_liquid": 0.5,
                  "pest_total": 70.0
                 }
        expectedResults = {"m_Pack_packaging_liquid_fertilisers_and_pesticides": 1.14,
                           "m_Pack_packaging_solid_fertilisers_and_pesticides": 29.0}
        
        results = packmodel.PackModel(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
