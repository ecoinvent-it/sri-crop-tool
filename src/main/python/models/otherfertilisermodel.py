from enum import Enum
from models.modelEnums import HeavyMetalType

class OtherFertiliserType(Enum):
    compost=1
    meat_and_bone_meal=2
    castor_oil_shell_coarse=3
    vinasse=4
    dried_poultry_manure=5
    stone_meal=6
    feather_meal=7
    horn_meal=8
    horn_shavings_fine=9
    sewagesludge_liquid=10
    sewagesludge_dehydrated=11
    sewagesludge_dried=12


class OtherFertModel(object):
    """Inputs:
      other_fertiliser_quantities: map OtherFertiliserType -> t fert/ha

    Outputs:
      computeP2O5:
        P2O5_total_otherfert: kg P2O5/ha
      computeN:
        N_total_otherfert: kg N/ha
      computeHeavyMetal:
        hm_total_otherfert : map HeavyMetalType -> mg i/ha (i:hm type)
    """
    
    _input_variables = ["other_fertiliser_quantities"
                       ]
    
    _FERT_DM = {    OtherFertiliserType.compost: 0.5,
                    OtherFertiliserType.meat_and_bone_meal: 0.95,
                    OtherFertiliserType.castor_oil_shell_coarse: 0.0,#TODO: complete when we will have more info
                    OtherFertiliserType.vinasse: 0.0,#TODO: complete when we will have more info
                    OtherFertiliserType.dried_poultry_manure: 1.0,
                    OtherFertiliserType.stone_meal: 0.0,#TODO: complete when we will have more info
                    OtherFertiliserType.feather_meal: 0.95,
                    OtherFertiliserType.horn_meal: 0.95,
                    OtherFertiliserType.horn_shavings_fine: 0.95,
                    OtherFertiliserType.sewagesludge_liquid: 0.057,
                    OtherFertiliserType.sewagesludge_dehydrated: 0.26,
                    OtherFertiliserType.sewagesludge_dried: 0.93
                    }
                                                                
    #src: Freiermuth 2006, table 13 if not specified
    # g/t TS
    _HM_FERT_VALUES = { OtherFertiliserType.compost: [0.36, 57.7, 183.5, 49.0, 16.3, 22.3, 0.12],
                        OtherFertiliserType.meat_and_bone_meal: [0.6, 208.0, 125.0, 3.8, 34.0, 5.9, 0.6],#Tiermehl
                        OtherFertiliserType.castor_oil_shell_coarse: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], #TODO: Complete when we will have more info
                        OtherFertiliserType.vinasse: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], #TODO: Complete when we will have more info
                        OtherFertiliserType.dried_poultry_manure: [0.25, 39.6, 468.4, 2.24, 7.9, 5.5, 0.2],#src WLFDB, #same as manure "Litter from deep pits from laying hens" (but different DM)
                        OtherFertiliserType.stone_meal: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], #TODO: Complete when we will have more info
                        OtherFertiliserType.feather_meal: [0.2, 7.3, 135.3, 8.3, 2.8, 65.0, 0.1],#Hornmehl
                        OtherFertiliserType.horn_meal: [0.2, 7.3, 135.3, 8.3, 2.8, 65.0, 0.1],#Hornmehl
                        OtherFertiliserType.horn_shavings_fine: [0.2, 7.3, 135.3, 8.3, 2.8, 65.0, 0.1],#Hornmehl
                        OtherFertiliserType.sewagesludge_liquid: [1.7, 341.0, 929.0, 94.0, 32.0, 74.0, 1.7], #Klärschlamm
                        OtherFertiliserType.sewagesludge_dehydrated: [1.7, 341.0, 929.0, 94.0, 32.0, 74.0, 1.7], #Klärschlamm
                        OtherFertiliserType.sewagesludge_dried: [1.7, 341.0, 929.0, 94.0, 32.0, 74.0, 1.7], #Klärschlamm
                    }
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in OtherFertModel._input_variables:
            setattr(self, key, inputs[key])
            
    #FIXME: To Complete
    def computeP2O5(self):
        return 0.0
    
    #FIXME: To Complete
    def computeN(self):
        return 0.0
    
    def computeHeavyMetal(self):
        total_hm_values = dict.fromkeys(HeavyMetalType,0.0)
        self._add_hm_values_for_fert_type(total_hm_values, self.other_fertiliser_quantities, self._HM_FERT_VALUES);
        return {'hm_total_otherfert':total_hm_values}
    
    def _add_hm_values_for_fert_type(self,total_hm_values, fert_quantities,fert_hm_map):
        for fertKey,fertQuantity in fert_quantities.items():
            for hm_element_index,hm_element_value in enumerate(fert_hm_map[fertKey],1):
                total_hm_values[HeavyMetalType(hm_element_index)] += hm_element_value * self._FERT_DM[fertKey] * fertQuantity * 1000.0

    