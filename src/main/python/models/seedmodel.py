from enum import Enum
from models.modelEnums import HeavyMetalType

class SeedType(Enum):
        field_bean=1
        flower=2
        pea=3
        vegetable=4
        barley=5
        grass=6
        clover=7
        potato=8
        maize=9
        rape=10
        rye=11
        soya_bean=12
        sunflower=13
        wheat=14
        sugar_fodder_beet=15
        tree_seedlings=16
        other=17

class SeedModel(object):
    """Inputs:
      seed_quantities: map SeedType -> quantity (kg or unit) / ha

    Outputs:
      computeHeavyMetal:
        hm_total_seed : map HeavyMetalType -> mg i/ha (i: hm type)
    """
    
    _input_variables = ["seed_quantities"
                       ]
    
    _SEED_DM = {
            SeedType.field_bean: 0.85,
            SeedType.flower: 0.73, #mean
            SeedType.pea: 0.85,
            SeedType.vegetable: 0.73, #mean
            SeedType.barley:  0.85,
            SeedType.grass: 0.73, #mean
            SeedType.clover: 0.73, #mean
            SeedType.potato:  0.18,
            SeedType.maize: 0.85,
            SeedType.rape: 0.90,
            SeedType.rye:  0.85,
            SeedType.soya_bean:  0.85,
            SeedType.sunflower: 0.85,
            SeedType.wheat:  0.85,
            SeedType.sugar_fodder_beet: 0.22,
            SeedType.tree_seedlings: 0.73, #mean
            SeedType.other: 0.73 #mean
                    }
                                                                
    #src: Table 7 Freiermuth 2006
    # mg/kg TS
    _HM_SEED_VALUES = { 
            SeedType.field_bean: [0.04, 6.0, 30.1, 0.87, 1.3, 0.69, 0],
            SeedType.flower:  [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.pea: [0.09, 10.0, 73.0, 0.16, 0.83, 0.32, 0.01],
            SeedType.vegetable:  [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.barley:  [0.08, 6.0, 44.5, 0.41, 0.39, 0.37, 0.06],
            SeedType.grass:  [0.13, 8.6, 40.0, 1.2, 1.68, 1.09, 0.15],
            SeedType.clover: [0.13, 8.6, 40.0, 1.2, 1.68, 1.09, 0.15], #grass
            SeedType.potato:  [0.04, 6.45, 15.0, 0.55, 0.33, 0.57, 0.09],
            SeedType.maize:  [0.03, 2.5, 21.5, 0.3, 1.16, 0.32, 0],
            SeedType.rape: [1.6, 3.3, 48.0, 5.25, 2.6, 0.5, 0.1],
            SeedType.rye:  [0.08, 6.0, 44.5, 0.41, 0.39, 0.37, 0.06],
            SeedType.soya_bean:  [0.06, 15.1, 47.7, 0.08, 5.32, 0.52, 0],
            SeedType.sunflower:  [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.wheat:  [0.15, 5.0, 45.0, 0.16, 0.3, 0.3, 0.01],
            SeedType.sugar_fodder_beet: [0.5, 10.3, 41.4, 1.8, 1.5, 1.9, 0.0],
            SeedType.tree_seedlings:  [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.other: [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04] #generic
        }
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in SeedModel._input_variables:
            setattr(self, key, inputs[key])
    
    def computeHeavyMetal(self):
        total_hm_values = dict.fromkeys(HeavyMetalType,0.0)
        self._add_hm_values_for_seed_type(total_hm_values, self.seed_quantities, self._HM_SEED_VALUES);
        return total_hm_values
    
    def _add_hm_values_for_seed_type(self,total_hm_values, seed_quantities,seed_hm_map):
        for seedKey,seedQuantity in seed_quantities.items():
            for hm_element_index,hm_element_value in enumerate(seed_hm_map[seedKey]):
                total_hm_values[HeavyMetalType(hm_element_index)] += hm_element_value * self._SEED_DM[seedKey] * seedQuantity

    