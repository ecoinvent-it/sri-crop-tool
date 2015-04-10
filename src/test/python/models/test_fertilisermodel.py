import unittest
from models.fertilisermodel import FertModel, \
                                    NFertiliserType, \
                                    PFertiliserType, \
                                    KFertiliserType, \
                                    OtherMineralFertiliserType
from models.modelEnums import HeavyMetalType

class Test(unittest.TestCase):
    inputs = {"n_fertiliser_quantities":
                    {  NFertiliserType.ammonium_nitrate: 0.1,
                       NFertiliserType.urea: 0.2,
                       NFertiliserType.ureaAN: 0.3,
                       NFertiliserType.mono_ammonium_phosphate: 0.4,
                       NFertiliserType.di_ammonium_phosphate: 0.5,
                       NFertiliserType.an_phosphate: 0.6,
                       NFertiliserType.lime_ammonium_nitrate: 0.7,
                       NFertiliserType.ammonium_sulphate: 0.8,
                       NFertiliserType.potassium_nitrate: 0.9,
                       NFertiliserType.ammonia_liquid:1.0
                    },
              "p_fertiliser_quantities":
                    {    PFertiliserType.triple_superphosphate: 0.1,
                         PFertiliserType.superphosphate: 0.2,
                         PFertiliserType.mono_ammonium_phosphate: 0.3,
                         PFertiliserType.di_ammonium_phosphate: 0.4,
                         PFertiliserType.an_phosphate: 0.5,
                         PFertiliserType.hypophosphate_raw_phosphate: 0.6,
                         PFertiliserType.ground_basic_slag: 0.7
                    },
              "k_fertiliser_quantities":
                    {   KFertiliserType.potassium_salt: 0.1,
                        KFertiliserType.potassium_sulphate: 0.2,
                        KFertiliserType.potassium_nitrate: 0.3,
                        KFertiliserType.patent_potassium: 0.4
                    },
              "other_mineral_fertiliser_quantities":
                    {   OtherMineralFertiliserType.ca_limestone: 0.1,
                        OtherMineralFertiliserType.ca_carbonation_linestone: 0.2,
                        OtherMineralFertiliserType.ca_seaweed_limestone: 0.3
                    },
              "soil_with_ph_under_or_7":0.45
             }
        
    def testNH3(self):
        expectedResults = 0.48673871
        results = FertModel(self.inputs).computeNH3()
        self.assertAlmostEqual(results, expectedResults)
        
    def testHeavyMetal(self):
        expectedResults = {
                            HeavyMetalType.cd: 115.843,
                            HeavyMetalType.cu: 610.731,
                            HeavyMetalType.zn: 2769.902,
                            HeavyMetalType.pb: 282.137,
                            HeavyMetalType.ni: 383.838,
                            HeavyMetalType.cr: 10076.781,
                            HeavyMetalType.hg: 0.0}

        results = FertModel(self.inputs).computeHeavyMetal();
        for key, value in expectedResults.items():
                self.assertAlmostEqual(results[key], value)

    
if __name__ == "__main__":
    unittest.main()
