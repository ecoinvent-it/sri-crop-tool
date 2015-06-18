from models.modelEnums import HeavyMetalType

class SeedModel(object):
    """Inputs:
      seed_quantities: map Crop -> quantity (kg or unit) / ha

    Outputs:
      computeHeavyMetal:
        hm_total_seed : map HeavyMetalType -> mg i/ha (i: hm type)
    """
    
    _input_variables = ["seed_quantities"
                       ]
    
    _SEED_DM = {
                "almond": 0.73, #tree_seedlings
                "apple": 0.73, #tree_seedlings
                "apricot": 0.73, #tree_seedlings
                "asparagus": 0.73, #vegetable
                "banana": 0.73, #tree_seedlings
                "carrot": 0.73, #vegetable
                "cocoa": 0.73, #other
                "coconut": 0.73, #tree_seedlings
                "coffee": 0.73, #other
                "lemonlime": 0.73, #tree_seedlings
                "linseed": 0.85, #pea
                "maizegrain": 0.85,
                "mandarin": 0.73, #tree_seedlings
                "mint": 0.73, #vegetable
                "oat": 0.73, #other
                "olive": 0.73, #tree_seedlings
                "onion": 0.73, #vegetable
                "orange": 0.73, #tree_seedlings
                "palmtree": 0.73, #tree_seedlings
                "peach": 0.73, #tree_seedlings
                "peanut": 0.73, #other
                "pear": 0.73, #tree_seedlings
                "pineapple": 0.73, #tree_seedlings
                "potato": 0.18,
                "rapeseed": 0.90,
                "rice":0.85, #wheat
                "soybean": 0.85,
                "strawberry": 0.73, #other
                "sugarbeet": 0.22,
                "sugarcane": 0.73, #other
                "sunflower": 0.85,
                "sweetcorn": 0.85, #maize
                "tea": 0.73, #other
                "tomato": 0.73, #other
                "wheat": 0.85
                    }
                                                                
    #src: Table 7 Freiermuth 2006
    # mg/kg TS
    _HM_SEED_VALUES = { 
                "almond": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "apple": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "apricot": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "asparagus": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #vegetable
                "banana": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "carrot": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #vegetable
                "cocoa": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #Other
                "coconut": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "coffee": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #Other
                "lemonlime": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "linseed": [0.09, 10.0, 73.0, 0.16, 0.83, 0.32, 0.01], #pea
                "maizegrain": [0.03, 2.5, 21.5, 0.3, 1.16, 0.32, 0],
                "mandarin": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "mint": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #vegetable
                "oat": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #Other
                "olive": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "onion": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #vegetable
                "orange": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "palmtree": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "peach": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "peanut": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #Other
                "pear": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "pineapple": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #tree_seedlings
                "potato": [0.04, 6.45, 15.0, 0.55, 0.33, 0.57, 0.09],
                "rapeseed": [1.6, 3.3, 48.0, 5.25, 2.6, 0.5, 0.1],
                "rice": [0.15, 5.0, 45.0, 0.16, 0.3, 0.3, 0.01], #wheat
                "soybean": [0.06, 15.1, 47.7, 0.08, 5.32, 0.52, 0],
                "strawberry": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #Other
                "sugarbeet": [0.5, 10.3, 41.4, 1.8, 1.5, 1.9, 0.0],
                "sugarcane": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #Other
                "sunflower": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04],
                "sweetcorn": [0.03, 2.5, 21.5, 0.3, 1.16, 0.32, 0], #maize
                "tea": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #Other
                "tomato": [0.1, 6.6, 32.0, 0.54, 1.04, 0.55, 0.04], #Other
                "wheat": [0.15, 5.0, 45.0, 0.16, 0.3, 0.3, 0.01]
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

    