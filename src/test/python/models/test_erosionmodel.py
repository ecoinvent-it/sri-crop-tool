import unittest
import models.erosionmodel as erosionmodel
from models.erosionmodel import AntiErosionPractice, TillageMethod

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"average_annual_precipitation":1200,
                "yearly_precipitation_as_snow":0.2,
                "annualized_irrigation":30000,
                "slope":0.03,
                "slope_length":50.0,
                "tillage_method":TillageMethod.spring_plow,
                "anti_erosion_practice":AntiErosionPractice.cross_slope,
                "soil_erodibility_factor":0.032,
                "crop_factor":0.5
                 }
        expectedResults = {"m_Erosion_eroded_soil": 20814.14111845}
        
        results = erosionmodel.ErosionModel(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
