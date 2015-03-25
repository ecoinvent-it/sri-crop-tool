class Co2Model(object):
    """Inputs:
      nitrogen_from_urea: kg N / ha
      nitrogen_from_ureaAN: kg N / ha
      part_of_urea_in_UAN: ratio
      calcium_from_lime: kg Ca / ha
      calcium_from_carbonation_lime: kg Ca / ha
      calcium_from_seaweed_lime: kg Ca / ha
      magnesium_from_fertilizer: kg Mg / ha
      magnesium_as_dolomite: ratio

    Outputs:
      m_co2_CO2: kg/ha
    """
    _input_variables = ["nitrogen_from_urea",
                        "nitrogen_from_ureaAN",
                        "part_of_urea_in_UAN",
                        "calcium_from_lime",
                        "calcium_from_carbonation_lime",
                        "calcium_from_seaweed_lime",
                        "magnesium_from_fertilizer",
                        "magnesium_as_dolomite"
                       ]
    
    _CO2 = 12.0107 + 2*15.9994
    _UREA = 12.0107 + 4*1.00794 + 2*14.00674 + 15.9994 #CH4N2O
    _UREA_N_TO_CO2_FACTOR =  12.0107/_UREA * _UREA/(14.00674*2) * _CO2/12.0107 #1.57
    
    _LIMESTONE = 40.078 + 12.0107 + 3*15.9994 #CaCO3
    _CA_TO_LIMESTONE = _LIMESTONE / 40.078;
    _LIMESTONE_TO_CO2_FACTOR = _CO2 / _LIMESTONE
    _CA_TO_CO2_FACTOR = _CA_TO_LIMESTONE * _LIMESTONE_TO_CO2_FACTOR #(_CO2)/ 40.078
    
    _DOLOMITE = 40.078 + 24.3050 + 2*12.0107 + 2*3*15.9994 #CaMg(CO3)2
    _MG_TO_DOLOMITE = _DOLOMITE / 24.3050;
    _DOLOMITE_TO_CO2_FACTOR = 2*_CO2 / _DOLOMITE
    _MG_TO_CO2_FACTOR = _MG_TO_DOLOMITE * _DOLOMITE_TO_CO2_FACTOR  # (_CO2)*2/24.305
    
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in Co2Model._input_variables:
            setattr(self, key, inputs[key])
            
    def compute(self):
        co2 = self._compute_co2()
        return {'m_co2_CO2': co2}
        
    def _compute_co2(self):
        co2_from_urea = self._compute_CO2_from_urea()
        co2_from_lime = self._compute_CO2_from_lime()
        return co2_from_urea + co2_from_lime;
    
    def _compute_CO2_from_urea(self):
        return self._UREA_N_TO_CO2_FACTOR * (self.nitrogen_from_urea + self.part_of_urea_in_UAN * self.nitrogen_from_ureaAN);
    
    def _compute_CO2_from_lime(self):
        return self._CA_TO_CO2_FACTOR * (self.calcium_from_lime + self.calcium_from_carbonation_lime + self.calcium_from_seaweed_lime) + self._compute_CO2_from_magnesium();

    def _compute_CO2_from_magnesium(self):
        return self._MG_TO_CO2_FACTOR * self.magnesium_from_fertilizer * self.magnesium_as_dolomite;
