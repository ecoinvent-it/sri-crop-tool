import unittest
import models.irrigationmodel as irrigationmodel
from models.irrigationmodel import IrrigationType

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {  "water_use_total":12.45,
                    "irrigation_types_proportions":
                    {
                    IrrigationType.surface_irrigation_no_energy:0.0,
                    IrrigationType.surface_irrigation_electricity:0.0,
                    IrrigationType.surface_irrigation_diesel:0.0,
                    IrrigationType.sprinkler_irrigation_electricity:1.0,
                    IrrigationType.sprinkler_irrigation_diesel:0.0,
                    IrrigationType.drip_irrigation_electricity:0.0,
                    IrrigationType.drip_irrigation_diesel:0.0
                     }
                 }
        expectedResults = {"m_Irr_water_to_air": 9.337499999,
                           "m_Irr_water_to_water_river": 2.49,
                           "m_Irr_water_to_water_groundwater": 0.6225
                 }
        
        results = irrigationmodel.IrrigationModel(inputs).compute()
        
        for key, value in expectedResults.items():
            #if type(value) is dict:
            #    for k,v in value.items():
            #        self.assertAlmostEqual(results[key][k], v)
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
