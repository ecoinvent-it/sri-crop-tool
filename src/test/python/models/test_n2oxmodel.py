import unittest
import models.n2oxmodel as n2oxmodel

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"nitrogen_from_all_manure": 120.4,
                  "nitrogen_from_other_organic_fert":98.4,
                  "nitrogen_from_mineral_fert": 145.0,
                  "nitrogen_from_crop_residues": 56.1,
                  "nitrate_to_groundwater": 45.2,
                  "nitrate_to_surfacewater": 20.0,
                  "ammonia_due_to_mineral_fert": 24.0,
                  "ammonia_due_to_manure":30.2
                 }
        expectedResults = {"m_N2ox_N2o_air": 7.540465863,
                           "m_N2ox_Nox_as_n2o_air": 14.5022719}
        
        results = n2oxmodel.N2OxModel(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
