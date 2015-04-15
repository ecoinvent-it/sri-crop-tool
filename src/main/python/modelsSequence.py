from models.co2model import Co2Model
from outputMapping import OutputMapping
from models.nmodel import NModel
from inputMappings import NonStrictInputMapping
from excelInputMappingRules import EXCEL_INPUT_MAPPING_RULES
from defaultGeneration import DefaultValuesWrapper, DEFAULTS_VALUES_GENERATORS
from collections import ChainMap
from models.irrigationmodel import IrrigationModel
from models.fertilisermodel import FertModel
from models.manuremodel import ManureModel
from models.pmodel import PModel
from models.otherorganicfertilisermodel import OtherOrganicFertModel
from models.erosionmodel import ErosionModel
from models.hmmodel import HmModel
from models.seedmodel import SeedModel

class ModelsSequence(object):
    
    def __init__(self, validatedInputs):
        self._intermediateValues = {}
        self.outputMapping = OutputMapping()
        self.allInputs = DefaultValuesWrapper(ChainMap(NonStrictInputMapping(validatedInputs, EXCEL_INPUT_MAPPING_RULES),
                                              self._intermediateValues, self.outputMapping.output), DEFAULTS_VALUES_GENERATORS)
        
    def executeSequence(self):
        self.outputMapping.mapAsIsOutput(self.allInputs)
        self._computeFertiliser()
        self._computeManure()
        self._computeOtherOrganicFertiliser()
        self._computeSeed()
        self._intermediateValues["eroded_soil"] = ErosionModel(self.allInputs).compute()["m_Erosion_eroded_soil"]
        self.outputMapping.mapIrrigationModel(IrrigationModel(self.allInputs).compute())
        self.outputMapping.mapFertilizers(self.allInputs)
        self.outputMapping.mapOtherOrganicFertilizers(self.allInputs)
        self.outputMapping.mapCo2Model(Co2Model(self.allInputs).compute())
        self.outputMapping.mapNModel(NModel(self.allInputs).compute())
        self.outputMapping.mapPModel(PModel(self.allInputs).compute())
        self.outputMapping.mapHMModel(HmModel(self.allInputs).compute())
        #self.outputMapping.mapUsedIntermidiateValues(self._intermediateValues)
        return self.outputMapping.output;
    
    def _computeFertiliser(self):
        fertM = FertModel(self.allInputs)
        self._intermediateValues["ammonia_due_to_mineral_fert"] = fertM.computeNH3()
        self._intermediateValues["hm_from_mineral_fert"] = fertM.computeHeavyMetal()
        
    def _computeManure(self):
        manureM = ManureModel(self.allInputs)
        self._intermediateValues["nitrogen_from_all_manure"] = manureM.computeN()
        pres = manureM.computeP2O5()
        self._intermediateValues["p2o5_in_liquid_manure"] = pres[0]
        self._intermediateValues["p2o5_in_solid_manure"] = pres[1]
        self._intermediateValues["hm_from_manure"] = manureM.computeHeavyMetal();
        self._intermediateValues["ammonia_due_to_manure"] = manureM.computeNH3()

    def _computeOtherOrganicFertiliser(self):
        otherfertM = OtherOrganicFertModel(self.allInputs)
        self._intermediateValues["nitrogen_from_other_orga_fert"] = otherfertM.computeN()
        self._intermediateValues["p2o5_in_liquid_sludge"] = otherfertM.computeP2O5()
        self._intermediateValues["hm_from_other_organic_fert"] = otherfertM.computeHeavyMetal()
        self._intermediateValues["ammonia_due_to_other_orga_fert"] = otherfertM.computeNH3()
        
    def _computeSeed(self):
        seedM = SeedModel(self.allInputs)
        self._intermediateValues["hm_from_seed"] = seedM.computeHeavyMetal()
