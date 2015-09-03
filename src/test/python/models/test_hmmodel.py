import unittest
from models.modelEnums import HeavyMetalType
from models.hmmodel import PesticideType, LandUseCategoryForHM, HmModel

class Test(unittest.TestCase):
    inputs = {"crop_cycle_per_year":2.0,
              "hm_from_manure":
                    {   HeavyMetalType.cd: 1100.0,
                        HeavyMetalType.cu: 2200.0,
                        HeavyMetalType.zn: 3300.0,
                        HeavyMetalType.pb: 4400.0,
                        HeavyMetalType.ni: 5500.0,
                        HeavyMetalType.cr: 6600.0,
                        HeavyMetalType.hg: 7700.0
                    },
              "hm_from_mineral_fert":
                    {   HeavyMetalType.cd: 1110.0,
                        HeavyMetalType.cu: 1220.0,
                        HeavyMetalType.zn: 1330.0,
                        HeavyMetalType.pb: 1440.0,
                        HeavyMetalType.ni: 1550.0,
                        HeavyMetalType.cr: 1660.0,
                        HeavyMetalType.hg: 1770.0
                    },
              "hm_from_other_organic_fert":
                    {   HeavyMetalType.cd: 2110.0,
                        HeavyMetalType.cu: 2220.0,
                        HeavyMetalType.zn: 2330.0,
                        HeavyMetalType.pb: 2440.0,
                        HeavyMetalType.ni: 2550.0,
                        HeavyMetalType.cr: 2660.0,
                        HeavyMetalType.hg: 2770.0
                    },
              "hm_from_seed":
                    {   HeavyMetalType.cd: 3110.0,
                        HeavyMetalType.cu: 3220.0,
                        HeavyMetalType.zn: 3330.0,
                        HeavyMetalType.pb: 3440.0,
                        HeavyMetalType.ni: 3550.0,
                        HeavyMetalType.cr: 3660.0,
                        HeavyMetalType.hg: 3770.0
                    },
              "hm_pesticides_quantities":
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
                            HeavyMetalType.cd: 0.007071552480406684,
                            HeavyMetalType.cu: 0.017519604181532904,
                            HeavyMetalType.zn: 0.01755668219778056,
                            HeavyMetalType.pb: 0.006337265077323208,
                            HeavyMetalType.ni: 0.01086194280837136,
                            HeavyMetalType.cr: 0.0035112362099359953,
                            HeavyMetalType.hg: 0.015984306683531527},
                           'm_hm_heavymetal_to_ground_water':{
                            HeavyMetalType.cd: 1.4325192802056556e-05,
                            HeavyMetalType.cu: 0.0010199805492520724,
                            HeavyMetalType.zn: 0.005199725365641214,
                            HeavyMetalType.pb: 0.0001001233981964879,
                            HeavyMetalType.ni: 0.0,
                            HeavyMetalType.cr: 0.005652471807375799,
                            HeavyMetalType.hg: 3.8939195509822265e-07},
                           'm_hm_heavymetal_to_surface_water':{
                            HeavyMetalType.cd: 9.867828076606685e-06,
                            HeavyMetalType.cu: 0.0007201035478238318,
                            HeavyMetalType.zn: 0.003506379840798713,
                            HeavyMetalType.pb: 8.175722927384908e-05,
                            HeavyMetalType.ni: 2.224287219512195e-05,
                            HeavyMetalType.cr: 0.003794316975068577,
                            HeavyMetalType.hg: 3.429017506704085e-07}
                        }
        results = HmModel(self.inputs).compute();
        for key, value in expectedResults.items():
            for k,v in value.items():
                self.assertAlmostEqual(results[key][k], v)

    
if __name__ == "__main__":
    unittest.main()
