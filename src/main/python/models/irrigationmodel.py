from enum import Enum

class IrrigationType(Enum):
    surface_irrigation_no_energy=1
    surface_irrigation_electricity=2
    surface_irrigation_diesel=3
    sprinkler_irrigation_electricity=4
    sprinkler_irrigation_diesel=5
    drip_irrigation_electricity=6
    drip_irrigation_diesel=7

class IrrigationModel(object):
    """Inputs:
        water_use_total: m3/ha
        irrigation_types_proportions: map IrrigationType -> ratio. Sum should be 1
      
    Outputs:
      m_Irr_water_to_air:m3/ha
      m_Irr_water_to_water_river: m3/ha
      m_Irr_water_to_water_groundwater: m3/ha
    """
    
    _input_variables = ["water_use_total",
                        "irrigation_types_proportions"
                       ]
    
    _IRRIGATION_EFFICIENCY_FACTOR_SURFACE = 0.45
    _IRRIGATION_EFFICIENCY_FACTOR_SPRINKLER = 0.75
    _IRRIGATION_EFFICIENCY_FACTOR_DRIP = 0.9
    
    def __init__(self, inputs):
        #TODO: Should we log usage of default value?
        for key in IrrigationModel._input_variables:
            setattr(self, key, inputs[key])
    
    def compute(self):
        irrigation_efficiency_ratio = self._compute_irrigation_efficiency_ratio()
        water_to_air = self._compute_evapotranspiration(irrigation_efficiency_ratio)
        water_to_water_river,water_to_water_groundwater = self._split_water_between_river_and_ground(self.water_use_total, water_to_air)
        
        return {"m_Irr_water_to_air":water_to_air,
                "m_Irr_water_to_water_river":water_to_water_river,
                "m_Irr_water_to_water_groundwater":water_to_water_groundwater
                }
    
    def _compute_irrigation_efficiency_ratio(self):
        return self._compute_surface_irrigation_ratio() + self._compute_sprinkler_irrigation_ratio() + self._compute_drip_irrigation_ratio();
    
    def _compute_surface_irrigation_ratio(self):
        return (self.irrigation_types_proportions[IrrigationType.surface_irrigation_diesel] 
                + self.irrigation_types_proportions[IrrigationType.surface_irrigation_electricity] 
                + self.irrigation_types_proportions[IrrigationType.surface_irrigation_no_energy]) * self._IRRIGATION_EFFICIENCY_FACTOR_SURFACE
    
    def _compute_sprinkler_irrigation_ratio(self):
        return (self.irrigation_types_proportions[IrrigationType.sprinkler_irrigation_electricity] 
                + self.irrigation_types_proportions[IrrigationType.sprinkler_irrigation_diesel]) * self._IRRIGATION_EFFICIENCY_FACTOR_SPRINKLER
    
    def _compute_drip_irrigation_ratio(self):
        return (self.irrigation_types_proportions[IrrigationType.drip_irrigation_electricity] 
                + self.irrigation_types_proportions[IrrigationType.drip_irrigation_diesel]) * self._IRRIGATION_EFFICIENCY_FACTOR_DRIP
    
    def _compute_evapotranspiration(self,irrigation_efficiency_ratio):
        return self.water_use_total * irrigation_efficiency_ratio;

    def _split_water_between_river_and_ground(self, total_water_use_irr, water_to_air):
        water_remaining_after_evapo = total_water_use_irr - water_to_air
        river = 0.8 * water_remaining_after_evapo
        ground = 0.2 * water_remaining_after_evapo
        return (river,ground)
    