import unittest
import models.nmodel as nmodel

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {
                  "ammonia_due_to_manure":30.2,
                  "ammonia_due_to_mineral_fert": 24.0,
                  "ammonia_due_to_other_orga_fert": 1.53,
                  "bulk_density_of_soil": 1200,
                  "c_per_n_ratio": 11,
                  "clay_content": 0.2,
                  "considered_soil_volume": 5000,
                  "drained_part": 0.3,
                  "nitrogen_from_all_manure": 120.4,
                  "nitrogen_from_crop_residues": 56.1,
                  "nitrogen_from_mineral_fert": 145.0,
                  "nitrogen_from_other_orga_fert": 20.35,
                  "nitrogen_uptake_by_crop": 107,
                  "norg_per_ntotal_ratio": 0.85,
                  "organic_carbon_content": 0.025,
                  "precipitation_per_crop_cycle": 8500,
                  "rooting_depth": 0.5,
                  "water_use_total": 300
                 }
        
        expectedResults = {"m_N_ammonia_total": 55.73,
                "m_N_nitrate_to_groundwater": 389.1356325753885,
                "m_N_nitrate_to_surfacewater": 166.77241396088075,
                "m_N_N2o_air": 7.625279384894846,
                "m_N_Nox_as_n2o_air": 11.39094064}
        
        results = nmodel.NModel(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
