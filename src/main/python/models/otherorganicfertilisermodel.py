from enum import Enum
from models.modelEnums import HeavyMetalType

class OtherOrganicFertiliserType(Enum):
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


class OtherOrganicFertModel(object):
    """Inputs:
      other_organic_fertiliser_quantities: map OtherOrganicFertiliserType -> t fert/ha

    Outputs:
      computeP2O5:
        P2O5_total_otherfert: kg P2O5/ha
      computeN:
        N_total_otherfert: kg N/ha
      computeNH3:
        nh3_total_otherfert: kg NH3/ha
      computeHeavyMetal:
        hm_total_otherfert : map HeavyMetalType -> mg i/ha (i:hm type)
    """
    
    _input_variables = ["other_organic_fertiliser_quantities"
                       ]
    
    _FERT_DM = {    OtherOrganicFertiliserType.compost: 0.5,
                    OtherOrganicFertiliserType.meat_and_bone_meal: 0.95,
                    OtherOrganicFertiliserType.castor_oil_shell_coarse: 0.0,#TODO: complete when we will have more info
                    OtherOrganicFertiliserType.vinasse: 0.0,#TODO: complete when we will have more info
                    OtherOrganicFertiliserType.dried_poultry_manure: 1.0,
                    OtherOrganicFertiliserType.stone_meal: 0.0,#TODO: complete when we will have more info
                    OtherOrganicFertiliserType.feather_meal: 0.95,
                    OtherOrganicFertiliserType.horn_meal: 0.95,
                    OtherOrganicFertiliserType.horn_shavings_fine: 0.95,
                    OtherOrganicFertiliserType.sewagesludge_liquid: 0.057,
                    OtherOrganicFertiliserType.sewagesludge_dehydrated: 0.26,
                    OtherOrganicFertiliserType.sewagesludge_dried: 0.93
                    }
    
    #src: Freiermuth 2006, table 13 * TS if not specified
    #kg/t FM
    _N_CONCENTRATION_ORG_FERT = {OtherOrganicFertiliserType.compost: 7.0,#Walther Ryser 2001, Tab. 48
                                OtherOrganicFertiliserType.meat_and_bone_meal: 0.0 * 0.95,#Tiermehl
                                OtherOrganicFertiliserType.castor_oil_shell_coarse: 0.0 * 0.0,#FIXME: complete when we will have more info
                                OtherOrganicFertiliserType.vinasse: 0.0 * 0.0,#FIXME: complete when we will have more info
                                OtherOrganicFertiliserType.dried_poultry_manure: 0.0 * 1.0,#FIXME: complete when we will have more info
                                OtherOrganicFertiliserType.stone_meal: 0.0 * 0.0,#FIXME: complete when we will have more info
                                OtherOrganicFertiliserType.feather_meal: 137.0 * 0.95,#Hornmehl
                                OtherOrganicFertiliserType.horn_meal: 137.0 * 0.95,#Hornmehl
                                OtherOrganicFertiliserType.horn_shavings_fine: 137.0 * 0.95,#Hornmehl
                                OtherOrganicFertiliserType.sewagesludge_liquid: 2.5, #Walther Ryser 2001, Tab. 48
                                OtherOrganicFertiliserType.sewagesludge_dehydrated: 9.7, #Walther Ryser 2001, Tab. 48
                                OtherOrganicFertiliserType.sewagesludge_dried: 8.4 #Walther Ryser 2001, Tab. 48
                                }
    
    #src: Walther Ryser 2001, Tab. 48
    #kg/t FM
    _P205_CONCENTRATION_IN_LIQUID_SEWAGE_SLUDGE = 3.5
                                                                
    #src: Freiermuth 2006, table 13 if not specified
    # g/t TS
    _HM_FERT_VALUES = { OtherOrganicFertiliserType.compost: [0.36, 57.7, 183.5, 49.0, 16.3, 22.3, 0.12],
                        OtherOrganicFertiliserType.meat_and_bone_meal: [0.6, 208.0, 125.0, 3.8, 34.0, 5.9, 0.6],#Tiermehl
                        OtherOrganicFertiliserType.castor_oil_shell_coarse: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], #TODO: Complete when we will have more info
                        OtherOrganicFertiliserType.vinasse: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], #TODO: Complete when we will have more info
                        OtherOrganicFertiliserType.dried_poultry_manure: [0.25, 39.6, 468.4, 2.24, 7.9, 5.5, 0.2],#src WLFDB, #same as manure "Litter from deep pits from laying hens" (but different DM)
                        OtherOrganicFertiliserType.stone_meal: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], #TODO: Complete when we will have more info
                        OtherOrganicFertiliserType.feather_meal: [0.2, 7.3, 135.3, 8.3, 2.8, 65.0, 0.1],#Hornmehl
                        OtherOrganicFertiliserType.horn_meal: [0.2, 7.3, 135.3, 8.3, 2.8, 65.0, 0.1],#Hornmehl
                        OtherOrganicFertiliserType.horn_shavings_fine: [0.2, 7.3, 135.3, 8.3, 2.8, 65.0, 0.1],#Hornmehl
                        OtherOrganicFertiliserType.sewagesludge_liquid: [1.7, 341.0, 929.0, 94.0, 32.0, 74.0, 1.7], #Klärschlamm
                        OtherOrganicFertiliserType.sewagesludge_dehydrated: [1.7, 341.0, 929.0, 94.0, 32.0, 74.0, 1.7], #Klärschlamm
                        OtherOrganicFertiliserType.sewagesludge_dried: [1.7, 341.0, 929.0, 94.0, 32.0, 74.0, 1.7], #Klärschlamm
                    }
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in OtherOrganicFertModel._input_variables:
            setattr(self, key, inputs[key])
            
    #FIXME: To validate
    def computeP2O5(self):
        return self.other_organic_fertiliser_quantities[OtherOrganicFertiliserType.sewagesludge_liquid] \
                * self._P205_CONCENTRATION_IN_LIQUID_SEWAGE_SLUDGE

    def computeN(self):
        return sum(v * self._N_CONCENTRATION_ORG_FERT[k] for k,v in self.other_organic_fertiliser_quantities.items())
    
    #FIXME: To Complete
    def computeNH3(self):
        return 0.0
    
    def computeHeavyMetal(self):
        total_hm_values = dict.fromkeys(HeavyMetalType,0.0)
        self._add_hm_values_for_fert_type(total_hm_values, self.other_organic_fertiliser_quantities, self._HM_FERT_VALUES);
        return total_hm_values
    
    def _add_hm_values_for_fert_type(self,total_hm_values, fert_quantities,fert_hm_map):
        for fertKey,fertQuantity in fert_quantities.items():
            for hm_element_index,hm_element_value in enumerate(fert_hm_map[fertKey]):
                total_hm_values[HeavyMetalType(hm_element_index)] += hm_element_value * self._FERT_DM[fertKey] * fertQuantity * 1000.0

    