from models.atomicmass import MA_NH3, MA_N
class OutputMapping(object):
    
    def __init__(self):
        self.output = {}
        
    def mapAsIsOutput(self, allInputs):
        self.output["country"] = allInputs["country"]
        
    def mapIrrigationModel(self, irrOutput):
        for key, value in irrOutput.items():
            self.output[key.replace("m_Irr_", "")] = value * 1000.0 #m3/ha -> kg
            
    def mapCo2Model(self, co2Output):
        for key, value in co2Output.items():
            self.output[key.replace("m_co2_", "")] = value
            
    def mapNModel(self, no3Output):
        for key, value in no3Output.items():
            self.output[key.replace("m_N_", "")] = value
            
    def mapErosionModel(self, erosionOutput):
        for key, value in erosionOutput.items():
            self.output[key.replace("m_Erosion_", "")] = value
            
    def mapPModel(self, pOutput):
        for key, value in pOutput.items():
            self.output[key.replace("m_P_", "")] = value
        #TODO: Is this the best place for that?
        self.output["PO4_surfacewater"] = self.output["PO4_surfacewater_drained"] + self.output["PO4_surfacewater_ro"]
    
    def mapFertilizers(self, allInputs):
        self._mapEnumMap(allInputs["n_fertiliser_quantities"])
        self._mapEnumMap(allInputs["p_fertiliser_quantities"])
        self._mapEnumMap(allInputs["k_fertiliser_quantities"])
        self._mapEnumMap(allInputs["other_mineral_fertiliser_quantities"])
        #TODO: Is this the best place for that?
        self.output["fert_n_ammonia_liquid_as_nh3"] = allInputs["fertnmin_ammonia_liquid"] * MA_NH3/MA_N
       
    def mapHMModel(self,hmOutput):
        for key, hmMap in hmOutput.items():
            prefix = key.replace("m_hm_", "") + "_"
            for k, v in hmMap.items():
                self.output[prefix + k.name] = v
            
    def _mapEnumMap(self, enumdict):
        for k,v in enumdict.items():
            self.output[k.value] = v
        
