import unittest
import models.n2oxmodel as n2oxmodel

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"n_total_manure": 120.4,
                  "n_total_mineral_fert": 145.0,
                  "n_crop_residues": 56.1,
                  "no3_groundwater": 45.2,
                  "no3_surfacewater": 20.0,
                  "nh3_total_mineral_fert": 24.0,
                  "nh3_total_liquid_manure":10.2,
                  "nh3_total_solid_manure": 20.0
                 }
        expectedResults = {"m_N2ox_N2o_air": 5.975708542,
                           "m_N2ox_Nox_air": 10.57972229}
        
        results = n2oxmodel.N2OxModel(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
