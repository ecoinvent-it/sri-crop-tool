from models.co2model import Co2Model
from outputMapping import OutputMapping
from models.no3model import No3Model
from inputMappings import NonStrictInputMapping
from excelInputMappingRules import EXCEL_INPUT_MAPPING_RULES
from defaultGeneration import DefaultValuesWrapper, DEFAULTS_VALUES_GENERATORS
from collections import ChainMap
from models.irrigationmodel import IrrigationModel
from models.fertilisermodel import FertModel
from models.n2oxmodel import N2OxModel
from models.manuremodel import ManureModel

class ModelsSequence(object):
    
    def __init__(self, validatedInputs):
        self._intermediateValues = {}
        self.outputMapping = OutputMapping()
        self.allInputs = DefaultValuesWrapper(ChainMap(NonStrictInputMapping(validatedInputs, EXCEL_INPUT_MAPPING_RULES),
                                              self._intermediateValues, self.outputMapping.output), DEFAULTS_VALUES_GENERATORS)
        
    def executeSequence(self):
        self._computeFertiliser()
        self._computeManure()
        self.outputMapping.mapIrrigationModel(IrrigationModel(self.allInputs).compute())
        self.outputMapping.mapFertilizers(self.allInputs)
        self.outputMapping.mapCo2Model(Co2Model(self.allInputs).compute())
        #self.outputMapping.mapNo3Model(No3Model(self.allInputs).compute())
        #self.outputMapping.mapN2oxModel(N2OxModel(self.allInputs).compute())
        return self.outputMapping.output;
    
    def _computeFertiliser(self):
        fertM = FertModel(self.allInputs)
        self._intermediateValues["nitrogen_from_mineral_fert"] = fertM.computeN()
        self._intermediateValues["ammonia_due_to_mineral_fert"] = fertM.computeNH3()
    
    def _computeManure(self):
        manureM = ManureModel(self.allInputs)
        self._intermediateValues["nitrogen_from_all_manure"] = manureM.computeN()
        pres = manureM.computeP2O5()
        self._intermediateValues["TODO"] = pres[0]
        self._intermediateValues["TODO"] = pres[1]
        self.outputMapping.mapManureNH3(manureM.computeNH3())
