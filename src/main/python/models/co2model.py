from models.fertilisermodel import NFertiliserType, OtherMineralFertiliserType

class Co2Model(object):
    """Inputs:
      #TODO: Should we directly receive a CO2 value from the fert module?
      n_fertiliser_quantities: map NFertiliserType -> kg N/ha
      part_of_urea_in_UAN: ratio
      other_mineral_fertiliser_quantities: map OtherMinFertiliserType -> kg Ca/ha
      magnesium_from_fertilizer: kg Mg / ha
      magnesium_as_dolomite: ratio

    Outputs:
      m_co2_CO2_from_fertilisers: kg/ha
    """
    _input_variables = ["n_fertiliser_quantities",
                        "part_of_urea_in_UAN",
                        "other_mineral_fertiliser_quantities",
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
        return {'m_co2_CO2_from_fertilisers': co2}
        
    def _compute_co2(self):
        co2_from_urea = self._compute_CO2_from_urea()
        co2_from_lime = self._compute_CO2_from_lime()
        return co2_from_urea + co2_from_lime;
    
    def _compute_CO2_from_urea(self):
        return self._UREA_N_TO_CO2_FACTOR * (self.n_fertiliser_quantities[NFertiliserType.urea] + self.part_of_urea_in_UAN * self.n_fertiliser_quantities[NFertiliserType.ureaAN]);
    
    def _compute_CO2_from_lime(self):
        return self._CA_TO_CO2_FACTOR * \
                  (self.other_mineral_fertiliser_quantities[OtherMineralFertiliserType.ca_limestone] + \
                   self.other_mineral_fertiliser_quantities[OtherMineralFertiliserType.ca_carbonation_linestone] + \
                   self.other_mineral_fertiliser_quantities[OtherMineralFertiliserType.ca_seaweed_limestone]) \
               + self._compute_CO2_from_magnesium();

    def _compute_CO2_from_magnesium(self):
        return self._MG_TO_CO2_FACTOR * self.magnesium_from_fertilizer * self.magnesium_as_dolomite;
