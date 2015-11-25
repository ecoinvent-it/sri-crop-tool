import unittest
import models.co2model as co2model
from models.fertilisermodel import NFertiliserType, OtherMineralFertiliserType

class Test(unittest.TestCase):
    
    def testModel(self):
        inputs = {"n_fertiliser_quantities": {NFertiliserType.urea: 2.0, NFertiliserType.ureaAN: 2.5},
                  "part_of_urea_in_UAN": 0.5,
                  "other_mineral_fertiliser_quantities": {
                      OtherMineralFertiliserType.ca_limestone: 4.0,
                      OtherMineralFertiliserType.ca_carbonation_limestone: 5.0,
                      OtherMineralFertiliserType.ca_seaweed_limestone: 5.0},
                  "magnesium_from_fertilizer": 4.0,
                  "magnesium_as_dolomite": 1.0
                    }
        expectedResults = {"m_co2_CO2_from_fertilisers": 34.96487811}
        
        results = co2model.Co2Model(inputs).compute()
        
        for key, value in expectedResults.items():
            self.assertAlmostEqual(results[key], value)
        
if __name__ == "__main__":
    unittest.main()
