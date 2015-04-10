import unittest
from models.modelEnums import HeavyMetalType
from models.hmmodel import PesticideType, LandUseCategoryForHM, HmModel

class Test(unittest.TestCase):
    inputs = {"hm_from_manure":
                    {   HeavyMetalType.cd: 1.1,
                        HeavyMetalType.cu: 2.2,
                        HeavyMetalType.zn: 3.3,
                        HeavyMetalType.pb: 4.4,
                        HeavyMetalType.ni: 5.5,
                        HeavyMetalType.cr: 6.6,
                        HeavyMetalType.hg: 7.7
                    },
              "hm_from_mineral_fert":
                    {   HeavyMetalType.cd: 11.1,
                        HeavyMetalType.cu: 12.2,
                        HeavyMetalType.zn: 13.3,
                        HeavyMetalType.pb: 14.4,
                        HeavyMetalType.ni: 15.5,
                        HeavyMetalType.cr: 16.6,
                        HeavyMetalType.hg: 17.7
                    },
              "hm_from_other_organic_fert":
                    {   HeavyMetalType.cd: 21.1,
                        HeavyMetalType.cu: 22.2,
                        HeavyMetalType.zn: 23.3,
                        HeavyMetalType.pb: 24.4,
                        HeavyMetalType.ni: 25.5,
                        HeavyMetalType.cr: 26.6,
                        HeavyMetalType.hg: 27.7
                    },
              "hm_from_seed":
                    {   HeavyMetalType.cd: 31.1,
                        HeavyMetalType.cu: 32.2,
                        HeavyMetalType.zn: 33.3,
                        HeavyMetalType.pb: 34.4,
                        HeavyMetalType.ni: 35.5,
                        HeavyMetalType.cr: 36.6,
                        HeavyMetalType.hg: 37.7
                    },
              "pesticides_quantities":
                    {   PesticideType.cu: 12.14,
                        PesticideType.mancozeb: 15.2,
                        PesticideType.metiram: 11.5,
                        PesticideType.propineb: 62.1,
                        PesticideType.zineb: 62.3,
                        PesticideType.ziram: 42.6
                    },
              "drained_part": 0.4,
              "eroded_soil":12.6,
              "hm_land_use_category":LandUseCategoryForHM.horticultural_crops
             }
        
    def testHeavyMetal(self):
        expectedResults = {'m_hm_heavymetal_to_soil':{
                            HeavyMetalType.cd: 5.425636757133336,
                            HeavyMetalType.cu: 2.6017050394326233,
                            HeavyMetalType.zn: 0.05926202634623969,
                            HeavyMetalType.pb: 0.32068577733307085,
                            HeavyMetalType.ni: 1.2100046055033435,
                            HeavyMetalType.cr: 1.9974101446193773,
                            HeavyMetalType.hg: 58.555680872214516},
                           'm_hm_heavymetal_to_ground_water':{
                            HeavyMetalType.cd: 2.5835260043956044e-06,
                            HeavyMetalType.cu: 7.270955511293038e-05,
                            HeavyMetalType.zn: 1.6151438269742165e-05,
                            HeavyMetalType.pb: 1.7107378220858893e-06,
                            HeavyMetalType.ni: 7.930930214144322e-07,
                            HeavyMetalType.cr: 0.0002954886104699711,
                            HeavyMetalType.hg: 6.106263886363636e-07},
                           'm_hm_heavymetal_to_surface_water':{
                            HeavyMetalType.cd: 1.684981684981685e-06,
                            HeavyMetalType.cu: 4.6638705367384134e-05,
                            HeavyMetalType.zn: 1.0685637145043578e-05,
                            HeavyMetalType.pb: 9.918200408997955e-07,
                            HeavyMetalType.ni: 0.0,
                            HeavyMetalType.cr: 0.0001960903543517825,
                            HeavyMetalType.hg: 3.353409090909091e-07}
                        }
        results = HmModel(self.inputs).compute();
        for key, value in expectedResults.items():
            for k,v in value.items():
                self.assertAlmostEqual(results[key][k], v)

    
if __name__ == "__main__":
    unittest.main()
