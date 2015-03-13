import unittest
import models.co2model as co2model

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"nitrogen_from_urea": 2.0,
                  "nitrogen_from_ureaAN": 2.5,
                  "part_of_urea_in_UAN": 0.5,
                  "calcium_from_lime": 4.0,
                  "calcium_from_carbonation_lime": 5.0,
                  "calcium_from_algae_lime": 5.0,
                  "magnesium_from_fertilizer": 4.0,
                  "magnesium_as_dolomite": 1.0
                    }
        expectedResults = {"m_co2_CO2": 34.96487811}
        
        results = co2model.Co2Model(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
