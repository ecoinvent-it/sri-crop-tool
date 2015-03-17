class N2OxModel(object):
    """Inputs:
      n_total_manure: kg N / ha
      n_total_mineral_fert: kg N / ha
      n_crop_residues: kg N / ha
      no3_groundwater: kg NO3/(ha*year)
      no3_surfacewater: kg NO3/(ha*year)
      nh3_total_mineral_fert: kg NH3/ha
      nh3_total_liquid_manure: kg NH3/ha
      nh3_total_solid_manure: kg NH3/ha

      
    Outputs:
      m_N2ox_N2o_air: kg N2o/ha
      m_N2ox_Nox_air: kg Nox/ha
    """
    
    _input_variables = ["n_total_manure",
                        "n_total_mineral_fert",
                        "n_crop_residues",
                        "no3_groundwater",
                        "no3_surfacewater",
                        "nh3_total_mineral_fert",
                        "nh3_total_liquid_manure",
                        "nh3_total_solid_manure"
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
        n_total_fert = self._compute_n_total_fert()
        nox = self._compute_nox_as_no2(n_total_fert)
        n2o = self._compute_n2o(n_total_fert,nox)
        return {"m_N2ox_N2o_air": n2o,
                "m_N2ox_Nox_air": nox}
        
    def _compute_n_total_fert(self):
        return self.n_total_manure + self.n_total_mineral_fert;
        
    def _compute_nox_as_no2(self,n_total_fert):
        return n_total_fert * 0.026 * self._NO_TO_N_FACTOR * self._N_TO_NO2_FACTOR
        
    def _compute_n2o(self, n_total_fert, nox):
        return  self._N_TO_N20 * (0.01 * (n_total_fert
                                            + self.n_crop_residues
                                            + self._NH3_TO_N_FACTOR * self._compute_nh3_total() 
                                            + self._NO2_TO_N_FACTOR * nox)
                                  + self._compute_n_from_no3()
                                  )
    
    def _compute_nh3_total(self):
        return self.nh3_total_mineral_fert + self.nh3_total_liquid_manure + self.nh3_total_solid_manure;
    
    def _compute_n_from_no3(self):
        return  0.0075 * self._NO3_TO_N_FACTOR * (self.no3_groundwater + self.no3_surfacewater)
