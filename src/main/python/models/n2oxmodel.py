class N2OxModel(object):
    """Inputs:
      nitrogen_from_all_manure: kg N / ha
      nitrogen_from_other_organic_fert: kg N / ha
      nitrogen_from_mineral_fert: kg N / ha
      nitrogen_from_crop_residues: kg N / ha
      nitrate_to_groundwater: kg NO3 / ha
      nitrate_to_surfacewater: kg NO3 / ha
      ammonia_due_to_mineral_fert: kg NH3/ha
      ammonia_due_to_manure: kg NH3/ha

    Outputs:
      m_N2ox_N2o_air: kg N2O/ha
      m_N2ox_Nox_as_n2o_air: kg NOx as N2O/ha
    """
    
    _input_variables = ["nitrogen_from_all_manure",
                        "nitrogen_from_other_organic_fert",
                        "nitrogen_from_mineral_fert",
                        "nitrogen_from_crop_residues",
                        "nitrate_to_groundwater",
                        "nitrate_to_surfacewater",
                        "ammonia_due_to_mineral_fert",
                        "ammonia_due_to_manure"
                       ]
    
    _N = 14.00674
    _O = 15.9994
    _H = 1.00794
    
    _NO = _N + _O #30
    _N2O = _NO + _N  #44
    _NO2 = _NO + _O #46
    _NO3 = _NO + 2*_O #62
    _NH3 = _N + 3*_H #17
    
    _NO_TO_N_FACTOR = _N / _NO # 14/30
    _N_TO_NO2_FACTOR = _NO2 / _N # 46/14
    _NO2_TO_N_FACTOR = 1.0 / _N_TO_NO2_FACTOR # 14/46
    _NO3_TO_N_FACTOR = _N / _NO3 #14/62
    _NH3_TO_N_FACTOR = _N / _NH3 # 14/17
    _N_TO_N20 = _N2O / (2*_N) #44/28
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in N2OxModel._input_variables:
            setattr(self, key, inputs[key])
    
    def compute(self):
        total_fert_nitrogen = self._compute_total_fert_nitrogen()
        nox = self._compute_nox_as_no2(total_fert_nitrogen)
        n2o = self._compute_n2o(total_fert_nitrogen,nox)
        return {"m_N2ox_N2o_air": n2o,
                "m_N2ox_Nox_as_n2o_air": nox}
        
    def _compute_total_fert_nitrogen(self):
        return self.nitrogen_from_all_manure + self.nitrogen_from_other_organic_fert + self.nitrogen_from_mineral_fert;
        
    def _compute_nox_as_no2(self,total_fert_nitrogen): #0.026 is a ratio in kg NO/kg N
        return total_fert_nitrogen * (0.026 * self._NO_TO_N_FACTOR) * self._N_TO_NO2_FACTOR
        
    def _compute_n2o(self, total_fert_nitrogen, nox):
        return  self._N_TO_N20 * (0.01 * (total_fert_nitrogen
                                            + self.nitrogen_from_crop_residues
                                            + self._NH3_TO_N_FACTOR * self._compute_total_due_ammonia() 
                                            + self._NO2_TO_N_FACTOR * nox)
                                  + self._compute_n2o_as_n_due_to_nitrate()
                                  )
    
    def _compute_total_due_ammonia(self):
        return self.ammonia_due_to_mineral_fert + self.ammonia_due_to_manure;
    
    def _compute_n2o_as_n_due_to_nitrate(self):
        return  0.0075 * self._NO3_TO_N_FACTOR * (self.nitrate_to_groundwater + self.nitrate_to_surfacewater)
