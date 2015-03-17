class No3Model(object):
    """Inputs:
      bulk_density_of_soil: kg/m3
      c_per_n_ratio: ratio
      clay_content: ratio
      considered_soil_volume: m3/ha
      drained_part: ratio
      fertilisers_gas_losses: ratio
      irrigation: mm/year
      nitrogen_from_fertiliser: kg N/(ha*year)
      nitrogen_uptake_by_crop: kg N/(ha*year)
      norg_per_ntotal_ratio: ratio
      organic_carbon_content: kg C/kg soil,
      precipitation: mm/year
      rooting_depth: m
      
    Outputs:
      m_No3_nitrate_to_groundwater: kg NO3/(ha*year)
      m_No3_nitrate_to_surfacewater: kg NO3/(ha*year)
    """
    
    _input_variables = ["bulk_density_of_soil",
                        "c_per_n_ratio",
                        "clay_content",
                        "considered_soil_volume",
                        "drained_part",
                        "fertilisers_gas_losses",
                        "irrigation",
                        "nitrogen_from_fertiliser",
                        "nitrogen_uptake_by_crop",
                        "norg_per_ntotal_ratio",
                        "organic_carbon_content",
                        "precipitation",
                        "rooting_depth"
                       ]
    
    _N = 14.00674
    _O = 15.9994
    _NO3 = _N + 3*_O
    _N_TO_NO3_FACTOR = _NO3/_N #62/14
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in No3Model._input_variables:
            setattr(self, key, inputs[key])
    
    def compute(self):
        corg = self._compute_carbon_in_soil_orga_matter()
        s = self._compute_considered_nitrogen_from_fertiliser()
        norg = self._compute_nitrogen_in_soil_orga_matter(corg)
        nitrogen = self._compute_nitrogen_leaching(s, norg)
        nitrate = self._convert_nitrogen_to_nitrate(nitrogen)
        no3gw, no3sw = self._split_nitrate_between_ground_and_surface_waters(nitrate)
        return {"m_No3_nitrate_to_groundwater": no3gw,
                "m_No3_nitrate_to_surfacewater": no3sw}
        
    def _compute_carbon_in_soil_orga_matter(self): # kg C/kg soil * kg soil/m3 * m3/ha -> kg C/ha
        return self.organic_carbon_content * self.bulk_density_of_soil * self.considered_soil_volume
    
    def _compute_considered_nitrogen_from_fertiliser(self):
        return self.nitrogen_from_fertiliser * (1-self.fertilisers_gas_losses)
                
    def _compute_nitrogen_in_soil_orga_matter(self, carbon_in_soil):
        return carbon_in_soil / self.c_per_n_ratio * self.norg_per_ntotal_ratio
    
    def _compute_nitrogen_leaching(self, s, nitrogen_in_soil):
        res = 21.37 + (self.precipitation + self.irrigation) \
            / (self.clay_content * 100 * self.rooting_depth) \
            * (0.0037 * s                                    \
                + 0.0000601 * nitrogen_in_soil               \
                - 0.00362 * self.nitrogen_uptake_by_crop)
            
        return max(0, res);
        
    def _convert_nitrogen_to_nitrate(self, nitrogen):
        return nitrogen * self._N_TO_NO3_FACTOR
    
    def _split_nitrate_between_ground_and_surface_waters(self, nitrate):
        surface = nitrate * self.drained_part
        ground = nitrate - surface
        return (ground, surface)
        