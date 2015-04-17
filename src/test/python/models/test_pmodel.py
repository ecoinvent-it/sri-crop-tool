import unittest
import models.pmodel as pmodel

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"crop_cycle_per_year": 1.2,
                  "drained_part": 0.3,
                  "eroded_reaching_river": 0.2,
                  "eroded_soil": 1000.0,
                  "eroded_soil_p_enrichment": 1.86,
                  "land_use_category": pmodel.LandUseCategory.grassland_extensive,
                  "p2o5_in_liquid_manure": 13.0,
                  "p2o5_in_liquid_sludge": 15.0,
                  "p2O5_from_mineral_fert": 17.0,
                  "p2o5_in_solid_manure": 19.0,
                  "p_content_in_soil": 0.00095
                  }
        expectedResults = {"m_P_PO4_groundwater": 0.11482872,
                           "m_P_PO4_surfacewater_drained": 0.2952739,
                           "m_P_PO4_surfacewater_ro": 0.5298755,
                           "m_P_P_surfacewater_erosion": 0.2945}
        
        results = pmodel.PModel(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
