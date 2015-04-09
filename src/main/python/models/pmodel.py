from enum import Enum
from models.atomicmass import P_TO_PO4_FACTOR

class LandUseCategory(Enum):
    arable_land='Arable land'
    fruit_trees='Fruit trees'
    grassland_intensive='Grassland intensive'
    grassland_extensive='Grassland extensive'
    summer_alpine_pastures='Summer alpine pastures'
    vegetables='Vegetables'
    viticulture='Viticulture'

#FIXME: Modify to have it per crop cycle
class PModel(object):
    """Inputs:
      crop_cycle_per_year: ratio
      drained_part: ratio
      eroded_reaching_river: ratio
      eroded_soil: kg soil/(ha*year)
      eroded_soil_p_enrichment: ratio
      land_use_category: LandUseCategory
      p2o5_in_liquid_manure: kg P2O5/(ha*crop cycle)
      p2o5_in_liquid_sludge: kg P2O5/(ha*crop cycle)
      p2o5_in_mineral_fertiliser: kg P2O5/(ha*crop cycle)
      p2o5_in_solid_manure: kg P2O5/(ha*crop cycle)
      p_content_in_soil: kg P/kg soil
      
    Outputs:
      m_P_PO4_groundwater: kg PO4/(ha*crop cycle)
      m_P_PO4_surfacewater_drained : kg PO4/(ha*crop cycle)
      m_P_PO4_surfacewater_ro: kg PO4/(ha*crop cycle)
      m_P_P_surfacewater_erosion: kg P/(ha*crop cycle)
    """
    
    _input_variables = ["crop_cycle_per_year",
                        "drained_part",
                        "eroded_reaching_river",
                        "eroded_soil",
                        "eroded_soil_p_enrichment",
                        "land_use_category",
                        "p2o5_in_liquid_manure",
                        "p2o5_in_liquid_sludge",
                        "p2o5_in_mineral_fertiliser",
                        "p2o5_in_solid_manure",
                        "p_content_in_soil"
                       ]
    
    #Factors are per year
    _AVERAGE_GROUND_WATER_P_LOSS_PER_LAND = {LandUseCategory.arable_land: 0.07,
                                             LandUseCategory.fruit_trees: 0.06,
                                             LandUseCategory.grassland_intensive: 0.06,
                                             LandUseCategory.grassland_extensive: 0.06,
                                             LandUseCategory.summer_alpine_pastures: 0.055,
                                             LandUseCategory.vegetables: 0.07,
                                             LandUseCategory.viticulture: 0.07
                                            }
    
    #Factors are per year
    _AVERAGE_RUNOFF_P_LOSS_PER_LAND = {LandUseCategory.arable_land: 0.175,
                                       LandUseCategory.fruit_trees: 0.25,
                                       LandUseCategory.grassland_intensive: 0.25,
                                       LandUseCategory.grassland_extensive: 0.15,
                                       LandUseCategory.summer_alpine_pastures: 0.25,
                                       LandUseCategory.vegetables: 0.175,
                                       LandUseCategory.viticulture: 0.175
                                       }
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in PModel._input_variables:
            setattr(self, key, inputs[key])
            
    def compute(self):
        p2o5_liquid_sources = self._compute_p205_in_liquid_sources()
        phosphorus = self._compute_phosphorus_leach_in_ground_water(p2o5_liquid_sources)
        po4gw, po4swd = self._compute_and_split_phosphorus_to_ground_and_surface_waters_phosphates(phosphorus)
        po4swro = self._compute_phosphate_runoff_to_rivers(p2o5_liquid_sources)
        psw_erosion = self._compute_phosphorus_from_erosion_to_rivers()
        
        return {"m_P_PO4_groundwater": po4gw,
                "m_P_PO4_surfacewater_drained": po4swd,
                "m_P_PO4_surfacewater_ro": po4swro,
                "m_P_P_surfacewater_erosion": psw_erosion}
        
    def _compute_p205_in_liquid_sources(self):
        return self.p2o5_in_liquid_manure + self.p2o5_in_liquid_sludge
    
    def _compute_phosphorus_leach_in_ground_water(self, p2o5_liquid_sources):
        average_p_loss = self._AVERAGE_GROUND_WATER_P_LOSS_PER_LAND[self.land_use_category] / self.crop_cycle_per_year
        correction_factor = 1 + 0.2/80.0 * p2o5_liquid_sources
        return average_p_loss * correction_factor
        
    def _compute_and_split_phosphorus_to_ground_and_surface_waters_phosphates(self, phosphorus):
        phosphate = phosphorus * P_TO_PO4_FACTOR
        ground = phosphate * (1 - self.drained_part)
        surface = (phosphate - ground) * 6
        return (ground, surface)
    
    def _compute_phosphate_runoff_to_rivers(self, p2o5_liquid_sources):
        correction_factor = 1 + 0.2 / 80.0 * self.p2o5_in_mineral_fertiliser \
                              + 0.7 / 80.0 * p2o5_liquid_sources           \
                              + 0.4 / 80.0 * self.p2o5_in_solid_manure
        average_p_loss = self._AVERAGE_RUNOFF_P_LOSS_PER_LAND[self.land_use_category] / self.crop_cycle_per_year
        prunoff = average_p_loss * correction_factor
        return prunoff * P_TO_PO4_FACTOR;

    def _compute_phosphorus_from_erosion_to_rivers(self):
        return self.p_content_in_soil * self.eroded_soil_p_enrichment * self.eroded_reaching_river \
                   * self.eroded_soil / self.crop_cycle_per_year
    