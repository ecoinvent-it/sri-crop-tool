from models.fertilisermodel import NFertiliserType
class PackModel(object):
    """Inputs:
        nitrogen_from_mineral_fert: kg N/ha
        p2O5_from_mineral_fert: kg P2O5/ha
        k2O_from_mineral_fert: kg K2O/ha
        magnesium_from_fertilizer: kg Mg/ha
        ca_from_mineral_fert: kg Ca/ha
        n_fertiliser_quantities: Map NFertiliserType -> kg N/ha
        pest_total: g/ha
    
    Outputs:
      m_pack_liquid_fertilisers_and_pesticides: kg/ha
      m_pack_solid_fertilisers_and_pesticides: kg/ha
    """
    
    _input_variables = ["nitrogen_from_mineral_fert",
                        "p2O5_from_mineral_fert",
                        "k2O_from_mineral_fert",
                        "magnesium_from_fertilizer",
                        "ca_from_mineral_fert",
                        "n_fertiliser_quantities",
                        "pest_total"
                       ]
    
    _DILUTION_FACTOR = 2.0;
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in PackModel._input_variables:
            setattr(self, key, inputs[key])
    
    def compute(self):
        totalFert = self.computeTotalFertilisers();
        fert_solid,fert_liquid = self.computeFertilisers(totalFert)
        pest_solid,pest_liquid = self.computePesticides()

        return {"m_Pack_packaging_liquid_fertilisers_and_pesticides": (pest_liquid + fert_liquid)*self._DILUTION_FACTOR,
                "m_Pack_packaging_solid_fertilisers_and_pesticides": (pest_solid + fert_solid)*self._DILUTION_FACTOR}
    
    
    def computeTotalFertilisers(self):
        return self.nitrogen_from_mineral_fert \
                + self.p2O5_from_mineral_fert \
                + self.k2O_from_mineral_fert \
                + self.magnesium_from_fertilizer \
                + self.ca_from_mineral_fert
        
    def computeFertilisers(self, totalFert):
        liquid = self.n_fertiliser_quantities[NFertiliserType.ammonia_liquid]
        solid = totalFert - liquid
        return (solid,liquid)
    
    def computePesticides(self):
        solid = 0.0
        liquid = self.pest_total
        return (solid / 1000.0,liquid /1000.0)
    