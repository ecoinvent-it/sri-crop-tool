class OutputMapping(object):
    
    def __init__(self):
        self.output = {}
        
    def mapIrrigationModel(self, irrOutput):
        for key, value in irrOutput.items():
            self.output[key.replace("m_Irr_", "")] = value
            
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
    
    def mapFertilizers(self, allInputs):
        self._mapEnumMap(allInputs["n_fertiliser_quantities"])
        self._mapEnumMap(allInputs["p_fertiliser_quantities"])
        self._mapEnumMap(allInputs["k_fertiliser_quantities"])
        self._mapEnumMap(allInputs["other_mineral_fertiliser_quantities"])
        #TODO: Is this the best place for that?
        self.output["fert_n_ammonia_liquid_as_nh3"] = allInputs["fertnmin_ammonia_liquid"] * 17.0/14.0 #FIXME: Exact value
       
    def mapHMModel(self,hmOutput):
        for key, hmMap in hmOutput.items():
            for k, v in hmMap:
                self.output[key.replace("m_hm_", "") + "_" + k] = v
            
    def _mapEnumMap(self, enumdict):
        for k,v in enumdict.items():
            self.output[k.value] = v
        
