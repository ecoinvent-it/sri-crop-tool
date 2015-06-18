import unittest
import models.lucmodel as lucmodel

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"crop": "almond",
                  "country":"AR",
                  "crop_cycle_per_year": 2.0
                    }
        expectedResults = {'m_LUC_luc_crop_type': "perennial", 'm_LUC_luc_formula': "0.069767441860465 * LUC_crop_specific + 0.2007738108116725 * (1-LUC_crop_specific)"}
        
        results = lucmodel.LUCModel(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
