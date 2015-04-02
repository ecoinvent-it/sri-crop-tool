from models import defaultModelInputs as dmi
from models.co2model import Co2Model
from outputMapping import OutputMapping
from models.no3model import No3Model
from inputMappings import NonStrictInputMapping
from excelInputMappingRules import EXCEL_INPUT_MAPPING_RULES
from defaultGeneration import DefaultValuesWrapper, DEFAULTS_VALUES_GENERATORS

class ModelsSequence(object):
    
    def __init__(self, validatedInputs):
        self.allInputs = DefaultValuesWrapper(NonStrictInputMapping(validatedInputs, EXCEL_INPUT_MAPPING_RULES),
                                            DEFAULTS_VALUES_GENERATORS)
        
    def executeSequence(self):
        outputMapping = OutputMapping()
        outputMapping.mapCo2Model(Co2Model(self.allInputs).compute())
        outputMapping.mapNo3Model(No3Model(self.allInputs).compute())
        return outputMapping.output;
