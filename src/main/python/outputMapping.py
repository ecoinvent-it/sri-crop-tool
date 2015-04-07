class OutputMapping(object):
    
    def __init__(self):
        self.output = {}
        
    def mapCo2Model(self, co2Output):
        for key, value in co2Output.items():
            self.output[key.replace("m_co2_", "")] = value
            
    def mapNo3Model(self, no3Output):
        for key, value in no3Output.items():
            self.output[key.replace("m_No3_", "")] = value
    
    def mapFertilizers(self, allInputs):
        self._mapEnumMap(allInputs["n_fertiliser_quantities"])
        self._mapEnumMap(allInputs["p_fertiliser_quantities"])
        self._mapEnumMap(allInputs["k_fertiliser_quantities"])
        self._mapEnumMap(allInputs["other_mineral_fertiliser_quantities"])
        #TODO: Is this the best place for that?
        self.output["fert_n_ammonia_liquid_as_nh3"] = allInputs["fertnmin_ammonia_liquid"] * 17.0/14.0 #FIXME: Exact value
            
    def _mapEnumMap(self, enumdict):
        for k,v in enumdict.items():
            self.output[k.value] = v
        
