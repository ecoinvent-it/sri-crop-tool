from enum import Enum
from models.modelEnums import HeavyMetalType

class SeedType(Enum):
        field_bean_seed_ip=1
        field_bean_seed_org=2
        flower_ip=3
        flower_org=4
        pea_ip=5
        pea_org=6
        vegetable_ip=7
        vegetable_org=8
        barley_ip=9
        barley_org=10
        grass=11
        clover=12
        potato_ip=13
        potato_org=14
        maize_ip=15
        maize_org=16
        rape=17
        rye_ip=18
        rye_org=19
        soya_bean_ip=20
        soya_bean_org=21
        sunflower_ip=22
        sunflower_org=23
        wheat_ip=24
        wheat_org=25
        sugar_fodder_beet=26
        tree_seedlings_ip=27
        tree_seedlings_org=28
        salix=29
        miscanthus=30
        other=31

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
            SeedType.field_bean_seed_ip: 0.85,
            SeedType.field_bean_seed_org: 0.85,
            SeedType.flower_ip: 0.73, #mean
            SeedType.flower_org: 0.73, #mean
            SeedType.pea_ip: 0.85,
            SeedType.pea_org: 0.85,
            SeedType.vegetable_ip: 0.73, #mean
            SeedType.vegetable_org:72.91, #mean
            SeedType.barley_ip:  0.85,
            SeedType.barley_org: 0.85,
            SeedType.grass: 0.73, #mean
            SeedType.clover: 0.73, #mean
            SeedType.potato_ip:  0.18,
            SeedType.potato_org: 0.18,
            SeedType.maize_ip: 0.85,
            SeedType.maize_org:0.85,
            SeedType.rape: 0.90,
            SeedType.rye_ip:  0.85,
            SeedType.rye_org: 0.85,
            SeedType.soya_bean_ip:  0.85,
            SeedType.soya_bean_org: 0.85,
            SeedType.sunflower_ip: 0.85,
            SeedType.sunflower_org: 0.85,
            SeedType.wheat_ip:  0.85,
            SeedType.wheat_org: 0.85,
            SeedType.sugar_fodder_beet: 0.22,
            SeedType.tree_seedlings_ip: 0.73, #mean
            SeedType.tree_seedlings_org: 0.73, #mean
            SeedType.salix: 1.0,
            SeedType.miscanthus: 1.0,
            SeedType.other: 0.73 #mean
                    }
                                                                
    #src: Table 7 Freiermuth 2006
    # mg/kg TS
    _HM_SEED_VALUES = { 
            SeedType.field_bean_seed_ip: [0.04, 6.0, 30.1, 0.87, 1.3, 0.69, 0],
            SeedType.field_bean_seed_org: [0.04, 6.0, 30.1, 0.87, 1.3, 0.69, 0],
            SeedType.flower_ip:  [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.flower_org: [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.pea_ip: [0.09, 10.0, 73.0, 0.16, 0.83, 0.32, 0.01],
            SeedType.pea_org:[0.09, 10.0, 73.0, 0.16, 0.83, 0.32, 0.01],
            SeedType.vegetable_ip:  [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.vegetable_org: [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.barley_ip:  [0.08, 6.0, 44.5, 0.41, 0.39, 0.37, 0.06],
            SeedType.barley_org: [0.08, 6.0, 44.5, 0.41, 0.39, 0.37, 0.06],
            SeedType.grass:  [0.13, 8.6, 40.0, 1.2, 1.68, 1.09, 0.15],
            SeedType.clover: [0.13, 8.6, 40.0, 1.2, 1.68, 1.09, 0.15], #grass
            SeedType.potato_ip:  [0.04, 6.45, 15.0, 0.55, 0.33, 0.57, 0.09],
            SeedType.potato_org: [0.04, 6.45, 15.0, 0.55, 0.33, 0.57, 0.09],
            SeedType.maize_ip:  [0.03, 2.5, 21.5, 0.3, 1.16, 0.32, 0],
            SeedType.maize_org: [0.03, 2.5, 21.5, 0.3, 1.16, 0.32, 0],
            SeedType.rape: [1.6, 3.3, 48.0, 5.25, 2.6, 0.5, 0.1],
            SeedType.rye_ip:  [0.08, 6.0, 44.5, 0.41, 0.39, 0.37, 0.06],
            SeedType.rye_org: [0.08, 6.0, 44.5, 0.41, 0.39, 0.37, 0.06],
            SeedType.soya_bean_ip:  [0.06, 15.1, 47.7, 0.08, 5.32, 0.52, 0],
            SeedType.soya_bean_org: [0.06, 15.1, 47.7, 0.08, 5.32, 0.52, 0],
            SeedType.sunflower_ip:  [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.sunflower_org: [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic,
            SeedType.wheat_ip:  [0.15, 5.0, 45.0, 0.16, 0.3, 0.3, 0.01],
            SeedType.wheat_org: [0.15, 5.0, 45.0, 0.16, 0.3, 0.3, 0.01],
            SeedType.sugar_fodder_beet: [0.5, 10.3, 41.4, 1.8, 1.5, 1.9, 0.0],
            SeedType.tree_seedlings_ip:  [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.tree_seedlings_org: [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #generic
            SeedType.salix: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], #FIXME: Use value when we have it, or maybe generic?
            SeedType.miscanthus: [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], #FIXME: Use value when we have it, or maybe generic?
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

    