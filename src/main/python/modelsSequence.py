from models import defaultModelInputs as dmi
from models.co2model import Co2Model
from outputMapping import OutputMapping

class ModelsSequence(object):
    
    def __init__(self, validatedInputs):
        self.validatedInputs = validatedInputs
        
    def executeSequence(self):
        outputMapping = OutputMapping(self.validatedInputs['yield_main_product_per_crop_cycle'])
        tmp= self.mapToCo2Model()
        print(tmp)
        outputMapping.mapCo2Model(Co2Model(tmp).compute())
        return outputMapping.output;
        
    _CO2_MAPPING = {"nitrogen_from_urea": "fertnmin_urea",
                    "nitrogen_from_ureaAN": "fertnmin_urea_an",
                    "part_of_urea_in_UAN": "__use_default",
                    "calcium_from_lime": "fertotherca_limestone",
                    "calcium_from_carbonation_lime": "fertotherca_carbonation_limestone",
                    "calcium_from_seaweed_lime": "fertotherca_seaweed_limestone",
                    "magnesium_from_fertilizer": "fert_other_total_mg",
                    "magnesium_as_dolomite": "__use_default_TODO"
                   }
        
    def mapToCo2Model(self):
        return {key: self.validatedInputs.get(value, dmi.Co2Model_defaults[key]) for key, value in ModelsSequence._CO2_MAPPING.items()}

