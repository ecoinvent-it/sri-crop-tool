import unittest
import models.no3model as no3model

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"bulk_density_of_soil": 1200,
                  "c_per_n_ratio": 11,
                  "clay_content": 0.2,
                  "considered_soil_volume": 5000,
                  "drained_part": 0.3,
                  "fertilisers_gas_losses": 0.1,
                  "irrigation": 30,
                  "nitrogen_from_fertiliser": 150,
                  "nitrogen_uptake_by_crop": 107,
                  "norg_per_ntotal_ratio": 0.85,
                  "organic_carbon_content": 0.025,
                  "precipitation": 850,
                  "rooting_depth": 0.5,
                 }
        expectedResults = {"m_No3_nitrate_to_groundwater": 286.7652489,
                           "m_No3_nitrate_to_surfacewater": 122.899392389}
        
        results = no3model.No3Model(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
