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
                            HeavyMetalType.cd: 1.464289234395887e-05,
                            HeavyMetalType.cu: 0.0010600970642411893,
                            HeavyMetalType.zn: 0.005239621629345784,
                            HeavyMetalType.pb: 0.00011513169533934504,
                            HeavyMetalType.ni: 2.224287219512195e-05,
                            HeavyMetalType.cr: 0.005678474244193842,
                            HeavyMetalType.hg: 4.726990690364827e-07},
                           'm_hm_heavymetal_to_surface_water':{
                            HeavyMetalType.cd: 9.550128534704371e-06,
                            HeavyMetalType.cu: 0.000679987032834715,
                            HeavyMetalType.zn: 0.0034664835770941427,
                            HeavyMetalType.pb: 6.674893213099193e-05,
                            HeavyMetalType.ni: 0.0,
                            HeavyMetalType.cr: 0.0037683145382505336,
                            HeavyMetalType.hg: 2.5959463673214843e-07}
                        }
        results = HmModel(self.inputs).compute();
        for key, value in expectedResults.items():
            for k,v in value.items():
                self.assertAlmostEqual(results[key][k], v)

    
if __name__ == "__main__":
    unittest.main()
