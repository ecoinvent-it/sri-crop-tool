from models.co2model import Co2Model
from outputMapping import OutputMapping
from models.no3model import No3Model
from inputMappings import NonStrictInputMapping
from excelInputMappingRules import EXCEL_INPUT_MAPPING_RULES
from defaultGeneration import DefaultValuesWrapper, DEFAULTS_VALUES_GENERATORS
from collections import ChainMap

class ModelsSequence(object):
    
    def __init__(self, validatedInputs):
        self.outputMapping = OutputMapping()
        self.allInputs = DefaultValuesWrapper(ChainMap(NonStrictInputMapping(validatedInputs, EXCEL_INPUT_MAPPING_RULES),
                                              self.outputMapping.output), DEFAULTS_VALUES_GENERATORS)
        
    def executeSequence(self):
        self.outputMapping.mapFertilizers(self.allInputs)
        self.outputMapping.mapCo2Model(Co2Model(self.allInputs).compute())
        self.outputMapping.mapNo3Model(No3Model(self.allInputs).compute())
        return self.outputMapping.output;
